//
//  StepHeaderView.swift
//
//

import SwiftUI
import SharedMobileUI

public struct StepHeaderView : View {
    @EnvironmentObject var assessmentState: AssessmentState
    @EnvironmentObject var pagedNavigation: PagedNavigationViewModel
    @ObservedObject var nodeState: StepState
    
    public init(_ nodeState: StepState) {
        self.nodeState = nodeState
    }
    
    public var body: some View {
        HStack {
            if nodeState.skipStepText == nil && !assessmentState.canPause {
                Spacer()
            }
            ExitButton(canPause: assessmentState.canPause)
            if let text = nodeState.skipStepText {
                Spacer()
                UnderlinedButton(label: text) {
                    nodeState.willSkip()
                    pagedNavigation.goForward()
                }
                .padding(.trailing, 15)
            }
            else if assessmentState.canPause {
                Spacer()
            }
        }
        .accentColor(.sageBlack)
        .fixedSize(horizontal: false, vertical: true)
    }
}

public struct StepHeaderTitleView <Content : View> : View {
    @EnvironmentObject var assessmentState: AssessmentState
    private let content: Content
    
    public init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content()
    }
    
    public var body: some View {
        HStack {
            ExitButton(canPause: assessmentState.canPause)
                .opacity(assessmentState.canPause ? 1 : 0)
            Spacer()
            content
            Spacer()
            ExitButton(canPause: assessmentState.canPause)
                .opacity(assessmentState.canPause ? 0 : 1)
        }
        .accentColor(.sageBlack)
        .fixedSize(horizontal: false, vertical: true)
    }
}

