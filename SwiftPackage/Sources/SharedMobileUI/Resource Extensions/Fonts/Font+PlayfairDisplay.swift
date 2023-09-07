//
//  Font+PlayfairDisplay.swift
//

import SwiftUI

extension Font {
    
    /// Returns the most appropriate Playfair Display font found by the system.
    ///
    /// This framework does not include all the possible sizes and weights for the font
    /// because the full font family is about 11 mb.
    ///
    /// - parameters:
    ///     - size: The size of the font requested.
    ///     - weight: The weight of the font requested.
    /// - returns: The closest font to the one requested that is registered for this application.
    public static func playfairDisplayFont(fixedSize size: CGFloat, weight: Font.Weight = .regular) -> Font {
        playfairDisplayFont(size, relativeTo: nil, weight: weight)
    }
    
    /// Returns the most appropriate Playfair Display font found by the system. 
    ///
    /// This framework does not include all the possible sizes and weights for the font
    /// because the full font family is about 11 mb.
    ///
    /// - parameters:
    ///     - size: The size of the font requested.
    ///     - textSyle: The relative text style for the text.
    ///     - weight: The weight of the font requested.
    /// - returns: The closest font to the one requested that is registered for this application.
    public static func playfairDisplayFont(_ size: CGFloat, relativeTo textStyle: TextStyle? = nil, weight: Font.Weight = .regular) -> Font {
        let fontName = FontWrapper.shared.fontName(weight)
        if #available(iOS 14.0, *) {
            if let textStyle = textStyle {
                return .custom(fontName, size: size, relativeTo: textStyle)
            }
            else {
                return .custom(fontName, fixedSize: size)
            }
        } else {
            return .custom(fontName, size: size)
        }
    }
}

/// The wrapper is used here to register the Lato fonts once only.
fileprivate class FontWrapper {
    
    static let shared = FontWrapper()
    
    private init() {
        let bundle = Bundle.module
        Font.registerFont(filename: "PlayfairDisplay-Italic.ttf", bundle: bundle)
    }
    
    func fontName(_ weight: Font.Weight) -> String {
        Font.isBoldTextEnabled ? "PlayfairDisplay-BoldItalic" : "PlayfairDisplay-Italic"
    }
    
    func italicFontName(_ weight: Font.Weight) -> String {
        Font.isBoldTextEnabled ? "PlayfairDisplay-BoldItalic" : "PlayfairDisplay-Italic"
    }
}

