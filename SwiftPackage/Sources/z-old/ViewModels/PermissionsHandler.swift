//
//  PermissionsHandler.swift
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

