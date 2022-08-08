//
//  ContentView.swift
//  iosAppPreview
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
import AssessmentModel

struct ContentView: View {
    @StateObject var viewModel: ViewModel = .init()
    let surveys: [AssessmentHolder] = [
        .init(assessment: surveyA),
        .init(assessment: surveyB),
        .init(assessment: surveyC),
        .init(assessment: countdownAssessment)
    ]

    var body: some View {
        List(surveys) { holder in
            Button(holder.assessment.title ?? holder.id) {
                viewModel.current = .init(holder.assessment)
            }
        }
        .fullScreenCover(isPresented: $viewModel.isPresented) {
            AssessmentListener(viewModel)
        }
    }
    
    class ViewModel : ObservableObject {
        @Published var isPresented: Bool = false
        var current: AssessmentState? {
            didSet {
                print("---")
                isPresented = (current != nil)
            }
        }
    }
    
    struct AssessmentListener : View {
        @ObservedObject var viewModel: ViewModel
        @ObservedObject var state: AssessmentState
        
        init(_ viewModel: ViewModel) {
            self.viewModel = viewModel
            self.state = viewModel.current!
        }
        
        var body: some View {
            AssessmentView(state)
                .onChange(of: state.status) { newValue in
                    print("assessment status = \(newValue)")
                    guard newValue >= .finished else { return }
                    // In a real use-case this is where you might save and upload data
                    viewModel.isPresented = false
                    viewModel.current = nil
                }
        }
    }
}

struct AssessmentHolder : Identifiable {
    var id: String { assessment.identifier }
    let assessment: Assessment
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

fileprivate let countdownAssessment = AssessmentObject(identifier: "Countdown", children: [
    OverviewStepObject(identifier: "overview",
                       title: "Countdown Example",
                       detail: "Example for testing a countdown step",
                       imageInfo: SageResourceImage(.default)),
    CountdownStepObject(identifier: "countdown", duration: 6),
    CompletionStepObject(identifier: "completion", title: "You're done!")
])
