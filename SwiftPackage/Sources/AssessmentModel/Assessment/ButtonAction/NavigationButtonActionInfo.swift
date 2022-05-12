//
//  NavigationButtonActionInfo.swift
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


/// The action of this ``ButtonActionInfo`` is to set up navigation to a different step. That navigation should happen immediately
/// without waiting for normal completion of any activity associated with this step. The behavior is similar to a "skip" button where
/// timers and spoken instructions are ignored.
public protocol NavigationButtonActionInfo : ButtonActionInfo {

    /// The identifier for the step to skip to if the action is called.
    var skipToIdentifier: NavigationIdentifier { get }
}

/// ``NavigationButtonActionInfoObject`` is a concrete implementation of ``NavigationButtonActionInfo`` that
/// can be used to customize the title and image displayed for a given action of the UI.
public struct NavigationButtonActionInfoObject : SerializableButtonActionInfo, Equatable {
    private enum CodingKeys : String, CodingKey, CaseIterable {
        case serializableType = "type", skipToIdentifier, buttonTitle, iconName, bundleIdentifier, packageName
    }
    public private(set) var serializableType: ButtonActionInfoType = .navigation
    
    public let skipToIdentifier: NavigationIdentifier
    public private(set) var buttonTitle: String?
    public private(set) var iconName: String?
    
    public private(set) var bundleIdentifier: String?
    public var packageName: String?
    public var factoryBundle: ResourceBundle? = nil
    
    public static func == (lhs: NavigationButtonActionInfoObject, rhs: NavigationButtonActionInfoObject) -> Bool {
        lhs.serializableType == rhs.serializableType &&
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
        return key == .serializableType
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .serializableType:
            return .init(constValue: ButtonActionInfoType.navigation)
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
