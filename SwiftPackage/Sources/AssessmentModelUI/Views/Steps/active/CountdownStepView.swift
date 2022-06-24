//
//  CountdownStepView.swift
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
    let timer = Timer.publish(every: 1, on: .main, in: .common).autoconnect()
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
                
            }
            .foregroundColor(.textForeground)
            .padding(.horizontal, horizontalPadding)
            
            Spacer()
        }
        .onAppear {
            countdown = Int(startDuration)
        }
        .onDisappear {
            timer.upstream.connect().cancel()
        }
        .onChange(of: assessmentState.showingPauseActions) { newValue in
            paused = newValue
            if paused, countdown > 0 {
                countdown = Int(startDuration)
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
