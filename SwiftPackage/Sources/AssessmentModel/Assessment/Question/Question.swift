//
//  Question.swift
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

/// A ``Question`` can be an input of a form or it might be a stand-alone question. It represents a
/// something that, when composited, will result in a single answer. It may compose input fields to do
/// so such as "What is your name?" with an answer of `{ "familyName" : "Smith", "givenName" : "John" }`
public protocol Question : ResultMapElement, ContentInfo {
    
    /// The ``AnswerType`` that is associated with this ``Question``.
    var answerType: AnswerType { get }
    
    /// Should the forward button be disabled until this question is answered?
    var optional: Bool { get }
    
    /// Is there a  single answer for this  question or is the answer a list of multiple choices or input items?
    var singleAnswer: Bool { get }
    
    /// This is a "hint" that can be used to vend a view that is appropriate to the given question. If the library
    /// responsible for rendering the question doesn't know how to handle the hint, then it will be ignored.
    var uiHint: QuestionUIHint? { get }
    
    /// Build the input items associated with this question.
    func buildInputItems() -> [InputItem]
    
    /// A question always has a result that is an `AnswerResult`
    func instantiateAnswerResult() -> AnswerResult
}

public protocol QuestionStep : Question, Step, ContentNode {
}

/// An ``InputItem`` describes a "part" of a ``Question`` representing a single answer.
///
/// For example, if a question is "what is your name" then the input items may include "given name" and "family name"
/// where separate text fields are used to allow the participant to enter their first and last name, and the question
/// may also include a list of titles from which to choose.
///
/// In another example, the input item could be a single cell in a list that shows the possible choices for a question.
/// In essence, this is akin to a single cell in a table view though the actual implementation may differ.
public protocol InputItem {

    /// The result identifier is an optional value that can be used to help in building the serializable answer result
    /// from this ``InputItem``. If null, then it is assumed that the ``Question`` that holds this ``InputItem``
    /// has some custom serialization strategy or only contains a single answer and this property can be ignored.
    var resultIdentifier: String? { get }
    
    /// The kind of object to expect for the serialization of the answer associated with this ``InputItem``. Typically,
    /// this will be an ``AnswerType`` that maps to a simple ``JsonType``,  but it is possible for the
    /// ``InputItem`` to translate to an object rather than a primitive.
    ///
    /// For example, the question could be about blood pressure where the participant answers the question with a string
    /// of "120/70" but the state handler is responsible for translating that into a data class with systolic and
    /// diastolic as properties that are themselves numbers.
    var answerType: AnswerType { get }
}

public struct QuestionUIHint : RawRepresentable, Hashable, Codable {
    public let rawValue: String
    public init(rawValue: String) {
        self.rawValue = rawValue
    }
    
    public enum Choice : String, Codable, CaseIterable {
        case checkbox, radioButton
        public var uiHint: QuestionUIHint { .init(rawValue: rawValue) }
    }
    
    public enum NumberField : String, Codable, CaseIterable {
        case textfield, slider, likert
        public var uiHint: QuestionUIHint { .init(rawValue: rawValue) }
    }
    
    public enum StringField : String, Codable, CaseIterable {
        case textfield, multipleLine
        public var uiHint: QuestionUIHint { .init(rawValue: rawValue) }
    }
    
    /// List of all the standard types.
    public static func allStandardTypes() -> [QuestionUIHint] {
        var hints = Set<QuestionUIHint>()
        hints.formUnion(Choice.allCases.map { $0.uiHint })
        hints.formUnion(NumberField.allCases.map { $0.uiHint })
        hints.formUnion(StringField.allCases.map { $0.uiHint })
        return Array(hints)
    }
}

extension QuestionUIHint : ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        self.init(rawValue: value)
    }
}

extension QuestionUIHint : DocumentableStringLiteral {
    public static func examples() -> [String] {
        return allStandardTypes().map{ $0.rawValue }
    }
}

open class AbstractQuestionStepObject : AbstractStepObject {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case optional, uiHint
        var relativeIndex: Int { 5 }
    }
    
    public let optional: Bool
    public let uiHint: QuestionUIHint?
    
    public init(identifier: String,
                title: String? = nil, subtitle: String? = nil, detail: String? = nil, imageInfo: ImageInfo? = nil,
                optional: Bool? = nil, uiHint: QuestionUIHint? = nil,
                shouldHideButtons: Set<ButtonType>? = nil, buttonMap: [ButtonType : ButtonActionInfo]? = nil, comment: String? = nil) {
        self.optional = optional ?? false
        self.uiHint = uiHint
        super.init(identifier: identifier,
                   title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo,
                   shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment)
    }
    
    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.optional = try container.decodeIfPresent(Bool.self, forKey: .optional) ?? false
        self.uiHint = try container.decodeIfPresent(QuestionUIHint.self, forKey: .uiHint)
        try super.init(from: decoder)
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
