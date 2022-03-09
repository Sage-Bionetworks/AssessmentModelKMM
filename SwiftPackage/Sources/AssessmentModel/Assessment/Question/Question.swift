//
//  Question.swift
//  
//  Copyright © 2020-2022 Sage Bionetworks. All rights reserved.
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

/// A ``Question`` can be an input of a form or it might be a stand-alone question. It represents a
/// something that, when composited, will result in a single answer. It may compose input fields to do
/// so such as "What is your name?" with an answer of `{ "familyName" : "Smith", "givenName" : "John" }`
public protocol Question : ResultMapElement, ContentInfo {
    
    /// The ``AnswerType`` that is associated with this ``Question``.
    var answerType: AnswerType { get }
    
    /// Should the forward button be disabled until this question is answered?
    var optional: Bool { get }
    
    /// Is there a  single answer for this  question or is the answer a list of multiple choices or input items?
    var singleAnswer: Bool { get }
    
    /// This is a "hint" that can be used to vend a view that is appropriate to the given question. If the library
    /// responsible for rendering the question doesn't know how to handle the hint, then it will be ignored.
    var uiHint: QuestionUIHint? { get }
    
    /// Build the input items associated with this question.
    func buildInputItems() -> [InputItem]
    
    /// A question always has a result that is an `AnswerResult`
    func instantiateAnswerResult() -> AnswerResult
}

public extension Question {
    func instantiateResult() -> ResultData {
        instantiateAnswerResult()
    }
}

public protocol QuestionStep : Question, Step, ContentNode {
}

public struct QuestionUIHint : RawRepresentable, Hashable, Codable {
    public let rawValue: String
    public init(rawValue: String) {
        self.rawValue = rawValue
    }
    
    public enum Choice : String, Codable, CaseIterable {
        case checkbox, radioButton
        public var uiHint: QuestionUIHint { .init(rawValue: rawValue) }
    }
    /// List of all the standard types.
    public static func allStandardTypes() -> [QuestionUIHint] {
        Choice.allCases.map { $0.uiHint }
    }
}

extension QuestionUIHint : ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        self.init(rawValue: value)
    }
}

extension QuestionUIHint : DocumentableStringLiteral {
    public static func examples() -> [String] {
        return allStandardTypes().map{ $0.rawValue }
    }
}
