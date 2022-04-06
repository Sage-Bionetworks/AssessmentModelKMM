//
//  AssessmentNavigationViewModel.swift
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
import SharedMobileUI
import AssessmentModel
import JsonModel

public final class AssessmentNavigationViewModel : AbstractAssessmentNavigationViewModel {
}

open class AbstractAssessmentNavigationViewModel : ObservableObject, NavigationState {
    
    @Published var navigationError: Error?
    @Published var current: NodeState?
    
    public let navigationViewModel: PagedNavigationViewModel = .init()
    
    public private(set) var state: AssessmentState!
    
    // TODO: syoung 04/04/2022 Handle branch nodes
    open var currentBranchNode: BranchNode {
        state.branchNode
    }
    
    open var currentBranchResult: BranchNodeResult {
        state.branchNodeResult
    }
    
    open var currentNavigator: Navigator {
        state.navigator
    }
    
    public init() {
        navigationViewModel.goForward = goForward
        navigationViewModel.goBack = goBack
    }
    
    open func initialize(_ assessmentState: AssessmentState) {
        self.state = assessmentState
        do {
            assessmentState.navigator = try assessmentState.assessment.instantiateNavigator(state: self)
            self.goForward()
        } catch {
            self.navigationError = error
        }
    }
    
    open func goForward() {
        // TODO: syoung 04/04/2022 Decide how to manage state where the animation should "go back"
        // TODO: syoung 04/04/2022 Handle exiting the assessment (rather than the section)
        // TODO: syoung 04/04/2022 Handle branch nodes
        
        // Update the end timestamp for the current result
        if let current = current {
            var result = current.result
            result.endDate = Date()
            currentBranchResult.appendStepHistory(with: result)
        }
        
        let nextNode = currentNavigator.nodeAfter(currentNode: current?.node, branchResult: currentBranchResult)
        guard let node = nextNode.node,
              let stepState = nodeState(for: node) as? StepState
        else {
            current = nil
            state.isFinished = true
            return
        }
        if let progress = currentNavigator.progress(currentNode: node, branchResult: currentBranchResult) {
            navigationViewModel.progressHidden = false
            navigationViewModel.currentIndex = progress.current
            navigationViewModel.pageCount = progress.total
        }
        else {
            navigationViewModel.progressHidden = true
        }
        navigationViewModel.backEnabled = canGoBack(step: stepState.step)
        currentBranchResult.appendStepHistory(with: stepState.result)
        current = stepState
    }
    
    open func nodeState(for node: Node) -> NodeState? {
        if let question = node as? QuestionStep {
            return QuestionState(question,
                                 answerResult: state.assessmentResult.copyResult(with: node.identifier),
                                 canPause: canPauseAssessment(step: question),
                                 skipStepText: self.skipButtonText(step: question))
        }
        else if let step = node as? Step {
            return InstructionState(step)
        }
        else {
            assertionFailure("Branch node handling is not implemented")
            return nil
        }
    }
    
    open func goBack() {
        // TODO: implement syoung 04/04/2022
    }
    
    open func canGoBack(step: Step) -> Bool {
        currentNavigator.allowBackNavigation(currentNode: step, branchResult: currentBranchResult) &&
        !shouldHide(.navigation(.goBackward), step: step)
    }
    
    open func canPauseAssessment(step: Step) -> Bool {
        currentNavigator.canPauseAssessment(currentNode: step, branchResult: currentBranchResult) &&
        !shouldHide(.navigation(.pause), step: step)
    }
    
    open func skipButtonText(step: Step) -> Text? {
        if shouldHide(.navigation(.skip), step: step) {
            return nil
        }
        else if let buttonInfo = buttonInfo(.navigation(.skip), step: step),
                let buttonTitle = buttonInfo.buttonTitle {
            return Text(buttonTitle)
        }
        else if step is QuestionStep {
            return Text("Skip question", bundle: .module)
        }
        else {
            return nil
        }
    }
    
    private func shouldHide(_ buttonType: ButtonType, step: Step) -> Bool {
        if let shouldHide = state.assessment.shouldHideButton(buttonType, node: step), shouldHide {
            return true
        }
        else if let shouldHide = currentBranchNode.shouldHideButton(buttonType, node: step), shouldHide {
            return true
        }
        else {
            return step.shouldHideButton(buttonType, node: step) ?? false
        }
    }
    
    private func buttonInfo(_ buttonType: ButtonType, step: Step) -> ButtonActionInfo? {
        step.button(buttonType, node: step) ??
        currentBranchNode.button(buttonType, node: step) ??
        state.assessment.button(buttonType, node: step)
    }
}
