//
//  TextInputItems.swift
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

/// An ``TextInputItem`` describes input entry that is freeform with ranges and validation. Typically, this is
/// presented as a text field, but depending upon the requirements of the survey designer, it may use a slider,
/// Likert scale, date picker, or other custom UI/UX to allow for validation of the entered value.
public protocol TextInputItem : InputItem {

    /// Options for displaying a text field. This is only applicable for certain types of UI hints
    /// and data types. If not applicable, it will be ignored.
    var keyboardOptions: KeyboardOptions { get }
    
    /// A localized string that displays a short text offering a hint to the user of the data to be entered for this field.
    var fieldLabel: String? { get }

    /// A localized string that displays placeholder information for the ``InputItem``.
    ///
    /// You can display placeholder text in a text field or text area to help users understand how to answer the item's
    /// question. If the input field brings up another view to enter the answer, this could also be used at the button title.
    var placeholder: String? { get }

    /// This can be used to return a class used to format and/or validate the text input.
    func buildTextValidator() -> TextEntryValidator
}

public protocol StringTextInputItem : TextInputItem {
    var characterLimit: Int? { get }
}

public protocol IntegerTextInputItem : TextInputItem {
    var range: IntegerRange? { get }
}

public protocol DoubleTextInputItem : TextInputItem {
    var range: DoubleRange? { get }
}

public protocol DurationTextInputItem : TextInputItem {
    var displayUnits: [DurationUnit] { get }
}

public final class TextInputItemSerializer : AbstractPolymorphicSerializer, PolymorphicSerializer {
    public var documentDescription: String? {
        """
        A `TextInputItem` describes input entry that is freeform with ranges and validation.
        Typically, this is presented as a text field, but depending upon the requirements of the
        survey designer, it may use a slider, Likert scale, date picker, or other custom UI/UX to
        allow for validation of the entered value.
        """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n")
    }
    
    public var jsonSchema: URL {
        URL(string: "\(AssessmentFactory.defaultFactory.modelName(for: self.interfaceName)).json", relativeTo: kSageJsonSchemaBaseURL)!
    }
    
    override init() {
        let examples: [SerializableTextInputItem] = [
            DoubleTextInputItemObject(),
            DurationTextInputItemObject(),
            IntegerTextInputItemObject(),
            StringTextInputItemObject(),
            YearTextInputItemObject(),
        ]
        self.examples = examples
    }
    
    public private(set) var examples: [TextInputItem]
    
    public override class func typeDocumentProperty() -> DocumentProperty {
        .init(propertyType: .reference(TextInputType.documentableType()))
    }
    
    public func add(_ example: SerializableTextInputItem) {
        if let idx = examples.firstIndex(where: {
            ($0 as! PolymorphicRepresentable).typeName == example.typeName }) {
            examples.remove(at: idx)
        }
        examples.append(example)
    }
    
    private enum InterfaceKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case resultIdentifier = "identifier", fieldLabel, placeholder
        var relativeIndex: Int { 1 }
    }
    
    public override class func codingKeys() -> [CodingKey] {
        var keys = super.codingKeys()
        keys.append(contentsOf: InterfaceKeys.allCases)
        return keys
    }
    
    public override class func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? InterfaceKeys else {
            return try super.documentProperty(for: codingKey)
        }
        switch key {
        case .resultIdentifier:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            """
                            The result identifier is an optional value that can be used to help in building the serializable answer result
                            from this ``InputItem``. If null, then it is assumed that the ``Question`` that holds this ``InputItem``
                            has some custom serialization strategy or only contains a single field and this property can be ignored.
                            """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n"))
        case .fieldLabel:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "A localized string that displays a short text offering a hint to the user of the data to be entered for this field.")
        case .placeholder:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "A localized string that displays placeholder information for the ``InputItem``.")
        }
    }
}

public protocol SerializableTextInputItem : TextInputItem, PolymorphicRepresentable, Encodable {
    var textInputType: TextInputType { get }
}

public extension SerializableTextInputItem {
    var typeName: String { return textInputType.rawValue }
}

public enum TextInputType : String, StringEnumSet, DocumentableStringEnum {
    case number, integer, string, year, duration
}

