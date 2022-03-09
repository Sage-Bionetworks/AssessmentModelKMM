//
//  KeyboardOptions.swift
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

public struct KeyboardOptionsObject : KeyboardOptions, Codable, Equatable {
    private enum CodingKeys : String, CodingKey, CaseIterable {
        case _isSecureTextEntry = "isSecureTextEntry"
        case _autocapitalizationType = "autocapitalizationType"
        case _autocorrectionType = "autocorrectionType"
        case _spellCheckingType = "spellCheckingType"
        case _keyboardType = "keyboardType"
    }
    
    public var isSecureTextEntry: Bool { _isSecureTextEntry ?? false }
    private var _isSecureTextEntry: Bool?
    
    public var autocapitalizationType: TextAutoCapitalizationType { _autocapitalizationType ?? .none }
    private var _autocapitalizationType: TextAutoCapitalizationType?
    
    public var autocorrectionType: TextAutoCorrectionType { _autocorrectionType ?? .no }
    private var _autocorrectionType: TextAutoCorrectionType?
    
    public var spellCheckingType: TextSpellCheckingType { _spellCheckingType ?? .no }
    private var _spellCheckingType: TextSpellCheckingType?
    
    public var keyboardType: KeyboardType { _keyboardType ?? .default }
    private var _keyboardType: KeyboardType?
    
    public init(isSecureTextEntry: Bool? = nil,
                autocapitalizationType: TextAutoCapitalizationType? = nil,
                autocorrectionType: TextAutoCorrectionType? = nil,
                spellCheckingType: TextSpellCheckingType? = nil,
                keyboardType: KeyboardType? = nil) {
        _isSecureTextEntry = isSecureTextEntry
        _autocapitalizationType = autocapitalizationType
        _autocorrectionType = autocorrectionType
        _spellCheckingType = spellCheckingType
        _keyboardType = keyboardType
    }
    
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
        case ._isSecureTextEntry:
            return .init(defaultValue: .boolean(false))
        case ._autocapitalizationType:
            return .init(propertyType: .reference(TextAutoCapitalizationType.documentableType()))
        case ._autocorrectionType:
            return .init(propertyType: .reference(TextAutoCorrectionType.documentableType()))
        case ._spellCheckingType:
            return .init(propertyType: .reference(TextSpellCheckingType.documentableType()))
        case ._keyboardType:
            return .init(propertyType: .reference(KeyboardType.documentableType()))
        }
    }
    
    public static func examples() -> [KeyboardOptionsObject] {
        [KeyboardOptionsObject(), .decimalEntryOptions]
    }
}

