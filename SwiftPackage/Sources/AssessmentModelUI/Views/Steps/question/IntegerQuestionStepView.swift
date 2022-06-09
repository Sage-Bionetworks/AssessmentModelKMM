//
//  IntegerQuestionStepView.swift
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

public struct IntegerQuestionStepView : View {
    @ObservedObject var questionState: QuestionState
    @StateObject var viewModel: IntegerQuestionViewModel = .init()
    
    public init(_ questionState: QuestionState) {
        self.questionState = questionState
    }
    
    public var body: some View {
        QuestionStepScrollView(keyboardAnchor: .none) {
            Spacer(minLength: 0)
            if let model = viewModel.inputViewModel {
                IntegerTextField(viewModel: model)
            }
        }
        .environmentObject(questionState)
        .fullscreenBackground(.darkSurveyBackground, backButtonStyle: .white)
        .onAppear {
            viewModel.onAppear(questionState)
        }
    }
    
    struct IntegerTextField : View {
        @ObservedObject var viewModel: IntegerInputViewModel
        
        public var body: some View {
            VStack(spacing: 24) {
                switch viewModel.viewType {
                case .likert:
                    LikertScaleView(viewModel: viewModel)
                case .slider:
                    textField()
                    SlidingScaleView(viewModel: viewModel)
                default:
                    textField()
                }
            }
        }
        
        @ViewBuilder
        func textField() -> some View {
            VStack(alignment: .leading, spacing: 6) {
                if let label = viewModel.fieldLabel {
                    Text(label)
                        .font(.fieldLabel)
                        .foregroundColor(.textForeground)
                }
                NumericTextField(value: $viewModel.value, isEditing: $viewModel.isEditing, inputItem: viewModel.inputItem)
                    .frame(width: 150, height: 56)
                    .id(KeyboardObserver.defaultKeyboardFocusedId)
            }
        }
    }
}

fileprivate struct PreviewIntegerQuestionStepView : View {
    let question: SimpleQuestionStep
    var body: some View {
        IntegerQuestionStepView(
            QuestionState(question,
                          skipStepText: Text("Skip question")))
            .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 2))
            .environmentObject(AssessmentState(AssessmentObject(previewStep: question)))
    }
}

struct IntegerQuestionStepView_Previews: PreviewProvider {
    static var previews: some View {
        PreviewIntegerQuestionStepView(question: integerExample1)
    }
}

let integerExample1 = SimpleQuestionStepObject(
    identifier: "simpleQ4",
    inputItem: IntegerTextInputItemObject(fieldLabel: "Enter an amount",
                                          placeholder: "52",
                                          formatOptions: .init(minimumValue: 0, maximumValue: 100)),
    title: "How much do you like apples?",
    uiHint: .NumberField.textfield.uiHint,
    nextNode: "followupQ"
)

let birthYearExample = SimpleQuestionStepObject(
    identifier: "simpleQ2",
    inputItem: YearTextInputItemObject(fieldLabel: "year of birth", formatOptions: .birthYear),
    title: "Enter a birth year",
    nextNode: "followupQ")
