//
//  QuestionSerializer.swift
//  
//
//  Copyright © 2020-2022 Sage Bionetworks. All rights reserved.
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

public protocol SerializableQuestion : Question, PolymorphicRepresentable {
}

public final class QuestionSerializer : IdentifiableInterfaceSerializer, PolymorphicSerializer {
    public var documentDescription: String? {
        """
        The interface for any `Question` that is serialized using the `Codable` protocol and the
        polymorphic serialization defined by this framework.
        """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n")
    }

    public var jsonSchema: URL {
        URL(string: "\(self.interfaceName).json", relativeTo: kSageJsonSchemaBaseURL)!
    }

    override init() {
        self.examples = [
            ChoiceQuestionStepObject.examples().first!
        ]
    }

    public private(set) var examples: [Question]

    /// Insert the given example into the example array, replacing any existing example with the
    /// same `typeName` as one of the new example.
    public func add(_ example: SerializableQuestion) {
        examples.removeAll(where: { $0.typeName == example.typeName })
        examples.append(example)
    }
}

open class AbstractQuestionStepObject : AbstractStepObject {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case optional, uiHint
        var relativeIndex: Int { 5 }
    }
    
    open private(set) var optional: Bool = false
    open private(set) var uiHint: QuestionUIHint?
    
    public init(identifier: String,
                title: String? = nil, subtitle: String? = nil, detail: String? = nil, imageInfo: ImageInfo? = nil,
                optional: Bool? = nil, uiHint: QuestionUIHint? = nil,
                hideButtons: [ButtonAction]? = nil, buttonMap: [ButtonAction : ButtonActionInfo]? = nil, comment: String? = nil) {
        super.init(identifier: identifier,
                   title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo,
                   hideButtons: hideButtons, buttonMap: buttonMap, comment: comment)
        self.optional = optional ?? self.optional
        self.uiHint = uiHint ?? self.uiHint
    }
    
    public required init(from decoder: Decoder) throws {
        try super.init(from: decoder)
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.optional = try container.decodeIfPresent(Bool.self, forKey: .optional) ?? self.optional
        self.uiHint = try container.decodeIfPresent(QuestionUIHint.self, forKey: .uiHint) ?? self.uiHint
    }
    
    open override func encode(to encoder: Encoder) throws {
        try super.encode(to: encoder)
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(optional, forKey: .optional)
        try container.encodeIfPresent(uiHint, forKey: .uiHint)
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
        case .optional:
            return .init(defaultValue: .boolean(false), propertyDescription:
                            """
                            If `true`, then the forward button should *always* enabled. If `false`, then the forward
                            button should be disabled until the question is answered. This is different from "skipping"
                            a question. Whether or not the skip button is shown in the UI is defined by the
                            `shouldHideActions` property.
                            """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n"))
        case .uiHint:
            return .init(propertyType: .reference(QuestionUIHint.documentableType()), propertyDescription:
                            """
                            This is a "hint" that can be used to vend a view that is appropriate to the given question.
                            If the library responsible for rendering the question doesn't know how to handle the hint,
                            then it will be ignored.
                            """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n"))
        }
    }
}

