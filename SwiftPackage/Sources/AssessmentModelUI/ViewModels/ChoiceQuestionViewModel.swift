//
//  ChoiceQuestionViewModel.swift
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

public final class ChoiceQuestionViewModel : ObservableObject {

    weak var questionState: QuestionState?
    
    @Published public var singleAnswer: Bool = true
    @Published public var choices: [ChoiceViewModel] = []
    @Published public var otherChoice: OtherChoiceViewModel? = nil
    
    public init() {
    }
    
    public func initialize(_ questionState: QuestionState) {
        guard let question = questionState.question as? ChoiceQuestion else {
            assertionFailure("Attempting to initialize a choice question with a state object that does not have the correct type.")
            return
        }
        
        // Set up the pointer
        self.questionState = questionState
        
        // Pull out whether or not the question has a single answer.
        self.singleAnswer = question.singleAnswer

        // Filter out the choices where there is a mapping of choice to value.
        let previousAnswer = questionState.answer
        var answers = previousAnswer?.toArray(of: question.baseType) ?? []
        var choices: [ChoiceViewModel] = question.choices.map {
            .init($0, selected: answers.selected($0, question, previousAnswer), selectionToggler: self)
        }
        // Handle remaining choices.
        self.otherChoice = question.other.map {
            let jsonValue = answers.count == 1 ? answers.first : nil
            let ret: OtherChoiceViewModel = .init($0, value: jsonValue, selectionToggler: self)
            if jsonValue != nil { answers = [] }
            return ret
        }
        answers.forEach {
            choices.append(.init(.init(value: $0, text: "\($0)"), selected: true, selectionToggler: self))
        }
        self.choices = choices
        
        // Update whether or not the question state has an answer selected.
        questionState.hasSelectedAnswer = choices.contains(where: { $0.selected }) || (self.otherChoice?.selected ?? false)
    }
    
    public func updateAnswer() {
        questionState?.answer = calculateAnswer()
    }

    func calculateAnswer() -> JsonElement? {
        if singleAnswer {
            if let otherValue = otherChoice?.jsonValue() {
                return .init(otherValue)
            }
            else if let selectedChoice = choices.first(where: { $0.selected }) {
                return selectedChoice.jsonChoice.matchingValue ?? .null
            }
            else {
                return nil
            }
        }
        else {
            var hasSelected: Bool = false
            var values: [JsonSerializable] = choices.compactMap {
                guard $0.selected else { return nil }
                hasSelected = true
                return $0.jsonChoice.matchingValue?.jsonObject()
            }
            if let otherValue = otherChoice?.jsonValue()?.jsonObject() {
                hasSelected = true
                values.append(otherValue)
            }
            return hasSelected ? .array(values) : nil
        }
    }
    
    private var updating: Bool = false
    
    func updateSelected(changed changedChoice: ObservableChoice) {
        guard !updating else { return }
        updating = true
        
        var hasSelected = changedChoice.selected
        
        let deselectOthers =
        (changedChoice.selected && singleAnswer) ||
        (changedChoice.selected && changedChoice.selectorType == .exclusive)
            
        let selectOthers = !singleAnswer && changedChoice.selected && changedChoice.selectorType == .all
        let deselectAllAbove = !changedChoice.selected && changedChoice.selectorType == .all
        
        let selectedOtherChoice = changedChoice.selected && otherChoice?.id == changedChoice.id
        
        choices.forEach { choice in
            guard choice.id != changedChoice.id else { return }
            if changedChoice.selected && (choice.selectorType == .exclusive || deselectOthers) {
                choice.selected = false
            }
            else if !changedChoice.selected && choice.selectorType == .all && !selectedOtherChoice {
                choice.selected = false
            }
            else if selectOthers && choice.selectorType == .default {
                choice.selected = true
            }
            else if deselectAllAbove && choice.selectorType == .default {
                choice.selected = false
            }
            else {
                hasSelected = hasSelected || choice.selected
            }
        }
        if deselectOthers && otherChoice?.id != changedChoice.id {
            otherChoice?.selected = false
        }
        
        // Update state of whether or not this view model has a valid selection.
        questionState?.hasSelectedAnswer = hasSelected || (otherChoice?.selected ?? false)
        updateAnswer()
        
        updating = false
    }

}

