//
//  PermissionStep.swift
//  
//

import Foundation
import JsonModel
import MobilePassiveData

/// The permission step protocol is used to allow showing an instruction that also requests one or more permissions.
public protocol PermissionStep : Step, ContentNode {
    /// The list of permissions to request when showing this step.
    var permissions: [PermissionInfo] { get }
}

public protocol PermissionInfo {
    var permissionType: AsyncActionType { get }
    var restrictedMessage: String? { get }
    var deniedMessage: String? { get }
    var optional: Bool { get }
}

public struct PermissionInfoObject : Codable, Hashable, PermissionInfo {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case permissionType, _optional = "optional", restrictedMessage, deniedMessage
    }
    
    public let permissionType: AsyncActionType
    public let restrictedMessage: String?
    public let deniedMessage: String?
    
    public var optional: Bool { _optional ?? true }
    public let _optional: Bool?
    
    public init(permission permissionType: AsyncActionType, optional: Bool? = nil, restrictedMessage: String? = nil, deniedMessage: String? = nil) {
        self.permissionType = permissionType
        self._optional = optional
        self.restrictedMessage = restrictedMessage
        self.deniedMessage = deniedMessage
    }
}

extension PermissionInfoObject : DocumentableStruct {

    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        (codingKey as? CodingKeys).map { $0 == .permissionType } ?? false
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not handled by \(self).")
        }
        switch key {
        case .permissionType:
            return .init(propertyType: .reference(AsyncActionType.documentableType()), propertyDescription:
                            "The permission type to request when showing this step.")
        case ._optional:
            return .init(defaultValue: .boolean(true), propertyDescription:
                            "Whether or not the permission is required to continue.")
        case .deniedMessage:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The localized message to display when the permission is denied. Typically, this is only shown when the permission is not optional.")
        case .restrictedMessage:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The localized message to display when the permission is restricted. Typically, this is only shown when the permission is not optional.")
        }
    }
    
    public static func examples() -> [PermissionInfoObject] {
        [.init(permission: .motion)]
    }
}

open class AbstractPermissionStepObject : AbstractInstructionStepObject, PermissionStep, PermissionInfo {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case permissionType, _optional = "optional", restrictedMessage, deniedMessage
        var relativeIndex: Int { 6 }
    }
    
    open var permissions: [PermissionInfo] { [self] }
    
    public let permissionType: AsyncActionType
    public let restrictedMessage: String?
    public let deniedMessage: String?
    
    public var optional: Bool { _optional ?? true }
    public let _optional: Bool?
    
    public init(identifier: String,
                permissionType: AsyncActionType, optional: Bool? = nil, restrictedMessage: String? = nil, deniedMessage: String? = nil,
                title: String? = nil, subtitle: String? = nil, detail: String? = nil, imageInfo: ImageInfo? = nil,
                shouldHideButtons: Set<ButtonType>? = nil, buttonMap: [ButtonType : ButtonActionInfo]? = nil, comment: String? = nil, nextNode: NavigationIdentifier? = nil,
                fullInstructionsOnly: Bool? = nil, spokenInstructions: [AbstractInstructionStepObject.SpokenInstructionKey : String]? = nil) {
        self.permissionType = permissionType
        self._optional = optional
        self.restrictedMessage = restrictedMessage
        self.deniedMessage = deniedMessage
        super.init(identifier: identifier,
                   title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo,
                   shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment, nextNode: nextNode,
                   fullInstructionsOnly: fullInstructionsOnly, spokenInstructions: spokenInstructions)
    }
    
    public init(identifier: String, copyFrom object: AbstractPermissionStepObject) {
        self.permissionType = object.permissionType
        self._optional = object._optional
        self.restrictedMessage = object.restrictedMessage
        self.deniedMessage = object.deniedMessage
        super.init(identifier: identifier, copyFrom: object)
    }
    
    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.permissionType = try container.decode(AsyncActionType.self, forKey: .permissionType)
        self._optional = try container.decodeIfPresent(Bool.self, forKey: ._optional)
        self.deniedMessage = try container.decodeIfPresent(String.self, forKey: .deniedMessage)
        self.restrictedMessage = try container.decodeIfPresent(String.self, forKey: .restrictedMessage)
        try super.init(from: decoder)
    }
    
    open override func encode(to encoder: Encoder) throws {
        try super.encode(to: encoder)
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(permissionType, forKey: .permissionType)
        try container.encodeIfPresent(_optional, forKey: ._optional)
        try container.encodeIfPresent(deniedMessage, forKey: .deniedMessage)
        try container.encodeIfPresent(restrictedMessage, forKey: .restrictedMessage)
    }
    
    override open class func codingKeys() -> [CodingKey] {
        var keys = super.codingKeys()
        let thisKeys: [CodingKey] = CodingKeys.allCases
        keys.append(contentsOf: thisKeys)
        return keys
    }
    
    override open class func isRequired(_ codingKey: CodingKey) -> Bool {
        (codingKey as? CodingKeys).map { $0 == .permissionType } ?? super.isRequired(codingKey)
    }

    override open class func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            return try super.documentProperty(for: codingKey)
        }
        switch key {
        case .permissionType:
            return .init(propertyType: .reference(AsyncActionType.documentableType()), propertyDescription:
                            "The permission type to request when showing this step.")
        case ._optional:
            return .init(defaultValue: .boolean(true), propertyDescription:
                            "Whether or not the permission is required to continue.")
        case .deniedMessage:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The localized message to display when the permission is denied. Typically, this is only shown when the permission is not optional.")
        case .restrictedMessage:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The localized message to display when the permission is restricted. Typically, this is only shown when the permission is not optional.")
        }
    }
}

public final class PermissionStepObject : AbstractPermissionStepObject, Encodable, DocumentableStruct, CopyWithIdentifier {
    public override class func defaultType() -> SerializableNodeType {
        .StandardTypes.permission.nodeType
    }
    
    public static func examples() -> [PermissionStepObject] {
        [.init(identifier: "example", permissionType: .motion)]
    }
    
    public func copy(with identifier: String) -> PermissionStepObject {
        .init(identifier: identifier, copyFrom: self)
    }
}
