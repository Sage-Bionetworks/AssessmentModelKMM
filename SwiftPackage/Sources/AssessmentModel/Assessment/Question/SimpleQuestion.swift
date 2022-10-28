//
//  SimpleQuestion.swift
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
