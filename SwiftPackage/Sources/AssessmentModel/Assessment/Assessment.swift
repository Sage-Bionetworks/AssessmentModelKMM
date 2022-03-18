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

open class AbstractNodeContainerObject : AbstractContentNodeObject {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case children = "steps"
        var relativeIndex: Int { 5 }
    }
    
    public let children: [Node]
    
    open func instantiateNavigator(state: NavigationState) -> Navigator {
        NodeNavigator(identifier: identifier, nodes: children)
    }
    
    public init(identifier: String,
                children: [Node],
                title: String? = nil, subtitle: String? = nil, detail: String? = nil, imageInfo: ImageInfo? = nil,
                shouldHideButtons: Set<ButtonType>? = nil, buttonMap: [ButtonType : ButtonActionInfo]? = nil, comment: String? = nil, nextNode: NavigationIdentifier? = nil) {
        self.children = children
        super.init(identifier: identifier,
                   title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo,
                   shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment, nextNode: nextNode)
    }
    
    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let childrenContainer = try container.nestedUnkeyedContainer(forKey: .children)
        self.children = try decoder.serializationFactory.decodePolymorphicArray(Node.self, from: childrenContainer)
        try super.init(from: decoder)
    }
    
    open override func encode(to encoder: Encoder) throws {
        try super.encode(to: encoder)
        var container = encoder.container(keyedBy: CodingKeys.self)
        var nestedContainer = container.nestedUnkeyedContainer(forKey: .children)
        try children.forEach { node in
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
        (codingKey is CodingKeys) || super.isRequired(codingKey)
    }
    
    open override class func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            return try super.documentProperty(for: codingKey)
        }
        switch key {
        case .children:
            return .init(propertyType: .interfaceArray("\(Node.self)"), propertyDescription:
                            "A sequential list of the nodes to display for this assessment or section.")
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
        .init(identifier: identifier,
              children: children,
              title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo,
              shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment, nextNode: nextNode)
    }
}

open class AbstractAssessmentObject : AbstractNodeContainerObject, Assessment {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case versionString = "version", estimatedMinutes, copyright
        var relativeIndex: Int { 3 }
    }
    
    public let versionString: String?
    public let estimatedMinutes: Int
    public let copyright: String?
    
    public init(identifier: String, children: [Node],
                version: String? = nil, estimatedMinutes: Int = 0, copyright: String? = nil,
                title: String? = nil, subtitle: String? = nil, detail: String? = nil, imageInfo: ImageInfo? = nil,
                shouldHideButtons: Set<ButtonType>? = nil, buttonMap: [ButtonType : ButtonActionInfo]? = nil, comment: String? = nil, nextNode: NavigationIdentifier? = nil) {
        self.versionString = version
        self.estimatedMinutes = estimatedMinutes
        self.copyright = copyright
        super.init(identifier: identifier, children: children,
                   title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo,
                   shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment, nextNode: nextNode)
    }
    
    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.versionString = try container.decodeIfPresent(String.self, forKey: .versionString)
        self.estimatedMinutes = try container.decodeIfPresent(Int.self, forKey: .estimatedMinutes) ?? 0
        self.copyright = try container.decodeIfPresent(String.self, forKey: .copyright)
        try super.init(from: decoder)
    }
    
    open override func encode(to encoder: Encoder) throws {
        try super.encode(to: encoder)
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(self.versionString, forKey: .versionString)
        try container.encodeIfPresent(self.copyright, forKey: .copyright)
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
        ])]
    }
}

extension AssessmentObject : DocumentableRootObject {

    public convenience init() {
        self.init(identifier: "example", children: [])
    }

    public var jsonSchema: URL {
        URL(string: "\(self.className).json", relativeTo: kSageJsonSchemaBaseURL)!
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


