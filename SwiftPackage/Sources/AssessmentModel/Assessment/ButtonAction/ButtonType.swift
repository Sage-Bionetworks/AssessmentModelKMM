//
//  ButtonAction.swift
//

import Foundation
import JsonModel

/// The ``ButtonType`` enum describes standard navigation actions that are common to a
/// given UI step. It is extendable using the custom field.
public enum ButtonType {
    
    /// Standard navigation elements that are common to most steps.
    case navigation(Navigation)
    
    /// Standard navigation elements that are common to most steps.
    public enum Navigation : String, CaseIterable {
        
        /// Navigate to the next step.
        case goForward
        
        /// Navigate to the previous step.
        case goBackward
        
        /// Skip the step and immediately go forward.
        case skip
        
        /// Exit the assessment. This action is associated with a button that will exit the assessment.
        case cancel
        
        /// Pause the assessment. Depending upon the assessment/step, this button may be associated
        /// with an action sheet or just pause a timer.
        case pause
        
        /// Go back in the assessment and show previously displayed instructions.
        case reviewInstructions
    }
    
    /// A custom action on the step. Must be handled by the app.
    case custom(String)
    
    /// The string for the custom action (if applicable).
    public var customAction: String? {
        if case .custom(let str) = self {
            return str
        } else {
            return nil
        }
    }
}

extension ButtonType: RawRepresentable, Codable, Hashable {
    
    public init(rawValue: String) {
        if let subtype = Navigation(rawValue: rawValue) {
            self = .navigation(subtype)
        }
        else {
            self = .custom(rawValue)
        }
    }
    
    public var rawValue: String {
        switch (self) {
        case .navigation(let value):
            return value.rawValue
            
        case .custom(let value):
            return value
        }
    }
}

extension ButtonType : ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        self.init(rawValue: value)
    }
}

extension ButtonType : CodingKey {
    public var stringValue: String {
        return self.rawValue
    }
    
    public init?(stringValue: String) {
        self.init(rawValue: stringValue)
    }
    
    public var intValue: Int? {
        return nil
    }
    
    public init?(intValue: Int) {
        return nil
    }
}

extension ButtonType : DocumentableStringEnum {
    public static func allValues() -> [String] {
        Navigation.allCases.map { $0.rawValue }
    }
}
