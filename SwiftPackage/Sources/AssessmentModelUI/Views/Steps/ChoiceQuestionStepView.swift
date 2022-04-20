//
//  ChoiceQuestionStepView.swift
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

public struct ChoiceQuestionStepView : View {
    @ObservedObject var questionState: QuestionState
    
    public init(_ questionState: QuestionState) {
        self.questionState = questionState
    }
    
    public var body: some View {
        VStack(spacing: 8) {
            QuestionHeaderView()
            QuestionStepScrollView {
                ChoiceQuestionView()
            }
        }
        .id("ChoiceQuestionStepView:\(questionState.id)")   // Give the view a unique id to force refresh
        .environmentObject(questionState)
        .fullscreenBackground(.surveyBackground)
    }
}

struct ChoiceQuestionView : View {
    @EnvironmentObject var keyboard: KeyboardObserver
    @EnvironmentObject var questionState: QuestionState
    @StateObject var viewModel: ChoiceQuestionViewModel = .init()
    
    var body: some View {
        LazyVStack(spacing: innerVerticalSpacing) {
            ForEach(viewModel.choices) { choice in
                ChoiceCell(choice: choice)
            }
            .simultaneousGesture(
                TapGesture()
                    .onEnded { _ in
                        keyboard.hideKeyboard()
                    }
            )
            if let other = viewModel.otherChoice {
                OtherCell(choice: other)
            }
        }
        .onAppear {
            viewModel.initialize(questionState)
        }
        .onChange(of: keyboard.keyboardFocused) { newValue in
            if !newValue {
                // When the keyboard looses focus, update answer.
                viewModel.updateAnswer()
            }
        }
        .singleChoice(viewModel.singleAnswer)
        .padding(.horizontal, 32)
    }
    
    struct ChoiceCell : View {
        @ObservedObject fileprivate var choice: ChoiceViewModel
        var body: some View {
            Toggle(choice.jsonChoice.label, isOn: $choice.selected)
                .selectionCell(isOn: $choice.selected)
        }
    }
    
    struct OtherCell : View {
        @ObservedObject var choice: OtherChoiceViewModel
        var body: some View {
            HStack(spacing: 0) {
                Toggle("", isOn: $choice.selected)
                MultilineTextField(text: $choice.value,
                                   isSelected: $choice.selected,
                                   inputItem: choice.inputItem,
                                   fieldLabel: choice.fieldLabel)
                    .accentColor(Color.sageBlack)
            }
            .selectionCell(isOn: $choice.selected, spacing: 3)
        }
    }
}

fileprivate struct SingleChoiceEnvironmentKey: EnvironmentKey {
    fileprivate static let defaultValue: Bool = true
}

extension EnvironmentValues {
    fileprivate var singleChoice: Bool {
        get { self[SingleChoiceEnvironmentKey.self] }
        set { self[SingleChoiceEnvironmentKey.self] = newValue }
    }
}

extension View {
    fileprivate func singleChoice(_ singleChoice: Bool) -> some View {
        environment(\.singleChoice, singleChoice)
    }
    
    fileprivate func selectionCell(isOn: Binding<Bool>, spacing: CGFloat = 8) -> some View {
        modifier(SelectionCell(isOn: isOn, spacing: spacing))
    }
}

struct SelectionCell : ViewModifier {
    @SwiftUI.Environment(\.singleChoice) var singleChoice: Bool
    @Binding fileprivate var isOn: Bool
    private let spacing: CGFloat
    
    init(isOn: Binding<Bool>, spacing: CGFloat = 8) {
        self._isOn = isOn
        self.spacing = spacing
    }

    func body(content: Content) -> some View {
        wrapContent(content)
            .font(.textField)
            .foregroundColor(.textForeground)
    }
    
    @ViewBuilder
    private func wrapContent(_ content: Content) -> some View {
        if singleChoice {
            content
                .toggleStyle(RadioButtonToggleStyle(spacing: spacing))
                .padding(.trailing, 16)
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(isOn ? Color.accentColor : Color.sageWhite)
                .clipShape(Capsule())
                .shadow(color: .hex2A2A2A.opacity(0.1), radius: 3, x: 1, y: 2)
            
        } else {
            content
                .toggleStyle(CheckboxToggleStyle(spacing: spacing))
                .padding(.trailing, 16)
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(isOn ? Color.accentColor : Color.sageWhite)
                .clipShape(RoundedRectangle(cornerRadius: 5))
                .shadow(color: .hex2A2A2A.opacity(0.1), radius: 3, x: 1, y: 2)
        }
    }
}

fileprivate struct PreviewChoiceQuestionStepView : View {
    let question: ChoiceQuestionStep
    var body: some View {
        ChoiceQuestionStepView(QuestionState(question, canPause: true, skipStepText: Text("Skip question")))
            .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 2))
            .environmentObject(AssessmentState(AssessmentObject(previewStep: question)))
    }
}

struct ChoiceQuestionStepView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            PreviewChoiceQuestionStepView(question: happyChoiceQuestion)
                .environment(\.sizeCategory, .medium)
            PreviewChoiceQuestionStepView(question: happyChoiceQuestion)
            PreviewChoiceQuestionStepView(question: favoriteFoodChoiceQuestion)
            PreviewChoiceQuestionStepView(question: favoriteFoodChoiceQuestion)
                .environment(\.sizeCategory, .accessibilityExtraLarge)
            PreviewChoiceQuestionStepView(question: favoriteColorsQuestion)
        }
    }
}

extension AssessmentObject {
    convenience init(previewStep: Step) {
        self.init(identifier: previewStep.identifier, children: [previewStep])
    }
}

let happyChoiceQuestion = ChoiceQuestionStepObject(identifier: "followupQ",
                                                   choices: .booleanChoices(),
                                                   title: "Are you happy with your choice?",
                                                   subtitle: "After thinking it over...",
                                                   detail: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                                                   surveyRules: [ .init(skipToIdentifier: "choiceQ1", matchingValue: .boolean(false)) ])

let favoriteColorsQuestion = ChoiceQuestionStepObject(identifier: "multipleChoice",
                         choices: [
                            "Blue",
                            "Green",
                            "Yellow",
                            "Red",
                            .init(text: "All of the above", selectorType: .all),
                            .init(text: "I don't have any", selectorType: .exclusive),
                         ],
                         baseType: .string,
                         singleChoice: false,
                         other: StringTextInputItemObject(),
                         title: "What are your favorite colors?")

let favoriteFoodChoiceQuestion = ChoiceQuestionStepObject(
    identifier: "favoriteFood",
    choices: [
        "Pizza",
        "Sushi",
        "Ice Cream",
        "Beans & Rice",
        "Tofu Tacos",
        "Bucatini Alla Carbonara",
        "Hot Dogs, Kraft Dinner & Potato Salad",
    ],
    baseType: .string,
    singleChoice: true,
    other: StringTextInputItemObject(),
    title: "What are you having for dinner next Tuesday after the soccer game?",
    subtitle: "After thinking it over...",
    detail: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    surveyRules: [
    .init(skipToIdentifier: "completion", matchingValue: .string("Pizza"), ruleOperator: .notEqual)
    ],
    buttonMap: [.navigation(.goForward) : ButtonActionInfoObject(buttonTitle: "Sumbit")]
)

