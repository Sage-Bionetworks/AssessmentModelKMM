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

@Serializable
public struct InterruptionHandlingObject : InterruptionHandling, Codable, Hashable {

    public let reviewIdentifier: NavigationIdentifier?
    public private(set) var canResume: Bool = true
    public private(set) var canSaveForLater: Bool = true
    public private(set) var canSkip: Bool = true

    public init(reviewIdentifier: NavigationIdentifier? = nil, canResume: Bool = true, canSaveForLater: Bool = true, canSkip: Bool = true) {
        self.reviewIdentifier = reviewIdentifier
        self.canResume = canResume
        self.canSaveForLater = canSaveForLater
        self.canSkip = canSkip
    }
    
    public init(_ overrideHandling: InterruptionHandling, _ defaultHandling: InterruptionHandling) {
        self.reviewIdentifier = overrideHandling.reviewIdentifier ?? defaultHandling.reviewIdentifier
        self.canResume = overrideHandling.canResume && defaultHandling.canResume
        self.canSaveForLater = overrideHandling.canSaveForLater && defaultHandling.canSaveForLater
        self.canSkip = overrideHandling.canSkip && defaultHandling.canSkip
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
        case .canSkip:
            return .init(defaultValue: .boolean(true), propertyDescription:
                            "Can this assessment be skipped (ie. declined) without affecting subsequent assessments?")
        case .canSaveForLater:
            return .init(defaultValue: .boolean(true), propertyDescription:
                            "Can partial results of this assessment be restored if the assessment is ended and continued later?")
        case .canResume:
            return .init(defaultValue: .boolean(true), propertyDescription:
                            "Can this assessment be resumed following an interruption?")
        }
    }
    
    public static func examples() -> [InterruptionHandlingObject] {
        [.init(reviewIdentifier: "instruction1", canResume: false, canSaveForLater: false, canSkip: true)]
    }
}
