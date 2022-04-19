//
//  GlobalProperties.swift
//
//
//  Copyright © 2022 Sage Bionetworks. All rights reserved.
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
import SharedMobileUI

let textFieldFontSize: CGFloat = 20

let outerVerticalPadding: CGFloat = 24
let innerVerticalSpacing: CGFloat = 16

extension Font {
    static let textField: Font = .latoFont(fixedSize: textFieldFontSize, weight: .bold)
    
    static let questionTitle: Font = .latoFont(24, relativeTo: .title, weight: .bold)
    static let questionSubtitle: Font = .latoFont(18, relativeTo: .subheadline, weight: .regular)
    static let questionDetail: Font = .latoFont(18, relativeTo: .footnote, weight: .regular)
    
    static let skipQuestionButton: Font = .latoFont(fixedSize: 18, weight: .regular)
    
    static let roundedButton: Font = .latoFont(fixedSize: 20, weight: .bold)
    
    static let pauseMenuTitle: Font = .latoFont(fixedSize: 24, weight: .bold)
}

#if canImport(UIKit)
import UIKit
extension UIFont {
    static let textField: UIFont = .latoFont(textFieldFontSize, relativeTo: nil, weight: .bold)
}
#endif

extension Color {
    static let surveyBackground: Color = .hexF6F6F6
    
    static let progressBackground: Color = .init(hex: "#A7A19C")!
    
    static let pauseMenuBackground: Color = .init(hex: "#575E71")!.opacity(0.95)
    static let pauseMenuForeground: Color = .init(hex: "#FCFCFC")!
    static let pauseMenuResumeText: Color = .init(hex: "#2A2A2A")!
}