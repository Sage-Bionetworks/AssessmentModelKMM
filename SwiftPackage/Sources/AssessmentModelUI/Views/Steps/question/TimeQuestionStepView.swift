//
//  TimeQuestionStepView.swift
//
//

import SwiftUI
import AssessmentModel
import JsonModel
import SharedMobileUI

public struct TimeQuestionStepView: View {
    @SwiftUI.Environment(\.innerSpacing) var innerSpacing: CGFloat
    @SwiftUI.Environment(\.horizontalPadding) var horizontalPadding: CGFloat
    @ObservedObject var questionState: QuestionState
    @StateObject var viewModel: TimeQuestionViewModel = .init()
    
    public init(_ questionState: QuestionState) {
        self.questionState = questionState
    }
    
    public var body: some View {
        QuestionStepScrollView(keyboardAnchor: .none) {
            if let range = viewModel.range {
                DatePicker("", selection: $viewModel.value, in: range, displayedComponents: .hourAndMinute)
                    .labelsHidden()
                    .padding(.horizontal, horizontalPadding)
                    #if os(iOS)
                    .datePickerStyle(WheelDatePickerStyle())
                    #endif
            }
        }
        .environmentObject(questionState)
        .fullscreenBackground(.darkSurveyBackground, backButtonStyle: .white)
        .onAppear {
            viewModel.onAppear(questionState)
        }
    }
}

fileprivate struct PreviewQuestionStepView : View {
    let question: SimpleQuestionStep
    var body: some View {
        TimeQuestionStepView(
            QuestionState(question,
                          skipStepText: Text("Skip question")))
            .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 2))
            .environmentObject(AssessmentState(AssessmentObject(previewStep: question)))
    }
}

struct TimeQuestionStepView_Previews: PreviewProvider {
    static var previews: some View {
        PreviewQuestionStepView(question: timeExample)
    }
}

let timeExample = SimpleQuestionStepObject(
    identifier: "simpleQ6",
    inputItem: TimeTextInputItemObject(),
    title: "What time is it on the moon?",
    nextNode: "followupQ"
)

