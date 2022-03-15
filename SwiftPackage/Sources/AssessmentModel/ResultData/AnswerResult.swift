//
//  AnswerResult.swift
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

/// An `AnswerResult` is used to hold a serializable answer to a question or measurement. This
/// protocol is defined as a class to allow for mutating the `jsonValue` without requiring the
/// controller to keep replacing the result in the collection or task result that contains the
/// value. However, that means that the instance *must* be explicitly copied when using this
/// to revisit a question.
public protocol AnswerResult : AnyObject, ResultData, AnswerFinder {
    
    /// Optional property for defining additional information about the answer expected for this result.
    var jsonAnswerType: AnswerType? { get }

    /// The answer held by this result.
    var jsonValue: JsonElement? { get set }
    
    /// The question text that was displayed for this answer result.
    var questionText: String? { get }
}

public protocol AnswerFinder {
    
    /// Find an *answer* result within this result. This method will return `nil` if there is a
    /// result but that result does **not** conform to to the `AnswerResult` protocol.
    ///
    /// - parameter identifier: The identifier associated with the result.
    /// - returns: The result or `nil` if not found.
    func findAnswer(with identifier: String) -> AnswerResult?
}

public extension AnswerResult {
    
    /// The `AnswerResult` conformance to the `AnswerFinder` protocol.
    func findAnswer(with identifier:String ) -> AnswerResult? {
        return self.identifier == identifier ? self : nil
    }
    
    /// When this protocol is used to support generic data in a research project, the researchers often want
    /// to encode the value in some special way (comma-delimited, for example) that is not supported as a
    /// `JsonElement` directly. By using the `answerType`, this allows this result to conform to any
    /// encoding strategy desired by the researchers who are using it in their studies, while keeping the
    /// model generic and reusable for the developers.
    func encodingValue() throws -> JsonElement? {
        try self.jsonAnswerType?.encodeAnswer(from: self.jsonValue) ?? self.jsonValue
    }
}

public final class AnswerResultObject : SerializableResultData, AnswerResult, MultiplatformResultData {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case serializableType = "type", identifier, startDate, endDate, jsonAnswerType = "answerType", jsonValue = "value", questionText, questionData
    }
    public private(set) var serializableType: SerializableResultType = .StandardTypes.answer.resultType
    
    public let identifier: String
    public let jsonAnswerType: AnswerType?
    public var jsonValue: JsonElement?
    public var questionText: String?
    public var startDateTime: Date
    public var endDateTime: Date?
    
    /// Additional data that researchers may wish to include with an answer result.
    public var questionData: JsonElement?
    
    public init(identifier: String, value: JsonElement, questionText: String? = nil, questionData: JsonElement? = nil) {
        self.identifier = identifier
        self.jsonAnswerType = value.answerType
        self.jsonValue = value
        self.questionText = questionText
        self.questionData = questionData
        self.startDateTime = Date()
    }
    
    public init(identifier: String, answerType: AnswerType?, value: JsonElement? = nil, questionText: String? = nil, questionData: JsonElement? = nil, startDate: Date = Date(), endDate: Date? = nil) {
        self.identifier = identifier
        self.jsonAnswerType = answerType
        self.jsonValue = value
        self.questionText = questionText
        self.questionData = questionData
        self.startDateTime = startDate
        self.endDateTime = endDate
    }
    
    public func deepCopy() -> AnswerResultObject {
        AnswerResultObject(identifier: identifier,
                           answerType: jsonAnswerType,
                           value: jsonValue,
                           questionText: questionText,
                           questionData: questionData,
                           startDate: startDateTime,
                           endDate: endDateTime)
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.identifier = try container.decode(String.self, forKey: .identifier)
        self.serializableType = try container.decode(SerializableResultType.self, forKey: .serializableType)
        self.questionData = try container.decodeIfPresent(JsonElement.self, forKey: .questionData)
        self.questionText = try container.decodeIfPresent(String.self, forKey: .questionText)
        if container.contains(.jsonAnswerType) {
            let nestedDecoder = try container.superDecoder(forKey: .jsonAnswerType)
            let answerType = try decoder.serializationFactory.decodePolymorphicObject(AnswerType.self, from: nestedDecoder)
            if container.contains(.jsonValue) {
                let jsonValueDecoder = try container.superDecoder(forKey: .jsonValue)
                self.jsonValue = try answerType.decodeValue(from: jsonValueDecoder)
            }
            else {
                self.jsonValue = nil
            }
            self.jsonAnswerType = answerType
        }
        else {
            self.jsonValue = try container.decodeIfPresent(JsonElement.self, forKey: .jsonValue)
            self.jsonAnswerType = nil
        }
        self.startDateTime = try container.decode(Date.self, forKey: .startDate)
        self.endDateTime = try container.decodeIfPresent(Date.self, forKey: .endDate)
    }
    
    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(identifier, forKey: .identifier)
        try container.encode(serializableType, forKey: .serializableType)
        try container.encodeIfPresent(self.questionData, forKey: .questionData)
        try container.encodeIfPresent(self.questionText, forKey: .questionText)
        try container.encode(self.startDateTime, forKey: .startDate)
        try container.encodeIfPresent(self.endDateTime, forKey: .endDate)
        if let info = self.jsonAnswerType {
            let encodable = try info as? Encodable ?? JsonElement.object(try info.jsonDictionary())
            let nestedEncoder = container.superEncoder(forKey: .jsonAnswerType)
            try encodable.encode(to: nestedEncoder)
        }
        let jsonVal = try self.encodingValue()
        try container.encodeIfPresent(jsonVal, forKey: .jsonValue)
    }
}

extension AnswerResultObject : DocumentableStruct {
    
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else { return false }
        return key == .serializableType || key == .identifier || key == .startDate
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .serializableType:
            return .init(constValue: SerializableResultType.StandardTypes.answer.resultType)
        case .identifier:
            return .init(propertyType: .primitive(.string))
        case .startDate, .endDate:
            return .init(propertyType: .format(.dateTime))
        case .jsonAnswerType:
            return .init(propertyType: .interface("\(AnswerType.self)"), propertyDescription:
                            "Optional property for defining additional information about the answer expected for this result.")
        case .jsonValue:
            return .init(propertyType: .any, propertyDescription:
                            "The answer held by this result.")
        case .questionText:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The question text that was displayed for this answer result.")
        case .questionData:
            return .init(propertyType: .any, propertyDescription:
                            "Additional data that researchers may wish to include with an answer result.")
        }
    }

    public static func examples() -> [AnswerResultObject] {
        let typeAndValue = AnswerTypeExamples.examplesWithValues()
        let date = ISO8601TimestampFormatter.date(from: "2017-10-16T22:28:09.000-07:00")!
        return typeAndValue.enumerated().map { (index, object) -> AnswerResultObject in
            let result = AnswerResultObject(identifier: "question\(index+1)", answerType: object.0, value: object.1)
            result.startDateTime = date
            result.endDateTime = date
            return result
        }
    }
}

