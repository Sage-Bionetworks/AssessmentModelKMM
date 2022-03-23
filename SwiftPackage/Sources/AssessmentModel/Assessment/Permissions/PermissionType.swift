//
//  PermissionType.swift
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
import SwiftUI

/// Permission type is used to define information required by either platform to request a permission.
public struct PermissionType : Codable, Hashable {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case name, userInfo
    }

    /// An identifier for this permission that can be interpreted by the given platform to request
    /// permissions as needed.
    public let name: String
    
    /// Any additional user information that is not defined by the ``name``.
    public var userInfo: [String : JsonSerializable]? {
        _userInfo.map {
            switch $0 {
            case .object(let value):
                return value
            default:
                return [:]
            }
        }
    }
    private let _userInfo: JsonElement?

    /// These are permissions that are recognized as standard, though the implementation details are
    /// outside of this library.
    public enum Standard: String, StringEnumSet, DocumentableStringEnum {
        
        /// Used to request access to services and GPS location as needed by the device to aquire information
        /// about the current weather conditions.
        case weather
        
        /// Used to request access to the participant's microphone.
        case microphone
        
        /// Used to request access to the participant's motion sensors.
        case motion
        
        /// Used to request permission to post local notifications.
        case notifications
        
        public var permissionType: PermissionType {
            .init(self)
        }
    }
    
    public init(_ standard: Standard) {
        self.name = standard.rawValue
        self._userInfo = nil
    }
    
    public init(_ name: String, userInfo: [String : JsonSerializable]? = nil) {
        self.name = name
        self._userInfo = userInfo.map { .object($0) }
    }
    
    public init(from decoder: Decoder) throws {
        if let container = try? decoder.singleValueContainer() {
            self.name = try container.decode(String.self)
            self._userInfo = nil
        }
        else {
            let container = try decoder.container(keyedBy: CodingKeys.self)
            self.name = try container.decode(String.self, forKey: .name)
            self._userInfo = try container.decodeIfPresent(JsonElement.self, forKey: .userInfo)
        }
    }
    
    public func encode(to encoder: Encoder) throws {
        if let json = _userInfo {
            var container = encoder.container(keyedBy: CodingKeys.self)
            try container.encode(name, forKey: .name)
            try container.encode(json, forKey: .userInfo)
        }
        else {
            var container = encoder.singleValueContainer()
            try container.encode(self.name)
        }
    }
}

extension PermissionType : ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        self.init(value)
    }
}

public extension PermissionType {
    static func == (lhs: PermissionType, rhs: Standard) -> Bool {
        lhs == rhs.permissionType
    }
    static func == (lhs: Standard, rhs: PermissionType) -> Bool {
        lhs.permissionType == rhs
    }
    static func == (lhs: PermissionType, rhs: String) -> Bool {
        lhs.name == rhs && lhs.userInfo == nil
    }
    static func == (lhs: String, rhs: PermissionType) -> Bool {
        lhs == rhs.name && rhs.userInfo == nil
    }
}

extension PermissionType : DocumentableAny {
    public static func jsonSchemaDefinition() -> [String : JsonSerializable] {
        [
            "anyOf" : [
                ["type" : "string"],
                [
                    "type" : "object",
                    "properties": [
                        "name": ["type": "string"],
                        "userInfo": ["type": "object"]
                    ],
                    "required": ["name"]
                ]
            ]
        ]
    }
}

