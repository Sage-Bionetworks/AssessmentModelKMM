//
//  RoundedButtonStyle.swift
//

import SwiftUI

/// A button with rounded corners.
public struct RoundedButtonStyle : ButtonStyle {
    private let foregroundColor: Color
    private let backgroundColor: Color
    private let horizontalPadding: CGFloat
    
    /// Initializer.
    /// - Parameters:
    ///   - backgroundColor: The background color for the button.
    ///   - foregroundColor: The color of the text.
    ///   - horizontalPadding: The horizontal padding.
    public init(_ backgroundColor: Color = .accentColor, foregroundColor: Color = .textForeground, horizontalPadding: CGFloat = 40) {
        self.backgroundColor = backgroundColor
        self.foregroundColor = foregroundColor
        self.horizontalPadding = horizontalPadding
    }
    
    @ViewBuilder public func makeBody(configuration: Self.Configuration) -> some View {
        configuration.label
            .roundedButton(backgroundColor, foregroundColor: foregroundColor, horizontalPadding: horizontalPadding)
    }
}

extension View {
    public func roundedButton(_ backgroundColor: Color = .accentColor, foregroundColor: Color = .textForeground, horizontalPadding: CGFloat = 40) -> some View {
        modifier(RoundedButtonModifier(backgroundColor, foregroundColor: foregroundColor, horizontalPadding: horizontalPadding))
    }
}

public struct RoundedButtonModifier : ViewModifier {
    private let foregroundColor: Color
    private let backgroundColor: Color
    private let horizontalPadding: CGFloat
    private let font: Font = DesignSystem.fontRules.buttonFont(at: 1, isSelected: false)
    
    public init(_ backgroundColor: Color = .accentColor, foregroundColor: Color = .textForeground, horizontalPadding: CGFloat = 40) {
        self.backgroundColor = backgroundColor
        self.foregroundColor = foregroundColor
        self.horizontalPadding = horizontalPadding
    }
    
    public func body(content: Content) -> some View {
        content
            .font(font)
            .foregroundColor(foregroundColor)
            .frame(minHeight: 48, idealHeight: 48)
            .frame(minWidth: 209, idealWidth: 209)
            .padding(.horizontal, horizontalPadding)
            .background(self.backgroundColor)
            .clipShape(Capsule())
            .shadow(color: Color.sageBlack.opacity(0.1), radius: 3, x: 1, y: 1)
    }
}
