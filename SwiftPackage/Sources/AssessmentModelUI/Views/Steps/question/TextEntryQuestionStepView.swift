//
//  TextEntryQuestionStepView.swift
//
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
                    showPlaceholder = !newValue && (viewModel.value?.isEmpty ?? true)
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
