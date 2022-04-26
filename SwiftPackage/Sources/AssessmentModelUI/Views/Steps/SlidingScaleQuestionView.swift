//
//  SlidingScaleQuestionView.swift
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

public struct SlidingScaleQuestionView : View {
    @ObservedObject var questionState: QuestionState
    
    public init(_ questionState: QuestionState) {
        self.questionState = questionState
    }
    
    public var body: some View {
        VStack(spacing: 8) {
            StepHeaderView(questionState)
            QuestionStepScrollView {
                Spacer()
                SlidingScaleView()
            }
        }
        .id("LikertScaleQuestionView:\(questionState.id)")   // Give the view a unique id to force refresh
        .environmentObject(questionState)
        .fullscreenBackground(.surveyBackground)
    }
}

struct SlidingScaleView : View {
    @SwiftUI.Environment(\.surveyTintColor) var surveyTint: Color
    @EnvironmentObject var keyboard: KeyboardObserver
    @EnvironmentObject var questionState: QuestionState
    @StateObject var viewModel: SlidingScaleViewModel = .init()
    @State var sliderWidth: CGFloat = 0
    @State var xOffset: CGFloat = 0
    @State var lastOffset: CGFloat = 0
    @State var gripSize: CGFloat = 0
    let circleSize: CGFloat = 40
    
    var body: some View {
        VStack {
            NumericTextField(value: $viewModel.value, inputItem: viewModel.inputItem)
                .frame(width: 150, height: 64)
                .padding(.bottom, keyboard.keyboardFocused ? 0 : 24)
            HStack {
                Text("\(viewModel.minValue)")
                slider()
                    .overlay(grip(), alignment: .leading)
                Text("\(viewModel.maxValue)")
            }
        }
        .padding(.horizontal, 32)
        .onAppear {
            viewModel.initialize(questionState)
        }
    }
    
    func updateOffset() {
        xOffset = viewModel.fraction * totalWidth() - (gripSize - circleSize)/2
    }
    
    func totalWidth() -> CGFloat {
        sliderWidth - circleSize
    }
    
    func updateFraction(_ newOffset: CGFloat) {
        guard !keyboard.keyboardFocused
        else {
            keyboard.hideKeyboard()
            return
        }
        let overallWidth = totalWidth()
        guard overallWidth > 0 else { return }
        let newValue = max(0, min(newOffset, overallWidth))
        viewModel.fraction = newValue / overallWidth
    }
        
    @ViewBuilder
    func slider() -> some View {
        ZStack(alignment: .center) {
            ZStack(alignment: .leading) {
                Rectangle()
                    .innerShadow(.sliderBackground)
                Rectangle()
                    .fill(Color.sageBlack)
                    .frame(width: sliderWidth * viewModel.fraction)
            }
            .frame(height: 6)
            .widthReader(width: $sliderWidth)
            .clipShape(Capsule())
        }
        .frame(minHeight: gripSize)
        .onChange(of: sliderWidth) { _ in
            updateOffset()
        }
        .onChange(of: viewModel.fraction) { _ in
            updateOffset()
        }
        .onTapLocationGesture { point in
            updateFraction(point.x - circleSize/2)
        }
    }
    
    @ViewBuilder
    func grip() -> some View {
        ZStack(alignment: .center) {
            Circle()
                .fill(surveyTint)
                .frame(width: circleSize, height: circleSize)
            Image("slider_grip", bundle: .module)
        }
        .widthReader(width: $gripSize)
        .offset(x: xOffset)
        .gesture(DragGesture(minimumDistance:0).onChanged { gestureValue in
            if abs(gestureValue.translation.width) < 0.1 {
                lastOffset = xOffset
            }
            else {
                updateFraction(lastOffset + gestureValue.translation.width)
            }
        })
    }
}

fileprivate struct PreviewSlidingScaleQuestionStepView : View {
    let question: SimpleQuestionStep
    let initialValue: Int
    var body: some View {
        SlidingScaleQuestionView(QuestionState(question,
                                               answerResult: AnswerResultObject(identifier: question.identifier,value: .integer(initialValue)),
                                               skipStepText: Text("Skip question")))
            .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 2))
            .environmentObject(AssessmentState(AssessmentObject(previewStep: question)))
    }
}

struct SlidingScaleQuestionView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            PreviewSlidingScaleQuestionStepView(question: slidingScaleExample1, initialValue: 20)
            PreviewSlidingScaleQuestionStepView(question: slidingScaleExample1, initialValue: 0)
            PreviewSlidingScaleQuestionStepView(question: slidingScaleExample1, initialValue: 100)
        }
    }
}

let slidingScaleExample1 = SimpleQuestionStepObject(
    identifier: "simpleQ4",
    inputItem: IntegerTextInputItemObject(formatOptions: .init(minimumValue: 0, maximumValue: 100, minimumLabel: "Not at all", maximumLabel: "Very much")),
    title: "How much do you like apples?",
    uiHint: .NumberField.slider.uiHint,
    nextNode: "followupQ"
)

