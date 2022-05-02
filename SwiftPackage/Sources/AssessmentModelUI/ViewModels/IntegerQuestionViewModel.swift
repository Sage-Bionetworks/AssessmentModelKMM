//
//  IntegerQuestionViewModel.swift
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
        questionState?.hasSelectedAnswer = (newValue != nil)
        questionState?.answer = newValue.map { .init($0) }
    }
}

