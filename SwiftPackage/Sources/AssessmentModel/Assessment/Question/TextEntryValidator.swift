//
//  TextEntryValidator.swift
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
import SwiftUI

public protocol TextEntryValidator {
    func localizedText(for answer: JsonValue?) -> String?
    func bindingValue(for text: String?) throws -> JsonValue?
    func validateText(_ text: String?) throws -> JsonValue?
    func validateAnswer(_ answer: JsonValue?) throws -> JsonValue?
}

public extension TextEntryValidator {
    func jsonElement(text: String?) throws -> JsonElement? {
        try validateText(text).map { .init($0) }
    }
}

public protocol GenericTextInputValidator : TextEntryValidator {
    associatedtype Value where Value : JsonValue
    
    /// For the given current answer, what is the text that should be displayed in the UI text field?
    func convertToText(from answer: Value?) -> String?
    
    /// Validate that the given text entered is valid and if so, return the converted value that is saved as the answer.
    func convertInput(text: String?) throws -> Value?
    
    /// Check a value that matches the range of values allowed by this validator.
    func convertInput(answer: Value?) throws -> Value?
}

public extension GenericTextInputValidator {
    func validateText(_ text: String?) throws -> JsonValue? {
        return try self.convertInput(text: text)
    }
    func validateAnswer(_ answer: JsonValue?) throws -> JsonValue? {
        guard let value = answer as? Value else { return nil }
        return try self.convertInput(answer: value)
    }
}

/// `TextInputValidatorError` is used when validating a user-entered answer.
public enum TextInputValidatorError: Error {
    
    /// The context for the error.
    public struct Context {
        /// The identifier for the `RSDInputField`.
        public let identifier: String?
        /// The value being validated.
        public let value: Any?
        /// A debug description for the error.
        public let debugDescription: String
        
        public init(identifier: String?, value: Any?, debugDescription: String) {
            self.identifier = identifier
            self.value = value
            self.debugDescription = debugDescription
        }
    }
    
    /// The value entered cannot be converted to the expected answer type.
    case invalidType(Context)
    
    /// The formatter could not convert the value entered to the expected answer type.
    case invalidFormatter(Formatter, Context)
    
    /// The value entered does not match the regex for this field.
    case invalidRegex(String?, Context)
    
    /// The text value entered exceeds the maximum allowed length.
    case exceedsMaxLength(Int, Context)
    
    /// The numeric value entered is less than the minimum allowed value.
    case lessThanMinimumValue(Decimal, Context)
    
    /// The numeric value entered is greater than the maximum allowed value.
    case greaterThanMaximumValue(Decimal, Context)
    
    /// The date entered is less than the minimum allowed date.
    case lessThanMinimumDate(Date, Context)
    
    /// The date entered is greater than the maximum allowed date.
    case greaterThanMaximumDate(Date, Context)
    
    /// The domain of the error.
    public static var errorDomain: String {
        return "TextInputValidatorErrorDomain"
    }
    
    /// The error code within the given domain.
    public var errorCode: Int {
        switch(self) {
        case .invalidType(_):
            return -1
        case .invalidFormatter(_,_):
            return -2
        case .invalidRegex(_,_):
            return -3
        case .exceedsMaxLength(_,_):
            return -4
        case .lessThanMinimumValue(_,_), .greaterThanMaximumValue(_,_):
            return -6
        case .lessThanMinimumDate(_,_), .greaterThanMaximumDate(_,_):
            return -7
        }
    }
    
    /// The context for the error.
    public var context: Context {
        switch(self) {
        case .invalidType(let context):
            return context
        case .invalidFormatter(_, let context):
            return context
        case .invalidRegex(_, let context):
            return context
        case .exceedsMaxLength(_, let context):
            return context
        case .lessThanMinimumValue(_, let context):
            return context
        case .greaterThanMaximumValue(_, let context):
            return context
        case .lessThanMinimumDate(_, let context):
            return context
        case .greaterThanMaximumDate(_, let context):
            return context
        }
    }
    
    /// The user-info dictionary.
    public var errorUserInfo: [String : Any] {
        let description: String = self.context.debugDescription
        return ["NSDebugDescription": description]
    }
}

// MARK : Value == String (string)

