//
//  NavigationButtonStyle.swift
//  
//

import SwiftUI

/// A  button style used to define the style of a button used in navigating.
public struct NavigationButtonStyle : ButtonStyle {
    @SwiftUI.Environment(\.backButtonStyle) var backButtonStyle: BackButtonStyle
    private let foregroundColor: Color
    private let backgroundColor: Color
    private let style: Style
    private let font: Font = DesignSystem.fontRules.buttonFont(at: 1, isSelected: false)
    
    /// The style of navigation. This can be ``forward``, ``backward``, or ``text`` where
    /// the ``text`` style indicates that the configuration ``Label`` should be shown rather
    /// than replacing the accessibility text with an image of an arrow.
    public enum Style {
        case text, forward, backward, forwardArrow, backArrow
    }
    
    /// Initializer.
    /// - Parameters:
    ///   - style: The ``Style`` of the button.
    ///   - foregroundColor: The foreground color.
    ///   - backgroundColor: The background color.
    public init(_ style: Style = .text,
                foregroundColor: Color = Color.sageWhite,
                backgroundColor: Color = Color.sageBlack) {
        self.style = style
        self.foregroundColor = foregroundColor
        self.backgroundColor = backgroundColor
    }
    
    @ViewBuilder public func makeBody(configuration: Self.Configuration) -> some View {
        switch style {
        case .forward:
            Image.nextButton
        case .backward:
            Image.backButton
                .background(Circle().fill(backButtonColor()))
        default:
            HStack {
                if style == .backArrow {
                    Image.backArrow
                }
                configuration.label
                if style == .forwardArrow {
                    Image.forwardArrow
                }
            }
            .font(font)
            .foregroundColor(self.foregroundColor)
            .fixedSize(horizontal: false, vertical: true)
            .frame(height: 48)
            .padding(.horizontal, 32)
            .background(self.backgroundColor)
            .clipShape(Capsule())
        }
    }
    
    func backButtonColor() -> Color {
        switch backButtonStyle {
        case .clear:
            return .clear
        case .white:
            return .sageWhite
        case .black:
            return .sageBlack
        }
    }
    
    public enum BackButtonStyle : String, Codable {
        case clear, white, black
    }
}

struct NavigationButtonStyle_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            Button("Button", action: {})
                .buttonStyle(NavigationButtonStyle())
            Button("Button", action: {})
                .buttonStyle(NavigationButtonStyle(.forward))
            Button("Button", action: {})
                .buttonStyle(NavigationButtonStyle(.forwardArrow))
        }
    }
}

struct BackButtonStyleEnvironmentKey: EnvironmentKey {
    static let defaultValue: NavigationButtonStyle.BackButtonStyle = .clear
}

extension EnvironmentValues {
    var backButtonStyle: NavigationButtonStyle.BackButtonStyle {
        get { self[BackButtonStyleEnvironmentKey.self] }
        set { self[BackButtonStyleEnvironmentKey.self] = newValue }
    }
}

extension View {
    func backButtonStyle(_ backButtonStyle: NavigationButtonStyle.BackButtonStyle) -> some View {
        environment(\.backButtonStyle, backButtonStyle)
    }
}
