//
//  Assessment.swift
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

open class AbstractNodeContainerObject : AbstractContentNodeObject, AsyncActionContainer {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case children = "steps", asyncActions
        var relativeIndex: Int { 5 }
    }
    
    public let children: [Node]
    
    public var asyncActions: [AsyncActionConfiguration] { _asyncActions ?? [] }
    private let _asyncActions: [AsyncActionConfiguration]?
    
    open func instantiateNavigator(state: NavigationState) throws -> Navigator {
        try NodeNavigator(identifier: identifier, nodes: children)
    }
    
    public init(identifier: String,
                children: [Node], asyncActions: [AsyncActionConfiguration]? = nil,
                title: String? = nil, subtitle: String? = nil, detail: String? = nil, imageInfo: ImageInfo? = nil,
                shouldHideButtons: Set<ButtonType>? = nil, buttonMap: [ButtonType : ButtonActionInfo]? = nil, comment: String? = nil, nextNode: NavigationIdentifier? = nil) {
        self.children = children
        self._asyncActions = asyncActions
        super.init(identifier: identifier,
                   title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo,
                   shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment, nextNode: nextNode)
    }
    
    public init(identifier: String, copyFrom object: AbstractNodeContainerObject) {
        self.children = object.children.map { child in
            (child as? CopyWithIdentifier).map { $0.copy(with: $0.identifier) as! Node } ?? child
        }
        self._asyncActions = object._asyncActions?.map { action in
            (action as? CopyWithIdentifier).map { $0.copy(with: $0.identifier) as! AsyncActionConfiguration } ?? action
        }
        super.init(identifier: identifier, copyFrom: object)
    }
    
    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let childrenContainer = try container.nestedUnkeyedContainer(forKey: .children)
        self.children = try decoder.serializationFactory.decodePolymorphicArray(Node.self, from: childrenContainer)
        if container.contains(.asyncActions) {
            let actionsContainer = try container.nestedUnkeyedContainer(forKey: .asyncActions)
            self._asyncActions = try decoder.serializationFactory.decodePolymorphicArray(AsyncActionConfiguration.self, from: actionsContainer)
        } else {
            self._asyncActions = nil
        }
        try super.init(from: decoder)
    }
    
    open override func encode(to encoder: Encoder) throws {
        try super.encode(to: encoder)
        try encodeArray(children, forKey: .children, to: encoder)
        if let actions = self._asyncActions {
            try encodeArray(actions, forKey: .asyncActions, to: encoder)
        }
    }
    
    private func encodeArray<T>(_ array: [T], forKey key: CodingKeys, to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        var nestedContainer = container.nestedUnkeyedContainer(forKey: key)
        try array.forEach { node in
            guard let encodable = node as? Encodable else {
                throw EncodingError.invalidValue(node, .init(codingPath: nestedContainer.codingPath, debugDescription:
                                                                "\(node) does not match the Encodable protocol."))
            }
            let nestedEncoder = nestedContainer.superEncoder()
            try encodable.encode(to: nestedEncoder)
        }
    }
    
    open override class func codingKeys() -> [CodingKey] {
        var keys = super.codingKeys()
        keys.append(contentsOf: CodingKeys.allCases)
        return keys
    }
    
    open override class func isRequired(_ codingKey: CodingKey) -> Bool {
        (codingKey as? CodingKeys).map { $0 == .children } ?? super.isRequired(codingKey)
    }
    
    open override class func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            return try super.documentProperty(for: codingKey)
        }
        switch key {
        case .children:
            return .init(propertyType: .interfaceArray("\(Node.self)"), propertyDescription:
                            "A sequential list of the nodes to display for this assessment or section.")
        case .asyncActions:
            return .init(propertyType: .interfaceArray("\(AsyncActionConfiguration.self)"), propertyDescription:
                            "A list of elements used to describe the configuration for background actions.")
        }
    }
}

open class AbstractSectionObject : AbstractNodeContainerObject, BranchNode, NavigationRule {
    open func instantiateBranchNodeResult() -> BranchNodeResult {
        BranchNodeResultObject(identifier: self.identifier)
    }
    
    override open func instantiateResult() -> ResultData {
        instantiateBranchNodeResult()
    }
}

public final class SectionObject : AbstractSectionObject, DocumentableStruct, CopyWithIdentifier {

    public override class func defaultType() -> SerializableNodeType {
        .StandardTypes.section.nodeType
    }
    
    public static func examples() -> [SectionObject] {
        [.init(identifier: "section", children: [
            SimpleQuestionStepObject(identifier: "favoriteColor", title: "What is your favorite color")
        ])]
    }
    
    public func copy(with identifier: String) -> SectionObject {
        .init(identifier: identifier, copyFrom: self)
    }
}

