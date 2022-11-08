//
//  IntegerQuestionViewModel.swift
//
//

import SwiftUI
import AssessmentModel
import JsonModel

class IntegerQuestionViewModel : ObservableObject, TextInputViewModelDelegate {
    
    weak var questionState: QuestionState?
    
    @Published var inputViewModel : IntegerInputViewModel?
    
    func onAppear(_ questionState: QuestionState) {
        self.questionState = questionState
        guard let question = questionState.question as? SimpleQuestion
        else {
            assertionFailure("Expecting the question to be a SimpleQuestion")
            questionState.answer = .null
            questionState.hasSelectedAnswer = true
            return
        }
        
        let inputItem = (questionState.question as? SimpleQuestion)?.inputItem ?? IntegerTextInputItemObject()
        let model = IntegerInputViewModel(questionState.question.identifier, inputItem: inputItem, uiHint: question.uiHint)
        model.delegate = self
        self.inputViewModel = model
            
        if model.usesScale, questionState.detail == nil,
           let minLabel = model.minLabel,
           let maxLabel = model.maxLabel {
            questionState.detail = Localization.localizedString("\(model.minValue) = \(minLabel)\n\(model.maxValue) = \(maxLabel)")
        }
        
        if model.usesScale, questionState.subtitle == nil {
            questionState.subtitle = Localization.localizedString("On a scale of \(model.minValue) to \(model.maxValue)")
        }
        
        // Set the value equal to the current question state answer.
        model.value = questionState.answer.flatMap {
            $0.jsonObject() as? Int
        }
    }
    
    func didUpdateValue(_ newValue: JsonValue?, with identifier: String) {
        guard let questionState = questionState else { return }
        questionState.hasSelectedAnswer = (newValue != nil) || questionState.question.optional
        questionState.answer = newValue.map { .init($0) }
    }
}

