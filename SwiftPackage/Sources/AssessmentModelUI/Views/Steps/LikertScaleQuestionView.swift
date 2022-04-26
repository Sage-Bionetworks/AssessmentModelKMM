//
//  LikertScaleQuestionView.swift
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

public struct LikertScaleQuestionView : View {
    @ObservedObject var questionState: QuestionState
    
    public init(_ questionState: QuestionState) {
        self.questionState = questionState
    }
    
    public var body: some View {
        VStack(spacing: 8) {
            StepHeaderView(questionState)
            QuestionStepScrollView {
                Spacer()
                LikertScaleView()
            }
        }
        .id("\(type(of: self)):\(questionState.id)")   // Give the view a unique id to force refresh
        .environmentObject(questionState)
        .fullscreenBackground(.surveyBackground)
    }
}

struct LikertScaleView : View {
    @EnvironmentObject var questionState: QuestionState
    @StateObject var viewModel: LikertScaleViewModel = .init()
    @State var width: CGFloat = 0
    
    var body: some View {
        ZStack(alignment: .top) {
            ScaleBar(fraction: $viewModel.fraction)
                .offset(x: 0, y: 22)
                .frame(width: max(0, width - 48))
                
            HStack(spacing: 0) {
                ForEach(viewModel.dots) { dot in
                    VStack(spacing: 0) {
                        DotView(dot: dot)
                        Text("\(dot.value)")
                    }
                    .onTapGesture {
                        viewModel.value = dot.value
                    }
                }
            }
            .widthReader(width: $width)
        }
        .onAppear {
            viewModel.initialize(questionState)
        }
    }
    
    struct ScaleBar : View {
        @Binding var fraction: Double
        var body: some View {
            GeometryReader { geometry in
                ZStack(alignment: .leading) {
                    Rectangle()
                        .innerShadow(.sliderBackground)
                    Rectangle()
                        .fill(Color.sageBlack)
                        .frame(width: geometry.size.width * fraction)
                }
            }
            .frame(height: 4)
        }
    }
    
    struct DotView : View {
        @ObservedObject var dot: LikertScaleViewModel.Dot
        var body: some View {
            ZStack {
                Circle()
                    .innerShadow(.likertDotBackground)
                Circle()
                    .fill(Color.sageBlack)
                    .opacity(dot.selected ? 1 : 0)
            }
            .frame(width: 24, height: 24)
            .padding(.all, 12)
        }
    }
}

extension Shape {
    func innerShadow(_ fillColor: Color) -> some View {
        self
            .fill(fillColor)
            .overlay(
                self
                    .stroke(fillColor, lineWidth: 1)
                    .shadow(color: .black.opacity(0.25), radius: 1, x: 0, y: 2)
                    .clipShape(self)
        )
    }
}

fileprivate struct PreviewLikertScaleQuestionStepView : View {
    let question: SimpleQuestionStep
    var body: some View {
        LikertScaleQuestionView(QuestionState(question,
                                              answerResult: AnswerResultObject(identifier: question.identifier,value: .integer(2)),
                                              skipStepText: Text("Skip question")))
            .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 2))
            .environmentObject(AssessmentState(AssessmentObject(previewStep: question)))
    }
}

struct LikertScaleQuestionView_Previews: PreviewProvider {
    static var previews: some View {
        PreviewLikertScaleQuestionStepView(question: likertExample1)
    }
}

let likertExample1 = SimpleQuestionStepObject(
    identifier: "simpleQ3",
    inputItem: IntegerTextInputItemObject(formatOptions: .init(minimumValue: 1, maximumValue: 5, minimumLabel: "Not at all", maximumLabel: "Very much")),
    title: "How much do you like apples?",
    uiHint: .NumberField.likert.uiHint,
    nextNode: "followupQ"
)
