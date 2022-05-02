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
    
    static let stepTitle: Font = .latoFont(24, relativeTo: .title, weight: .bold)
    static let stepSubtitle: Font = .latoFont(18, relativeTo: .subheadline, weight: .regular)
    static let stepDetail: Font = .latoFont(18, relativeTo: .footnote, weight: .regular)
    
    static let underlinedButton: Font = .latoFont(fixedSize: 18, weight: .regular)
    static let roundedButton: Font = DesignSystem.fontRules.buttonFont(at: 1, isSelected: false)
    
    static let pauseMenuTitle: Font = .latoFont(fixedSize: 24, weight: .bold)
    
    static let fieldLabel: Font = .latoFont(18, relativeTo: .caption, weight: .regular)
}

#if canImport(UIKit)
import UIKit
extension UIFont {
    static let textField: UIFont = .latoFont(textFieldFontSize, relativeTo: nil, weight: .bold)
}
#endif

extension Color {
    static let lightSurveyBackground: Color = .init("lightSurveyBackground", bundle: .module)
    static let darkSurveyBackground: Color = .init("darkSurveyBackground", bundle: .module)
    
    static let progressBackground: Color = .init(hex: "#A7A19C")!
    
    static let pauseMenuBackground: Color = .init("pauseMenuBackground", bundle: .module)
    static let pauseMenuForeground: Color = .init("pauseMenuForeground", bundle: .module)
    static let pauseMenuResumeText: Color = .init("pauseMenuResumeText", bundle: .module)
    
    static let sliderBackground: Color = .init("sliderBackground", bundle: .module)
    static let likertDotBackground: Color = .init("likertDotBackground", bundle: .module)
}

struct SurveyTintColorEnvironmentKey: EnvironmentKey {
    static let defaultValue: Color = .init("surveyTint", bundle: .module)
}

extension EnvironmentValues {
    public var surveyTintColor: Color {
        get { self[SurveyTintColorEnvironmentKey.self] }
        set { self[SurveyTintColorEnvironmentKey.self] = newValue }
    }
}

extension View {
    public func surveyTintColor(_ templateColor: Color) -> some View {
        environment(\.surveyTintColor, templateColor)
    }
}
