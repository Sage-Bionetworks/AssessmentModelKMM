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

/// The state objects are all simple objects without any business or navigation logic. This is done because
/// SwiftUI  @EnvironmentObject observables have to be castable using NSClassFromString so the environment
/// objects have to be final classes.
open class NodeState : ObservableObject, Identifiable {
    public let id: String
    
    public let node: Node
    public var result: ResultData
    
    init(node: Node, result: ResultData, parentId: String? = nil) {
        self.id = "\(parentId ?? "")/\(node.identifier)"
        self.node = node
        self.result = result
    }
}

open class BranchState : NodeState {
    public final var branchNode: BranchNode { node as! BranchNode }
    public final var branchNodeResult: BranchNodeResult {
        get { result as! BranchNodeResult }
        set { result = newValue }
    }
    
    var navigator: Navigator!
    
    public init(branch: BranchNode, result: BranchNodeResult?, parentId: String? = nil) {
        super.init(node: branch, result: result ?? branch.instantiateBranchNodeResult(), parentId: parentId)
    }
}

open class StepState : NodeState {
    public final var step: Step { node as! Step }
    
    open var forwardEnabled: Bool { true }
    open var progressHidden: Bool { false }
    
    public init(step: Step, result: ResultData, parentId: String? = nil) {
        super.init(node: step, result: result, parentId: parentId)
    }
}

/// State object for an assessment.
public final class AssessmentState : BranchState {
    public var assessment: Assessment { node as! Assessment }
    public var assessmentResult: AssessmentResult { result as! AssessmentResult }
    
    public let interuptionHandling: InterruptionHandling

    @Published public var status: Status = .running
    @Published public var currentStep: StepState?
    @Published public var showingPauseActions: Bool = false
    @Published public var navigationError: Error?

    public init(_ assessment: Assessment, restoredResult: AssessmentResult? = nil, interuptionHandling: InterruptionHandling? = nil) {
        let result = restoredResult ?? assessment.instantiateAssessmentResult()
        self.interuptionHandling = interuptionHandling ?? assessment.interruptionHandling
        super.init(branch: assessment, result: result)
    }
    
    public enum Status : Int, Hashable, Comparable {
        case running, readyToSave, finished, declined, continueLater, error
    }
}

extension RawRepresentable where RawValue == Int {
    public static func < (lhs: Self, rhs: Self) -> Bool {
        lhs.rawValue < rhs.rawValue
    }
}

/// State object for an instruction.
public final class InstructionState : StepState {
    
    override public var progressHidden: Bool { node is OverviewStep || node is CompletionStep }

    @Published public var image: Image?
    @Published public var title: String?
    @Published public var subtitle: String?
    @Published public var detail: String?
    
    public init(_ instruction: Step, parentId: String? = nil) {
        if let contentNode = instruction as? ContentNode {
            self.title = contentNode.title
            self.subtitle = contentNode.subtitle
            self.detail = contentNode.detail
            if let imageInfo = contentNode.imageInfo as? FetchableImage {
                self.image = Image(imageInfo.imageName, bundle: imageInfo.bundle)
            }
        }
        super.init(step: instruction, result: instruction.instantiateResult(), parentId: parentId)
    }
}

/// State object for a question.
public final class QuestionState : StepState {
    public var question: QuestionStep { step as! QuestionStep }
    public var answerResult: AnswerResult { result as! AnswerResult }

    public override var forwardEnabled: Bool { question.optional || hasSelectedAnswer }
    public let canPause: Bool
    public let skipStepText: Text?
    
    @Published public var title: String
    @Published public var subtitle: String?
    @Published public var detail: String?
    @Published public var hasSelectedAnswer: Bool
    
    public init(_ question: QuestionStep, parentId: String? = nil, answerResult: AnswerResult? = nil, canPause: Bool = true, skipStepText: Text? = nil) {
        self.title = question.title ?? question.subtitle ?? question.detail ?? ""
        self.subtitle = question.title == nil ? nil : question.subtitle
        self.detail = question.title == nil && question.subtitle == nil ? nil : question.detail
        self.canPause = canPause
        self.skipStepText = skipStepText
        let result = answerResult ?? question.instantiateAnswerResult()
        self.hasSelectedAnswer = result.jsonValue != nil
        super.init(step: question, result: result, parentId: parentId)
    }
}

extension BranchNodeResult {
    func copyResult<T>(with identifier: String) -> T? {
        stepHistory.last { $0.identifier == identifier }.flatMap { $0.deepCopyWithNewStartDate() as? T }
    }
}

extension ResultData {
    func deepCopyWithNewStartDate() -> Self {
        var ret = self.deepCopy()
        ret.startDate = Date()
        if var multiplatformResult = ret as? MultiplatformResultData {
            multiplatformResult.endDateTime = nil
            return multiplatformResult as! Self
        }
        else {
            ret.endDate = ret.startDate
            return ret
        }
    }
}

extension ResourceInfo {
    var bundle: Bundle? {
        self.factoryBundle as? Bundle ?? self.bundleIdentifier.flatMap { Bundle(identifier: $0) }
    }
}

extension UUID {
    func guid(count: Int) -> Substring {
        let uuidString = self.uuidString
        return uuidString[..<uuidString.index(uuidString.startIndex, offsetBy: count)]
    }
}
