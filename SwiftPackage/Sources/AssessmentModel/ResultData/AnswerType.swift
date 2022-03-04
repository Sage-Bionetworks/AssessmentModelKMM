//
//  AnswerType.swift
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

/// The coding information for an answer. This allows for adding custom information to the "kind" of question because
/// JSON only supports a small subset of value types that are often encoded and can be described using JSON Schema
/// or Swagger.
public protocol AnswerType : PolymorphicTyped, DictionaryRepresentable {

    /// The `JsonType` for the base value.
    var baseType: JsonType { get }
    
    /// Decode the JsonElement for this AnswerType from the given decoder.
    ///
    /// - parameter decoder: The nested decoder for this json element.
    /// - returns: The decoded value or `nil` if the value is not present.
    /// - throws: `DecodingError` if the encountered stored value cannot be decoded.
    func decodeValue(from decoder: Decoder) throws -> JsonElement
    
    /// Decode a `JsonElement` into the expected class type.
    ///
    /// - parameter jsonValue: The JSON value (from an array or dictionary) with the answer.
    /// - returns: The decoded value or `nil` if the value is not present.
    /// - throws: `DecodingError` if the encountered stored value cannot be decoded.
    func decodeAnswer(from jsonValue: JsonElement?) throws -> Any?
    
    /// Returns a `JsonElement` that is encoded for this answer type from the given value.
    ///
    /// - parameter value: The value to encode.
    /// - returns: The JSON serializable object for this encodable.
    func encodeAnswer(from value: Any?) throws -> JsonElement
}

public final class AnswerTypeSerializer : AbstractPolymorphicSerializer, PolymorphicSerializer {
    public var documentDescription: String? {
        """
        `AnswerType` is used to allow carrying additional information about the properties of a
        JSON-encoded `AnswerResult`.
        """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n")
    }
    
    public var jsonSchema: URL {
        URL(string: "\(self.interfaceName).json", relativeTo: kSageJsonSchemaBaseURL)!
    }
    
    override init() {
        examples = [
            AnswerTypeArray.examples().first!,
            AnswerTypeBoolean.examples().first!,
            AnswerTypeDateTime.examples().first!,
            AnswerTypeInteger.examples().first!,
            AnswerTypeMeasurement.examples().first!,
            AnswerTypeNumber.examples().first!,
            AnswerTypeObject.examples().first!,
            AnswerTypeString.examples().first!,
        ]
    }
    
    public private(set) var examples: [AnswerType]
    
    public override class func typeDocumentProperty() -> DocumentProperty {
        .init(propertyType: .reference(AnswerTypeType.documentableType()))
    }
    
    public func add(_ example: AnswerType) {
        if let idx = examples.firstIndex(where: { $0.typeName == example.typeName }) {
            examples.remove(at: idx)
        }
        examples.append(example)
    }
}

public protocol SerializableAnswerType : AnswerType, PolymorphicRepresentable, Encodable {
    var serializableType: AnswerTypeType { get }
}

extension SerializableAnswerType {
    public var typeName: String { serializableType.stringValue }
    
    public func jsonDictionary() throws -> [String : JsonSerializable] {
        try jsonEncodedDictionary()
    }
}

public struct AnswerTypeType : TypeRepresentable, Codable, Hashable {
    public let rawValue: String
    public init(rawValue: String) {
        self.rawValue = rawValue
    }
    
    public init(jsonType: JsonType) {
        self.rawValue = jsonType.rawValue
    }
    
    static public let measurement: AnswerTypeType = "measurement"
    static public let dateTime: AnswerTypeType = "date-time"
    
    static public let array: AnswerTypeType = AnswerTypeType(jsonType: .array)
    static public let boolean: AnswerTypeType = AnswerTypeType(jsonType: .boolean)
    static public let integer: AnswerTypeType = AnswerTypeType(jsonType: .integer)
    static public let null: AnswerTypeType = AnswerTypeType(jsonType: .null)
    static public let number: AnswerTypeType = AnswerTypeType(jsonType: .number)
    static public let object: AnswerTypeType = AnswerTypeType(jsonType: .object)
    static public let string: AnswerTypeType = AnswerTypeType(jsonType: .string)
    
    static func allStandardTypes() -> [AnswerTypeType] {
        return [.measurement, .dateTime, .array, .boolean, .integer, .number, .object, .string]
    }
}

