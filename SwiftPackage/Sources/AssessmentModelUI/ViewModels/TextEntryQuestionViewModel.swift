//
//  TextEntryQuestionViewModel.swift
//
//

import SwiftUI
import AssessmentModel
import JsonModel

class TextEntryQuestionViewModel : ObservableObject, TextInputViewModelDelegate {
    
    weak var questionState: QuestionState?
    
    @Published var inputViewModel : StringInputViewModel?
    @Published var detail: String?
    
    func onAppear(_ questionState: QuestionState) {
        self.questionState = questionState
        guard let question = questionState.question as? SimpleQuestion
        else {
            assertionFailure("Expecting the question to be a SimpleQuestion")
            questionState.answer = .null
            questionState.hasSelectedAnswer = true
            return
        }
        
        let inputItem = (questionState.question as? SimpleQuestion)?.inputItem ?? StringTextInputItemObject()
        self.inputViewModel = .init(question.identifier, inputItem: inputItem)
        self.inputViewModel!.delegate = self
            
        if questionState.detail == nil {
            let format = NSLocalizedString("(Maximum %1$d characters)", bundle: .module, comment: "Maximum characters for test input")
            questionState.detail = String.localizedStringWithFormat(format, self.inputViewModel!.characterLimit)
        }
        
        // Set the value equal to the current question state answer.
        self.inputViewModel!.value = questionState.answer.flatMap {
            $0.jsonObject() as? String
        }
    }
    
    func didUpdateValue(_ newValue: JsonValue?, with identifier: String) {
        guard let questionState = questionState else { return }
        let text = (newValue as? String) ?? ""
        questionState.hasSelectedAnswer = true
        questionState.answer = .string(text)
    }
}
