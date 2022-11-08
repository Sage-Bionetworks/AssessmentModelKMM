//
//  ForwardButton.swift
//
//

import SwiftUI
import SharedMobileUI

public struct ForwardButton <Content: View> : View {
    @SwiftUI.Environment(\.verticalPadding) var verticalPadding: CGFloat
    @SwiftUI.Environment(\.horizontalPadding) var horizontalPadding: CGFloat
    @EnvironmentObject var pagedNavigation: PagedNavigationViewModel
    let content: Content
    
    init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content()
    }

    public var body: some View {
        Button(action: pagedNavigation.goForward) {
            content
                .frame(minWidth: 209)
        }
        .buttonStyle(NavigationButtonStyle(.text))
        .padding(.horizontal, horizontalPadding)
        .padding(.vertical, verticalPadding)
    }
}