public extension GenericTextInputValidator where Value == String {
    func localizedText(for answer: JsonValue?) -> String? {
        guard let value = answer?.jsonObject() else { return nil }
        return self.convertToText(from: "\(value)")
    }
    func convertToText(from answer: String?) -> String? { answer }
    func convertInput(text: String?) throws -> String? {
        try convertInput(answer: text)
    }
    func bindingValue(for text: String?) throws -> JsonValue? {
        text
    }
}

/// A pass thru validator is used where validation is always successful.
public struct PassThruValidator : GenericTextInputValidator {
    public init() {}
    public func convertInput(answer: String?) throws -> String? { answer }
}

/// A validator that can validate text entered using a regular expression.
public struct RegExValidator : GenericTextInputValidator, Codable {
    private enum CodingKeys : String, CodingKey, CaseIterable {
        case pattern, invalidMessage
    }

    let pattern: NSRegularExpression
    let invalidMessage: String

    public init(pattern: NSRegularExpression, invalidMessage: String) {
        self.pattern = pattern
        self.invalidMessage = invalidMessage
    }

    public func convertInput(answer: String?) throws -> String? {
        guard let text = answer else { return nil }
        guard _regExMatches(text) else {
            let context = TextInputValidatorError.Context(identifier: nil, value: text, debugDescription: invalidMessage)
            throw TextInputValidatorError.invalidRegex(text, context)
        }
        return text
    }

    private func _regExMatches(_ string: String) -> Bool {
        pattern.numberOfMatches(in: string, options: [], range: NSRange(string.startIndex..., in: string)) == 1
    }

    // MARK: Codable

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let pattern = try container.decode(String.self, forKey: .pattern)
        do {
            self.pattern = try NSRegularExpression(pattern: pattern, options: [])
        } catch {
            throw DecodingError.dataCorrupted(.init(codingPath: decoder.codingPath,
                                                    debugDescription: "Cannot decode '\(pattern)' as a regular expression.",
                                                    underlyingError: error))
        }
        self.invalidMessage = try container.decode(String.self, forKey: .invalidMessage)
    }

    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(self.pattern.pattern, forKey: .pattern)
        try container.encode(self.invalidMessage, forKey: .invalidMessage)
    }
}

extension RegExValidator : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }

    public static func isRequired(_ codingKey: CodingKey) -> Bool { true }

    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .pattern:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The string value must be a valid a regex pattern.")
        case .invalidMessage:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The invalid message is the localized message to display to the participant if the text entered is invalid.")
        }
    }

    public static func examples() -> [RegExValidator] {
        let pattern = try! NSRegularExpression(pattern: "^[0-9]*$", options: [])
        return [RegExValidator(pattern: pattern, invalidMessage: "Only entering numbers is allowed.")]
    }
}

// MARK : Value == JsonNumber (integer, double, year)

public protocol ScaleRange {
    var minimumLabel: String? { get }
    var maximumLabel: String? { get }
}

public protocol IntegerRange : ScaleRange {
    var minimumValue: Int? { get }
    var maximumValue: Int? { get }
}

public protocol DoubleRange : ScaleRange {
    var minimumValue: Double? { get }
    var maximumValue: Double? { get }
}

/// `Codable` string enum for the number formatter.
public enum NumberFormatStyle : String, StringEnumSet, DocumentableStringEnum {
    case none, decimal, currency, percent, scientific, spellOut, ordinal

    public func formatterStyle() -> NumberFormatter.Style {
        guard let idx = NumberFormatStyle.allCases.firstIndex(of: self),
            let style = NumberFormatter.Style(rawValue: UInt(idx))
            else {
                return .none
        }
        return style
    }
}

public protocol NumberValidator : GenericTextInputValidator where Value : JsonNumber {

    var numberStyle: NumberFormatStyle! { get }
    var usesGroupingSeparator: Bool! { get }
    var maximumFractionDigits: Int! { get }

    var minimumValue: Value? { get }
    var maximumValue: Value? { get }
    var stepInterval: Value? { get }

    var minInvalidMessage: String? { get }
    var maxInvalidMessage: String? { get }
    var invalidMessage: String? { get }

    func convertToValue(from number: NSNumber) -> Value
}

public extension NumberValidator {