open class AbstractAssessmentObject : AbstractNodeContainerObject, Assessment {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case jsonSchema = "$schema", versionString, estimatedMinutes, copyright, interruptionHandling
        var relativeIndex: Int { 3 }
    }
    
    public let versionString: String?
    public let estimatedMinutes: Int
    public let copyright: String?
    
    open var interruptionHandling: InterruptionHandling { _interruptionHandling ?? InterruptionHandlingObject() }
    private let _interruptionHandling: InterruptionHandlingObject?
    
    open var jsonSchema: URL {
        _jsonSchema ?? URL(string: "\(type(of: self)).json", relativeTo: kSageJsonSchemaBaseURL)!
    }
    private let _jsonSchema: URL?
    
    public init(identifier: String, children: [Node],
                version: String? = nil, estimatedMinutes: Int = 0, copyright: String? = nil, interruptionHandling: InterruptionHandlingObject? = nil,
                title: String? = nil, subtitle: String? = nil, detail: String? = nil, imageInfo: ImageInfo? = nil,
                shouldHideButtons: Set<ButtonType>? = nil, buttonMap: [ButtonType : ButtonActionInfo]? = nil, comment: String? = nil) {
        self.versionString = version
        self.estimatedMinutes = estimatedMinutes
        self.copyright = copyright
        self._jsonSchema = nil
        self._interruptionHandling = interruptionHandling
        super.init(identifier: identifier, children: children,
                   title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo,
                   shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment)
    }
    
    public init(identifier: String, copyFrom object: AbstractAssessmentObject) {
        self.versionString = object.versionString
        self.estimatedMinutes = object.estimatedMinutes
        self.copyright = object.copyright
        self._jsonSchema = object._jsonSchema
        self._interruptionHandling = object._interruptionHandling
        super.init(identifier: identifier, copyFrom: object)
    }
    
    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.versionString = try container.decodeIfPresent(String.self, forKey: .versionString)
        self.estimatedMinutes = try container.decodeIfPresent(Int.self, forKey: .estimatedMinutes) ?? 0
        self.copyright = try container.decodeIfPresent(String.self, forKey: .copyright)
        self._jsonSchema = try container.decodeIfPresent(URL.self, forKey: .jsonSchema)
        self._interruptionHandling = try container.decodeIfPresent(InterruptionHandlingObject.self, forKey: .interruptionHandling)
        try super.init(from: decoder)
    }
    
    open override func encode(to encoder: Encoder) throws {
        try super.encode(to: encoder)
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(self.versionString, forKey: .versionString)
        try container.encodeIfPresent(self.copyright, forKey: .copyright)
        try container.encodeIfPresent(self._interruptionHandling, forKey: .interruptionHandling)
        try container.encode(self.jsonSchema, forKey: .jsonSchema)
        if estimatedMinutes > 0 {
            try container.encode(estimatedMinutes, forKey: .estimatedMinutes)
        }
    }
    
    open func instantiateAssessmentResult() -> AssessmentResult {
        AssessmentResultObject(identifier: self.identifier, versionString: self.versionString)
    }
    
    open override func instantiateResult() -> ResultData {
        instantiateAssessmentResult()
    }
    
    // Top-level Assessment nodes do not support direct navigation.
    override class func supportsNextNode() -> Bool {
        false
    }
    
    open override class func codingKeys() -> [CodingKey] {
        var keys = super.codingKeys()
        keys.append(contentsOf: CodingKeys.allCases)
        return keys
    }
    
    open override class func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            return try super.documentProperty(for: codingKey)
        }
        switch key {
        case .versionString:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "A version for the assessment.")
        case .copyright:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "Copyright information for the assessment.")
        case .estimatedMinutes:
            return .init(defaultValue: .integer(0), propertyDescription:
                            "Estimated number of minutes to perform the assessment.")
        case .jsonSchema:
            return .init(propertyType: .format(.uri), propertyDescription:
                            "The URI for the json schema for this assessment.")
        case .interruptionHandling:
            return .init(propertyType: .reference(InterruptionHandlingObject.documentableType()), propertyDescription:
                            "The interruption handling for this assessment.")
        }
    }
}

public final class AssessmentObject : AbstractAssessmentObject, DocumentableStruct {

    public override class func defaultType() -> SerializableNodeType {
        .StandardTypes.assessment.nodeType
    }
    
    public static func examples() -> [AssessmentObject] {
        [.init(identifier: "assessment", children: [
            SimpleQuestionStepObject(identifier: "favoriteColor", title: "What is your favorite color")
        ]),
         surveyA
        ]
    }
}

extension AssessmentObject : DocumentableRootObject {

    public convenience init() {
        self.init(identifier: "example", children: [])
    }

    public var documentDescription: String? {
        "An assessment that is defined by a sequential series of steps."
    }
}

public final class AssessmentSerializer : IdentifiableInterfaceSerializer, PolymorphicSerializer {
    public var documentDescription: String? {
        """
        The interface for any `Assessment` that is serialized using the `Codable` protocol and the
        polymorphic serialization defined by this framework.
        """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n")
    }
    
    public var jsonSchema: URL {
        URL(string: "\(self.interfaceName).json", relativeTo: kSageJsonSchemaBaseURL)!
    }
    
    override init() {
        self.examples = [
            AssessmentObject.examples().first!,
        ]
    }
    
    public private(set) var examples: [Assessment]
    
    /// Insert the given example into the example array, replacing any existing example with the
    /// same `typeName` as one of the new example.
    public func add<T : Assessment>(_ example: T) where T : SerializableNode {
        examples.removeAll(where: { $0.typeName == example.typeName })
        examples.append(example)
    }
}


