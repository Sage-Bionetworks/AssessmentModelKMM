//
//  NodeState.swift
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

/**
 * The state objects are all simple objects without any business or navigation logic. This is done because
 * SwiftUI  @EnvironmentObject observables have to be castable using NSClassFromString.
 */


/// State object for a branch node.
public final class BranchNodeState : ObservableObject {
    
    weak var parent: BranchNodeState?
    
    public let branchNode: BranchNode
    public let branchResult: BranchNodeResult
    
    public init(_ branchNode: BranchNode, parent: BranchNodeState? = nil) {
        self.branchNode = branchNode
        self.branchResult = parent?.branchResult.copyResult(with: branchNode.identifier) ?? branchNode.instantiateBranchNodeResult()
    }
}

public protocol QuestionViewModel : AnyObject {
    func calculateAnswer() -> JsonElement
}

/// State object for a question.
public final class QuestionState : ObservableObject {
    
    public let question: Question
    public let answerResult: AnswerResult
    
    @Published public var title: String
    @Published public var subtitle: String?
    @Published public var detail: String?
    
    @Published public var hasSelectedAnswer: Bool = false
    
    weak public var viewModel: QuestionViewModel?
    
    public init(_ question: Question, previousAnswer: AnswerResult? = nil) {
        self.question = question
        self.answerResult = previousAnswer?.deepCopy() ?? question.instantiateAnswerResult()
        self.title = question.title ?? question.subtitle ?? question.detail ?? ""
        self.subtitle = question.title == nil ? nil : question.subtitle
        self.detail = question.title == nil && question.subtitle == nil ? nil : question.detail
    }
}

extension BranchNodeResult {
    func copyResult<T>(with identifier: String) -> T? {
        stepHistory.last { $0.identifier == identifier }.flatMap { $0.deepCopy() as? T }
    }
}
