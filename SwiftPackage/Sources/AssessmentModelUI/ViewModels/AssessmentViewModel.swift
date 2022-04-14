//
//  AssessmentViewModel.swift
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

open class AssessmentViewModel : ObservableObject, NavigationState {
    
    @Published var forwardCount: Int = 0
    @Published var backCount: Int = 0
    
    public let navigationViewModel: PagedNavigationViewModel = .init()
    public private(set) var viewVender: AssessmentStepViewVender!
    public private(set) var state: AssessmentState!
    
    public init() {
        navigationViewModel.goForward = goForward
        navigationViewModel.goBack = goBack
    }
    
    func initialize(_ assessmentState: AssessmentState, viewVender: AssessmentStepViewVender) {
        guard assessmentState.id != self.state?.id else { return }
        
        self.state = assessmentState
        self.viewVender = viewVender

        do {
            assessmentState.navigator = try assessmentState.assessment.instantiateNavigator(state: self)
            self.goForward()
        } catch {
            assessmentState.navigationError = error
        }
    }
    
    // MARK: Current branch handling
    
    // TODO: syoung 04/04/2022 Handle branch nodes
    open var currentBranchState: NodeState {
        state
    }
    
    open var currentBranchNode: BranchNode {
        state.branchNode
    }
    
    open var currentBranchResult: BranchNodeResult {
        state.branchNodeResult
    }
    
    open var currentNavigator: Navigator {
        state.navigator
    }
    
    // MARK: Pause menu handling
    
    open func resume() {
        self.state.showingPauseActions = false
    }
    
    open func reviewInstructions() {
        guard let node = reviewNode(),
              let stepState = nodeState(for: node) as? StepState
        else {
            return
        }
        moveTo(nextNode: .init(node: node, direction: .backward), stepState: stepState)
        resume()
    }
    
    private func reviewNode() -> Node? {
        guard let reviewIdentifier = self.state.interuptionHandling.reviewIdentifier
        else {
            return nil
        }
        switch reviewIdentifier {
        case .reserved(let reservedKey):
            // TODO: syoung 04/14/2022 Handle branch nodes
            return (reservedKey == .beginning) ? state.navigator.firstNode() : nil
        case .node(let identifier):
            return currentNavigator.node(identifier: identifier)
        }
    }
    
    open func skipAssessment() {
        self.state.status = .declined
    }
    
    open func exitAssessment() {
        self.state.status = .earlyExit
    }
    
    // MARK: Navigation handling
    
    open func goForward() {
        self.forwardCount += 1
        
        // TODO: syoung 04/04/2022 Handle branch nodes
        
        // Update the end timestamp for the current result
        if let current = state.currentStep {
            var result = current.result
            result.endDate = Date()
            currentBranchResult.appendStepHistory(with: result)
        }
        
        // Get the next node
        let nextNode = currentNavigator.nodeAfter(currentNode: state.currentStep?.node, branchResult: currentBranchResult)
        guard let node = nextNode.node,
              let stepState = nodeState(for: node) as? StepState
        else {
            state.currentStep = nil
            currentBranchState.result.endDate = Date()
            state.status = .finished
            return
        }
        
        // Move to the node
        moveTo(nextNode: nextNode, stepState: stepState)
    }
    
    private func moveTo(nextNode: NavigationPoint, stepState: StepState) {
        // Add the result to the step history
        currentBranchResult.appendStepHistory(with: stepState.result)
        
        // Set the new current state
        state.currentStep = stepState
        
        // Update the navigator
        navigationViewModel.currentDirection = nextNode.direction == .backward ? .backward : .forward
        navigationViewModel.backEnabled = canGoBack(step: stepState.step)
        navigationViewModel.forwardEnabled = stepState.forwardEnabled || !viewVender.isSupported(step: stepState.step)
        if let progress = currentNavigator.progress(currentNode: stepState.step, branchResult: currentBranchResult) {
            navigationViewModel.progressHidden = stepState.progressHidden
            navigationViewModel.currentIndex = progress.current
            navigationViewModel.pageCount = progress.total
        }
        else {
            navigationViewModel.pageCount = .max
            navigationViewModel.currentIndex += 1
            navigationViewModel.progressHidden = true
        }
    }
    
    open func nodeState(for node: Node) -> NodeState? {
        if let question = node as? QuestionStep {
            return QuestionState(question,
                                 parentId: currentBranchState.id,
                                 answerResult: state.assessmentResult.copyResult(with: node.identifier),
                                 canPause: canPauseAssessment(step: question),
                                 skipStepText: self.skipButtonText(step: question))
        }
        else if let step = node as? Step {
            return InstructionState(step, parentId: currentBranchState.id)
        }
        else {
            assertionFailure("Branch node handling is not implemented")
            return nil
        }
    }
    
    open func goBack() {
        self.backCount += 1
        
        // Get the previous node
        let nextNode = currentNavigator.nodeBefore(currentNode: state.currentStep?.node, branchResult: currentBranchResult)
        guard let node = nextNode.node,
              let stepState = nodeState(for: node) as? StepState
        else {
            // TODO: syoung 04/06/2022 Handling going back to a earlier branch
            return
        }
        
        // Move to the node
        moveTo(nextNode: nextNode, stepState: stepState)
    }
    
    open func canGoBack(step: Step) -> Bool {
        currentNavigator.allowBackNavigation(currentNode: step, branchResult: currentBranchResult) &&
        !shouldHide(.navigation(.goBackward), step: step)
    }
    
    open func canPauseAssessment(step: Step) -> Bool {
        state.interuptionHandling.canPause &&
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