public struct StringTextInputItemObject : SerializableTextInputItem, StringTextInputItem {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case textInputType = "type", resultIdentifier = "identifier", fieldLabel, placeholder, _keyboardOptions = "keyboardOptions", regExValidator, characterLimit
    }
    public private(set) var textInputType: TextInputType = .string
    public let answerType: AnswerType = AnswerTypeString()
    
    public let resultIdentifier: String?
    public let fieldLabel: String?
    public let placeholder: String?
    public let characterLimit: Int?

    public var keyboardOptions: KeyboardOptions {
        _keyboardOptions ?? KeyboardOptionsObject()
    }
    private var _keyboardOptions: KeyboardOptionsObject?

    private let regExValidator: RegExValidator?
    
    public init(fieldLabel: String? = nil,
                placeholder: String? = nil,
                resultIdentifier: String? = nil,
                keyboardOptions: KeyboardOptionsObject? = nil,
                regExValidator: RegExValidator? = nil,
                characterLimit: Int? = nil) {
        self.fieldLabel = fieldLabel
        self.placeholder = placeholder
        self.resultIdentifier = resultIdentifier
        self._keyboardOptions = keyboardOptions
        self.regExValidator = regExValidator
        self.characterLimit = characterLimit
    }
    
    public func buildTextValidator() -> TextEntryValidator {
        regExValidator ?? PassThruValidator()
    }
}

extension StringTextInputItemObject : DocumentableStruct {

    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        codingKey.stringValue == "type"
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not handled by \(self).")
        }
        switch key {
        case .textInputType:
            return .init(constValue: TextInputType.string)
        case .resultIdentifier, .fieldLabel, .placeholder:
            return .init(propertyType: .primitive(.string))
        case ._keyboardOptions:
            return .init(propertyType: .reference(KeyboardOptionsObject.documentableType()), propertyDescription:
                            "The keyboard options to use with this text field.")
        case .regExValidator:
            return .init(propertyType: .reference(RegExValidator.documentableType()), propertyDescription:
                            "The regex validator to use to validate this text field.")
        case .characterLimit:
            return .init(propertyType: .primitive(.integer), propertyDescription:
                            "The character limit for text entry.")
        }
    }
    
    public static func examples() -> [StringTextInputItemObject] {
        [.init()]
    }
}

public struct IntegerTextInputItemObject : SerializableTextInputItem, IntegerTextInputItem {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case textInputType = "type", resultIdentifier = "identifier", fieldLabel, placeholder, formatOptions
    }
    public private(set) var textInputType: TextInputType = .integer
    public let answerType: AnswerType = AnswerTypeInteger()
    
    public let resultIdentifier: String?
    public let fieldLabel: String?
    public let placeholder: String?

    public var keyboardOptions: KeyboardOptions {
        KeyboardOptionsObject.integerEntryOptions
    }

    public let formatOptions: IntegerFormatOptions?
    
    public var range: IntegerRange? { formatOptions }
    
    public init(fieldLabel: String? = nil,
                placeholder: String? = nil,
                resultIdentifier: String? = nil,
                formatOptions: IntegerFormatOptions? = nil) {
        self.fieldLabel = fieldLabel
        self.placeholder = placeholder
        self.resultIdentifier = resultIdentifier
        self.formatOptions = formatOptions
    }
    
    public func buildTextValidator() -> TextEntryValidator {
        formatOptions ?? IntegerFormatOptions()
    }
}

extension IntegerTextInputItemObject : DocumentableStruct {

    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        codingKey.stringValue == "type"
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not handled by \(self).")
        }
        switch key {
        case .textInputType:
            return .init(constValue: TextInputType.integer)
        case .resultIdentifier, .fieldLabel, .placeholder:
            return .init(propertyType: .primitive(.string))
        case .formatOptions:
            return .init(propertyType: .reference(IntegerFormatOptions.documentableType()), propertyDescription:
                            "The formatting and range options to use with input item.")
        }
    }
    
    public static func examples() -> [IntegerTextInputItemObject] {
        [.init()]
    }
}

public struct DoubleTextInputItemObject : SerializableTextInputItem, DoubleTextInputItem {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case textInputType = "type", resultIdentifier = "identifier", fieldLabel, placeholder, formatOptions
    }
    public private(set) var textInputType: TextInputType = .number
    public var answerType: AnswerType {
        AnswerTypeNumber(significantDigits: formatOptions?.significantDigits)
    }
    
    public let resultIdentifier: String?
    public let fieldLabel: String?
    public let placeholder: String?

    public var keyboardOptions: KeyboardOptions {
        KeyboardOptionsObject.decimalEntryOptions
    }

    public let formatOptions: DoubleFormatOptions?
    
    public var range: DoubleRange? { formatOptions }
    
    public init(fieldLabel: String? = nil,
                placeholder: String? = nil,
                resultIdentifier: String? = nil,
                formatOptions: DoubleFormatOptions? = nil) {
        self.fieldLabel = fieldLabel
        self.placeholder = placeholder
        self.resultIdentifier = resultIdentifier
        self.formatOptions = formatOptions
    }
    
    public func buildTextValidator() -> TextEntryValidator {
        formatOptions ?? DoubleFormatOptions()
    }
}

