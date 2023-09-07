//
//  FullscreenBackground.swift
//
//

import SwiftUI

struct FullscreenBackgroundEnvironmentKey: EnvironmentKey {
    static let defaultValue: Color = .screenBackground
}

extension EnvironmentValues {
    public var fullscreenBackgroundColor: Color {
        get { self[FullscreenBackgroundEnvironmentKey.self] }
        set { self[FullscreenBackgroundEnvironmentKey.self] = newValue }
    }
}

extension View {
    func fullscreenBackgroundColor(_ backgroundColor: Color) -> some View {
        environment(\.fullscreenBackgroundColor, backgroundColor)
    }

    public func fullscreenBackground(_ backgroundColor: Color = .screenBackground,
                                     backButtonStyle: NavigationButtonStyle.BackButtonStyle = .clear) -> some View {
        modifier(FullscreenBackground())
            .fullscreenBackgroundColor(backgroundColor)
            .backButtonStyle(backButtonStyle)
    }
}

struct FullscreenBackground : ViewModifier {
    @SwiftUI.Environment(\.fullscreenBackgroundColor) var backgroundColor: Color
    
    func body(content: Content) -> some View {
        backgroundColor
            .edgesIgnoringSafeArea(.all)
            .overlay(content)
    }
}