extension AnswerTypeType : ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        self.init(rawValue: value)
    }
}

extension AnswerTypeType : DocumentableStringLiteral {
    public static func examples() -> [String] {
        allStandardTypes().map { $0.rawValue }
    }
}

public protocol BaseAnswerType : SerializableAnswerType {
    static var defaultJsonType: JsonType { get }
}

extension BaseAnswerType {
    public var typeName: String { serializableType.rawValue }
    
    public var baseType: JsonType {
        return type(of: self).defaultJsonType
    }
}

extension JsonType {
    public var answerType : AnswerType {
        switch self {
        case .boolean:
            return AnswerTypeBoolean()
        case .string:
            return AnswerTypeString()
        case .number:
            return AnswerTypeNumber()
        case .integer:
            return AnswerTypeInteger()
        case .null:
            return AnswerTypeNull()
        case .array:
            return AnswerTypeArray()
        case .object:
            return AnswerTypeObject()
        }
    }
}

extension AnswerType {
    fileprivate func decodingError(_ codingPath: [CodingKey] = []) -> DecodingError {
        let context = DecodingError.Context(codingPath: codingPath,
                                            debugDescription: "Could not decode the value into the expected JsonType for \(self)")
        return DecodingError.dataCorrupted(context)
    }
    fileprivate func encodingError(_ value: Any, _ codingPath: [CodingKey] = []) -> EncodingError {
        let context = EncodingError.Context(codingPath: codingPath,
                                            debugDescription: "Could not encode \(value) to the expected JsonType for \(self)")
        return EncodingError.invalidValue(value, context)
    }
}

public struct AnswerTypeObject : BaseAnswerType, Codable, Hashable {
    private enum CodingKeys : String, CodingKey, CaseIterable {
        case serializableType = "type"
    }
    public static let defaultJsonType: JsonType = .object
    public static let defaultType: AnswerTypeType = .object
    public private(set) var serializableType: AnswerTypeType = Self.defaultType
    public init() {
    }
    
    public func decodeValue(from decoder: Decoder) throws -> JsonElement {
        let obj = try JsonElement(from: decoder)
        switch obj {
        case .null, .object(_):
            return obj
        default:
            throw decodingError(decoder.codingPath)
        }
    }
    
    public func decodeAnswer(from jsonValue: JsonElement?) throws -> Any? {
        guard let value = jsonValue, value != .null else { return nil }
        guard case .object(let obj) = value else {
            throw decodingError()
        }
        return obj
    }
    
    public func encodeAnswer(from value: Any?) throws -> JsonElement {
        guard let value = value else { return .null }
        if let jsonElement = value as? JsonElement, case .object(_) = jsonElement {
            return jsonElement
        }
        else if let obj = value as? NSDictionary {
            return JsonElement(obj)
        }
        else {
            throw encodingError(value)
        }
    }
}

public struct AnswerTypeString : BaseAnswerType, Codable, Hashable {
    private enum CodingKeys : String, CodingKey, CaseIterable {
        case serializableType = "type"
    }
    public static let defaultJsonType: JsonType = .string
    public static let defaultType: AnswerTypeType = .string
    public private(set) var serializableType: AnswerTypeType = Self.defaultType
    public init() {
    }
    
    public func decodeValue(from decoder: Decoder) throws -> JsonElement {
        let obj = try JsonElement(from: decoder)
        switch obj {
        case .null, .string(_):
            return obj
        default:
            throw decodingError(decoder.codingPath)
        }
    }
    
    public func decodeAnswer(from jsonValue: JsonElement?) throws -> Any? {
        guard let value = jsonValue, value != .null else { return nil }
        guard case .string(let obj) = value else {
            throw decodingError()
        }
        return obj
    }
    
    public func encodeAnswer(from value: Any?) throws -> JsonElement {
        guard let value = value else { return .null }
        if let jsonElement = value as? JsonElement, case .string(_) = jsonElement {
            return jsonElement
        }
        else {
            if let comparableValue = value as? CustomStringConvertible {
                return .string(comparableValue.description)
            }
            else {
                return .string("\(value)")
            }
        }
    }
}

public struct AnswerTypeBoolean : BaseAnswerType, Codable, Hashable {
    private enum CodingKeys : String, CodingKey, CaseIterable {
        case serializableType = "type"
    }
    public static let defaultJsonType: JsonType = .boolean
    public static let defaultType: AnswerTypeType = .boolean
    public private(set) var serializableType: AnswerTypeType = Self.defaultType
    public init() {
    }
    
