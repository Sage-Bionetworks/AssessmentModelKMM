//
//  SimpleQuestionViewModel.swift
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

class SimpleQuestionViewModel<Value : JsonValue> : ObservableObject {
    
    weak var questionState: QuestionState?
    var validator: TextEntryValidator?

    @Published var isEditing: Bool = false {
        didSet {
            updateState()
        }
    }
    
    @Published var value: Value?
    @Published var fieldLabel: String?
    @Published var placeholder: String?
    @Published var validationError: Error?
    
    fileprivate var updating = false
    
    init() {
    }
    
    func onAppear(_ questionState: QuestionState) {
        guard let question = questionState.question as? SimpleQuestion
        else {
            assertionFailure("Attempting to initialize a question with a state object that does not have the correct type.")
            return
        }
        
        self.questionState = questionState
        self.fieldLabel = question.inputItem.fieldLabel
        self.placeholder = question.inputItem.placeholder
        self.validator = question.inputItem.buildTextValidator()
        self.value = questionState.answer?.jsonObject() as? Value
    }
    
    func updateState() {
        guard !isEditing else { return }
        do {
            let constrainedValue = try validator?.validateAnswer(self.value)
            self.value = constrainedValue as? Value
            validationError = nil
            questionState?.answer = constrainedValue.map { .init($0) }
            questionState?.hasSelectedAnswer = (questionState?.answer != nil)
        }
        catch {
            validationError = error
            questionState?.hasSelectedAnswer = false
            questionState?.answer = nil
        }
    }
}

class IntegerQuestionViewModel : SimpleQuestionViewModel<Int> {
    
    @Published var minLabel: String?
    @Published var maxLabel: String?
    @Published var minValue: Int = .min
    @Published var maxValue: Int = .max
    
    @Published var fraction: Double = 0 {
        didSet {
            guard !updating, usesFraction else { return }
            updating = true
            constrainedValue = Int(round(Double(maxValue - minValue) * fraction)) + minValue
            updateState()
            updating = false
        }
    }
    
    override var value: Int? {
        didSet {
            guard !updating else { return }
            updating = true
            constrainedValue = self.value.map { max(minValue, min(maxValue, $0)) }
            if usesFraction, let newValue = constrainedValue {
                self.fraction = Double(newValue - minValue) / Double(maxValue - minValue)
            }
            updateState()
            updating = false
        }
    }
    
    fileprivate var constrainedValue: Int?
    fileprivate var usesFraction: Bool { false }
    
    override func onAppear(_ questionState: QuestionState) {
        super.onAppear(questionState)
        
        guard let question = questionState.question as? SimpleQuestion,
              let range = question.inputItem.buildTextValidator() as? IntegerRange
        else {
            assertionFailure("Attempting to initialize a question with a state object that does not have the correct type.")
            return
        }
        
        // Set up the scale
        range.minimumValue.map { self.minValue = $0 }
        range.maximumValue.map { self.maxValue = $0 }
        range.minimumLabel.map { self.minLabel = $0 }
        range.maximumLabel.map { self.maxLabel = $0 }
    }

    override func updateState() {
        guard !isEditing else { return }
        if usesFraction {
            self.value = constrainedValue
        }
        if let answerValue = self.value, minValue <= answerValue, answerValue <= maxValue {
            questionState?.hasSelectedAnswer = true
            questionState?.answer = .integer(answerValue)
        }
        else {
            questionState?.hasSelectedAnswer = false
            questionState?.answer = nil
        }
    }
}

class IntegerScaleViewModel : IntegerQuestionViewModel {
    
    override var usesFraction: Bool {
        .min < minValue && maxValue < .max
    }
    
    override func onAppear(_ questionState: QuestionState) {
        super.onAppear(questionState)
        
        guard minValue != .min, maxValue != .max
        else {
            assertionFailure("Attempting to initialize a Likert question with a state object that does not have the correct type.")
            return
        }
        
        if questionState.detail == nil,
           let minLabel = minLabel,
           let maxLabel = maxLabel {
            questionState.detail = Localization.localizedString("\(minValue) = \(minLabel)\n\(maxValue) = \(maxLabel)")
        }
        
        if questionState.subtitle == nil {
            questionState.subtitle = Localization.localizedString("On a scale of \(minValue) to \(maxValue)")
        }
    }
}

final class SlidingScaleViewModel : IntegerScaleViewModel {
    
    override var constrainedValue: Int? {
        get { super.constrainedValue ?? 0 }
        set { super.constrainedValue = newValue ?? 0 }
    }
    
    override func onAppear(_ questionState: QuestionState) {
        if questionState.answer == nil {
            // For a sliding scale, the initial value is 0
            questionState.answer = .integer(0)
        }
        super.onAppear(questionState)
    }
}

final class LikertScaleViewModel : IntegerScaleViewModel {
    
    @Published var dots: [Dot] = []
    
    override func onAppear(_ questionState: QuestionState) {
        super.onAppear(questionState)
        dots = Array(minValue...maxValue).map {
            .init($0)
        }
        self.value.map { updateSelected($0) }
    }

    override func updateState() {
        super.updateState()
        self.value.map { updateSelected($0) }
    }
    
    func updateSelected(_ newValue: Int) {
        dots.forEach {
            $0.selected = $0.value <= newValue
        }
    }
    
    final class Dot : ObservableObject, Identifiable {
        var id: String { "\(value)" }
        
        let value: Int
        @Published var selected: Bool = false

        init(_ value: Int) {
            self.value = value
        }
    }
}

