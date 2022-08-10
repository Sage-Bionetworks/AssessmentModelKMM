//
//  OverviewStep.swift
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
import MobilePassiveData

/// An overview step is  intended to be used to mark the *beginning* of an assessment.
public protocol OverviewStep : PermissionStep {
}

open class AbstractOverviewStepObject : AbstractStepObject, OverviewStep {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case permissions, icons
        var relativeIndex: Int { 5 }
    }
    
    open var permissions: [PermissionInfo] { _permissions ?? [] }
    private let _permissions: [PermissionInfoObject]?
    
    open private(set) var icons: [OverviewIcon]?

    public init(identifier: String,
                title: String? = nil, subtitle: String? = nil, detail: String? = nil, imageInfo: ImageInfo? = nil,
                permissions: [PermissionInfoObject]? = nil, icons: [OverviewIcon]? = nil,
                shouldHideButtons: Set<ButtonType>? = nil, buttonMap: [ButtonType : ButtonActionInfo]? = nil, comment: String? = nil, nextNode: NavigationIdentifier? = nil) {
        self._permissions = permissions
        self.icons = icons
        super.init(identifier: identifier,
                   title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo,
                   shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment, nextNode: nextNode)
    }
    
    public init(identifier: String, copyFrom object: AbstractOverviewStepObject) {
        self._permissions = object._permissions
        self.icons = object.icons
        super.init(identifier: identifier, copyFrom: object)
    }
    
    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self._permissions = try container.decodeIfPresent([PermissionInfoObject].self, forKey: .permissions)
        self.icons = try container.decodeIfPresent([OverviewIcon].self, forKey: .icons)
        try super.init(from: decoder)
    }
    
    open override func encode(to encoder: Encoder) throws {
        try super.encode(to: encoder)
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(_permissions, forKey: .permissions)
        try container.encodeIfPresent(icons, forKey: .icons)
    }
    
    override open class func codingKeys() -> [CodingKey] {
        var keys = super.codingKeys()
        let thisKeys: [CodingKey] = CodingKeys.allCases
        keys.append(contentsOf: thisKeys)
        return keys
    }

    override open class func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            return try super.documentProperty(for: codingKey)
        }
        switch key {
        case .permissions:
            return .init(propertyType: .referenceArray(PermissionInfoObject.documentableType()), propertyDescription:
                            "A list of permissions to request for this assessment.")
        case .icons:
            return .init(propertyType: .referenceArray(OverviewIcon.documentableType()), propertyDescription:
                            "A list of icons associated with this overview screen.")
        }
    }
}

public final class OverviewStepObject : AbstractOverviewStepObject, Encodable, DocumentableStruct, CopyWithIdentifier {
    public override class func defaultType() -> SerializableNodeType {
        .StandardTypes.overview.nodeType
    }
    
    public static func examples() -> [OverviewStepObject] {
        [.init(identifier: "example")]
    }
    
    public func copy(with identifier: String) -> OverviewStepObject {
        .init(identifier: identifier, copyFrom: self)
    }
}

public struct OverviewIcon : Codable, Equatable, DocumentableStruct {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case icon, title
    }
    
    public let icon: String
    public let title: String
    
    public static func examples() -> [OverviewIcon] {
        [.init(icon: "hello", title: "Hello, World!")]
    }
    
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        true
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not handled by \(self).")
        }
        switch key {
        case .icon:
            return .init(propertyType: .primitive(.string), propertyDescription: "The name of the icon.")
        case .title:
            return .init(propertyType: .primitive(.string), propertyDescription: "The localized title for the icon.")
        }
    }
}
