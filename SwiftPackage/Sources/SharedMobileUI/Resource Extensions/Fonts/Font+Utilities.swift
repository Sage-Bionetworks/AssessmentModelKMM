//
//  Font+Utilities.swift
//

import SwiftUI

#if os(macOS)
import AppKit
fileprivate typealias TypedFont = NSFont
#else
import UIKit
fileprivate typealias TypedFont = UIFont
#endif

extension Font {
    
    /// Is accessibility turned "ON" for bold text?
    public static var isBoldTextEnabled: Bool {
        #if os(macOS)
            return false
        #else
            return UIAccessibility.isBoldTextEnabled
        #endif
    }
    
    /// Rough estimate of the dynamic text size for body text style. This can be used to resize or hide
    /// secondary images when the text would be extra large.
    @available(*, deprecated, message: "Use `@ScaledMetric` or `@SwiftUI.Environment(\\.sizeCategory)` instead." )
    public static func fontRatio() -> CGFloat {
        let font = TypedFont.preferredFont(forTextStyle: .body)
        return font.pointSize / 17
    }

    /// https://stackoverflow.com/questions/30507905/xcode-using-custom-fonts-inside-dynamic-framework
    public static func registerFont(filename: String, bundle: Bundle) {
        guard let pathForResourceString = bundle.path(forResource: filename, ofType: nil) else {
            print("WARNING! Failed to register font \(filename) - path for resource not found")
            return
        }
        
        guard let fontData = NSData(contentsOfFile: pathForResourceString) else {
            print("WARNING! Failed to register font \(filename) - font data for could not be loaded.")
            return
        }
        
        guard let dataProvider = CGDataProvider(data: fontData) else {
            print("WARNING! Failed to register font \(filename) - data provider could not be loaded.")
            return
        }
        
        guard let font = CGFont(dataProvider) else {
            print("WARNING! Failed to register font \(filename) - font could not be loaded.")
            return
        }
        
        var errorRef: Unmanaged<CFError>? = nil
        if (CTFontManagerRegisterGraphicsFont(font, &errorRef) == false) {
            print("WARNING! Failed to register font \(filename) - register graphics font failed - this font may have already been registered in the main bundle.")
        }
    }
}