    var formatter : NumberFormatter {
        let formatter = NumberFormatter()
        formatter.usesGroupingSeparator = self.usesGroupingSeparator
        formatter.numberStyle = self.numberStyle.formatterStyle()
        formatter.maximumFractionDigits = self.maximumFractionDigits
        return formatter
    }

    var defaultInvalidMessage : String {
        invalidMessage ?? "The number entered is not valid."
    }
    
    func localizedText(for answer: JsonValue?) -> String? {
        guard let value = answer?.jsonObject() as? Value
        else {
            return nil
        }
        return self.convertToText(from: value)
    }

    func bindingValue(for text: String?) throws -> JsonValue? {
        try text.flatMap { str in
            try self.formatter.number(from: str).flatMap { num in
                if let maxNum = self.maximumValue?.jsonNumber(),
                   let maxStr = self.formatter.string(from: maxNum),
                   str.count > maxStr.count {
                    let message = self.maxInvalidMessage ?? defaultInvalidMessage
                    let context = TextInputValidatorError.Context(identifier: nil, value: num, debugDescription: message)
                    throw TextInputValidatorError.greaterThanMaximumValue(maxNum.decimalValue, context)
                }
                return self.convertToValue(from: num)
            }
        }
    }
    
    func convertToText(from answer: Value?) -> String? {
        guard let num = answer?.jsonNumber() else { return nil }
        return self.formatter.string(from: num)
    }

    func convertInput(answer: Value?) throws -> Value? {
        guard let answer = answer else { return nil }
        if let str = answer as? String {
            return try convertInput(text: str)
        }
        else if let num = (answer as? NSNumber) ?? answer.jsonNumber() {
            return try validateNumber(num)
        }
        else {
            let context = TextInputValidatorError.Context(identifier: nil, value: answer, debugDescription: "\(answer) is not a String or a Number")
            throw TextInputValidatorError.invalidType(context)
        }
    }

    func convertInput(text: String?) throws -> Value? {
        guard let str = text, let num = self.formatter.number(from: str) else {
            try validateNil()
            return nil
        }
        return try validateNumber(num)
    }
    
    func validateMax(_ num: NSNumber) throws {
        if let max = self.maximumValue?.jsonNumber(), num.decimalValue > max.decimalValue {
            let message = self.maxInvalidMessage ?? defaultInvalidMessage
            let context = TextInputValidatorError.Context(identifier: nil, value: num, debugDescription: message)
            throw TextInputValidatorError.greaterThanMaximumValue(max.decimalValue, context)
        }
    }

    func validateMin(_ num: NSNumber) throws {
        if let min = self.minimumValue?.jsonNumber(), num.decimalValue < min.decimalValue {
            let message = self.minInvalidMessage ?? defaultInvalidMessage
            let context = TextInputValidatorError.Context(identifier: nil, value: num, debugDescription: message)
            throw TextInputValidatorError.lessThanMinimumValue(min.decimalValue, context)
        }
    }
    
    func validateNumber(_ num: NSNumber) throws -> Value? {
        try validateMin(num)
        try validateMax(num)
        return convertToValue(from: num)
    }

    func validateNil() throws {
        guard minimumValue == nil && maximumValue == nil else {
            let context = TextInputValidatorError.Context(identifier: nil, value: nil, debugDescription: self.defaultInvalidMessage)
            throw TextInputValidatorError.invalidType(context)
        }
    }
}

public struct IntegerFormatOptions : Codable, NumberValidator, IntegerRange {
    public typealias Value = Int

    private enum CodingKeys : String, CodingKey, CaseIterable {
        case _numberStyle = "numberStyle",
             _usesGroupingSeparator = "usesGroupingSeparator",
             minimumValue, maximumValue, stepInterval,
             minInvalidMessage, maxInvalidMessage, invalidMessage,
             minimumLabel, maximumLabel
    }

    public var numberStyle: NumberFormatStyle! {
        get { _numberStyle ?? NumberFormatStyle.none }
        set(newValue) { _numberStyle = newValue }
    }
    private var _numberStyle: NumberFormatStyle?

    public var usesGroupingSeparator: Bool! {
        get { _usesGroupingSeparator ?? true }
        set(newValue) { _usesGroupingSeparator = newValue }
    }
    private var _usesGroupingSeparator: Bool?

