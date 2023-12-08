//
//  ButtonActionSerializer.swift
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
@available(*, deprecated, message: "Use @SerialName")
public struct ButtonActionInfoType : TypeRepresentable, Codable, Hashable {
    
    public let rawValue: String
    
    public init(rawValue: String) {
        self.rawValue = rawValue
    }
    
    /// Defaults to creating a `ButtonActionInfoObject`.
    public static let `default`: ButtonActionInfoType = "default"
    
    /// Defaults to creating a `NavigationButtonActionInfoObject`.
    public static let navigation: ButtonActionInfoType = "navigation"
    
    public static func allStandardTypes() -> [ButtonActionInfoType] {
        return [.default]
    }
}

@available(*, deprecated, message: "Use @SerialName")
extension ButtonActionInfoType : ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        self.init(rawValue: value)
    }
}

@available(*, deprecated, message: "Use @SerialName")
extension ButtonActionInfoType : DocumentableStringLiteral {
    public static func examples() -> [String] {
        return allStandardTypes().map{ $0.rawValue }
    }
}

public final class ButtonActionSerializer : GenericPolymorphicSerializer<ButtonActionInfo>, DocumentableInterface {
    public var documentDescription: String? {
        """
        `ButtonActionInfo` protocol can be used to customize the title and image displayed for a given
        action of the UI.
        """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n")
    }
    
    public var jsonSchema: URL {
        URL(string: "\(AssessmentFactory.defaultFactory.modelName(for: self.interfaceName)).json", relativeTo: kBDHJsonSchemaBaseURL)!
    }
    
    override init() {
        super.init([
            ButtonActionInfoObject.examples().first!,
            NavigationButtonActionInfoObject.examples().first!,
        ])
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

public protocol SerializableButtonActionInfo : ButtonActionInfo, DecodableBundleInfo, PolymorphicSerializableTyped, Codable {
}

/// ``ButtonActionInfoObject`` is a concrete implementation of ``ButtonActionInfo`` that can be used to
/// customize the title and image displayed for a given action of the UI.
@Serializable
@SerialName("default")
public struct ButtonActionInfoObject : SerializableButtonActionInfo, Equatable, Codable {
    
    public private(set) var buttonTitle: String?
    public private(set) var iconName: String?
    public private(set) var bundleIdentifier: String?
    public var packageName: String?
    @Transient public var factoryBundle: ResourceBundle? = nil
    
    public static func == (lhs: ButtonActionInfoObject, rhs: ButtonActionInfoObject) -> Bool {
        lhs.typeName == rhs.typeName &&
        lhs.buttonTitle == rhs.buttonTitle &&
        lhs.iconName == rhs.iconName &&
        lhs.bundleIdentifier == rhs.bundleIdentifier &&
        lhs.packageName == rhs.packageName
    }
    
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
        return codingKey.stringValue == "type"
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .typeName:
            return .init(constValue: serialTypeName)
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

