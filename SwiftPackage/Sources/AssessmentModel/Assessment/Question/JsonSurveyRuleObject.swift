//
//  JsonSurveyRuleObject.swift
//  
//

import Foundation
import JsonModel

@Serializable
public struct JsonSurveyRuleObject : Codable, Hashable, JsonSurveyRule {

    public var skipToIdentifier: NavigationIdentifier = .reserved(.exit)
    @SerialName("matchingAnswer") public var matchingValue: JsonElement?
    public var ruleOperator: SurveyRuleOperator?
    
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
