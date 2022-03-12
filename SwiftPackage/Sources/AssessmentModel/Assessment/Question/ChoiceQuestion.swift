//
//  ChoiceQuestion.swift
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

public protocol ChoiceInputItem : InputItem, ChoiceOption {
}

/// A choice option is a light-weight interface for a list of choices that should be displayed to the participant.
public protocol ChoiceOption {

    /// This is the JSON serializable element selected as one of the possible answers for a ``Question``. For certain
    /// special cases, the value may depend upon whether or not the item is selected.
    ///
    /// For example, a boolean ``Question`` may be displayed using choice items of "Yes" and "No" in a list. The choices
    /// would both be ``exclusive`` and the returned value for "Yes" could be `true` if ``selected`` and `null`
    /// if not while for the "No", it could be `false` if ``selected`` and `null` if not.
    func jsonElement(selected: Bool) -> JsonElement?

    /// A localized label (ie. title) for this choice.
    var label: String { get }

    /// Additional detail shown below the ``label``
    var detail: String? { get }
    
    /// An image associated with this choice.
    var iconName: String? { get }

    /// Does selecting this ``ChoiceOption`` mean that the other options should be deselected or
    /// selected as well?
    ///
    /// For example, this can be used in a multiple selection question to allow for a "none of the above"
    /// choice that is `exclusive` to the other items or an "all of the above" choice that should
    /// select `all` other choices as well (except those that are marked as `exclusive`).
    var selectorType: ChoiceSelectorType { get }
}

/// The selection rule associated with a given ``ChoiceOption``.
public enum ChoiceSelectorType : String, StringEnumSet, DocumentableStringEnum {
    case `default`, exclusive, all
}

public protocol ChoiceQuestion : Question {
    var choices: [JsonChoice] { get }
    var baseType: JsonType { get }
    var other: TextInputItem? { get }
}

public extension ChoiceQuestion {
    var answerType: AnswerType {
        singleAnswer ? baseType.answerType : AnswerTypeArray(baseType: baseType)
    }
    
    func buildInputItems() -> [InputItem] {
        var items: [InputItem] = choices
        if let otherItem = other {
            items.append(otherItem)
        }
        return items
    }
    
    func instantiateAnswerResult() -> AnswerResult {
        AnswerResultObject(identifier: self.identifier, answerType: self.answerType, value: nil, questionText: self.title)
    }
}

/// A choice input item where the value is defined by a ``JsonElement``.
public protocol JsonChoice : ChoiceInputItem {
    var matchingValue: JsonElement? { get }
}

public extension JsonChoice {
    
    var answerType: AnswerType {
        matchingValue.map { $0.answerType } ?? AnswerTypeNull()
    }
    
    var resultIdentifier: String? {
        matchingValue.map { "\($0)"}
    }
     
    func jsonElement(selected: Bool) -> JsonElement? {
        selected ? matchingValue : nil
    }
}

/// An abstract implementation is provided to allow different "type" identifiers with the same encoding.
open class AbstractChoiceQuestionStepObject : AbstractQuestionStepObject, ChoiceQuestion, QuestionStep {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case baseType, singleAnswer = "singleChoice", choices, other
        var relativeIndex: Int { 5 }
    }
    
    public var choices: [JsonChoice] { _choices }
    private let _choices: [JsonChoiceObject]
    public let other: TextInputItem?
    public let baseType: JsonType
    public let singleAnswer: Bool
    
    override open func instantiateResult() -> ResultData {
        instantiateAnswerResult()
    }
    
    public init(identifier: String, choices: [JsonChoiceObject], baseType: JsonType? = nil, singleChoice: Bool = true, other: TextInputItem? = nil,
                title: String? = nil, subtitle: String? = nil, detail: String? = nil, imageInfo: ImageInfo? = nil,
                optional: Bool? = nil, uiHint: QuestionUIHint? = nil,
                shouldHideButtons: Set<ButtonAction>? = nil, buttonMap: [ButtonAction : ButtonActionInfo]? = nil, comment: String? = nil) {
        self._choices = choices
        self.baseType = baseType ?? choices.baseType()
        self.singleAnswer = singleChoice
        self.other = other
        super.init(identifier: identifier,
                   title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo,
                   optional: optional, uiHint: uiHint,
                   shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment)
        if let error = _validate() {
            fatalError(error.debugDescription)
        }
    }
    
    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let choices = try container.decode([JsonChoiceObject].self, forKey: .choices)
        self._choices = choices
        self.baseType = try container.decodeIfPresent(JsonType.self, forKey: .baseType) ?? choices.baseType()
        self.singleAnswer = try container.decodeIfPresent(Bool.self, forKey: .singleAnswer) ?? true
        if container.contains(.other) {
            let nestedDecoder = try container.superDecoder(forKey: .other)
            self.other = try decoder.serializationFactory.decodePolymorphicObject(TextInputItem.self, from: nestedDecoder)
        }
        else {
            self.other = nil
        }
        try super.init(from: decoder)
        if let error = _validate() {
            var codingPath = decoder.codingPath
            codingPath.append(error.codingKey)
            throw DecodingError.typeMismatch(JsonType.self,
                                             .init(codingPath: codingPath, debugDescription: error.debugDescription))
        }
    }
    
    // Look to see that all the types match.
    private func _validate() -> (codingKey: CodingKeys, debugDescription: String)? {
        for choice in self.choices {
            if let match = choice.matchingValue, match.jsonType != self.baseType {
                return (.choices, "The json type for this choice of \(match) does not match the `baseType=\(baseType)` for this question.")
            }
        }
        if let otherAnswerType = self.other?.answerType, otherAnswerType.baseType != self.baseType {
            return (.other, "The json type for the 'other' text field does not match the `baseType=\(baseType)` for this question.")
        }
        return nil
    }

    public override func encode(to encoder: Encoder) throws {
        try super.encode(to: encoder)
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(self.singleAnswer, forKey: .singleAnswer)
        try container.encode(self._choices, forKey: .choices)
        try container.encode(self.baseType, forKey: .baseType)
        try encodeObject(object: self.other, to: encoder, forKey: CodingKeys.other)
    }
    
    override public class func codingKeys() -> [CodingKey] {
        var keys = super.codingKeys()
        keys.append(contentsOf: CodingKeys.allCases)
        return keys
    }

    override public class func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else {
            return super.isRequired(codingKey)
        }
        return key == .choices
    }

    override public class func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            return try super.documentProperty(for: codingKey)
        }
        switch key {
        case .baseType:
            return .init(propertyType: .reference(JsonType.documentableType()), propertyDescription:
                            "The json type of the choices in this collection. The `value` for all choices should be either of the same json type or null.")
        case .choices:
            return .init(propertyType: .referenceArray(JsonChoiceObject.documentableType()), propertyDescription:
                            "The list of choices for this question.")
        case .singleAnswer:
            return .init(propertyType: .primitive(.boolean), propertyDescription:
                            "Is there a single choice for this question? (Default = true)")
        case .other:
            return .init(propertyType: .interface("\(TextInputItem.self)"), propertyDescription:
                            "An input item that defines the properties of the textfield used to add free text as another choice.")
        }
    }
}