    public func decodeValue(from decoder: Decoder) throws -> JsonElement {
        let obj = try JsonElement(from: decoder)
        switch obj {
        case .null, .boolean(_):
            return obj
        case .integer(let value):
            return .boolean((value as NSNumber).boolValue)
        case .number(let value):
            return .boolean(value.jsonNumber()?.boolValue ?? false)
        case .string(let value):
            return .boolean((value as NSString).boolValue)
        default:
            throw decodingError(decoder.codingPath)
        }
    }
    
    public func decodeAnswer(from jsonValue: JsonElement?) throws -> Any? {
        guard let value = jsonValue, value != .null else { return nil }
        switch value {
        case .boolean(let boolValue):
            return boolValue
        case .integer(let intValue):
            return intValue != 0
        case .number(let numValue):
            return numValue.jsonNumber()?.boolValue
        case .string(let stringValue):
            return (stringValue as NSString).boolValue
        default:
            throw decodingError()
        }
    }
    
    public func encodeAnswer(from value: Any?) throws -> JsonElement {
        guard let value = value else { return .null }
        if let jsonElement = value as? JsonElement, case .boolean(_) = jsonElement {
            return jsonElement
        }
        else if let obj = value as? Bool {
            return .boolean(obj)
        }
        else {
            throw encodingError(value)
        }
    }
}

public struct AnswerTypeInteger : BaseAnswerType, Codable, Hashable {
    private enum CodingKeys : String, CodingKey, CaseIterable {
        case serializableType = "type"
    }
    public static let defaultJsonType: JsonType = .integer
    public static let defaultType: AnswerTypeType = .integer
    public private(set) var serializableType: AnswerTypeType = Self.defaultType
    public init() {
    }
    
    public func decodeValue(from decoder: Decoder) throws -> JsonElement {
        let obj = try JsonElement(from: decoder)
        switch obj {
        case .null, .integer(_):
            return obj
        case .number(let value):
            return .integer(value.jsonNumber()?.intValue ?? 0)
        case .string(let value):
            return .integer((value as NSString).integerValue)
        default:
            throw decodingError(decoder.codingPath)
        }
    }
    
    public func decodeAnswer(from jsonValue: JsonElement?) throws -> Any? {
        guard let value = jsonValue, value != .null else { return nil }
        switch value {
        case .integer(let intValue):
            return intValue
        case .number(let numValue):
            return numValue.jsonNumber()?.intValue
        case .string(let stringValue):
            return (stringValue as NSString).integerValue
        default:
            throw decodingError()
        }
    }
    
    public func encodeAnswer(from value: Any?) throws -> JsonElement {
        guard let value = value else { return .null }
        if let jsonElement = value as? JsonElement, case .integer(_) = jsonElement {
            return jsonElement
        }
        else if let num = (value as? NSNumber) ?? (value as? JsonNumber)?.jsonNumber() {
            return .integer(num.intValue)
        }
        else {
            throw encodingError(value)
        }
    }
}

public struct AnswerTypeNumber : BaseAnswerType, Codable, Hashable {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case serializableType = "type", significantDigits
    }
    public static let defaultJsonType: JsonType = .number
    public static let defaultType: AnswerTypeType = .number
    public private(set) var serializableType: AnswerTypeType = Self.defaultType
    public let significantDigits: Int?
    public init(significantDigits: Int? = nil) {
        self.significantDigits = significantDigits
    }
}
extension AnswerTypeNumber : NumberJsonType {
}

protocol NumberJsonType : AnswerType {
}

extension NumberJsonType {
    public func decodeValue(from decoder: Decoder) throws -> JsonElement {
        let obj = try JsonElement(from: decoder)
        switch obj {
        case .null, .number(_):
            return obj
        case .integer(let value):
            return .number(value)
        case .string(let value):
            return .number((value as NSString).doubleValue)
        default:
            throw decodingError(decoder.codingPath)
        }
    }
    
    public func decodeAnswer(from jsonValue: JsonElement?) throws -> Any? {
        guard let value = jsonValue, value != .null else { return nil }
        switch value {
        case .integer(let intValue):
            return intValue
        case .number(let numValue):
            return numValue
        case .string(let stringValue):
            return (stringValue as NSString).doubleValue
        default:
            throw decodingError()
        }
    }
    
