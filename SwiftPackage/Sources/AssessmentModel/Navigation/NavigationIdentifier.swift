//
//  NavigationIdentifier.swift
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
