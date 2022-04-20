//
//  PermissionsHandler.swift
//
//  Copyright © 2021 Sage Bionetworks. All rights reserved.
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
import MobilePassiveData
import AssessmentModel

struct PermissionsHandler {
    fileprivate let permissions: [PermissionWrapper]
    
    init(requestedPermissions: Set<AnyHashable>?) {
        self.permissions = requestedPermissions?.compactMap({
            if let info = $0 as? AssessmentModel.PermissionInfo {
                return PermissionWrapper(info)
            }
            else if let info = $0 as? StandardPermission {
                return PermissionWrapper(info)
            }
            else if let pType = $0 as? AssessmentModel.PermissionType {
                return PermissionWrapper(pType.name)
            }
            else if let pType = $0 as? MobilePassiveData.PermissionType {
                return PermissionWrapper(pType.identifier)
            }
            else {
                return nil
            }
        }) ?? []
    }
    
    func requestPermissions(_ completion: @escaping ((MobilePassiveData.Permission?, Error?) -> Void)) {
        guard self.permissions.count > 0
            else {
            completion(nil, nil)
            return
        }
        self._chainedRequest(0, completion)
    }
    
    private func _chainedRequest(_ currentIndex: Int, _ completion: @escaping ((MobilePassiveData.Permission?, Error?) -> Void)) {
        DispatchQueue.main.async {
            let nextIndex = currentIndex + 1
            let permission = self.permissions[currentIndex]
            PermissionAuthorizationHandler.requestAuthorization(for: permission) { (status, error) in
                let failedPermission = (status != .authorized && !permission.optional) ? permission : nil
                if (nextIndex >= permissions.count) || (failedPermission != nil) {
                    DispatchQueue.main.async {
                        completion(failedPermission, error)
                    }
                }
                else {
                    self._chainedRequest(nextIndex, completion)
                }
            }
        }
    }
}

fileprivate class PermissionWrapper : MobilePassiveData.Permission {
    
    init(_ info: AssessmentModel.PermissionInfo) {
        self.identifier = info.permissionType.name
        self.optional = info.optional
        self.reason = info.reason
        self.restrictedMessage = info.restrictedMessage
        self.deniedMessage = info.deniedMessage
    }
    
    init(_ info: StandardPermission) {
        self.identifier = info.identifier
        self.optional = info.isOptional
        self.reason = info.reason
        self.restrictedMessage = info.restrictedMessage
        self.deniedMessage = info.deniedMessage
    }
    
    convenience init?(_ identifier: String) {
        guard let pType = StandardPermissionType(rawValue: identifier) else { return nil }
        self.init(StandardPermission(permissionType: pType))
    }
    
    let identifier: String
    let optional: Bool
    let reason: String?
    let restrictedMessage: String?
    let deniedMessage: String?
    
    var title: String? { nil }
    
    func message(for status: PermissionAuthorizationStatus) -> String? {
        switch status {
        case .restricted:
            return restrictedMessage
        default:
            return deniedMessage
        }
    }
}

