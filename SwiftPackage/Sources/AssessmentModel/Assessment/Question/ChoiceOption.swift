//
//  ChoiceOption.swift
//  
//
//  Copyright © 2017-2022 Sage Bionetworks. All rights reserved.
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

import Foundation
import JsonModel

/// A choice option is a light-weight interface for a list of choices that should be displayed to the participant.
public protocol ChoiceOption {

    /// This is the JSON serializable element selected as one of the possible answers for a ``Question``. For certain
    /// special cases, the value may depend upon whether or not the item is selected.
    ///
    /// For example, a boolean ``Question`` may be displayed using choice items of "Yes" and "No" in a list. The choices
    /// would both be ``exclusive`` and the returned value for "Yes" could be `true` if ``selected`` and `null`
    /// if not while for the "No", it could be `false` if ``selected`` and `null` if not.
    func jsonElement(selected: Bool) -> JsonElement?

    /// A localized string that displays a short text offering a hint to the user of the data to be entered for this field.
    var fieldLabel: String { get }

    /// Additional detail shown below the ``fieldLabel``
    var detail: String? { get }
    
    /// An image associated with this choice.
    var icon: ImageInfo? { get }

    /// Does selecting this ``ChoiceOption`` mean that the other options should be deselected or
    /// selected as well?
    ///
    /// For example, this can be used in a multiple selection question to allow for a "none of the above"
    /// choice that is `exclusive` to the other items or an "all of the above" choice that should
    /// select `all` other choices as well (except those that are marked as `exclusive`).
    var selectorType: ChoiceSelectorType { get }
}

/// The selection rule associated with a given ``ChoiceOption``.
public enum ChoiceSelectorType : String, StringEnumSet, DocumentableStringEnum {
    case `default`, exclusive, all
}

public struct ChoiceOptionObject : ChoiceOption, Codable, Hashable {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case matchingValue = "value", fieldLabel="text", detail, _selectorType = "selectorType"
    }
    
    public let matchingValue: JsonElement?
    public let fieldLabel: String
    public let detail: String?
    
    public var selectorType: ChoiceSelectorType {
        _selectorType ?? .default
    }
    private let _selectorType: ChoiceSelectorType?
    
    public func jsonElement(selected: Bool) -> JsonElement? {
        selected ? matchingValue : nil
    }
    
    // Images are not currently supported. syoung 03/08/2022
    public var icon: ImageInfo? { nil }
    
    public init(value: JsonElement?,
                text: String,
                detail: String? = nil,
                selectorType: ChoiceSelectorType? = nil) {
        self.matchingValue = value
        self.fieldLabel = text
        self.detail = detail
        self._selectorType = selectorType
    }
    
    public init(text: String) {
        self.fieldLabel = text
        self.matchingValue = .string(text)
        self.detail = nil
        self._selectorType = nil
    }
}

extension ChoiceOptionObject : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        (codingKey as? CodingKeys) == .fieldLabel
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .matchingValue:
            return .init(propertyType: .any, propertyDescription:
                    """
                    The matching value is any json element, but all json elements within the collection
                    of choices should have the same json type.
                    """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n"))
        case .fieldLabel:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "A localized string to show as the selection label.")
        case .detail:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "Additional detail to display below the primary label.")
        case ._selectorType:
            return .init(propertyType: .reference(ChoiceSelectorType.documentableType()), propertyDescription:
                    """
                    Does selecting this choice mean that the other options should be deselected or selected as well?
                    
                    For example, this can be used in a multiple selection question to allow for a 'none of the above'
                    choice that is `exclusive` to the other items or an 'all of the above' choice that should
                    select `all` other choices as well (except those that are marked as `exclusive`).
                    """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n"))
        }
    }
    
    public static func examples() -> [ChoiceOptionObject] {
        return [.init(value: .integer(1), text: "One")]
    }
}