extension DoubleTextInputItemObject : DocumentableStruct {

    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        codingKey.stringValue == "type"
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not handled by \(self).")
        }
        switch key {
        case .textInputType:
            return .init(constValue: TextInputType.number)
        case .resultIdentifier, .fieldLabel, .placeholder:
            return .init(propertyType: .primitive(.string))
        case .formatOptions:
            return .init(propertyType: .reference(DoubleFormatOptions.documentableType()), propertyDescription:
                            "The formatting and range options to use with input item.")
        }
    }
    
    public static func examples() -> [DoubleTextInputItemObject] {
        [.init()]
    }
}

public struct DurationTextInputItemObject : SerializableTextInputItem, DurationTextInputItem {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case textInputType = "type", resultIdentifier = "identifier", fieldLabel, placeholder, _displayUnits = "displayUnits"
    }
    public private(set) var textInputType: TextInputType = .duration
    public var answerType: AnswerType {
        AnswerTypeDuration(displayUnits: displayUnits)
    }
    
    public let resultIdentifier: String?
    public let fieldLabel: String?
    public let placeholder: String?

    public var keyboardOptions: KeyboardOptions {
        KeyboardOptionsObject.integerEntryOptions
    }

    public var displayUnits: [DurationUnit] {
        _displayUnits ?? DurationUnit.defaultDispayUnits
    }
    private let _displayUnits: [DurationUnit]?
    
    public init(fieldLabel: String? = nil,
                placeholder: String? = nil,
                resultIdentifier: String? = nil,
                displayUnits: [DurationUnit]? = nil) {
        self.fieldLabel = fieldLabel
        self.placeholder = placeholder
        self.resultIdentifier = resultIdentifier
        self._displayUnits = displayUnits
    }
    
    public func buildTextValidator() -> TextEntryValidator {
        DoubleFormatOptions()
    }
}

extension DurationTextInputItemObject : DocumentableStruct {

    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        codingKey.stringValue == "type"
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not handled by \(self).")
        }
        switch key {
        case .textInputType:
            return .init(constValue: TextInputType.number)
        case .resultIdentifier, .fieldLabel, .placeholder:
            return .init(propertyType: .primitive(.string))
        case ._displayUnits:
            return .init(propertyType: .referenceArray(DurationUnit.documentableType()), propertyDescription:
                            "The display units to show for duration.")
        }
    }
    
    public static func examples() -> [DurationTextInputItemObject] {
        [.init()]
    }
}

public struct YearTextInputItemObject : SerializableTextInputItem, IntegerTextInputItem {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case textInputType = "type", resultIdentifier = "identifier", fieldLabel, placeholder, formatOptions
    }
    public private(set) var textInputType: TextInputType = .year
    public let answerType: AnswerType = AnswerTypeInteger()
    
    public let resultIdentifier: String?
    public let fieldLabel: String?
    public let placeholder: String?

    public var keyboardOptions: KeyboardOptions {
        KeyboardOptionsObject.integerEntryOptions
    }

    public let formatOptions: YearFormatOptions?
    
    public var range: IntegerRange? { formatOptions }
    
    public init(fieldLabel: String? = nil,
                placeholder: String? = "YYYY",
                resultIdentifier: String? = nil,
                formatOptions: YearFormatOptions? = nil) {
        self.fieldLabel = fieldLabel
        self.placeholder = placeholder
        self.resultIdentifier = resultIdentifier
        self.formatOptions = formatOptions
    }
    
    public func buildTextValidator() -> TextEntryValidator {
        formatOptions ?? YearFormatOptions()
    }
}

extension YearTextInputItemObject : DocumentableStruct {

    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        codingKey.stringValue == "type"
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not handled by \(self).")
        }
        switch key {
        case .textInputType:
            return .init(constValue: TextInputType.year)
        case .resultIdentifier, .fieldLabel, .placeholder:
            return .init(propertyType: .primitive(.string))
        case .formatOptions:
            return .init(propertyType: .reference(YearFormatOptions.documentableType()), propertyDescription:
                            "The formatting and range options to use with input item.")
        }
    }
    
    public static func examples() -> [YearTextInputItemObject] {
        [.init()]
    }
}

