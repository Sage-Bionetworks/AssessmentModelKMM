//
//  CountdownStepView.swift
//
//

import SwiftUI
import AssessmentModel
import SharedMobileUI

public struct CountdownStepView: View {
    @SwiftUI.Environment(\.surveyTintColor) var surveyTint: Color
    @SwiftUI.Environment(\.horizontalPadding) var horizontalPadding: CGFloat
    @EnvironmentObject var assessmentState: AssessmentState
    @EnvironmentObject var pagedNavigation: PagedNavigationViewModel
    @ObservedObject var nodeState: StepState
    @State var countdown: Int = 5
    @State var paused: Bool = false
    @State var progress: CGFloat = .zero
    @State var timer = Timer.publish(every: 1, on: .main, in: .common).autoconnect()
    let startDuration: TimeInterval
    
    public init(_ nodeState: StepState) {
        self.nodeState = nodeState
        self.startDuration = (nodeState.step as? CountdownStep)?.duration ?? 5.0
    }
    
    public var body: some View {
        VStack {
            StepHeaderView(nodeState)
            
            Spacer()
            
            // Countdown
            VStack(spacing: 16) {
                Text("Begin in...", bundle: .module)
                    .font(.stepTitle)
                Text("\(countdown)")
                    .font(.latoFont(96, relativeTo: .title, weight: .bold))
                    .padding(64)
                    .background(
                        Circle()
                            .trim(from: 0.0, to: min(progress, 1.0))
                            .stroke(style: StrokeStyle(lineWidth: 8, lineCap: .round))
                            .foregroundColor(surveyTint)
                            .rotationEffect(Angle(degrees: 270.0))
                    )
            }
            .foregroundColor(.textForeground)
            .padding(.horizontal, horizontalPadding)
            
            Spacer()
        }
        .onAppear {
            start()
        }
        .onDisappear {
            timer.upstream.connect().cancel()
        }
        .onChange(of: assessmentState.showingPauseActions) { newValue in
            guard paused != newValue else { return }
            paused = newValue
            if paused {
                timer.upstream.connect().cancel()
                stop()
            }
            else if countdown > 0 {
                timer = Timer.publish(every: 1, on: .main, in: .common).autoconnect()
                start()
            }
        }
        .onReceive(timer) { time in
            guard !paused, countdown > 0 else { return }
            countdown = max(countdown - 1, 0)
            if countdown == 0 {
                pagedNavigation.goForward()
            }
        }
    }
    
    func start() {
        countdown = Int(startDuration)
        withAnimation(.linear(duration: startDuration)) {
            progress = 1.0
        }
    }
    
    func stop() {
        withAnimation(.linear(duration: 0)) {
            progress = 0
        }
    }
}

struct CountdownStepView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            CountdownStepView(StepState(step: example1, parentId: nil))
                .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 0))
                .environmentObject(AssessmentState(AssessmentObject(previewStep: example1)))
            CountdownStepView(StepState(step: example1, parentId: nil))
                .environment(\.sizeCategory, .accessibilityExtraExtraLarge)
                .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 0))
                .environmentObject(AssessmentState(AssessmentObject(previewStep: example1)))
        }
    }
}

fileprivate let example1 = CountdownStepObject(identifier: "example1", duration: 5)
