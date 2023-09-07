//
//  ScreenBackground.swift
//

import SwiftUI

/// A view for setting the screen background to be ``Color.screenBackground``.
public struct ScreenBackground<Content> : View where Content : View {
    private let content: Content
    public init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content()
    }
    public var body: some View {
        Color.screenBackground
            .edgesIgnoringSafeArea(.all)
            .overlay(content)
    }
}

struct ScreenBackground_Previews: PreviewProvider {
    static var previews: some View {
        ScreenBackground { Text(/*@START_MENU_TOKEN@*/"Hello, World!"/*@END_MENU_TOKEN@*/) }
    }
}