protocol SelectionToggler : AnyObject {
    func updateSelected(changed choice: ObservableChoice)
}

extension ChoiceQuestionViewModel : SelectionToggler {
}

protocol ObservableChoice : AnyObject {
    var id: String { get }
    var selectorType: ChoiceSelectorType { get }
    var selected: Bool { get }
}

public final class ChoiceViewModel : ObservableObject, Identifiable {
    public var id: String { jsonChoice.label }
    
    /// Whether or not the choice is selected.
    @Published public var selected: Bool {
        didSet {
            selectionToggler?.updateSelected(changed: self)
        }
    }
    
    public let jsonChoice: JsonChoice
    
    weak var selectionToggler: SelectionToggler!
    var selectorType: ChoiceSelectorType { jsonChoice.selectorType }
    
    init(_ jsonChoice: JsonChoice, selected: Bool, selectionToggler: SelectionToggler) {
        self.jsonChoice = jsonChoice
        self.selected = selected
        self.selectionToggler = selectionToggler
    }
}

extension ChoiceViewModel : ObservableChoice {
}

public final class OtherChoiceViewModel : ObservableObject, Identifiable {
    public let id: String = "$OtherTextEntry"
    
    /// Whether or not the choice is selected.
    @Published public var selected: Bool {
        didSet {
            selectionToggler?.updateSelected(changed: self)
        }
    }
    
    weak var selectionToggler: SelectionToggler!
    var selectorType: ChoiceSelectorType { .default }

    let fieldLabel: String
    let inputItem: TextInputItem
    let validator: TextEntryValidator
    
    /// The value of the choice.
    @Published public var value: String {
        didSet {
            // TODO: syoung 03/29/2022 Handle validation and update selection state
        }
    }
    
    func jsonValue() -> JsonValue? {
        guard selected, !value.isEmpty else { return nil }
        return try? validator.validateText(value)
    }
    
    init(_ inputItem: TextInputItem, value: JsonElement?, selectionToggler: SelectionToggler) {
        self.inputItem = inputItem
        let validator = inputItem.buildTextValidator()
        self.fieldLabel = inputItem.fieldLabel ?? Localization.localizedString("Other")
        self.value = validator.localizedText(for: value) ?? ""
        self.validator = validator
        self.selected = value != nil && value != .null
        self.selectionToggler = selectionToggler
    }
}

extension OtherChoiceViewModel : ObservableChoice {
}
                
fileprivate extension Array where Element == JsonElement {
    mutating func selected(_ jsonChoice: JsonChoice, _ question: ChoiceQuestion, _ previousAnswer: JsonElement?) -> Bool {
        if let matchingValue = jsonChoice.matchingValue {
            if let idx = self.firstIndex(of: matchingValue) {
                self.remove(at: idx)
                return true
            }
            else {
                return false
            }
        }
        else if question.singleAnswer {
            return previousAnswer == .null
        }
        else {
            return previousAnswer == .array([]) && jsonChoice.selectorType == .exclusive
        }
    }
}

extension JsonElement {
    
    init?(_ json: JsonSerializable, baseType: JsonType) {
        if baseType == .string {
            self = .string("\(json)")
        }
        else if baseType == .number {
            if let value = json as? JsonNumber {
                self = .number(value)
            }
            else if let num = json as? NSNumber {
                self = .number(num.doubleValue)
            }
            else {
                return nil
            }
        }
        else if baseType == .boolean, let value = json as? Bool {
            self = .boolean(value)
        }
        else if baseType == .integer, let value = json as? NSNumber {
            self = .integer(value.intValue)
        }
        else {
            return nil
        }
    }
    
    func toArray(of baseType: JsonType) -> [JsonElement] {
        switch self {
        case .null:
            return []
        case .array(let values):
            return values.compactMap { .init($0, baseType: baseType) }
        default:
            return [self]
        }
    }
}
