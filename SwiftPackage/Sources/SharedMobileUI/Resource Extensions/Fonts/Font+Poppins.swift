//
//  Font+Lato.swift
//

import SwiftUI

extension Font {
    
    /// Returns the most appropriate Poppins font found by the system. This will first check for the
    /// Poppins font that matches the requested weight. If not found (because this framework only
    /// includes a subset of the full Poppins font family), then it will fall back to the most
    /// appropriate font registered from this framework.
    ///
    /// This framework does not include all the possible sizes and weights for the Poppins font
    /// because the full font family is about 11 mb.
    ///
    /// - parameters:
    ///     - size: The size of the font requested.
    ///     - weight: The weight of the font requested.
    /// - returns: The closest font to the one requested that is registered for this application.
    public static func poppinsFont(fixedSize size: CGFloat, weight: Font.Weight = .regular) -> Font {
        poppinsFont(size, relativeTo: nil, weight: weight)
    }
    
    /// Returns the most appropriate Poppins font found by the system. This will first check for the
    /// Poppins font that matches the requested weight. If not found (because this framework only
    /// includes a subset of the full Poppins font family), then it will fall back to the most
    /// appropriate font registered from this framework.
    ///
    /// This framework does not include all the possible sizes and weights for the Poppins font
    /// because the full font family is about 11 mb.
    ///
    /// - parameters:
    ///     - size: The size of the font requested.
    ///     - textSyle: The relative text style for the text.
    ///     - weight: The weight of the font requested.
    /// - returns: The closest font to the one requested that is registered for this application.
    public static func poppinsFont(_ size: CGFloat, relativeTo textStyle: TextStyle? = nil, weight: Font.Weight = .regular) -> Font {
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
        Font.registerFont(filename: "Poppins-Medium.ttf", bundle: bundle)
        Font.registerFont(filename: "Poppins-Bold.ttf", bundle: bundle)
    }
    
    func fontName(_ weight: Font.Weight) -> String {
        switch weight {
        case .ultraLight, .thin, .light:
            return "Poppins-Medium"
        case .semibold, .heavy, .bold, .black:
            return "Poppins-Bold"
        default:
            return Font.isBoldTextEnabled ? "Poppins-Bold" : "Poppins-Medium"
        }
    }
}

