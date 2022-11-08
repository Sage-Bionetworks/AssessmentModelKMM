//
//  SlidingScaleView.swift
//
//

import SwiftUI
import AssessmentModel
import JsonModel
import ResultModel
import SharedMobileUI

struct SlidingScaleView : View {
    @SwiftUI.Environment(\.horizontalPadding) var horizontalPadding: CGFloat
    @SwiftUI.Environment(\.surveyTintColor) var surveyTint: Color
    @EnvironmentObject var keyboard: KeyboardObserver
    @EnvironmentObject var questionState: QuestionState
    @ObservedObject var viewModel: IntegerInputViewModel
    
    @State var sliderWidth: CGFloat = 0
    @State var xOffset: CGFloat = 0
    @State var lastOffset: CGFloat = 0
    @State var gripSize: CGFloat = 0
    let circleSize: CGFloat = 40
    
    init(viewModel: IntegerInputViewModel) {
        self.viewModel = viewModel
    }
    
    var body: some View {
        HStack {
            Text("\(viewModel.minValue)")
            slider()
                .overlay(grip(), alignment: .leading)
            Text("\(viewModel.maxValue)")
        }.padding(.horizontal, horizontalPadding)
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
        IntegerQuestionStepView(QuestionState(question,
                                               answerResult: AnswerResultObject(identifier: question.identifier,value: .integer(initialValue)),
                                               skipStepText: Text("Skip question")))
            .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 2))
            .environmentObject(AssessmentState(AssessmentObject(previewStep: question)))
    }
}

struct SlidingScaleQuestionView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            PreviewSlidingScaleQuestionStepView(question: slidingScaleExample, initialValue: 20)
            PreviewSlidingScaleQuestionStepView(question: slidingScaleExample, initialValue: 0)
            PreviewSlidingScaleQuestionStepView(question: slidingScaleExample, initialValue: 100)
        }
    }
}

let slidingScaleExample = SimpleQuestionStepObject(
    identifier: "simpleQ4",
    inputItem: IntegerTextInputItemObject(formatOptions: .init(minimumValue: 0, maximumValue: 100, minimumLabel: "Not at all", maximumLabel: "Very much")),
    title: "How much do you like apples?",
    uiHint: .NumberField.slider.uiHint,
    nextNode: "followupQ"
)

