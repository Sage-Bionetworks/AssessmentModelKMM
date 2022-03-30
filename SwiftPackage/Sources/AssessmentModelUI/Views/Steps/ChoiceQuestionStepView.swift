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

public struct ChoiceQuestionStepView: View {
    @StateObject var keyboard: KeyboardObserver = .init()
    
    private let question: ChoiceQuestionStep
    
    public init(_ question: ChoiceQuestionStep) {
        self.question = question
    }
    
    public var body: some View {
        ScrollViewReader { scrollView in
            ScrollView {
                ChoiceQuestionView(question)
                    .onChange(of: keyboard.keyboardFocused) { newValue in
                        if newValue {
                            scrollView.scrollTo(keyboard.keyboardFocusedId, anchor: .bottom)
                        }
                    }
                Spacer()
                    .frame(height: keyboard.keyboardFocused ? keyboard.keyboardHeight + 32 : 0)
                    .id(KeyboardObserver.defaultKeyboardFocusedId)
            }
        }
        .environmentObject(keyboard)
        .background(Color.surveyBackgroundColor)
    }
}

public struct ChoiceQuestionView: View {
    @EnvironmentObject var keyboard: KeyboardObserver
    @StateObject var viewModel: ChoiceQuestionViewModel = .init()

    private let question: ChoiceQuestion
    
    public init(_ question: ChoiceQuestion) {
        self.question = question
    }
    
    public var body: some View {
        LazyVStack(spacing: 16) {
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
        .singleChoice(question.singleAnswer)
        .padding(.horizontal, 32)
        .onAppear {
            // TODO: syoung 03/29/2022 Set previous answer
            viewModel.initialize(question, previousAnswer: nil)
        }
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
        return modifier(SelectionCell(isOn: isOn, spacing: spacing))
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
            .font(.latoFont(20, relativeTo: .body, weight: .bold))
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


struct ChoiceQuestionStepView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ChoiceQuestionStepView(favoriteFoodChoiceQuestion)
            ChoiceQuestionStepView(multipleChoiceQuestion)
        }
    }
}

let multipleChoiceQuestion = ChoiceQuestionStepObject(identifier: "multipleChoice",
                         choices: [
                            "blue",
                            "red",
                            "green",
                            "yellow",
                            .init(text: "All of the above", selectorType: .all),
                            .init(text: "I don't have any", selectorType: .exclusive),
                         ],
                         baseType: .string,
                         singleChoice: false,
                         other: StringTextInputItemObject(),
                         title: "What are your favorite colors?",
                         detail: "Choose all that apply")

let favoriteFoodChoiceQuestion = ChoiceQuestionStepObject(identifier: "favoriteFood",
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
                         title: "What are you having for dinner?",
                         surveyRules: [
                            .init(skipToIdentifier: "completion", matchingValue: .string("Pizza"), ruleOperator: .notEqual)
                         ])

