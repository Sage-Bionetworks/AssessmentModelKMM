//
//  InterruptionHandling.swift
//  
//

import Foundation
import JsonModel

public protocol InterruptionHandling {
    
    /// Once interrupted, can the associated assessment be resumed?
    var canResume: Bool { get }
    
    /// Can partial results for this assessment be saved and the assessment resumed at some indeterminate time in the future?
    var canSaveForLater: Bool { get }
    
    /// Can this assessment be skipped or is it required for subsequent assessments that rely upon this one?
    var canSkip: Bool { get }
    
    /// An assessment might be designed to allow reviewing the instructions for that assessment, with direct navigation to
    /// the first "instruction" node. If so, that identifier can be defined for the assessment using this identifier.
    var reviewIdentifier: NavigationIdentifier? { get }
}

public extension InterruptionHandling {
    var canPause: Bool {
        canResume || canSkip || (reviewIdentifier != nil)
    }
}

public struct InterruptionHandlingObject : InterruptionHandling, Codable, Hashable {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case _canResume = "canResume", reviewIdentifier, _canSkip = "canSkip", _canSaveForLater = "canSaveForLater"
    }
    
    public var canResume: Bool { _canResume ?? true }
    private let _canResume: Bool?
    
    public var canSaveForLater: Bool { _canSaveForLater ?? true }
    private let _canSaveForLater: Bool?
    
    public var canSkip: Bool { _canSkip ?? true }
    private let _canSkip: Bool?
    
    public let reviewIdentifier: NavigationIdentifier?
    
    public init(reviewIdentifier: NavigationIdentifier? = nil, canResume: Bool? = nil, canSaveForLater: Bool? = nil, canSkip: Bool? = nil) {
        self.reviewIdentifier = reviewIdentifier
        self._canResume = canResume
        self._canSaveForLater = canSaveForLater
        self._canSkip = canSkip
    }
    
    public init(_ overrideHandling: InterruptionHandling, _ defaultHandling: InterruptionHandling) {
        self.reviewIdentifier = overrideHandling.reviewIdentifier ?? defaultHandling.reviewIdentifier
        self._canResume = overrideHandling.canResume && defaultHandling.canResume
        self._canSaveForLater = overrideHandling.canSaveForLater && defaultHandling.canSaveForLater
        self._canSkip = overrideHandling.canSkip && defaultHandling.canSkip
    }
}

extension InterruptionHandlingObject : DocumentableStruct {

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
        case .reviewIdentifier:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "An assessment might be designed to allow reviewing the instructions for that assessment with direct navigation to the first instruction node.")
        case ._canSkip:
            return .init(defaultValue: .boolean(true), propertyDescription:
                            "Can this assessment be skipped (ie. declined) without affecting subsequent assessments?")
        case ._canSaveForLater:
            return .init(defaultValue: .boolean(true), propertyDescription:
                            "Can partial results of this assessment be restored if the assessment is ended and continued later?")
        case ._canResume:
            return .init(defaultValue: .boolean(true), propertyDescription:
                            "Can this assessment be resumed following an interruption?")
        }
    }
    
    public static func examples() -> [InterruptionHandlingObject] {
        [.init(reviewIdentifier: "instruction1", canResume: false, canSaveForLater: false, canSkip: true)]
    }
}
