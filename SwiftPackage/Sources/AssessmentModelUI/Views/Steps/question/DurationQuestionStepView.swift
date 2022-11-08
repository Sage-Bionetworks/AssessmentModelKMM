//
//  DurationQuestionStepView.swift
//
//

import SwiftUI
import AssessmentModel
import JsonModel
import SharedMobileUI

public struct DurationQuestionStepView: View {
    @SwiftUI.Environment(\.horizontalPadding) var horizontalPadding: CGFloat
    @ObservedObject var questionState: QuestionState
    @StateObject var viewModel: DurationQuestionViewModel = .init()
    
    public init(_ questionState: QuestionState) {
        self.questionState = questionState
    }
    
    public var body: some View {
        QuestionStepScrollView(keyboardAnchor: .none) {
            Spacer(minLength: 0)
            HStack(spacing: 10) {
                ForEach(viewModel.inputFields) {
                    IntField(viewModel: $0)
                }
            }
            .id(KeyboardObserver.defaultKeyboardFocusedId)
            GeometryReader { geometry in
                HStack(spacing: 10) {
                    ForEach(viewModel.inputFields) {
                        DurationUnitPicker(viewModel: $0)
                            .frame(maxWidth: geometry.size.width / 2)
                            .clipped()
                    }
                }
            }
            .padding(.horizontal, horizontalPadding)
        }
        .environmentObject(questionState)
        .fullscreenBackground(.darkSurveyBackground, backButtonStyle: .white)
        .onAppear {
            viewModel.onAppear(questionState)
        }
    }
    
    struct IntField : View {
        @ObservedObject var viewModel: IntegerInputViewModel
        var body: some View {
            VStack(alignment: .leading, spacing: 6) {
                Text(viewModel.fieldLabel ?? viewModel.id)
                    .foregroundColor(.textForeground)
                    .font(.durationFieldLabel)
                NumericTextField(value: $viewModel.value, isEditing: $viewModel.isEditing, inputItem: viewModel.inputItem)
                    .frame(width: 64, height: 56)
            }
        }
    }
    
    struct DurationUnitPicker : View {
        @ObservedObject var viewModel: IntegerInputViewModel
        var body: some View {
            Picker(viewModel.fieldLabel?.lowercased() ?? "", selection: $viewModel.pickerValue) {
                ForEach(viewModel.pickerValues, id: \.self) { value in
                    HStack {
                        Text("\(value)")
                        Text("\(viewModel.fieldLabel?.lowercased() ?? "")")
                            .opacity(value == viewModel.pickerValue ? 1 : 0)
                    }
                }
            }
            #if os(iOS)
            .pickerStyle(WheelPickerStyle())
            #endif
        }
    }
}

fileprivate struct PreviewDurationQuestionStepView : View {
    let question: SimpleQuestionStep
    var body: some View {
        DurationQuestionStepView(
            QuestionState(question,
                          skipStepText: Text("Skip question")))
            .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 2))
            .environmentObject(AssessmentState(AssessmentObject(previewStep: question)))
    }
}

struct DurationQuestionStepView_Previews: PreviewProvider {
    static var previews: some View {
        PreviewDurationQuestionStepView(question: durationExample)
    }
}

let durationExample = SimpleQuestionStepObject(
    identifier: "simpleQ5",
    inputItem: DurationTextInputItemObject(),
    title: "How long does it take to travel to the moon?",
    nextNode: "followupQ"
)
