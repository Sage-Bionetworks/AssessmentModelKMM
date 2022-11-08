//
//  QuestionStepScrollView.swift
//
//

import SwiftUI
import SharedMobileUI

public struct QuestionStepScrollView<Content> : View where Content : View {
    @SwiftUI.Environment(\.fullscreenBackgroundColor) var backgroundColor: Color
    @SwiftUI.Environment(\.sizeCategory) var sizeCategory
    @SwiftUI.Environment(\.innerSpacing) var innerVerticalSpacing: CGFloat
    @SwiftUI.Environment(\.verticalPadding) var outerVerticalPadding: CGFloat
    @EnvironmentObject var questionState: QuestionState
    @EnvironmentObject var pagedNavigation: PagedNavigationViewModel
    @StateObject var keyboard: KeyboardObserver = .init()
    
    private let content: Content
    private let keyboardAnchor: KeyboardAnchor
    private let shouldCollapseHeader: Bool
    private let shouldScrollOnFocus: Bool
    
    public init(keyboardAnchor: KeyboardAnchor = .bottom, shouldScrollOnFocus: Bool = true, @ViewBuilder content: @escaping () -> Content) {
        self.keyboardAnchor = keyboardAnchor
        self.shouldCollapseHeader = shouldScrollOnFocus && (keyboardAnchor == .bottom)
        self.shouldScrollOnFocus = shouldScrollOnFocus
        self.content = content()
    }
    
    public enum KeyboardAnchor : Int {
        case none, top, bottom
        var anchor: UnitPoint? {
            switch self {
            case .top:
                return .top
            case .bottom:
                return .bottom
            default:
                return nil
            }
        }
    }
    
    @State var minTitleHeight: CGFloat = 0
    @State var titleHeight: CGFloat = 0
    @State var subtitleHeight: CGFloat = 0
    @State var detailHeight: CGFloat = 0
    
    @State var scrollOffset: CGFloat = 0

    @State var collapsedTitleHeight: CGFloat = 0
    @State var collapsedHeader: Bool = true
    @State var overlayHeaderHidden: Bool = true
    @State var dividerHidden: Bool = true
        
    public var body: some View {
        VStack(spacing: 0) {
            StepHeaderView(questionState)
            ZStack(alignment: .top) {
                // Used to calulate the minimum title height of 2 lines.
                minTitleCalculator()
                // Scrollview is in the middle
                contentScrollView()
                // The overlay header is displayed on top of the scrollview
                overlayHeader()
                Divider()
                    .background(Color.hexB8B8B8)
                    .opacity(dividerHidden ? 0 : 1)
            }
        }
        .environmentObject(keyboard)
        .onAppear {
            updateNavigationState()
        }
        .onChange(of: questionState.hasSelectedAnswer) { _ in
            updateNavigationState()
        }
    }
     
    func updateNavigationState() {
        // The forward navigation is enabled if the question state has an answer or the question is optional.
        pagedNavigation.forwardEnabled = questionState.hasSelectedAnswer || questionState.question.optional
    }
    
    @ViewBuilder
    func minTitleCalculator() -> some View {
        Text(questionState.title)
            .opacity(0)
            .lineLimit(2)
            .headerTextStyle(.stepTitle)
            .padding(.vertical, 6)
            .heightReader(height: $minTitleHeight)
    }
    
    @ViewBuilder
    func contentScrollView() -> some View {
        GeometryReader { scrollViewGeometry in
            ScrollViewReader { scrollView in
                ScrollView {  // Main content for the view includes header, content, and navigation footer
                    VStack(spacing: 0) {
                        GeometryReader { proxy in
                            Color.clear
                                .preference(key: ScrollFramePreferenceKey.self,
                                            value: proxy.frame(in: .named("scroll")))
                        }.frame(height: 0)
                        inlineHeader()
                        content
                        Spacer(minLength: 0)
                        Color.clear
                            .frame(height: keyboard.keyboardState.focused ? 32 : 0)
                            .id("$ScrollView.bottomSpacer")
                        SurveyNavigationView()
                            .frame(minHeight: 0, maxHeight: keyboard.keyboardState.focused ? 0 : .none)
                            .id("bottomNav:\(questionState.id)")
                    }
                    .frame(minHeight: scrollViewGeometry.size.height)
                }
                .coordinateSpace(name: "scroll")
                .simultaneousGesture(
                    TapGesture()
                        .onEnded { _ in
                            self.collapsedHeader = true
                        }
                )
                .onPreferenceChange(ScrollFramePreferenceKey.self) { value in
                    DispatchQueue.main.async {
                        updateScrollOffset(-floor(value.minY))
                    }
                }
                .onAppear {
                    scrollToNavButtons(scrollView)
                }
                .onChange(of: keyboard.keyboardState) { newValue in
                    if newValue.focused {
                        if shouldScrollOnFocus {
                            scrollToTextEntry(scrollView)
                        }
                    }
                    else {
                        scrollToNavButtons(scrollView)
                    }
                }
                .onChange(of: keyboard.textFieldHeight) { _ in
                    if keyboardAnchor == .bottom, keyboard.cursorAtEnd {
                        if keyboard.keyboardState.focused {
                            scrollToTextEntry(scrollView)
                        }
                        else {
                            scrollToNavButtons(scrollView)
                        }
                    }
                }
            }
        }
    }
    
