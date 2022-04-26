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
    
    @Published var formatOptions: IntegerFormatOptions?
    @Published var inputItem: TextInputItem?
    @Published var minValue: Int = 1
    @Published var maxValue: Int = 5
    @Published var stepInterval: Int = 1
    
    @Published var fraction: Double = 0 {
        didSet {
            guard !updating else { return }
            updating = true
            self.value = Int(round(Double(maxValue - minValue) * fraction)) + minValue
            updateState()
            updating = false
        }
    }
    
    @Published var value: Int? {
        didSet {
            guard !updating else { return }
            updating = true
            let constrainedValue = self.value.map { max(minValue, min(maxValue, $0)) } ?? 0
            if let current = self.value, current != constrainedValue {
                self.value = constrainedValue
            }
            self.fraction = Double(constrainedValue - minValue) / Double(maxValue - minValue)
            updateState()
            updating = false
        }
    }
    
    private var updating = false
    
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
        self.inputItem = question.inputItem
        
        // Set up the scale
        self.minValue = min
        self.maxValue = max
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
        guard minValue < maxValue else { return }
        questionState?.hasSelectedAnswer = (value != nil)
        questionState?.answerResult.jsonValue = value.map { .integer($0) }
    }
}

public final class SlidingScaleViewModel : AbstractIntegerScaleQuestionViewModel {
    
    public override func initialize(_ questionState: QuestionState) {
        if questionState.answerResult.jsonValue == nil {
            // For a sliding scale, the initial value is 0
            questionState.answerResult.jsonValue = .integer(0)
        }
        super.initialize(questionState)
    }
}

public final class LikertScaleViewModel : AbstractIntegerScaleQuestionViewModel {
    
    @Published public var dots: [Dot] = []
    
    public override func initialize(_ questionState: QuestionState) {
        super.initialize(questionState)
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
    
    public final class Dot : ObservableObject, Identifiable {
        public var id: String { "\(value)" }
        
        public let value: Int
        @Published var selected: Bool = false

        init(_ value: Int) {
            self.value = value
        }
    }
}

