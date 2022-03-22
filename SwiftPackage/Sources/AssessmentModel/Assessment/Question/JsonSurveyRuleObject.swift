//
//  JsonSurveyRuleObject.swift
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

public struct JsonSurveyRuleObject : Codable, Hashable, JsonSurveyRule {
    private enum CodingKeys: String, OrderedEnumCodingKey {
        case matchingValue = "matchingAnswer", skipToIdentifier, ruleOperator
    }
    
    public var skipToIdentifier: NavigationIdentifier
    public var ruleOperator: SurveyRuleOperator?
    public var matchingValue: JsonElement?
    
    public init(skipToIdentifier: String? = nil, matchingValue: JsonElement? = nil, ruleOperator: SurveyRuleOperator? = nil) {
        self.skipToIdentifier = skipToIdentifier.map { .init(rawValue: $0) } ?? .reserved(.exit)
        self.matchingValue = matchingValue
        self.ruleOperator = ruleOperator
    }
    
    public init(skipToIdentifier: NavigationIdentifier, matchingValue: JsonElement? = nil, ruleOperator: SurveyRuleOperator? = nil) {
        self.skipToIdentifier = skipToIdentifier
        self.matchingValue = matchingValue
        self.ruleOperator = ruleOperator
    }
}

extension JsonSurveyRuleObject : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        (codingKey as? CodingKeys).map { $0 == .skipToIdentifier } ?? false
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .skipToIdentifier:
            return .init(propertyType: .reference(NavigationIdentifier.documentableType()), propertyDescription:
                            "The node identifier or reversed key to jump to if the answer matches.")
        case .matchingValue:
            return .init(propertyType: .any, propertyDescription:
                            "The json value to evaluate against a result answer.")
        case .ruleOperator:
            return .init(propertyType: .reference(SurveyRuleOperator.documentableType()), propertyDescription:
                            "The rule operator to apply to the result comparison.")
        }
    }
    
    public static func examples() -> [JsonSurveyRuleObject] {
        [.init(skipToIdentifier: .reserved(.exit), matchingValue: .string("Stop"), ruleOperator: .equal)]
    }
}
