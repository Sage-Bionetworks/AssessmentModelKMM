//
//  KeyboardOptions.swift
//

import Foundation
import JsonModel

/// A set of options for the keyboard to use for test entry.
public protocol KeyboardOptions {
    
    /// Is the text field for password entry?
    var isSecureTextEntry: Bool { get }
    
    /// Auto-capitalization type for the text field.
    var autocapitalizationType: TextAutoCapitalizationType { get }
    
    /// Auto-correction type for the text field.
    var autocorrectionType: TextAutoCorrectionType { get }
    
    /// Spell checking type for the text field.
    var spellCheckingType: TextSpellCheckingType { get }
    
    /// Keyboard type for the text field.
    var keyboardType: KeyboardType { get }
}

/// `Codable` enum for the auto-capitalization type for an input text field.
/// - keywords: ["none", "words", "sentences", "allCharacters"]
public enum TextAutoCapitalizationType : String, Codable, StringEnumSet, DocumentableStringEnum {
    case none, words, sentences, allCharacters
}


/// `Codable` enum for the auto-correction type for an input text field.
public enum TextAutoCorrectionType : String, Codable, StringEnumSet, DocumentableStringEnum {
    case `default`, no, yes
}


/// `Codable` enum for the spell checking type for an input text field.
public enum TextSpellCheckingType  : String, Codable, StringEnumSet, DocumentableStringEnum {
    case `default`, no, yes
}


/// `Codable` enum for the keyboard type for an input text field.
public enum KeyboardType  : String, Codable, StringEnumSet, DocumentableStringEnum {
    case `default`, asciiCapable, numbersAndPunctuation, URL, numberPad, phonePad, namePhonePad, emailAddress, decimalPad, twitter, webSearch, asciiCapableNumberPad
}

@Serializable
public struct KeyboardOptionsObject : KeyboardOptions, Codable, Equatable {
    public private(set) var isSecureTextEntry: Bool = false
    public private(set) var autocapitalizationType: TextAutoCapitalizationType = .none
    public private(set) var autocorrectionType: TextAutoCorrectionType = .no
    public private(set) var spellCheckingType: TextSpellCheckingType = .no
    public private(set) var keyboardType: KeyboardType = .default
}

extension KeyboardOptionsObject {
    
    public static let integerEntryOptions = KeyboardOptionsObject(isSecureTextEntry: false,
                                                                  autocapitalizationType: TextAutoCapitalizationType.none,
                                                                  autocorrectionType: .no,
                                                                  spellCheckingType: .no,
                                                                  keyboardType: .numberPad)

    public static let decimalEntryOptions = KeyboardOptionsObject(isSecureTextEntry: false,
                                                                  autocapitalizationType: TextAutoCapitalizationType.none,
                                                                  autocorrectionType: .no,
                                                                  spellCheckingType: .no,
                                                                  keyboardType: .decimalPad)
    
    public static let dateTimeEntryOptions = KeyboardOptionsObject(isSecureTextEntry: false,
                                                                   autocapitalizationType: TextAutoCapitalizationType.none,
                                                                   autocorrectionType: .no,
                                                                   spellCheckingType: .no,
                                                                   keyboardType: .numbersAndPunctuation)
    
    public static let measurementEntryOptions = KeyboardOptionsObject(isSecureTextEntry: false,
                                                                   autocapitalizationType: TextAutoCapitalizationType.none,
                                                                   autocorrectionType: .no,
                                                                   spellCheckingType: .no,
                                                                   keyboardType: .numbersAndPunctuation)
}

extension KeyboardOptionsObject : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool { false }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .isSecureTextEntry:
            return .init(defaultValue: .boolean(false))
        case .autocapitalizationType:
            return .init(propertyType: .reference(TextAutoCapitalizationType.documentableType()))
        case .autocorrectionType:
            return .init(propertyType: .reference(TextAutoCorrectionType.documentableType()))
        case .spellCheckingType:
            return .init(propertyType: .reference(TextSpellCheckingType.documentableType()))
        case .keyboardType:
            return .init(propertyType: .reference(KeyboardType.documentableType()))
        }
    }
    
    public static func examples() -> [KeyboardOptionsObject] {
        [KeyboardOptionsObject(), .decimalEntryOptions]
    }
}

