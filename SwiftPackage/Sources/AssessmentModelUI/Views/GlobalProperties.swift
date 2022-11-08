//
//  GlobalProperties.swift
//
//

import SwiftUI
import SharedMobileUI

let textFieldFontSize: CGFloat = 20

extension Font {
    static let textField: Font = .latoFont(fixedSize: textFieldFontSize, weight: .bold)
    
    static let stepTitle: Font = .latoFont(24, relativeTo: .title, weight: .bold)
    static let stepSubtitle: Font = .latoFont(18, relativeTo: .subheadline, weight: .regular)
    static let stepDetail: Font = .latoFont(18, relativeTo: .footnote, weight: .regular)
    
    static let underlinedButton: Font = .latoFont(fixedSize: 18, weight: .regular)
    static let roundedButton: Font = DesignSystem.fontRules.buttonFont(at: 1, isSelected: false)
    
    static let pauseMenuTitle: Font = .latoFont(fixedSize: 24, weight: .bold)
    
    static let fieldLabel: Font = .latoFont(18, relativeTo: .caption, weight: .regular)
    static let durationFieldLabel: Font = .latoFont(fixedSize: 18, weight: .regular)
    
    static let likertLabel: Font = .latoFont(fixedSize: 20, weight: .bold)
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

struct InnerSpacingEnvironmentKey: EnvironmentKey {
    static let defaultValue: CGFloat = 16
}

struct VerticalPaddingEnvironmentKey: EnvironmentKey {
    static let defaultValue: CGFloat = 24
}

struct HorizontalPaddingEnvironmentKey: EnvironmentKey {
    static let defaultValue: CGFloat = 32
}

enum TextFieldStyle : Int {
    case otherChoice, freeText
}

struct TextFieldStyleEnvironmentKey: EnvironmentKey {
    static let defaultValue: TextFieldStyle = .otherChoice
}

extension EnvironmentValues {
    
    public var surveyTintColor: Color {
        get { self[SurveyTintColorEnvironmentKey.self] }
        set { self[SurveyTintColorEnvironmentKey.self] = newValue }
    }
    
    var verticalPadding: CGFloat {
        get { self[VerticalPaddingEnvironmentKey.self] }
        set { self[VerticalPaddingEnvironmentKey.self] = newValue }
    }
    
    var horizontalPadding: CGFloat {
        get { self[HorizontalPaddingEnvironmentKey.self] }
        set { self[HorizontalPaddingEnvironmentKey.self] = newValue }
    }
    
    var innerSpacing: CGFloat {
        get { self[InnerSpacingEnvironmentKey.self] }
        set { self[InnerSpacingEnvironmentKey.self] = newValue }
    }
}

extension View {
    public func surveyTintColor(_ templateColor: Color) -> some View {
        environment(\.surveyTintColor, templateColor)
    }
    
    func innerSpacing(_ newValue: CGFloat) -> some View {
        environment(\.innerSpacing, newValue)
    }
    
    func verticalPadding(_ newValue: CGFloat) -> some View {
        environment(\.verticalPadding, newValue)
    }
    
    func horizontalPadding(_ newValue: CGFloat) -> some View {
        environment(\.horizontalPadding, newValue)
    }
}