    public func encodeAnswer(from value: Any?) throws -> JsonElement {
        guard let value = value else { return .null }
        if let jsonElement = value as? JsonElement, case .number(_) = jsonElement {
            return jsonElement
        }
        else if let num = (value as? JsonNumber) ?? (value as? NSNumber)?.doubleValue {
            return .number(num)
        }
        else {
            throw encodingError(value)
        }
    }
}

public struct AnswerTypeNull : BaseAnswerType, Codable, Hashable {
    private enum CodingKeys : String, CodingKey, CaseIterable {
        case serializableType = "type"
    }
    public static let defaultJsonType: JsonType = .null
    public static let defaultType: AnswerTypeType = .null
    public private(set) var serializableType: AnswerTypeType = Self.defaultType
    public init() {
    }
    
    public func decodeValue(from decoder: Decoder) throws -> JsonElement {
        throw decodingError(decoder.codingPath)
    }
    
    public func decodeAnswer(from jsonValue: JsonElement?) throws -> Any? {
        throw decodingError()
    }
    
    public func encodeAnswer(from value: Any?) throws -> JsonElement {
        return .null
    }
}

public struct AnswerTypeArray : SerializableAnswerType, Codable, Hashable {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case serializableType = "type", baseType, sequenceSeparator
    }
    public static let defaultType: AnswerTypeType = .array
    public private(set) var serializableType: AnswerTypeType = Self.defaultType
    public let baseType: JsonType
    public let sequenceSeparator: String?
    public init(baseType: JsonType = .string, sequenceSeparator: String? = nil) {
        self.baseType = baseType
        self.sequenceSeparator = sequenceSeparator
    }
    
    public func decodeValue(from decoder: Decoder) throws -> JsonElement {
        let obj = try JsonElement(from: decoder)
        return try _decodeArray(obj, decoder.codingPath)
    }
    
    private func _decodeArray(_ obj: JsonElement, _ codingPath: [CodingKey]) throws -> JsonElement {
        if case .string(let stringValue) = obj,
            let separator = sequenceSeparator {
            let stringArray = stringValue.components(separatedBy: separator)
            let arr: [JsonSerializable] = try stringArray.map {
                switch self.baseType {
                case .integer:
                    return ($0 as NSString).integerValue
                case .boolean:
                    return ($0 as NSString).boolValue
                case .number:
                    return ($0 as NSString).doubleValue
                case .string:
                    return $0
                default:
                    let context = DecodingError.Context(codingPath: codingPath,
                                                        debugDescription: "A base type of `object` is not valid for an AnswerTypeArray with a non-nil separator")
                    throw DecodingError.dataCorrupted(context)
                }
            }
            return .array(arr)
        }
        else if case .array(_) = obj {
            return obj
        }
        else {
            throw decodingError(codingPath)
        }
    }
    
    public func decodeAnswer(from jsonValue: JsonElement?) throws -> Any? {
        guard let obj = jsonValue else { return nil }
        let convertedObj = try _decodeArray(obj, [])
        guard case .array(let arr) = convertedObj else {
            throw decodingError()
        }
        return arr
    }
    
    public func encodeAnswer(from value: Any?) throws -> JsonElement {
        guard let value = value else { return .null }
        let obj = (value as? JsonElement)?.jsonObject() ?? value
        let arr: [Any] = (obj as? [Any]) ?? [obj]
        if let separator = sequenceSeparator {
            let str = arr.map {
                if let comparableValue = $0 as? CustomStringConvertible {
                    return comparableValue.description
                }
                else {
                    return "\($0)"
                }
            }.joined(separator: separator)
            return .string(str)
        }
        else {
            return .array(arr.jsonArray())
        }
    }
}

