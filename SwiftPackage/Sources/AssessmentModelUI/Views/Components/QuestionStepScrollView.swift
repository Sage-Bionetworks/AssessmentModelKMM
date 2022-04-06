//
//  QuestionStepScrollView.swift
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

public struct QuestionStepScrollView<Content> : View where Content : View {
    @SwiftUI.Environment(\.fullscreenBackgroundColor) var backgroundColor: Color
    @SwiftUI.Environment(\.sizeCategory) var sizeCategory
    @EnvironmentObject var questionState: QuestionState
    @EnvironmentObject var pagedNavigation: PagedNavigationViewModel
    @StateObject var keyboard: KeyboardObserver = .init()
    
    private let keyboardSpacing: CGFloat
    private let content: Content
    
    public init(keyboardSpacing: CGFloat = 32, @ViewBuilder content: @escaping () -> Content) {
        self.keyboardSpacing = keyboardSpacing
        self.content = content()
    }
    
    @State var minTitleHeight: CGFloat = 0
    @State var titleHeight: CGFloat = 0
    @State var subtitleHeight: CGFloat = 0
    @State var detailHeight: CGFloat = 0
    @State var scrollOffset: CGFloat = 0

    @State var collapsedTitleHeight: CGFloat = 0
    @State var collapsedHeader: Bool = true
        
    public var body: some View {
        ZStack(alignment: .top) {
            
            // Used to calulate the minimum title height of 2 lines.
            minTitleCalculator()
            
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
                            keyboardSpacer(scrollView)
                            Spacer()
                            SurveyNavigationView()
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
                        if questionState.hasSelectedAnswer {
                            scrollView.scrollTo("bottomNav:\(questionState.id)", anchor: .bottom)
                        }
                    }
                }
            }
            
            // The overlay header is displayed on top of the scrollview
            overlayHeader()
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
    }
    
    @ViewBuilder
    func minTitleCalculator() -> some View {
        Text(questionState.title)
            .opacity(0)
            .lineLimit(2)
            .headerTextStyle(.defaultQuestionTitleFont)
            .padding(.vertical, innerVerticalSpacing)
            .heightReader(height: $minTitleHeight)
    }
    
    @ViewBuilder
    func inlineHeader() -> some View {
        if let subtitle = questionState.subtitle {
            Text(subtitle)
                .headerTextStyle(.defaultQuestionSubtitleFont)
                .padding(.top, innerVerticalSpacing)
                .heightReader(height: $subtitleHeight)
        }
        Text(questionState.title)
            .headerTextStyle(.defaultQuestionTitleFont)
            .fixedSize(horizontal: false, vertical: true)
            .padding(.vertical, innerVerticalSpacing)
            .heightReader(height: $titleHeight)
        if let detail = questionState.detail {
            Text(detail)
                .headerTextStyle(.defaultQuestionDetailFont)
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
                    .headerTextStyle(.defaultQuestionSubtitleFont)
                    .padding(.top, collapsedHeader ? 0 : innerVerticalSpacing)
                    .frame(height: collapsedHeader ? 0 : subtitleHeight)
            }
            Text(questionState.title)
                .headerTextStyle(.defaultQuestionTitleFont)
                .padding(.vertical, innerVerticalSpacing)
                .frame(maxHeight: collapsedHeader ? collapsedTitleHeight : titleHeight)
            if let detail = questionState.detail {
                Text(detail)
                    .headerTextStyle(.defaultQuestionDetailFont)
                    .padding(.bottom, collapsedHeader ? 0 : innerVerticalSpacing)
                    .frame(height: collapsedHeader ? 0 : detailHeight)
            }
        }
        .background(
            backgroundColor
                .shadow(color: .hex2A2A2A.opacity(0.1), radius: 3, x: 1, y: 2)
                .mask(Rectangle().padding(.bottom, -20))
        )
        .opacity(scrollOffset <= subtitleHeight || sizeCategory >= .accessibilityLarge ? 0 : 1)
        .onTapGesture {
            if scrollOffset > subtitleHeight && !keyboard.keyboardFocused {
                withAnimation {
                    collapsedHeader = false
                }
            }
        }
    }
    
    @ViewBuilder
    func keyboardSpacer(_ scrollView: ScrollViewProxy) -> some View {
        Spacer()
            .frame(height: keyboard.keyboardFocused ? keyboard.keyboardHeight + keyboardSpacing : 0)
            .id(KeyboardObserver.defaultKeyboardFocusedId)
            .onChange(of: keyboard.keyboardFocused) { newValue in
                if newValue {
                    scrollView.scrollTo(keyboard.keyboardFocusedId, anchor: .bottom)
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

