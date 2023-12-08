//
//  NavigationButtonActionInfo.swift
//

import Foundation
import JsonModel


/// The action of this ``ButtonActionInfo`` is to set up navigation to a different step. That navigation should happen immediately
/// without waiting for normal completion of any activity associated with this step. The behavior is similar to a "skip" button where
/// timers and spoken instructions are ignored.
public protocol NavigationButtonActionInfo : ButtonActionInfo {

    /// The identifier for the step to skip to if the action is called.
    var skipToIdentifier: NavigationIdentifier { get }
}

/// ``NavigationButtonActionInfoObject`` is a concrete implementation of ``NavigationButtonActionInfo`` that
/// can be used to customize the title and image displayed for a given action of the UI.
@Serializable
@SerialName("navigation")
public struct NavigationButtonActionInfoObject : SerializableButtonActionInfo, Equatable, Codable {
  
    public let skipToIdentifier: NavigationIdentifier
    public private(set) var buttonTitle: String?
    public private(set) var iconName: String?
    
    public private(set) var bundleIdentifier: String?
    public var packageName: String?
    @Transient public var factoryBundle: ResourceBundle? = nil
    
    public static func == (lhs: NavigationButtonActionInfoObject, rhs: NavigationButtonActionInfoObject) -> Bool {
        lhs.typeName == rhs.typeName &&
        lhs.buttonTitle == rhs.buttonTitle &&
        lhs.iconName == rhs.iconName &&
        lhs.bundleIdentifier == rhs.bundleIdentifier &&
        lhs.packageName == rhs.packageName &&
        lhs.skipToIdentifier == rhs.skipToIdentifier
    }
    
    /// Default initializer for a button with text.
    /// - parameter buttonTitle: The title to display on the button associated with this action.
    public init(skipToIdentifier: NavigationIdentifier, buttonTitle: String) {
        self.skipToIdentifier = skipToIdentifier
        self.buttonTitle = buttonTitle
        self.iconName = nil
    }
    
    /// Default initializer for a button with an image.
    /// - parameters:
    ///     - iconName: The name of the image to display on the button.
    ///     - bundleIdentifier: The bundle identifier for the resource bundle that contains the image.
    public init(skipToIdentifier: NavigationIdentifier, iconName: String, bundleIdentifier: String? = nil) {
        self.skipToIdentifier = skipToIdentifier
        self.buttonTitle = nil
        self.iconName = iconName
        self.bundleIdentifier = bundleIdentifier
    }
    
    /// Default initializer for a button with an image.
    /// - parameters:
    ///     - iconName: The name of the image to display on the button.
    ///     - bundle: The resource bundle that contains the image.
    public init(skipToIdentifier: NavigationIdentifier, iconName: String, bundle: ResourceBundle?) {
        self.skipToIdentifier = skipToIdentifier
        self.buttonTitle = nil
        self.iconName = iconName
        self.bundleIdentifier = bundle?.bundleIdentifier
        self.factoryBundle = bundle
    }
}

extension NavigationButtonActionInfoObject : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else { return false }
        return key == .typeName || key == .skipToIdentifier
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
        case .skipToIdentifier:
            return .init(propertyType: .reference(NavigationIdentifier.documentableType()), propertyDescription:
                            "The identifier to navigate to when tapping this button.")
        }
    }

    public static func examples() -> [NavigationButtonActionInfoObject] {
        let titleAction = NavigationButtonActionInfoObject(skipToIdentifier: "foo", buttonTitle: "Go, Dogs! Go")
        let imageAction = NavigationButtonActionInfoObject(skipToIdentifier: "foo", iconName: "closeX", bundleIdentifier: "org.example.SharedResources")
        return [titleAction, imageAction]
    }
}