public final class ChoiceQuestionStepObject : AbstractChoiceQuestionStepObject, Encodable, DocumentableStruct {

    public override class func defaultType() -> SerializableNodeType {
        .StandardTypes.choiceQuestion.nodeType
    }
    
    public static func examples() -> [ChoiceQuestionStepObject] {
        [.init(identifier: "foo", choices: [.init(text: "ba"), .init(text: "la"), .init(text: "lala")])]
    }
    
    override public class func isRequired(_ codingKey: CodingKey) -> Bool {
        // For simplicity, the base type is required.
        super.isRequired(codingKey) || codingKey.stringValue == "baseType"
    }
}

public struct JsonChoiceObject : JsonChoice, Codable, Hashable {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case matchingValue = "value", label = "text", detail, iconName = "icon", _selectorType = "selectorType", _exclusive = "exclusive"
    }
    
    public let matchingValue: JsonElement?
    public let label: String
    public let detail: String?
    public let iconName: String?
    public var selectorType: ChoiceSelectorType {
        _selectorType ?? (_exclusive == true ? .exclusive : .default)
    }
    private let _selectorType: ChoiceSelectorType?
    
    // TODO: Deprecated. Included to supported for older json files that do not support "all of the above". syoung 03/10/2022
    private let _exclusive: Bool?
    
    public init(value: JsonElement? = nil,
                text: String,
                detail: String? = nil,
                selectorType: ChoiceSelectorType? = nil) {
        self.matchingValue = value
        self.label = text
        self.detail = detail
        self._selectorType = selectorType
        self._exclusive = nil
        self.iconName = nil
    }
    
    public init(text: String) {
        self.label = text
        self.matchingValue = .string(text)
        self.detail = nil
        self._selectorType = nil
        self._exclusive = nil
        self.iconName = nil
    }
}

extension JsonChoiceObject : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases.filter { $0 != ._exclusive }
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        (codingKey as? CodingKeys) == .label
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .matchingValue:
            return .init(propertyType: .any, propertyDescription:
                            """
                            The matching value is any json element, but all json elements within the collection
                            of choices should have the same json type.
                            """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n"))
        case .label:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "A localized string to show as the selection label.")
        case .detail:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "Additional detail to display below the primary label.")
        case ._selectorType:
            return .init(propertyType: .reference(ChoiceSelectorType.documentableType()), propertyDescription:
                            """
                            Does selecting this choice mean that the other options should be deselected or selected as well?
                            
                            For example, this can be used in a multiple selection question to allow for a 'none of the above'
                            choice that is `exclusive` to the other items or an 'all of the above' choice that should
                            select `all` other choices as well (except those that are marked as `exclusive`).
                            """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n"))
        case ._exclusive:
            return .init(defaultValue: .boolean(false), propertyDescription:
                            "Deprecated.")
        case .iconName:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "An image associated with this choice.")
        }
    }
    
    public static func examples() -> [JsonChoiceObject] {
        return [.init(value: .integer(1), text: "One")]
    }
}

fileprivate extension Array where Element : JsonChoice {
    func baseType() -> JsonType {
        first(where: {
            $0.matchingValue != nil && $0.matchingValue != JsonElement.null
        })?.matchingValue?.jsonType ?? .null
    }
}