public struct AnswerTypeDateTime : BaseAnswerType, Codable, Hashable {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case serializableType = "type", _codingFormat = "codingFormat"
    }
    public static let defaultJsonType: JsonType = .string
    public static let defaultType: AnswerTypeType = .dateTime
    public private(set) var serializableType: AnswerTypeType = Self.defaultType
    
    public var codingFormat: String {
        _codingFormat ?? ISO8601TimestampFormatter.dateFormat
    }
    private let _codingFormat: String?
    
    public var formatter: DateFormatter {
        let formatter = DateFormatter()
        formatter.dateFormat = codingFormat
        return formatter
    }
    
    public init(codingFormat: String? = nil) {
        self._codingFormat = codingFormat
    }
    
    public func decodeValue(from decoder: Decoder) throws -> JsonElement {
        let obj = try JsonElement(from: decoder)
        switch obj {
        case .null, .string(_):
            return obj
        default:
            throw decodingError(decoder.codingPath)
        }
    }
    
    public func decodeAnswer(from jsonValue: JsonElement?) throws -> Any? {
        guard let value = jsonValue else { return nil }
        guard case .string(let obj) = value else {
            throw decodingError()
        }
        return formatter.date(from: obj)
    }
    
    public func encodeAnswer(from value: Any?) throws -> JsonElement {
        guard let value = value else { return .null }
        if let jsonElement = value as? JsonElement, case .string(_) = jsonElement {
            return jsonElement
        }
        else if let stringValue = value as? String {
            return .string(stringValue)
        }
        else if let dateValue = value as? Date {
            let stringValue = formatter.string(from: dateValue)
            return .string(stringValue)
        }
        else {
            throw encodingError(value)
        }
    }
}

public struct AnswerTypeMeasurement : BaseAnswerType, Codable, Hashable {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case serializableType = "type", unit
    }
    public static let defaultJsonType: JsonType = .number
    public static let defaultType: AnswerTypeType = .measurement
    public private(set) var serializableType: AnswerTypeType = Self.defaultType
    public let unit: String?
    
    public init(unit: String? = nil) {
        self.unit = unit
    }
}
extension AnswerTypeMeasurement : NumberJsonType {
}

// MARK: Documentable

protocol AnswerTypeDocumentable {
    static func exampleTypeAndValues() -> [(AnswerType, JsonElement)]
}

struct AnswerTypeExamples {
    
    static func examplesWithValues() -> [(AnswerType, JsonElement)] {
        documentableTypes.flatMap { $0.exampleTypeAndValues() }
    }
    
    static let documentableTypes: [AnswerTypeDocumentable.Type] = [
        AnswerTypeBoolean.self,
        AnswerTypeInteger.self,
        AnswerTypeNumber.self,
        AnswerTypeObject.self,
        AnswerTypeString.self,
        AnswerTypeArray.self,
        AnswerTypeDateTime.self,
        AnswerTypeMeasurement.self,
    ]
}

extension AnswerTypeObject : AnswerTypeDocumentable, DocumentableStruct {
    public static func codingKeys() -> [CodingKey] { CodingKeys.allCases }
    public static func isRequired(_ codingKey: CodingKey) -> Bool { true }
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        .init(constValue: defaultType)
    }
    
    public static func examples() -> [AnswerTypeObject] {
        exampleTypeAndValues().map { $0.0 as! Self }
    }

    static func exampleTypeAndValues() -> [(AnswerType, JsonElement)] {
        [(AnswerTypeObject(), .object(["foo":"ba"]))]
    }
}

extension AnswerTypeString : AnswerTypeDocumentable, DocumentableStruct {
    public static func codingKeys() -> [CodingKey] { CodingKeys.allCases }
    public static func isRequired(_ codingKey: CodingKey) -> Bool { true }
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        .init(constValue: defaultType)
    }

    public static func examples() -> [AnswerTypeString] {
        exampleTypeAndValues().map { $0.0 as! Self }
    }

    static func exampleTypeAndValues() -> [(AnswerType, JsonElement)] {
        [(AnswerTypeString(), .string("foo"))]
    }
}

extension AnswerTypeBoolean : AnswerTypeDocumentable, DocumentableStruct {
    public static func codingKeys() -> [CodingKey] { CodingKeys.allCases }
    public static func isRequired(_ codingKey: CodingKey) -> Bool { true }
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        .init(constValue: defaultType)
    }

    public static func examples() -> [AnswerTypeBoolean] {
        exampleTypeAndValues().map { $0.0 as! Self }
    }

    static func exampleTypeAndValues() -> [(AnswerType, JsonElement)] {
        [(AnswerTypeBoolean(), .boolean(true))]
    }
}

extension AnswerTypeInteger : AnswerTypeDocumentable, DocumentableStruct {
    public static func codingKeys() -> [CodingKey] { CodingKeys.allCases }
    public static func isRequired(_ codingKey: CodingKey) -> Bool { true }
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        .init(constValue: defaultType)
    }

    public static func examples() -> [AnswerTypeInteger] {
        exampleTypeAndValues().map { $0.0 as! Self }
    }

    static func exampleTypeAndValues() -> [(AnswerType, JsonElement)] {
        [(AnswerTypeInteger(), .integer(42))]
    }
}

