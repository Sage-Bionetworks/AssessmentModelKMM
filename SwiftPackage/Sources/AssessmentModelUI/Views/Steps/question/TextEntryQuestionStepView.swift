//
//  TextEntryQuestionStepView.swift
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
import JsonModel
import SharedMobileUI

public struct TextEntryQuestionStepView: View {
    @ObservedObject var questionState: QuestionState
    @StateObject var viewModel: TextEntryQuestionViewModel = .init()
    
    public init(_ questionState: QuestionState) {
        self.questionState = questionState
    }
        
    public var body: some View {
        QuestionStepScrollView(shouldScrollOnFocus: false) {
            if let model = viewModel.inputViewModel {
                TextViewWrapper(viewModel: model)
            }
        }
        .id("\(type(of: self)):\(questionState.id)")   // Give the view a unique id to force refresh
        .environmentObject(questionState)
        .innerSpacing(6)
        .fullscreenBackground(.darkSurveyBackground, backButtonStyle: .white)
        .onAppear {
            viewModel.onAppear(questionState)
        }
    }
    
    struct TextViewWrapper : View {
        @SwiftUI.Environment(\.fullscreenBackgroundColor) var backgroundColor: Color
        @SwiftUI.Environment(\.horizontalPadding) var horizontalPadding: CGFloat
        @EnvironmentObject var pagedNavigation: PagedNavigationViewModel
        @EnvironmentObject var keyboard: KeyboardObserver
        @EnvironmentObject var questionState: QuestionState
        @ObservedObject var viewModel: StringInputViewModel
        @State var showPlaceholder: Bool = false
        
        var body: some View {
            ZStack(alignment: .topLeading) {
                Text("Tap to respond")
                    .font(.italicLatoFont(18, relativeTo: .body, weight: .regular))
                    .padding()
                    .frame(maxWidth: .infinity, alignment: .topLeading)
                    .frame(minHeight: showPlaceholder ? 200 : .none, alignment: .topLeading)
                    .background(backgroundColor)
                    .border(Color.sageBlack, width: 1)
                    .opacity(showPlaceholder ? 1 : 0)
                    .onTapGesture {
                        if showPlaceholder {
                            viewModel.isEditing = true
                        }
                    }
                
                MultilineTextField(text: $viewModel.value,
                                   isSelected: $viewModel.isEditing,
                                   inputItem: viewModel.inputItem,
                                   fontStyle: .freeText)
            }
            .padding(.horizontal, horizontalPadding)
            .onChange(of: keyboard.keyboardFocused) { newValue in
                viewModel.isEditing = newValue
                withAnimation {
                    showPlaceholder = !newValue && !questionState.hasSelectedAnswer
                }
            }
            .onAppear {
                // When this view appears, look to see if there is a selected answer.
                // If there is, then we're good. If not, forward direction should bring up the
                // keyboard but going back to the question should not.
                if !questionState.hasSelectedAnswer {
                    if pagedNavigation.currentDirection == .forward {
                        viewModel.isEditing = true
                    }
                    else {
                        showPlaceholder = true
                    }
                }
            }
        }
    }
}

fileprivate struct PreviewTextEntryQuestionStepView : View {
    let question: SimpleQuestionStep
    var body: some View {
        TextEntryQuestionStepView(QuestionState(question, skipStepText: Text("Skip question")))
            .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 2))
            .environmentObject(AssessmentState(AssessmentObject(previewStep: question)))
    }
}

struct TextEntryQuestionStepView_Previews: PreviewProvider {
    static var previews: some View {
        PreviewTextEntryQuestionStepView(question: textEntryExample)
    }
}

let textEntryExample = SimpleQuestionStepObject(
    identifier: "simpleQ1",
    inputItem: StringTextInputItemObject(placeholder: "I like cake"),
    title: "Enter some text",
    nextNode: "followupQ"
)
