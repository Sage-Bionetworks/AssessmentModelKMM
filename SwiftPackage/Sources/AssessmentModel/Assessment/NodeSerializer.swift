//
//  NodeSerializer.swift
//  
//
//  Copyright © 2017-2022 Sage Bionetworks. All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1.  Redistributions of source code must retain the above copyright notice, this
// list of conditions and the following disclaimer.
//
// 2.  Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following disclaimer in the documentation and/or
// other materials provided with the distribution.
//
// 3.  Neither the name of the copyright holder(s) nor the names of any contributors
// may be used to endorse or promote products derived from this software without
// specific prior written permission. No license is granted to the trademarks of
// the copyright holders even if such marks are included in this software.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//

import Foundation
import JsonModel

/// `SerializableNode` is the base implementation for `Node` that is serialized using
/// the `Codable` protocol and the polymorphic serialization defined by this framework.
///
public protocol SerializableNode : Node, PolymorphicRepresentable {
    var serializableType: SerializableNodeType { get }
}

extension SerializableNode {
    public var typeName: String { serializableType.stringValue }
}

/// `SerializableNodeType` is an extendable string enum used by the `SerializationFactory` to
/// create the appropriate result type.
public struct SerializableNodeType : TypeRepresentable, Codable, Hashable {
    
    public let rawValue: String
    
    public init(rawValue: String) {
        self.rawValue = rawValue
    }
    
    public enum StandardTypes : String, CaseIterable {
        case choiceQuestion, simpleQuestion, overview, instruction, completion
        
        public var nodeType: SerializableNodeType {
            .init(rawValue: self.rawValue)
        }
    }
    
    /// List of all the standard types.
    public static func allStandardTypes() -> [SerializableNodeType] {
        StandardTypes.allCases.map { $0.nodeType }
    }
}

extension SerializableNodeType : ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        self.init(rawValue: value)
    }
}

extension SerializableNodeType : DocumentableStringLiteral {
    public static func examples() -> [String] {
        return allStandardTypes().map{ $0.rawValue }
    }
}

public final class NodeSerializer : IdentifiableInterfaceSerializer, PolymorphicSerializer {
    public var documentDescription: String? {
        """
        The interface for any `Node` that is serialized using the `Codable` protocol and the
        polymorphic serialization defined by this framework.
        """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n")
    }
    
    public var jsonSchema: URL {
        URL(string: "\(self.interfaceName).json", relativeTo: kSageJsonSchemaBaseURL)!
    }
    
    override init() {
        self.examples = [
            SimpleQuestionStepObject.examples().first!,
            ChoiceQuestionStepObject.examples().first!,
            InstructionStepObject.examples().first!,
            OverviewStepObject.examples().first!,
        ]
    }
    
    public private(set) var examples: [Node]
    
    public override class func typeDocumentProperty() -> DocumentProperty {
        .init(propertyType: .reference(SerializableNodeType.documentableType()))
    }
    
    /// Insert the given example into the example array, replacing any existing example with the
    /// same `typeName` as one of the new example.
    public func add(_ example: SerializableNode) {
        examples.removeAll(where: { $0.typeName == example.typeName })
        examples.append(example)
    }
    
    /// Insert the given examples into the example array, replacing any existing examples with the
    /// same `typeName` as one of the new examples.
    public func add(contentsOf newExamples: [SerializableNode]) {
        let newNames = newExamples.map { $0.typeName }
        self.examples.removeAll(where: { newNames.contains($0.typeName) })
        self.examples.append(contentsOf: newExamples)
    }
}