    public var maximumFractionDigits: Int! { 0 }

    public var minimumValue: Int?
    public var maximumValue: Int?
    public var stepInterval: Int?
    
    public var minimumLabel: String?
    public var maximumLabel: String?

    public var minInvalidMessage: String?
    public var maxInvalidMessage: String?
    public var invalidMessage: String?

    public func convertToValue(from number: NSNumber) -> Int {
        number.intValue
    }

    public init(minimumValue: Int? = nil, maximumValue: Int? = nil, minimumLabel: String? = nil, maximumLabel: String? = nil) {
        self.minimumValue = minimumValue
        self.maximumValue = maximumValue
        self.minimumLabel = minimumLabel
        self.maximumLabel = maximumLabel
    }
}

extension IntegerFormatOptions : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }

    public static func isRequired(_ codingKey: CodingKey) -> Bool { false }

    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case ._numberStyle:
            return .init(propertyType: .reference(NumberFormatStyle.documentableType()))
        case ._usesGroupingSeparator:
            return .init(propertyType: .primitive(.boolean))
        case .minimumValue, .maximumValue, .stepInterval:
            return .init(propertyType: .primitive(.integer))
        case .minInvalidMessage, .maxInvalidMessage, .invalidMessage:
            return .init(propertyType: .primitive(.string))
        case .minimumLabel, .maximumLabel:
            return .init(propertyType: .primitive(.string))
        }
    }

    public static func examples() -> [IntegerFormatOptions] {
        let exA = IntegerFormatOptions()
        var exB = IntegerFormatOptions()
        exB._numberStyle = .currency
        exB._usesGroupingSeparator = false
        exB.invalidMessage = "This number is not valid"
        exB.maximumValue = 200
        exB.minimumValue = 0
        exB.stepInterval = 5
        exB.minInvalidMessage = "Minimum value is zero"
        exB.maxInvalidMessage = "Maximum value is $200"
        return [exA, exB]
    }
}

public struct YearFormatOptions : Codable, NumberValidator, IntegerRange {
    public typealias Value = Int

    private enum CodingKeys : String, CodingKey, CaseIterable {
        case allowFuture, allowPast, minimumYear, maximumYear,
            minInvalidMessage, maxInvalidMessage, invalidMessage
    }
    
    public static let pastOnly: YearFormatOptions = {
        var options = YearFormatOptions()
        options.allowFuture = false
        return options
    }()
    
    public static let futureOnly: YearFormatOptions = {
        var options = YearFormatOptions()
        options.allowPast = false
        return options
    }()
    
    public static let birthYear: YearFormatOptions = {
        var options = YearFormatOptions()
        options.allowFuture = false
        options.minimumYear = 1900
        return options
    }()

    public var allowFuture: Bool?
    public var allowPast: Bool?
    public var minimumYear: Int?
    public var maximumYear: Int?

    public var minInvalidMessage: String?
    public var maxInvalidMessage: String?
    public var invalidMessage: String?
    
    public var minimumLabel: String? { nil }
    public var maximumLabel: String? { nil }

    public init() {
    }

    public var numberStyle: NumberFormatStyle! { NumberFormatStyle.none }
    public var usesGroupingSeparator: Bool! { false }
    public var maximumFractionDigits: Int! { 0 }

    public var minimumValue: Int? {
        minimumYear ?? ((allowPast ?? true) ? nil : Date().year)
    }
    public var maximumValue: Int? {
        maximumYear ?? ((allowFuture ?? true) ? nil : Date().year)
    }
    public var stepInterval: Int? { 1 }

    public func convertToValue(from number: NSNumber) -> Int {
        number.intValue
    }
}

extension YearFormatOptions : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }

    public static func isRequired(_ codingKey: CodingKey) -> Bool { false }

    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .allowFuture, .allowPast:
            return .init(propertyType: .primitive(.boolean))
        case .maximumYear, .minimumYear:
            return .init(propertyType: .primitive(.integer))
        case .minInvalidMessage, .maxInvalidMessage, .invalidMessage:
            return .init(propertyType: .primitive(.string))
        }
    }

    public static func examples() -> [YearFormatOptions] {
        let exA = YearFormatOptions()
        var exB = YearFormatOptions()
        exB.allowPast = false
        exB.maximumYear = 2030
        var exC = YearFormatOptions()
        exC.allowFuture = false
        exC.minimumYear = 1900
        return [exA, exB, exC]
    }
}

