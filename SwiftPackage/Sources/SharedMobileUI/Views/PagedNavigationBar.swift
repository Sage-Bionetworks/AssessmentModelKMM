//
//  PagedNavigationBar.swift
//

import SwiftUI

/// A view for displaying forward/back navigation buttons and a progress indicator.
/// This view uses the ``PagedNavigationViewModel`` as its view model with that
/// object set up as an `@EnvironmentObject`.
public struct PagedNavigationBar : View {
    @EnvironmentObject private var viewModel: PagedNavigationViewModel
    let showsDots: Bool
    
    public init(showsDots: Bool = true) {
        self.showsDots = showsDots
    }
    
    public var body: some View {
        VStack(spacing: 0.0) {
            
            if self.showsDots, viewModel.pageCount > 0, !viewModel.progressHidden {
                PagingDotsView()
                    .padding(.vertical, 8)
            }
                
            HStack {
                Button(action: viewModel.goBack, label: {
                    Text("Back", bundle: .module)
                })
                .buttonStyle(NavigationButtonStyle(.backward))
                .opacity(viewModel.backEnabled ? 1.0 : 0.0)
                
                Spacer()
                                
                Button(action: viewModel.goForward, label: {
                    if let buttonText = viewModel.forwardButtonText {
                        buttonText
                    }
                    else {
                        Text("Next", bundle: .module)
                    }
                })
                .buttonStyle(NavigationButtonStyle((viewModel.forwardButtonText == nil) ? .forwardArrow : .text))
                .buttonEnabled(viewModel.forwardEnabled)
            }
        }
    }
}

extension View {
    public func buttonEnabled(_ enabled: Bool) -> some View {
        modifier(ButtonEnabledModifier(enabled: enabled))
    }
}

public struct ButtonEnabledModifier : ViewModifier {
    var enabled: Bool
    public init(enabled: Bool) {
        self.enabled = enabled
    }
    
    public func body(content: Content) -> some View {
        content
            .opacity(enabled ? 1.0 : 0.5)
            .disabled(!enabled)
    }
}

struct PagingDotsView : View {
    @EnvironmentObject var viewModel: PagedNavigationViewModel
    let dotSize: CGFloat = 10
    var body: some View {
        HStack {
            ForEach(0..<viewModel.pageCount, id: \.self) { index in // 1
                      Circle()
                        .stroke(Color.sageBlack)
                        .background(Circle().foregroundColor(viewModel.currentIndex == index ?  .sageBlack : Color.clear))
                        .frame(width: dotSize, height: dotSize)
                        .id(index) // 4
                  }
        }
    }
}

struct PagedNavigationBar_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            PagedNavigationBar()
                .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 2))
            PagedNavigationBar()
                .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 2))
                .preferredColorScheme(.dark)
            PagedNavigationBar()
                .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 0))
            PagedNavigationBar()
                .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 1, buttonText: Text("Next")))
            PagedNavigationBar()
                .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 1, buttonText: Text("Next")))
                .environment(\.sizeCategory, .accessibilityExtraExtraExtraLarge)
            PagedNavigationBar()
                .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 1, buttonText: Text("Next")))
                .preferredColorScheme(.dark)
        }
    }
}

