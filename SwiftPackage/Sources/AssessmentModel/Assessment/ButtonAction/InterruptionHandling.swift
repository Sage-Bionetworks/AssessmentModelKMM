//
//  InterruptionHandling.swift
//  
//
//  Copyright © 2022 Sage Bionetworks. All rights reserved.
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
    
    public init(reviewIdentifier: NavigationIdentifier? = nil, canResume: Bool? = nil, canContinueLater: Bool? = nil, canSkip: Bool? = nil) {
        self.reviewIdentifier = reviewIdentifier
        self._canResume = canResume
        self._canSaveForLater = canContinueLater
        self._canSkip = canSkip
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
        [.init(reviewIdentifier: "instruction1", canResume: false, canContinueLater: false, canSkip: true)]
    }
}
