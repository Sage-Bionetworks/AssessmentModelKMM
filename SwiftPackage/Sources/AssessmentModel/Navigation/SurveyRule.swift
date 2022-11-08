//
//  SurveyRule.swift
//

import Foundation
import JsonModel
import ResultModel

/// Defines an evaluation rule and returns a step identifier if appropriate.
public protocol SurveyRule {
    
    /// For a given result (if any), what is the step that the survey should go to next?
    ///
    /// - parameter result: The result to evaluate.
    /// - returns: The identifier to skip to if the result evaluates to `true`.
    func evaluateRule(with result: ResultData?) -> NavigationIdentifier?
}

/// Can be used to compare a stored result to a matching value.
public protocol JsonComparable {
    
    /// Expected matching value for a rule.
    var matchingValue: JsonElement? { get }
    
    /// The rule operator to apply. (Default == .equal)
    var ruleOperator: SurveyRuleOperator? { get }
}

/// A survey rule that matches an expected result to the answer and vends a skip identifier
/// if the match is evaluated to `true`.
public protocol JsonSurveyRule : SurveyRule, JsonComparable {
    
    /// Skip identifier for this rule.
    var skipToIdentifier: NavigationIdentifier { get }
}

/// Operators that can be used to evaluate a ``JsonComparableRule`` survey rule.
public enum SurveyRuleOperator: String, StringEnumSet, DocumentableStringEnum {
    
    /// The answer value is equal to the value being evaluated.
    case equal              = "eq"
    
    /// The answer value is *not* equal to the value being evaluated.
    case notEqual           = "ne"
    
    /// The answer value is less than the value being evaluated.
    case lessThan           = "lt"
    
    /// The answer value is greater than the value being evaluated.
    case greaterThan        = "gt"
    
    /// The answer value is less than or equal to the value being evaluated.
    case lessThanEqual      = "le"
    
    /// The answer value is greater than or equal to the value being evaluated.
    case greaterThanEqual   = "ge"
}

extension JsonSurveyRule {
    public func evaluateRule(with result: ResultData?) -> NavigationIdentifier? {
        (result as? AnswerResult).map { self.isMatching(result: $0) } ?? false ? self.skipToIdentifier : nil
    }
}

extension JsonComparable {
    
    public func isMatching(result: AnswerResult) -> Bool {
        isMatching(answer: result.jsonValue ?? .null, answerType: result.jsonAnswerType)
    }
    
    public func isMatching(answer: JsonElement, answerType: AnswerType? = nil) -> Bool {
        let op = self.ruleOperator ?? .equal
        let match = self.matchingValue ?? .null
        switch op {
        case .equal:
            switch match {
            case .integer(_), .number(_):
                return match.isEqualNumber(answer, answerType: answerType)
            default:
                return match == answer
            }
        case .notEqual:
            return match != answer
        case .greaterThan:
            return answer > match
        case .greaterThanEqual:
            return answer >= match
        case .lessThan:
            return answer < match
        case .lessThanEqual:
            return answer <= match
        }
    }
}

extension JsonElement {
    fileprivate func isEqualNumber(_ rhs: JsonElement, answerType: AnswerType?) -> Bool {
        if self == rhs {
            return true // handles case where both are null or infinity
        }
        else if let ln = self.jsonNumber()?.doubleValue, let rn = rhs.jsonNumber()?.doubleValue {
            let jsonType = answerType?.baseType ?? self.jsonType
            let digits = (answerType as? DecimalAnswerType)?.significantDigits ?? (jsonType == .integer ? 0 : 5)
            let roundTo = pow(10, Double(digits))
            return round(ln * roundTo) == round(rn * roundTo) // handles cases where there may be a roundoff error
        }
        else {
            return false // not equal
        }
    }
}

