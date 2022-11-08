//
//  ChoiceQuestionStepView.swift
//  
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
        QuestionStepScrollView {
            ChoiceQuestionView()
        }
        .environmentObject(questionState)
        .fullscreenBackground(.lightSurveyBackground)
    }
}

struct ChoiceQuestionView : View {
    @SwiftUI.Environment(\.innerSpacing) var innerSpacing: CGFloat
    @SwiftUI.Environment(\.horizontalPadding) var horizontalPadding: CGFloat
    @EnvironmentObject var keyboard: KeyboardObserver
    @EnvironmentObject var questionState: QuestionState
    @StateObject var viewModel: ChoiceQuestionViewModel = .init()
    
    var body: some View {
        LazyVStack(spacing: innerSpacing) {
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
        .padding(.horizontal, horizontalPadding)
    }
    
    struct ChoiceCell : View {
        @ObservedObject fileprivate var choice: ChoiceViewModel
        var body: some View {
            Toggle(choice.jsonChoice.label, isOn: $choice.selected)
                .selectionCell(isOn: $choice.selected)
        }
    }
    
    struct OtherCell : View {
        @EnvironmentObject private var keyboard: KeyboardObserver
        @ObservedObject var choice: OtherChoiceViewModel
        var body: some View {
            HStack(spacing: 0) {
                Toggle(isOn: $choice.selected) {
                    MultilineTextField(text: $choice.value,
                                       isSelected: $choice.selected,
                                       inputItem: choice.inputItem,
                                       fieldLabel: choice.fieldLabel)
                        .accentColor(Color.sageBlack)
                        .characterLimit(50)
                }
            }
            .selectionCell(isOn: $choice.selected, spacing: 3)
            #if os(iOS)
            .environment(\.editMode, .constant(keyboard.keyboardFocused ? EditMode.active : EditMode.inactive))
            #endif
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
    @SwiftUI.Environment(\.surveyTintColor) var surveyTint: Color
    @SwiftUI.Environment(\.singleChoice) var singleChoice: Bool
    @Binding fileprivate var isOn: Bool
    private let spacing: CGFloat
    
    init(isOn: Binding<Bool>, spacing: CGFloat = 8) {
        self._isOn = isOn
        self.spacing = spacing
    }

    func body(content: Content) -> some View {
        content
            .toggleStyle(SelectionToggleStyle(spacing: spacing, selectedColor: surveyTint, isSingleSelect: singleChoice))
            .font(.textField)
            .foregroundColor(.textForeground)
    }
}

fileprivate struct PreviewChoiceQuestionStepView : View {
    let question: ChoiceQuestionStep
    var body: some View {
        ChoiceQuestionStepView(QuestionState(question, skipStepText: Text("Skip question")))
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

let favoriteColorsQuestion = ChoiceQuestionStepObject(
    identifier: "multipleChoice",
    choices: [
        "Blue",
        "Yellow",
        "Red",
        .init(text: "All of the above", selectorType: .all),
        .init(text: "I don't have any", selectorType: .exclusive),
    ],
    baseType: .string,
    singleChoice: false,
    other: StringTextInputItemObject(),
    title: "What are your favorite colors?",
    buttonMap: [.navigation(.goForward) : ButtonActionInfoObject(buttonTitle: "Submit")]
)

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
        .init(skipToIdentifier: "multipleChoice", matchingValue: .string("Pizza"), ruleOperator: .notEqual)
    ]
)

