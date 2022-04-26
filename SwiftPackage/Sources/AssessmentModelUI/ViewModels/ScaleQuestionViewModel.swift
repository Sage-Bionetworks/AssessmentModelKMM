//
//  ScaleQuestionViewModel.swift
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

open class AbstractIntegerScaleQuestionViewModel : ObservableObject {

    weak var questionState: QuestionState?
    var formatOptions: IntegerFormatOptions?
    
    @Published var min: Int = 1
    @Published var max: Int = 5
    @Published var stepInterval: Int = 1
    @Published var fraction: Double = 0
    
    @Published var value: Int? {
        didSet {
            updateState()
        }
    }
    
    public init() {
    }
    
    open func initialize(_ questionState: QuestionState) {
        guard let question = questionState.question as? SimpleQuestion,
              let options = question.inputItem.buildTextValidator() as? IntegerFormatOptions,
              let min = options.minimumValue, let max = options.maximumValue
        else {
            assertionFailure("Attempting to initialize a Likert question with a state object that does not have the correct type.")
            return
        }
        
        // Set up the initial state
        self.questionState = questionState
        self.formatOptions = options
        
        // Set up the scale
        self.min = min
        self.max = max
        self.stepInterval = options.stepInterval ?? 1
        
        if questionState.detail == nil,
           let minLabel = options.minimumLabel,
           let maxLabel = options.maximumLabel {
            questionState.detail = Localization.localizedString("\(min) = \(minLabel)\n\(max) = \(maxLabel)")
        }
        
        if questionState.subtitle == nil {
            questionState.subtitle = Localization.localizedString("On a scale of \(min) to \(max)")
        }
        
        if case .integer(let value) = questionState.answerResult.jsonValue {
            self.value = value
        }
    }
    
    func updateState() {
        guard min < max else { return }
        self.fraction = self.value.map { Double($0 - min) / Double(max - min) } ?? 0
        questionState?.hasSelectedAnswer = (value != nil)
        questionState?.answerResult.jsonValue = value.map { .integer($0) }
    }
}

public final class LikertScaleViewModel : AbstractIntegerScaleQuestionViewModel {
    
    @Published public var dots: [Dot] = []
    
    public override func initialize(_ questionState: QuestionState) {
        super.initialize(questionState)
        dots = Array(min...max).map {
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
    
    public final class Dot : ObservableObject, Identifiable {
        public var id: String { "\(value)" }
        
        public let value: Int
        @Published var selected: Bool = false

        init(_ value: Int) {
            self.value = value
        }
    }
}

