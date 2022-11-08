//
//  DurationQuestionViewModel.swift
//
//

import SwiftUI
import AssessmentModel
import JsonModel
import ResultModel

class DurationQuestionViewModel : ObservableObject, TextInputViewModelDelegate {
    
    weak var questionState: QuestionState?
    
    @Published var inputFields : [IntegerInputViewModel] = []
    
    func onAppear(_ questionState: QuestionState) {
        guard self.questionState?.id != questionState.id,
              let question = questionState.question as? SimpleQuestion,
              let inputItem = question.inputItem as? DurationTextInputItem
        else {
            assertionFailure("Expecting the question to be a SimpleQuestion with a duration input item")
            questionState.answer = .null
            questionState.hasSelectedAnswer = true
            return
        }
        self.questionState = questionState
        
        var remaining: Double = questionState.answer.flatMap { $0.jsonObject() as? Double } ?? 0
        self.inputFields = inputItem.displayUnits.map { unit in
            let amount: Double? = questionState.hasSelectedAnswer ? floor(remaining / unit.secondsMultiplier) : nil
            amount.map { remaining -= $0 * unit.secondsMultiplier }
            let model = IntegerInputViewModel(unit.rawValue,
                                              inputItem: IntegerTextInputItemObject(fieldLabel: unit.fieldLabel),
                                              uiHint: .NumberField.picker.uiHint,
                                              range: unit.range,
                                              initialValue: amount.map { Int($0) })
            model.delegate = self
            return model
        }
    }
    
    private var isInitializing: Bool = false
    
    func didUpdateValue(_ newValue: JsonValue?, with identifier: String) {
        guard !isInitializing, let questionState = questionState else { return }
        let answer: Double? = inputFields.reduce(nil) { partialResult, model in
            guard let value = model.value, let unit = DurationUnit(rawValue: model.id)
            else {
                return partialResult
            }
            return (partialResult ?? 0) + Double(value) * unit.secondsMultiplier
        }
        questionState.hasSelectedAnswer = (answer != nil) || questionState.question.optional
        questionState.answer = answer.map { .init($0) }
    }
}

extension DurationUnit {
    
    var range: IntegerRange {
        switch self {
        case .hour:
            return IntegerFormatOptions(minimumValue: 0, maximumValue: 24)
        case .minute:
            return IntegerFormatOptions(minimumValue: 0, maximumValue: 60)
        case .second:
            return IntegerFormatOptions(minimumValue: 0, maximumValue: 60)
        }
    }
    
    var secondsMultiplier: Double {
        switch self {
        case .hour:
            return 60.0 * 60.0
        case .minute:
            return 60.0
        case .second:
            return 1.0
        }
    }
    
    var fieldLabel: String {
        switch self {
        case .hour:
            return Localization.localizedString("Hours")
        case .minute:
            return Localization.localizedString("Mins")
        case .second:
            return Localization.localizedString("S")
        }
    }
}
