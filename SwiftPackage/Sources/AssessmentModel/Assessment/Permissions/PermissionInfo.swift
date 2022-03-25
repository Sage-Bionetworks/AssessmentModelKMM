//
//  PermissionInfo.swift
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

/// A generic configuration object with information about a given permission. The permission can be used by the
/// app to handle gracefully requesting authorization from the user for access to sensors, services, and hardware
/// required by the app.
public protocol PermissionInfo {

    /// The permission type associated with this request. This can be used to generalize requesting certain permissions
    /// if the common UX for a given platform recognizes the permission type.
    var permissionType: PermissionType { get }

    /// Is the permission optional?
    ///
    /// - example:
    ///
    /// Test A requires the motion sensors to calculate the results, in which case this permission should be required
    /// and the participant should be blocked from performing the task if the permission is not included.
    ///
    /// Test B uses the motion sensors (if available) to inform the results but can still receive valuable information
    /// about the participant without them. In this case, the permission is optional and the participant should be
    /// allowed to continue without permission to access the motion sensors.
    var optional: Bool { get }

    /// The localized message to show when displaying an alert the participant that the application cannot run an
    /// assessment because access to a required sensor is restricted.
    var restrictedMessage: String? { get }

    /// The localized message to show when displaying an alert the participant that the application cannot run an
    /// assessment because access to a required sensor is denied.
    var deniedMessage: String? { get }
}

public struct PermissionInfoObject : PermissionInfo, Codable, Hashable {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case permissionType, _optional = "optional", deniedMessage, restrictedMessage
    }
    
    public let permissionType: PermissionType
    public let restrictedMessage: String?
    public let deniedMessage: String?
    
    public var optional: Bool { _optional ?? true }
    private let _optional: Bool?
    
    public init(permission: PermissionType.Standard,
                optional: Bool? = nil,
                restrictedMessage: String? = nil,
                deniedMessage: String? = nil) {
        self.init(permission.permissionType, optional: optional, restrictedMessage: restrictedMessage, deniedMessage: deniedMessage)
    }
    
    public init(_ permissionType: PermissionType,
                optional: Bool? = nil,
                restrictedMessage: String? = nil,
                deniedMessage: String? = nil) {
        self.permissionType = permissionType
        self.deniedMessage = deniedMessage
        self.restrictedMessage = restrictedMessage
        self._optional = optional
    }
}

extension PermissionInfoObject : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }

    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else { return false }
        return key == .permissionType
    }

    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .permissionType:
            return .init(propertyType: .reference(PermissionType.documentableType()))
        case .restrictedMessage:
            return .init(propertyType: .primitive(.string))
        case .deniedMessage:
            return .init(propertyType: .primitive(.string))
        case ._optional:
            return .init(propertyType: .primitive(.boolean))
        }
    }

    public static func examples() -> [PermissionInfoObject] {
        let exampleA = PermissionInfoObject(permission: .motion)
        let exampleB = PermissionInfoObject("camera",
                                            optional: false,
                                            restrictedMessage: "Your camera access is restricted",
                                            deniedMessage: "You didn't give permission")
        return [exampleA, exampleB]
    }
}