extension Date {
    fileprivate var year: Int {
        Calendar(identifier: .iso8601).component(.year, from: self)
    }
}

public struct DoubleFormatOptions : Codable, NumberValidator, DoubleRange {
    public typealias Value = Double

    private enum CodingKeys : String, CodingKey, CaseIterable {
        case _numberStyle = "numberStyle",
             _usesGroupingSeparator = "usesGroupingSeparator",
             _maximumFractionDigits = "maximumFractionDigits",
             minimumValue, maximumValue, stepInterval,
             minInvalidMessage, maxInvalidMessage, invalidMessage,
             minimumLabel, maximumLabel
    }

    public var numberStyle: NumberFormatStyle! {
        get { _numberStyle ?? NumberFormatStyle.none }
        set(newValue) { _numberStyle = newValue }
    }
    private var _numberStyle: NumberFormatStyle?

    public var usesGroupingSeparator: Bool! {
        get { _usesGroupingSeparator ?? true }
        set(newValue) { _usesGroupingSeparator = newValue }
    }
    private var _usesGroupingSeparator: Bool?

    public var maximumFractionDigits: Int! {
        get { _maximumFractionDigits ?? 2 }
        set(newValue) { _maximumFractionDigits = newValue }
    }
    private var _maximumFractionDigits: Int?
    
    public var significantDigits : Int? {
        _maximumFractionDigits
    }

    public var minimumValue: Double?
    public var maximumValue: Double?
    public var stepInterval: Double?
    
    public var minimumLabel: String?
    public var maximumLabel: String?

    public var minInvalidMessage: String?
    public var maxInvalidMessage: String?
    public var invalidMessage: String?

    public init(minimumValue: Double? = nil, maximumValue: Double? = nil, minimumLabel: String? = nil, maximumLabel: String? = nil) {
        self.minimumValue = minimumValue
        self.maximumValue = maximumValue
        self.minimumLabel = minimumLabel
        self.maximumLabel = maximumLabel
    }

    public func convertToValue(from number: NSNumber) -> Double {
        number.doubleValue
    }
}

extension DoubleFormatOptions : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }

    public static func isRequired(_ codingKey: CodingKey) -> Bool { false }

    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case ._numberStyle:
            return .init(propertyType: .reference(NumberFormatStyle.documentableType()))
        case ._usesGroupingSeparator:
            return .init(propertyType: .primitive(.boolean))
        case ._maximumFractionDigits:
            return .init(propertyType: .primitive(.integer))
        case .minimumValue, .maximumValue, .stepInterval:
            return .init(propertyType: .primitive(.number))
        case .minInvalidMessage, .maxInvalidMessage, .invalidMessage:
            return .init(propertyType: .primitive(.string))
        case .minimumLabel, .maximumLabel:
            return .init(propertyType: .primitive(.string))
        }
    }

    public static func examples() -> [DoubleFormatOptions] {
        let exA = DoubleFormatOptions()
        var exB = DoubleFormatOptions()
        exB._maximumFractionDigits = 2
        exB._numberStyle = .currency
        exB._usesGroupingSeparator = false
        exB.invalidMessage = "This number is not valid"
        exB.maximumValue = 200.0
        exB.minimumValue = 0.0
        exB.stepInterval = 0.01
        exB.minInvalidMessage = "Minimum value is zero"
        exB.maxInvalidMessage = "Maximum value is $200"
        return [exA, exB]
    }
}

public struct LocalTime : Codable, Hashable, Comparable, JsonValue {
    public let hour: Int
    public let minute: Int
    
    public static let min: LocalTime = .init(hour: 0, minute: 0)!
    public static let max: LocalTime = .init(hour: 24, minute: 0)!
    
    public init?(hour: Int, minute: Int) {
        // Allow hour to be both 24 and 0 to indicate start and end of day
        guard hour >= 0, minute < 60, minute >= 0,
              (hour < 24 || minute == 0)
        else {
            return nil
        }
        self.hour = hour
        self.minute = minute
    }
    