extension AnswerTypeNumber : AnswerTypeDocumentable, DocumentableStruct {
    public static func codingKeys() -> [CodingKey] { CodingKeys.allCases }
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else { return false }
        return key == .serializableType
    }
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .serializableType:
            return .init(constValue: defaultType)
        case .significantDigits:
            return .init(propertyType: .primitive(.number), propertyDescription:
                            "The number of significant digits to use in encoding the answer.")
        }
    }

    public static func examples() -> [AnswerTypeNumber] {
        exampleTypeAndValues().map { $0.0 as! Self }
    }

    static func exampleTypeAndValues() -> [(AnswerType, JsonElement)] {
        [(AnswerTypeNumber(), .number(3.14))]
    }
}

extension AnswerTypeArray : AnswerTypeDocumentable, DocumentableStruct {
    public static func codingKeys() -> [CodingKey] { CodingKeys.allCases }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else { return false }
        return key == .serializableType || key == .baseType
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .serializableType:
            return .init(constValue: defaultType)
        case .baseType:
            return .init(propertyType: .reference(JsonType.documentableType()), propertyDescription:
                            "The base type of the array.")
        case .sequenceSeparator:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The sequence separator to use for arrays that should be encoded as strings.")
        }
    }

    public static func examples() -> [AnswerTypeArray] {
        exampleTypeAndValues().map { $0.0 as! Self }
    }
    
    static func exampleTypeAndValues() -> [(AnswerType, JsonElement)] {
        [
            (AnswerTypeArray(baseType: .number), .array([3.2, 5.1])),
            (AnswerTypeArray(baseType: .integer), .array([1, 5])),
            (AnswerTypeArray(baseType: .string), .array(["foo", "ba", "lalala"])),
        ]
    }
}

extension AnswerTypeDateTime : AnswerTypeDocumentable, DocumentableStruct {
    public static func codingKeys() -> [CodingKey] { CodingKeys.allCases }

    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else { return false }
        return key == .serializableType
    }

    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .serializableType:
            return .init(constValue: defaultType)
        case ._codingFormat:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The iso8601 format for the date-time components used by this answer type.")
        }
    }

    public static func examples() -> [AnswerTypeDateTime] {
        exampleTypeAndValues().map { $0.0 as! Self }
    }
    
    static func exampleTypeAndValues() -> [(AnswerType, JsonElement)] {
        [
            (AnswerTypeDateTime(codingFormat: "yyyy-MM"), .string("2020-04")),
            (AnswerTypeDateTime(codingFormat: "HH:mm"), .string("08:30")),
            (AnswerTypeDateTime(), .string("2017-10-16T22:28:09.000-07:00")),
        ]
    }
}

extension AnswerTypeMeasurement : AnswerTypeDocumentable, DocumentableStruct {
    public static func codingKeys() -> [CodingKey] { CodingKeys.allCases }

    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else { return false }
        return key == .serializableType
    }

    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .serializableType:
            return .init(constValue: defaultType)
        case .unit:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The unit of measurement into which the value is converted for storage.")
        }
    }

    public static func examples() -> [AnswerTypeMeasurement] {
        exampleTypeAndValues().map { $0.0 as! Self }
    }
    
    static func exampleTypeAndValues() -> [(AnswerType, JsonElement)] {
        [(AnswerTypeMeasurement(unit: "cm"), .number(170.2))]
    }
}

public extension JsonElement {
    var answerType: AnswerType {
        switch self {
        case .null:
            return AnswerTypeNull()
        case .boolean(_):
            return AnswerTypeBoolean()
        case .string(_):
            return AnswerTypeString()
        case .integer(_):
            return AnswerTypeInteger()
        case .number(_):
            return AnswerTypeNumber()
        case .array(let arr):
            if arr is [Int] {
                return AnswerTypeArray(baseType: .integer)
            } else if arr is [NSNumber] || arr is [JsonNumber] {
                return AnswerTypeArray(baseType: .number)
            } else if arr is [String] {
                return AnswerTypeArray(baseType: .string)
            } else {
                return AnswerTypeArray(baseType: .object)
            }
        case .object(_):
            return AnswerTypeObject()
        }
    }
}

