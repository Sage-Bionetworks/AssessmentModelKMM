//
//  SimpleQuestion.swift
//  
//

import Foundation
import JsonModel
import ResultModel

/// A simple question can be represented as text entry of a single value. The UI/UX for presenting the question
/// might not use a text field, based on the ``QuestionUIHint`` and the requirements of the application
/// design, but it translates into a single json value that can be represented as a string.
public protocol SimpleQuestion : Question {
    
    /// The single text input for this question.
    var inputItem: TextInputItem { get }
}

public extension SimpleQuestion {
    
    var answerType: AnswerType {
        inputItem.answerType
    }
    
    var singleAnswer: Bool {
        true
    }
    
    func buildInputItems() -> [InputItem] {
        [inputItem]
    }
    
    func instantiateAnswerResult() -> AnswerResult {
        AnswerResultObject(identifier: self.identifier, answerType: self.answerType, value: nil, questionText: self.title)
    }
}

public protocol SimpleQuestionStep : SimpleQuestion, QuestionStep {
}

/// An abstract implementation is provided to allow different "type" identifiers with the same encoding.
open class AbstractSimpleQuestionStepObject : AbstractQuestionStepObject, SimpleQuestionStep {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case inputItem
        var relativeIndex: Int { 6 }
    }
    
    public let inputItem: TextInputItem
    
    /// Allow subclasses to define their own default for the input item type.
    open class func defaultTextInputItem() -> TextInputItem {
        StringTextInputItemObject()
    }
    
    override open func instantiateResult() -> ResultData {
        instantiateAnswerResult()
    }
    
    public init(identifier: String,
                inputItem: TextInputItem? = nil,
                title: String? = nil, subtitle: String? = nil, detail: String? = nil, imageInfo: ImageInfo? = nil,
                optional: Bool? = nil, uiHint: QuestionUIHint? = nil, surveyRules: [JsonSurveyRuleObject]? = nil,
                shouldHideButtons: Set<ButtonType>? = nil, buttonMap: [ButtonType : ButtonActionInfo]? = nil, comment: String? = nil, nextNode: NavigationIdentifier? = nil) {
        self.inputItem = inputItem ?? Self.defaultTextInputItem()
        super.init(identifier: identifier, title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo, optional: optional, uiHint: uiHint, surveyRules: surveyRules, shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment, nextNode: nextNode)
    }
    
    public init(identifier: String, copyFrom object: AbstractSimpleQuestionStepObject) {
        self.inputItem = object.inputItem
        super.init(identifier: identifier, copyFrom: object)
    }
    
    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        if container.contains(.inputItem) {
            let nestedDecoder = try container.superDecoder(forKey: .inputItem)
            self.inputItem = try decoder.serializationFactory.decodePolymorphicObject(TextInputItem.self, from: nestedDecoder)
        }
        else {
            self.inputItem = Self.defaultTextInputItem()
        }
        try super.init(from: decoder)
    }

    public override func encode(to encoder: Encoder) throws {
        try super.encode(to: encoder)
        try encodeObject(object: inputItem, to: encoder, forKey: CodingKeys.inputItem)
    }

    override open class func codingKeys() -> [CodingKey] {
        var keys = super.codingKeys()
        let thisKeys: [CodingKey] = CodingKeys.allCases
        keys.append(contentsOf: thisKeys)
        return keys
    }

    override open class func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else {
            return super.isRequired(codingKey)
        }
        return key == .inputItem
    }

    override open class func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            return try super.documentProperty(for: codingKey)
        }
        switch key {
        case .inputItem:
            return .init(propertyType: .interface("\(TextInputItem.self)"), propertyDescription:
                            "The single text input for this question.")
        }
    }
}

public final class SimpleQuestionStepObject : AbstractSimpleQuestionStepObject, Encodable, DocumentableStruct, CopyWithIdentifier {
    
    public override class func defaultType() -> SerializableNodeType {
        .StandardTypes.simpleQuestion.nodeType
    }
    
    public static func examples() -> [SimpleQuestionStepObject] {
        [.init(identifier: "foo")]
    }
    
    public func copy(with identifier: String) -> SimpleQuestionStepObject {
        .init(identifier: identifier, copyFrom: self)
    }
}
