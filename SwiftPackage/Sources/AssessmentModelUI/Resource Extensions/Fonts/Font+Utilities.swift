//
//  Font+Utilities.swift
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

#if os(macOS)
import AppKit
fileprivate typealias TypedFont = NSFont
#else
import UIKit
fileprivate typealias TypedFont = UIFont
#endif

extension Font {
    
    /// Rough estimate of the dynamic text size for body text style. This can be used to resize or hide
    /// secondary images when the text would be extra large.
    static func fontRatio() -> CGFloat {
        let font = TypedFont.preferredFont(forTextStyle: .body)
        return font.pointSize / 17
    }

    /// https://stackoverflow.com/questions/30507905/xcode-using-custom-fonts-inside-dynamic-framework
    static func registerFont(filename: String, bundle: Bundle) {
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

