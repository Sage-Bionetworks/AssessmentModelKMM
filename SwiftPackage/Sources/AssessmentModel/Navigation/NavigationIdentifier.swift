//
//  NavigationIdentifier.swift
//  
//

import Foundation
import JsonModel

public enum NavigationIdentifier {
    
    case reserved(ReservedKey)
    
    public enum ReservedKey : String, StringEnumSet, DocumentableStringEnum {
        /// Exit the assessment
        case exit
        /// Go to the next section
        case nextSection
        /// Go to the beginning (ie. the first step)
        case beginning
    }
    
    case node(String)
}

extension NavigationIdentifier: RawRepresentable, Codable, Hashable {
    
    public init(rawValue: String) {
        if let subtype = ReservedKey(rawValue: rawValue) {
            self = .reserved(subtype)
        }
        else {
            self = .node(rawValue)
        }
    }
    
    public var rawValue: String {
        switch (self) {
        case .reserved(let value):
            return value.rawValue
            
        case .node(let value):
            return value
        }
    }
    
    public static func == (lhs: NavigationIdentifier, rhs: String) -> Bool {
        lhs.rawValue == rhs
    }
    
    public static func == (lhs: String, rhs: NavigationIdentifier) -> Bool {
        lhs == rhs.rawValue
    }
}

extension NavigationIdentifier : ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        self.init(rawValue: value)
    }
}

extension NavigationIdentifier : DocumentableStringLiteral {
    public static func examples() -> [String] {
        ReservedKey.allValues()
    }
}
