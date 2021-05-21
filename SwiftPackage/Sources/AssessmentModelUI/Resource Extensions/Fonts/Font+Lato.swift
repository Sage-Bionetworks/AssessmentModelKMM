//
//  Font+Lato.swift
//
//  Copyright © 2019-2021 Sage Bionetworks. All rights reserved.
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

import SwiftUI

extension Font {
    
    /// Returns the most appropriate Lato font found by the system. This will first check for the
    /// Lato font that matches the requested weight. If not found (because this framework only
    /// includes a subset of the full Lato font family), then it will fall back to the most
    /// appropriate font registered from this framework.
    ///
    /// This framework does not include all the possible sizes and weights for the Lato font
    /// because the full font family is about 11 mb.
    ///
    /// - parameters:
    ///     - size: The size of the font requested.
    ///     - textSyle: The relative text style for the text.
    ///     - weight: The weight of the font requested.
    /// - returns: The closest font to the one requested that is registered for this application.
    static func latoFont(_ size: CGFloat, relativeTo textStyle: TextStyle = .body, weight: Font.Weight = .regular) -> Font {
        let fontName = FontWrapper.shared.fontName(weight)
        if #available(iOS 14.0, *) {
            return .custom(fontName, size: size, relativeTo: textStyle)
        } else {
            return .custom(fontName, size: size)
        }
    }
    
    /// Returns the Lato italic font embedded in this framework.
    /// - seealso: `latoFont()`
    static func italicLatoFont(_ size: CGFloat, relativeTo textStyle: TextStyle = .body, weight: Font.Weight = .regular) -> Font {
        let fontName = FontWrapper.shared.italicFontName(weight)
        if #available(iOS 14.0, *) {
            return .custom(fontName, size: size, relativeTo: textStyle)
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
        Font.registerFont(filename: "lato_black.ttf", bundle: bundle)
        Font.registerFont(filename: "lato_bold.ttf", bundle: bundle)
        Font.registerFont(filename: "lato_bolditalic.ttf", bundle: bundle)
        Font.registerFont(filename: "lato_italic.ttf", bundle: bundle)
        Font.registerFont(filename: "lato_light.ttf", bundle: bundle)
        Font.registerFont(filename: "lato_lightitalic.ttf", bundle: bundle)
        Font.registerFont(filename: "lato_regular.ttf", bundle: bundle)
    }
    
    func fontName(_ weight: Font.Weight) -> String {
        switch weight {
        case .ultraLight:
            return "Lato-Light"
        case .thin:
            return "Lato-Light"
        case .light:
            return "Lato-Light"
        case .semibold:
            return "Lato-Bold"
        case .heavy:
            return "Lato-Bold"
        case .bold:
            return "Lato-Bold"
        case .black:
            return "Lato-Black"
        case .medium:
            return "Lato-Regular"
        default:
            return "Lato-Regular"
        }
    }
    
    func italicFontName(_ weight: Font.Weight) -> String {
        switch weight {
        case .ultraLight:
            return "Lato-LightItalic"
        case .thin:
            return "Lato-LightItalic"
        case .light:
            return "Lato-LightItalic"
        case .semibold:
            return "Lato-BoldItalic"
        case .bold:
            return "Lato-BoldItalic"
        case .heavy:
            return "Lato-BoldItalic"
        case .black:
            return "Lato-BoldItalic"
        case .medium:
            return "Lato-Italic"
        default:
            return "Lato-Italic"
        }
    }
}

