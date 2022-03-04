//
//  ButtonActionSerializer.swift
//
//  Copyright © 2018-2022 Sage Bionetworks. All rights reserved.
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

/// The `ButtonActionInfo` protocol can be used to customize the title and image displayed for a
/// given action of the UI.
public protocol ButtonActionInfo : PolymorphicTyped, ResourceInfo {
    
    /// The title to display on the button associated with this action.
    var buttonTitle: String? { get }
    
    /// The name of the icon to display on the button associated with this action.
    var iconName: String? { get }
}

/// This is used to decode a `ButtonActionInfo` using a polymorphic serializer.
public struct ButtonActionInfoType : TypeRepresentable, Codable, Hashable {
    
    public let rawValue: String
    
    public init(rawValue: String) {
        self.rawValue = rawValue
    }
    
    /// Defaults to creating a `ButtonActionInfoObject`.
    public static let `default`: ButtonActionInfoType = "default"
    
    public static func allStandardTypes() -> [ButtonActionInfoType] {
        return [.default]
    }
}

extension ButtonActionInfoType : ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        self.init(rawValue: value)
    }
}

extension ButtonActionInfoType : DocumentableStringLiteral {
    public static func examples() -> [String] {
        return allStandardTypes().map{ $0.rawValue }
    }
}

public final class ButtonActionSerializer : AbstractPolymorphicSerializer, PolymorphicSerializer {
    public var documentDescription: String? {
        """
        `ButtonActionInfo` protocol can be used to customize the title and image displayed for a given
        action of the UI.
        """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n")
    }
    
    public var jsonSchema: URL {
        URL(string: "\(AssessmentFactory.shared.modelName(for: self.interfaceName)).json", relativeTo: kSageJsonSchemaBaseURL)!
    }
    
    override init() {
        examples = [
            ButtonActionInfoObject.examples().first!,
        ]
    }
    
    public private(set) var examples: [ButtonActionInfo]
    
    public override class func typeDocumentProperty() -> DocumentProperty {
        .init(propertyType: .reference(ButtonActionInfoType.documentableType()))
    }
    
    public func add(_ example: SerializableButtonActionInfo) {
        examples.removeAll(where: { $0.typeName == example.typeName })
        examples.append(example)
    }
    
    private enum InterfaceKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case buttonTitle, iconName, bundleIdentifier, packageName
        var relativeIndex: Int { 2 }
    }
    
    public override class func codingKeys() -> [CodingKey] {
        var keys = super.codingKeys()
        keys.append(contentsOf: InterfaceKeys.allCases)
        return keys
    }
    
    public override class func isRequired(_ codingKey: CodingKey) -> Bool {
        !(codingKey is InterfaceKeys) && super.isRequired(codingKey)
    }
    
    public override class func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? InterfaceKeys else {
            return try super.documentProperty(for: codingKey)
        }
        switch key {
        case .buttonTitle:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The localized title for the button.")
        case .iconName:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The icon name for the embedded icon.")
        case .bundleIdentifier:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The bundle identifier for the iOS resource bundle that contains the image.")
        case .packageName:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The Android package for the resource.")
        }
    }
}

public protocol SerializableButtonActionInfo : ButtonActionInfo, PolymorphicRepresentable, Encodable {
    var serializableType: ButtonActionInfoType { get }
}

public extension SerializableButtonActionInfo {
    var typeName: String { return serializableType.rawValue }
}

/// ``ButtonActionInfoObject`` is a concrete implementation of ``ButtonActionInfo`` that can be used to
/// customize the title and image displayed for a given action of the UI.
public struct ButtonActionInfoObject : SerializableButtonActionInfo {
    private enum CodingKeys : String, CodingKey, CaseIterable {
        case serializableType = "type", buttonTitle, iconName, bundleIdentifier, packageName
    }
    public private(set) var serializableType: ButtonActionInfoType = .default
    
    public private(set) var buttonTitle: String?
    public private(set) var iconName: String?
    public private(set) var bundleIdentifier: String?
    public private(set) var packageName: String?
    public var factoryBundle: ResourceBundle? = nil
    
    /// Default initializer for a button with text.
    /// - parameter buttonTitle: The title to display on the button associated with this action.
    public init(buttonTitle: String) {
        self.buttonTitle = buttonTitle
        self.iconName = nil
    }
    
    /// Default initializer for a button with an image.
    /// - parameters:
    ///     - iconName: The name of the image to display on the button.
    ///     - bundleIdentifier: The bundle identifier for the resource bundle that contains the image.
    public init(iconName: String, bundleIdentifier: String? = nil) {
        self.buttonTitle = nil
        self.iconName = iconName
        self.bundleIdentifier = bundleIdentifier
    }
    
    /// Default initializer for a button with an image.
    /// - parameters:
    ///     - iconName: The name of the image to display on the button.
    ///     - bundle: The resource bundle that contains the image.
    public init(iconName: String, bundle: ResourceBundle?) {
        self.buttonTitle = nil
        self.iconName = iconName
        self.bundleIdentifier = bundle?.bundleIdentifier
        self.factoryBundle = bundle
    }
}

extension ButtonActionInfoObject : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else { return false }
        return key == .serializableType
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .serializableType:
            return .init(constValue: ButtonActionInfoType.default)
        case .buttonTitle, .iconName, .bundleIdentifier, .packageName:
            return .init(propertyType: .primitive(.string))
        }
    }

    public static func examples() -> [ButtonActionInfoObject] {
        let titleAction = ButtonActionInfoObject(buttonTitle: "Go, Dogs! Go")
        let imageAction = ButtonActionInfoObject(iconName: "closeX", bundleIdentifier: "org.example.SharedResources")
        return [titleAction, imageAction]
    }
}

