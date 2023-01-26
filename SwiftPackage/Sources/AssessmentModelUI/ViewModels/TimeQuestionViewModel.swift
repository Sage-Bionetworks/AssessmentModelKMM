//
//  TimeQuestionViewModel.swift
//
//

import SwiftUI
import AssessmentModel
import JsonModel

@MainActor
final class TimeQuestionViewModel : ObservableObject {
    
    weak var questionState: QuestionState?
    
    @Published var value: Date = Date() {
        didSet {
            questionState?.answer = LocalTime(from: value).jsonElement()
            questionState?.hasSelectedAnswer = true
        }
    }
    
    @Published var range: ClosedRange<Date>?
        
    func onAppear(_ questionState: QuestionState) {
        guard self.questionState?.id != questionState.id,
              let question = questionState.question as? SimpleQuestion,
              let inputItem = question.inputItem as? TimeTextInputItem
        else {
            assertionFailure("Expecting the question to be a SimpleQuestion with a duration input item")
            questionState.answer = .null
            questionState.hasSelectedAnswer = true
            return
        }
        self.questionState = questionState
        
        let now = Date()
        let min = (inputItem.range.allowPast ?? true) ? (inputItem.range.minimumValue ?? .min).date(from: now) : now
        let max = (inputItem.range.allowFuture ?? true) ? (inputItem.range.maximumValue ?? .max).date(from: now) : now
        self.value = questionState.answer.flatMap { LocalTime(from: $0)?.date(from: now) } ?? now
        self.range = .init(uncheckedBounds: (min, max))
    }
}

