//
//  TextEntryViewModel.swift
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

protocol TextInputViewModelDelegate : AnyObject {
    func didUpdateValue(_ newValue: JsonValue?, with identifier: String)
}

class TextInputViewModel<Value : JsonValue> : ObservableObject, Identifiable {
    
    weak var delegate: TextInputViewModelDelegate?
    
    let id: String
    let inputItem: TextInputItem
    let validator: TextEntryValidator

    @Published var isEditing: Bool = false {
        didSet {
            updateState()
        }
    }
    
    @Published var value: Value?
    @Published var fieldLabel: String?
    @Published var placeholder: String?
    @Published var characterLimit: Int = 250    // TODO: syoung 05/02/2022 Add a character limit to the serializable model
    @Published var validationError: Error?
    
    init(_ identifier: String, inputItem: TextInputItem, validator: TextEntryValidator? = nil, initialValue: Value? = nil) {
        self.id = identifier
        self.inputItem = inputItem
        self.validator = validator ?? inputItem.buildTextValidator()
        self.value = initialValue
        self.fieldLabel = inputItem.fieldLabel
        self.placeholder = inputItem.placeholder
    }
    
    func updateState() {
        guard !isEditing else { return }
        do {
            let constrainedValue = try validator.validateAnswer(self.value)
            self.value = constrainedValue as? Value
            validationError = nil
            delegate?.didUpdateValue(self.value, with: self.id)
        }
        catch {
            validationError = error
            delegate?.didUpdateValue(nil, with: self.id)
        }
    }
}

class StringInputViewModel : TextInputViewModel<String> {
}

class IntegerInputViewModel : TextInputViewModel<Int> {
    
    @Published var viewType: QuestionUIHint.NumberField = .textfield
    @Published var minLabel: String?
    @Published var maxLabel: String?
    @Published var minValue: Int = .min
    @Published var maxValue: Int = .max

    override var value: Int? {
        didSet {
            guard !updating else { return }
            updating = true
            constrainedValue = self.value.map { max(minValue, min(maxValue, $0)) } ?? defaultValue
            if usesScale, let newValue = constrainedValue {
                self.fraction = max(0, min(1, Double(newValue - minValue) / Double(maxValue - minValue)))
                self.pickerValue = newValue
            }
            updateState()
            updating = false
        }
    }
    
    @Published var usesScale: Bool = false
    @Published var dots: [Dot] = []
    @Published var fraction: Double = 0 {
        didSet {
            guard !updating, usesScale else { return }
            updating = true
            constrainedValue = Int(round(Double(maxValue - minValue) * fraction)) + minValue
            updateState()
            updating = false
        }
    }
    
    @Published var pickerValues: [Int] = []
    @Published var pickerValue: Int = 0 {
        didSet {
            guard !updating else { return }
            updating = true
            constrainedValue = pickerValue
            updateState()
            updating = false
        }
    }

    fileprivate var constrainedValue: Int?
    fileprivate var updating: Bool = false
    fileprivate var defaultValue: Int?
    
    init(_ identifier: String, inputItem: TextInputItem, uiHint: QuestionUIHint? = nil, range: IntegerRange? = nil, initialValue: Int? = nil) {
        let range = range ?? inputItem.buildTextValidator() as? IntegerRange ?? IntegerFormatOptions()
        
        super.init(identifier, inputItem: inputItem, validator: range as? TextEntryValidator, initialValue: initialValue)

        // Set up the ranges.
        range.minimumValue.map { minValue = $0 }
        range.maximumValue.map { maxValue = $0 }
        range.minimumLabel.map { minLabel = $0 }
        range.maximumLabel.map { maxLabel = $0 }
        
        // Set up whether or not the scale is used.
        if uiHint == .NumberField.slider.uiHint {
            // A slider is required to have a min and a max.
            // If valid ranges aren't set, then the slider should be hidden.
            usesScale = (minValue != .min && maxValue != .max && minValue < maxValue)
            defaultValue = usesScale ? minValue : nil
            viewType = usesScale ? .slider : .textfield
        }
        else if uiHint == .NumberField.likert.uiHint {
            // For a Likert scale UI/UX, the view is hard coded to *not* show a text field
            // so the min and max are required. Adjust them so mistakes in the model don't
            // result in a crash.
            usesScale = (minValue < maxValue && (maxValue - minValue) <= 7)
            viewType = usesScale ? .likert : .textfield
            if usesScale {
                dots = Array(minValue...maxValue).map {
                    .init($0)
                }
            }
        }
        else if uiHint == .NumberField.picker.uiHint {
            usesScale = (minValue != .min && maxValue != .max && minValue < maxValue)
            if usesScale {
                defaultValue = minValue
                pickerValues = Array(minValue...maxValue)
                pickerValue = initialValue ?? minValue
            }
        }
    }

    override func updateState() {
        guard !isEditing else { return }
        if usesScale {
            self.value = constrainedValue
            self.value.map { updateSelected($0) }
        }
        if let answerValue = self.value, minValue <= answerValue, answerValue <= maxValue {
            delegate?.didUpdateValue(answerValue, with: self.id)
        }
        else {
            delegate?.didUpdateValue(nil, with: self.id)
        }
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