    public init?(from str: String) {
        let split = str.split(separator: ":")
        guard split.count >= 2,
              let hour = Int(split[0]),
              let minute = Int(split[1]),
              let time = Self.init(hour: hour, minute: minute)
        else {
            return nil
        }
        self = time
    }
    
    public init(from date: Date) {
        let calendar = Calendar(identifier: .iso8601)
        let dateComponents = calendar.dateComponents([.hour, .minute], from: date)
        self.hour = dateComponents.hour!
        self.minute = dateComponents.minute!
    }
    
    public init?(from jsonElement: JsonElement) {
        guard case .string(let str) = jsonElement,
              let time = Self.init(from: str)
        else {
            return nil
        }
        self = time
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.singleValueContainer()
        let str = try container.decode(String.self)
        guard let time = Self.init(from: str) else {
            throw DecodingError.dataCorrupted(.init(codingPath: decoder.codingPath, debugDescription: "LocalTime string format not expected: \(str)"))
        }
        self = time
    }
    
    public func encode(to encoder: Encoder) throws {
        var container = encoder.singleValueContainer()
        let str = stringValue()
        try container.encode(str)
    }
    
    public func jsonElement() -> JsonElement {
        .string(stringValue())
    }
    
    public func jsonObject() -> JsonSerializable {
        stringValue()
    }
    
    public func date(from date: Date = Date()) -> Date {
        Calendar(identifier: .iso8601).startOfDay(for: date).addingTimeInterval(Double(self.hour * 60 * 60 + self.minute * 60))
    }
    
    func stringValue() -> String {
        String(format: "%02d:%02d:00.000", hour, minute)
    }
    
    public static func < (lhs: LocalTime, rhs: LocalTime) -> Bool {
        lhs.hour * 60 + lhs.minute < rhs.hour * 60 + rhs.minute
    }
}

public protocol TimeRange {
    var allowFuture: Bool? { get }
    var allowPast: Bool? { get }
    var minimumValue: LocalTime? { get }
    var maximumValue: LocalTime? { get }
}

public struct TimeFormatOptions : TimeRange, Hashable, Codable {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case allowFuture, allowPast, minimumValue, maximumValue
    }
    
    public let allowFuture: Bool?
    public let allowPast: Bool?
    public let minimumValue: LocalTime?
    public let maximumValue: LocalTime?
    
    public static let pastOnly: TimeFormatOptions = .init(allowFuture: false, allowPast: nil, minimumValue: nil, maximumValue: nil)
    public static let futureOnly: TimeFormatOptions = .init(allowFuture: nil, allowPast: false, minimumValue: nil, maximumValue: nil)
    
    public init() {
        self.init(allowFuture: nil, allowPast: nil, minimumValue: nil, maximumValue: nil)
    }
    
    public init(from min: LocalTime, to max: LocalTime) {
        self.init(allowFuture: nil, allowPast: nil, minimumValue: min, maximumValue: max)
    }
    
    private init(allowFuture: Bool?, allowPast: Bool?, minimumValue: LocalTime?, maximumValue: LocalTime?) {
        self.allowFuture = allowFuture
        self.allowPast = allowPast
        self.minimumValue = minimumValue
        self.maximumValue = maximumValue
    }
}

extension TimeFormatOptions : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }

    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        false
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .allowPast:
            return .init(propertyType: .primitive(.boolean), propertyDescription:
                            "Should this time range allow times that occured in the past? (ie. earlier today)")
        case .allowFuture:
            return .init(propertyType: .primitive(.boolean), propertyDescription:
                            "Should this time range allow times that occur in the future? (ie. later today)")
        case .minimumValue:
            return .init(propertyType: .format(.time), propertyDescription:
                            "Should this time range be timeboxed to have a minimum time of day?")
        case .maximumValue:
            return .init(propertyType: .format(.time), propertyDescription:
                            "Should this time range be timeboxed to have a maximum time of day?")
        }
    }
    
    public static func examples() -> [TimeFormatOptions] {
        [.init(), .pastOnly, .futureOnly, .init(from: .init(hour: 6, minute: 0)!, to: .init(hour: 10, minute: 30)!)]
    }
}