    func scrollToNavButtons(_ scrollView: ScrollViewProxy) {
        if questionState.hasSelectedAnswer {
            scrollView.scrollTo("bottomNav:\(questionState.id)", anchor: .bottom)
        }
    }
    
    func scrollToTextEntry(_ scrollView: ScrollViewProxy) {
        let scrollId = keyboardAnchor == .bottom ? "$ScrollView.bottomSpacer" : keyboard.keyboardFocusedId
        scrollView.scrollTo(scrollId, anchor: keyboardAnchor.anchor)
    }
    
    func updateScrollOffset(_ scrollOffset: CGFloat) {
        let headerHeight = subtitleHeight + detailHeight - innerVerticalSpacing + 4
        let beyondDetail = scrollOffset - headerHeight
        if beyondDetail > 0 {
            self.collapsedTitleHeight = max(minTitleHeight, titleHeight - beyondDetail)
        }
        else {
            self.collapsedTitleHeight = titleHeight
        }
        self.collapsedHeader = true
        self.scrollOffset = scrollOffset
        withAnimation {
            self.overlayHeaderHidden = scrollOffset <= subtitleHeight || sizeCategory >= .accessibilityLarge || !shouldCollapseHeader
            self.dividerHidden = scrollOffset <= 0 || !overlayHeaderHidden
        }
    }
    
    @ViewBuilder
    func inlineHeader() -> some View {
        if let subtitle = questionState.subtitle {
            Text(subtitle)
                .headerTextStyle(.stepSubtitle)
                .padding(.top, innerVerticalSpacing)
                .heightReader(height: $subtitleHeight)
        }
        Text(questionState.title)
            .headerTextStyle(.stepTitle)
            .fixedSize(horizontal: false, vertical: true)
            .padding(.vertical, innerVerticalSpacing)
            .heightReader(height: $titleHeight)
        if let detail = questionState.detail {
            Text(detail)
                .headerTextStyle(.stepDetail)
                .padding(.bottom, innerVerticalSpacing)
                .heightReader(height: $detailHeight)
        }
        Spacer().frame(height: outerVerticalPadding - innerVerticalSpacing)
    }
    
    @ViewBuilder
    func overlayHeader() -> some View {
        VStack(spacing: 0) {
            if let subtitle = questionState.subtitle {
                Text(subtitle)
                    .headerTextStyle(.stepSubtitle)
                    .padding(.top, collapsedHeader ? 0 : innerVerticalSpacing)
                    .frame(height: collapsedHeader ? 0 : subtitleHeight)
            }
            Text(questionState.title)
                .headerTextStyle(.stepTitle)
                .frame(height: collapsedHeader ? collapsedTitleHeight : titleHeight, alignment: .leading)
            if let detail = questionState.detail {
                Text(detail)
                    .headerTextStyle(.stepDetail)
                    .padding(.bottom, collapsedHeader ? 0 : innerVerticalSpacing)
                    .frame(height: collapsedHeader ? 0 : detailHeight)
            }
        }
        .background(
            backgroundColor
                .shadow(color: .hex2A2A2A.opacity(0.1), radius: 3, x: 1, y: 2)
                .mask(Rectangle().padding(.bottom, -20))
        )
        .opacity(overlayHeaderHidden ? 0 : 1)
        .onTapGesture {
            if scrollOffset > subtitleHeight && !keyboard.keyboardFocused {
                withAnimation {
                    collapsedHeader = false
                }
            }
        }
    }
}

extension View {
    fileprivate func headerTextStyle(_ font: Font) -> some View {
        modifier(HeaderText(font: font))
    }
}

fileprivate struct HeaderText : ViewModifier {
    let font: Font
    func body(content: Content) -> some View {
        content
            .font(font)
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal, 36)
    }
}

fileprivate struct ScrollFramePreferenceKey: PreferenceKey {
    static var defaultValue: CGRect = .zero
    static func reduce(value: inout CGRect, nextValue: () -> CGRect) {}
}

