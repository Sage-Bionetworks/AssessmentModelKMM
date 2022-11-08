//
//  KeyboardOptions+SwiftUI.swift
//  
//

import SwiftUI
import AssessmentModel

#if os(iOS)

extension TextAutoCapitalizationType {

    /// Return the `UITextAutocapitalizationType` that maps to this enum.
    public var uiType: UITextAutocapitalizationType {
        .init(rawValue: self.indexPosition) ?? .none
    }

    /// Return the `TextInputAutocapitalization` that maps to this enum.
    @available(iOS 15.0, *)
    public var textInputType: TextInputAutocapitalization {
        switch self {
        case .words:
            return .words
        case .sentences:
            return .sentences
        case .allCharacters:
            return .characters
        default:
            return .never
        }
    }
}

extension TextAutoCorrectionType {

    /// Return the `UITextAutocorrectionType` that maps to this enum.
    public var uiType: UITextAutocorrectionType {
        .init(rawValue: self.indexPosition) ?? .default
    }
}

extension TextSpellCheckingType {

    /// Return the `UITextSpellCheckingType` that maps to this enum.
    public var uiType: UITextSpellCheckingType {
        .init(rawValue: self.indexPosition) ?? .default
    }
}

extension KeyboardType {

    /// Return the `UIKeyboardType` that maps to this enum.
    public var uiType: UIKeyboardType {
        UIKeyboardType(rawValue: self.indexPosition) ?? .default
    }
}

#endif

