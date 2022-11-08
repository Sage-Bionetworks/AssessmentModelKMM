//
//  IntegerQuestionStepView.swift
//
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