open class AbstractNodeObject : SerializableNode {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case serializableType="type", identifier, comment, shouldHideButtons="shouldHideActions", buttonMap="actions"
        var relativeIndex: Int { 2 }
    }
    public private(set) var serializableType: SerializableNodeType = .init(rawValue: "null")
    
    public let identifier: String
    public let comment: String?
    public let shouldHideButtons: Set<ButtonAction>
    public let buttonMap: [ButtonAction : ButtonActionInfo]
    
    open class func defaultType() -> SerializableNodeType {
        fatalError("The default type *must* be overriden for this abstract class")
    }
    
    public init(identifier: String,
                shouldHideButtons: Set<ButtonAction>? = nil,
                buttonMap: [ButtonAction : ButtonActionInfo]? = nil,
                comment: String? = nil) {
        self.identifier = identifier
        self.comment = comment
        self.shouldHideButtons = shouldHideButtons ?? []
        self.buttonMap = buttonMap ?? [:]
        self.serializableType = type(of: self).defaultType()
    }

    public func instantiateResult() -> ResultData {
        ResultObject(identifier: self.identifier)
    }

    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.serializableType = type(of: self).defaultType()
        self.identifier = try container.decode(String.self, forKey: .identifier)
        self.comment = try container.decodeIfPresent(String.self, forKey: .comment)
        self.shouldHideButtons = try container.decodeIfPresent(Set<ButtonAction>.self, forKey: .shouldHideButtons) ?? []
        if container.contains(.buttonMap) {
            let nestedDecoder = try container.superDecoder(forKey: .buttonMap)
            let nestedContainer = try nestedDecoder.container(keyedBy: AnyCodingKey.self)
            var buttonMap = [ButtonAction : ButtonActionInfo]()
            for key in nestedContainer.allKeys {
                let objectDecoder = try nestedContainer.superDecoder(forKey: key)
                let actionType = ButtonAction(rawValue: key.stringValue)
                let action = try decoder.serializationFactory.decodePolymorphicObject(ButtonActionInfo.self, from: objectDecoder)
                buttonMap[actionType] = action
            }
            self.buttonMap = buttonMap
        }
        else {
            self.buttonMap = [:]
        }
    }

    /// Define the encoder, but do not require protocol conformance of subclasses.
    /// - parameter encoder: The encoder to use to encode this instance.
    /// - throws: `EncodingError`
    open func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)

        try container.encode(serializableType, forKey: .serializableType)
        try container.encode(identifier, forKey: .identifier)
        try container.encodeIfPresent(comment, forKey: .comment)
        
        if self.buttonMap.count > 0 {
            var nestedContainer = container.nestedContainer(keyedBy: ButtonAction.self, forKey: .buttonMap)
            try self.buttonMap.forEach { (key, action) in
                guard let encodableAction = action as? Encodable else { return }
                let objectEncoder = nestedContainer.superEncoder(forKey: key)
                try encodableAction.encode(to: objectEncoder)
            }
        }
        if self.shouldHideButtons.count > 0 {
            try container.encode(self.shouldHideButtons, forKey: .shouldHideButtons)
        }
    }

    // DocumentableObject implementation

    open class func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }

    open class func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else {
            return false
        }
        return (key == .identifier) || (key == .serializableType)
    }

    open class func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not handled by \(self).")
        }
        switch key {
        case .serializableType:
            return .init(constValue: defaultType())
        case .identifier:
            return .init(propertyType: .primitive(.string))
        case .comment:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "A developer-facing comment about this node.")
        case .buttonMap:
            return .init(propertyType: .interfaceDictionary("\(ButtonActionInfo.self)"), propertyDescription:
                            "A mapping of button action to content information for that button.")
        case .shouldHideButtons:
            return .init(propertyType: .referenceArray(ButtonAction.documentableType()), propertyDescription:
                            "A list of buttons that should be hidden even if the default is to show them.")
        }
    }
}

open class AbstractContentNodeObject : AbstractNodeObject, ContentNode {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case title, subtitle, detail, imageInfo = "image"
        var relativeIndex: Int { 3 }
    }
    
    public let title: String?
    public let subtitle: String?
    public let detail: String?
    public let imageInfo: ImageInfo?
    
    public init(identifier: String,
                title: String? = nil, subtitle: String? = nil, detail: String? = nil, imageInfo: ImageInfo? = nil,
                shouldHideButtons: Set<ButtonAction>? = nil, buttonMap: [ButtonAction : ButtonActionInfo]? = nil, comment: String? = nil) {
        self.title = title
        self.subtitle = subtitle
        self.detail = detail
        self.imageInfo = imageInfo
        super.init(identifier: identifier, shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment)
    }
    
    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.title = try container.decodeIfPresent(String.self, forKey: .title)
        self.subtitle = try container.decodeIfPresent(String.self, forKey: .subtitle)
        self.detail = try container.decodeIfPresent(String.self, forKey: .detail)
        if container.contains(.imageInfo) {
            let nestedDecoder = try container.superDecoder(forKey: .imageInfo)
            self.imageInfo = try decoder.serializationFactory.decodePolymorphicObject(ImageInfo.self, from: nestedDecoder)
        }
        else {
            self.imageInfo = nil
        }
        try super.init(from: decoder)
    }
    
    open override func encode(to encoder: Encoder) throws {
        try super.encode(to: encoder)
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(title, forKey: .title)
        try container.encodeIfPresent(subtitle, forKey: .subtitle)
        try container.encodeIfPresent(detail, forKey: .detail)
        try encodeObject(object: self.imageInfo, to: encoder, forKey: CodingKeys.imageInfo)
    }
    
    override open class func codingKeys() -> [CodingKey] {
        var keys = super.codingKeys()
        keys.append(contentsOf: CodingKeys.allCases)
        return keys
    }

    override open class func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            return try super.documentProperty(for: codingKey)
        }
        switch key {
        case .title:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The primary text to display for the node in a localized string. The UI should display this using a larger font.")
        case .subtitle:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "A subtitle to display for the node in a localized string.")
        case .detail:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "Detail text to display for the node in a localized string.")
        case .imageInfo:
            return .init(propertyType: .interface("\(ImageInfo.self)"), propertyDescription:
                            "An image or animation to display with this node.")
        }
    }
}

open class AbstractStepObject : AbstractContentNodeObject, Step {
    /// Default implementation returns `nil`.
    open func spokenInstruction(at timeInterval: TimeInterval) -> String? {
        nil
    }
}

internal func encodeObject<Key>(object: Any?, to encoder: Encoder, forKey: Key) throws where Key : CodingKey {
    guard let obj = object else { return }
    guard let encodable = obj as? Encodable else {
        var codingPath = encoder.codingPath
        codingPath.append(forKey)
        let context = EncodingError.Context(codingPath: codingPath, debugDescription: "\(obj) does not conform to the `Encodable` protocol")
        throw EncodingError.invalidValue(obj, context)
    }
    var container = encoder.container(keyedBy: Key.self)
    let nestedEncoder = container.superEncoder(forKey: forKey)
    try encodable.encode(to: nestedEncoder)
}

