//
//  TextEntryViewModel.swift
//
//

import SwiftUI
import AssessmentModel
import JsonModel

protocol TextInputViewModelDelegate : AnyObject {
    @MainActor func didUpdateValue(_ newValue: JsonValue?, with identifier: String)
}

@MainActor
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

final class StringInputViewModel : TextInputViewModel<String> {
}

final class IntegerInputViewModel : TextInputViewModel<Int> {
    
    @Published var viewType: QuestionUIHint.NumberField = .textfield
    @Published var minLabel: String?
    @Published var maxLabel: String?
    @Published var minValue: Int
    @Published var maxValue: Int

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
    
    @Published var usesScale: Bool
    @Published var dots: [Dot]
    @Published var fraction: Double {
        didSet {
            guard !updating, usesScale else { return }
            updating = true
            constrainedValue = Int(round(Double(maxValue - minValue) * fraction)) + minValue
            updateState()
            updating = false
        }
    }
    
    @Published var pickerValues: [Int]
    @Published var pickerValue: Int {
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
        
        // Set up the ranges
        self.minLabel = range.minimumLabel
        self.maxLabel = range.maximumLabel
        let minValue = range.minimumValue ?? .min
        let maxValue = range.maximumValue ?? .max
        
        // Validate the ranges and change the type if needed
        var viewType: QuestionUIHint.NumberField = uiHint.flatMap { .init(rawValue: $0.rawValue) } ?? .textfield
        let usesScale = viewType.usesScale(minValue: minValue, maxValue: maxValue)
        if !usesScale {
            viewType = .textfield
        }
        
        // Switch the set up based on the view type *after* checking scaling validity
        let pickerValues = viewType.pickerValues(minValue: minValue, maxValue: maxValue)
        let defaultValue = viewType.defaultValue(minValue: minValue, maxValue: maxValue)
        let newValue = initialValue ?? ((viewType == .slider) ? defaultValue : nil)
        
        self.minValue = minValue
        self.maxValue = maxValue
        self.usesScale = usesScale
        self.viewType = viewType
        self.defaultValue = defaultValue
        self.pickerValue = newValue ?? minValue
        self.pickerValues = pickerValues
        self.dots = (viewType == .likert) ? pickerValues.map { .init($0) } : []
        self.fraction = newValue.map { max(0, min(1, Double($0 - minValue) / Double(maxValue - minValue))) } ?? 0

        super.init(identifier,
                   inputItem: inputItem,
                   validator: range as? TextEntryValidator,
                   initialValue: newValue)
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

extension QuestionUIHint.NumberField {
    
    func usesScale(minValue: Int, maxValue: Int) -> Bool {
        switch self {
        case .slider, .picker:
            return (minValue != .min && maxValue != .max && minValue < maxValue)
        case .likert:
            return (minValue < maxValue && (maxValue - minValue) <= 7)
        default:
            return false
        }
    }
    
    func pickerValues(minValue: Int, maxValue: Int) -> [Int] {
        switch self {
        case .picker, .likert:
            return Array(minValue...maxValue)
        default:
            return []
        }
    }
    
    func defaultValue(minValue: Int, maxValue: Int) -> Int? {
        switch self {
        case .picker, .slider:
            return minValue
        default:
            return nil
        }
    }
}

