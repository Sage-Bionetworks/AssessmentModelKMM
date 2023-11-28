//
//  ChoiceQuestion.swift
//  
//

import Foundation
import JsonModel
import ResultModel

public protocol ChoiceInputItem : InputItem, ChoiceOption {
}

/// A choice option is a lightweight interface for a list of choices that should be displayed to the participant.
public protocol ChoiceOption {

    /// This is the JSON serializable element selected as one of the possible answers for a ``Question``. For certain
    /// special cases, the value may depend upon whether or not the item is selected.
    ///
    /// For example, a boolean ``Question`` may be displayed using choice items of "Yes" and "No" in a list. The choices
    /// would both be ``exclusive`` and the returned value for "Yes" could be `true` if ``selected`` and `null`
    /// if not while for the "No", it could be `false` if ``selected`` and `null` if not.
    func jsonElement(selected: Bool) -> JsonElement?

    /// A localized label (i.e. title) for this choice.
    var label: String { get }

    /// Additional detail shown below the ``label``.
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

public protocol ChoiceQuestionStep : ChoiceQuestion, QuestionStep {
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

/// An abstract implementation is provided to allow different "type" identifiers with the same encoding.
open class AbstractChoiceQuestionStepObject : AbstractQuestionStepObject, ChoiceQuestionStep {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case baseType, singleAnswer = "singleChoice", choices, other
        var relativeIndex: Int { 6 }
    }
    
    public let choices: [JsonChoice]
    public let other: TextInputItem?
    public let baseType: JsonType
    public let singleAnswer: Bool
    
    override open func instantiateResult() -> ResultData {
        instantiateAnswerResult()
    }
    
    public init(identifier: String, choices: [JsonChoice], baseType: JsonType? = nil, singleChoice: Bool = true, other: TextInputItem? = nil,
                title: String? = nil, subtitle: String? = nil, detail: String? = nil, imageInfo: ImageInfo? = nil,
                optional: Bool? = nil, uiHint: QuestionUIHint? = nil, surveyRules: [JsonSurveyRuleObject]? = nil,
                shouldHideButtons: Set<ButtonType>? = nil, buttonMap: [ButtonType : ButtonActionInfo]? = nil, comment: String? = nil, nextNode: NavigationIdentifier? = nil) {
        self.choices = choices
        self.baseType = baseType ?? choices.baseType()
        self.singleAnswer = singleChoice
        self.other = other
        super.init(identifier: identifier,
                   title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo,
                   optional: optional, uiHint: uiHint, surveyRules: surveyRules,
                   shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment, nextNode: nextNode)
        if let error = _validate() {
            fatalError(error.debugDescription)
        }
    }
    
    public init(identifier: String, copyFrom object: AbstractChoiceQuestionStepObject) {
        self.choices = object.choices
        self.baseType = object.baseType
        self.singleAnswer = object.singleAnswer
        self.other = object.other
        super.init(identifier: identifier, copyFrom: object)
    }
    
    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let choices = try container.decode([JsonChoice].self, forKey: .choices)
        self.choices = choices
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
        if Set(self.choices).count != self.choices.count {
            return (.choices, "The choices for this question are not unique.")
        }
        return nil
    }

    public override func encode(to encoder: Encoder) throws {
        try super.encode(to: encoder)
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(self.singleAnswer, forKey: .singleAnswer)
        try container.encode(self.choices, forKey: .choices)
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
            return .init(propertyType: .referenceArray(JsonChoice.documentableType()), propertyDescription:
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

public final class ChoiceQuestionStepObject : AbstractChoiceQuestionStepObject, Encodable, DocumentableStruct, CopyWithIdentifier {

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
    
    public func copy(with identifier: String) -> ChoiceQuestionStepObject {
        .init(identifier: identifier, copyFrom: self)
    }
}

@Serializable
public struct JsonChoice : ChoiceInputItem, Codable, Hashable {

    @SerialName("value") public let matchingValue: JsonElement?
    @SerialName("text") public let label: String
    @SerialName("icon") public let detail: String?
    public let iconName: String?
    public private(set) var selectorType: ChoiceSelectorType = .default
    
    public init(value: JsonElement? = nil,
                text: String,
                detail: String? = nil,
                selectorType: ChoiceSelectorType = .default) {
        self.matchingValue = value
        self.label = text
        self.detail = detail
        self.selectorType = selectorType
        self.iconName = nil
    }
    
    public init(text: String) {
        self.label = text
        self.matchingValue = .string(text)
        self.detail = nil
        self.selectorType = .default
        self.iconName = nil
    }
    
    public var answerType: AnswerType {
        matchingValue.map { $0.answerType } ?? AnswerTypeNull()
    }
    
    public var resultIdentifier: String? {
        matchingValue.map { "\($0)"}
    }
     
    public func jsonElement(selected: Bool) -> JsonElement? {
        selected ? matchingValue : nil
    }
}

extension JsonChoice : ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        self.init(text: value)
    }
}

extension JsonChoice : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        return CodingKeys.allCases
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
        case .selectorType:
            return .init(propertyType: .reference(ChoiceSelectorType.documentableType()), propertyDescription:
                            """
                            Does selecting this choice mean that the other options should be deselected or selected as well?
                            
                            For example, this can be used in a multiple selection question to allow for a 'none of the above'
                            choice that is `exclusive` to the other items or an 'all of the above' choice that should
                            select `all` other choices as well (except those that are marked as `exclusive`).
                            """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n"))
        case .iconName:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "An image associated with this choice.")
        }
    }
    
    public static func examples() -> [JsonChoice] {
        return [.init(value: .integer(1), text: "One")]
    }
}

extension Array where Element == JsonChoice {
    func baseType() -> JsonType {
        first(where: {
            $0.matchingValue != nil && $0.matchingValue != JsonElement.null
        })?.matchingValue?.jsonType ?? .null
    }
    
    public static func booleanChoices() -> [JsonChoice] {
        [.init(value: .boolean(true), text: "Yes"), .init(value: .boolean(false), text: "No")]
    }
}
