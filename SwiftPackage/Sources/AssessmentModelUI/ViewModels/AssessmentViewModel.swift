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
        self.currentBranchState = assessmentState

        do {
            assessmentState.navigator = try assessmentState.assessment.instantiateNavigator(state: self)
            self.goForward()
        } catch {
            assessmentState.navigationError = error
            assessmentState.status = .error
        }
    }
    
    // MARK: Current branch handling
    
    open private(set) var currentBranchState: BranchState!
    
    public var currentBranchResult: BranchNodeResult {
        currentBranchState.branchNodeResult
    }
    
    public var currentNavigator: Navigator {
        currentBranchState.navigator
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
            guard reservedKey == .beginning, let node = state.navigator.firstNode()
            else {
                return nil
            }
            currentBranchState = state
            return node
            
        case .node(let identifier):
            return currentNavigator.node(identifier: identifier)
        }
    }
    
    open func skipAssessment() {
        self.state.status = .declined
    }
    
    open func exitAssessment() {
        self.state.status = .continueLater
    }
    
    // MARK: Navigation handling
    
    open func goForward() {
        guard state != nil, currentBranchState != nil, currentBranchState.navigator != nil
        else {
            return
        }
        
        self.forwardCount += 1
                
        // Update the end timestamp for the current result
        if let current = state.currentStep {
            var result = current.result
            result.endDate = Date()
            currentBranchResult.appendStepHistory(with: result)
        }
        
        goForward(from: state.currentStep?.node)
    }
    
    private func goForward(from previousNode: Node?) {
        
        // Get the next node
        let nextNode = currentNavigator.nodeAfter(currentNode: previousNode, branchResult: currentBranchResult)
        guard let node = nextNode.node,
              let nodeState = nodeState(for: node)
        else {
            moveToNextSection()
            return
        }
        
        // Move to the node
        if let stepState = nodeState as? StepState {
            if currentBranchState.id == state.id,
               state.navigator.isCompleted(currentNode: stepState.step, branchResult: state.assessmentResult) {
                // If this is a completion step, the assessment is the top level object,
                // and backwards navigation is disabled then mark as "ready to save".
                state.result.endDate = Date()
                state.status = .readyToSave
            }
            moveTo(nextNode: nextNode, stepState: stepState)
        }
        else if let branchState = nodeState as? BranchState {
            moveInto(branchState: branchState, direction: .forward)
        }
        else {
            assertionFailure("Navigation from this point is undefined for this state machine: \(node)")
            markAsFinished()
        }
    }
    
    private func moveToNextSection() {
        currentBranchState.result.endDate = Date()
        if currentBranchState.id == state.id {
            markAsFinished()
        }
        else {
            let branchNode = currentBranchState.node
            currentBranchState = state
            goForward(from: branchNode)
        }
    }
    
    private func markAsFinished() {
        state.currentStep = nil
        state.status = .finished
    }
    
    private func moveTo(nextNode: NavigationPoint, stepState: StepState) {
        // Add the result to the step history
        currentBranchResult.appendStepHistory(with: stepState.result)
        
        // Set the new current state
        state.currentStep = stepState
        
        // Update the navigator
        navigationViewModel.forwardButtonText = goForwardButtonText(step: stepState.step)
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
    
    private func moveInto(branchState: BranchState, direction: PathMarker.Direction) {
        currentBranchResult.appendStepHistory(with: branchState.result)
        currentBranchState = branchState
        if direction == .forward {
            goForward(from: nil)
        }
        else {
            goBack(from: nil)
        }
    }
    
    open func nodeState(for node: Node) -> NodeState? {
        if let branchNode = node as? BranchNode {
            guard currentBranchState.id == state.id else {
                assertionFailure("Nested sections within sections is not supported by this state handler.")
                return nil
            }
            let branchState = BranchState(branch: branchNode,
                                          result: currentBranchResult.copyResult(with: branchNode.identifier),
                                          parentId: state.id)
            do {
                branchState.navigator = try branchState.branchNode.instantiateNavigator(state: self)
                return branchState
            } catch {
                state.navigationError = error
                state.status = .error
                return nil
            }
        }
        else if let question = node as? QuestionStep {
            return QuestionState(question,
                                 parentId: currentBranchState.id,
                                 answerResult: currentBranchResult.copyResult(with: node.identifier),
                                 canPause: canPauseAssessment(step: question),
                                 skipStepText: self.skipButtonText(step: question))
        }
        else if let step = node as? Step {
            return InstructionState(step, parentId: currentBranchState.id)
        }
        else {
            assertionFailure("Cannot create step or branch state for this node: \(node)")
            return nil
        }
    }
    
    open func goBack() {
        self.backCount += 1
        
        // Update the current result. This will ensure that if the mutable result on
        // the current state is a struct, that the step history replaces the struct
        // with the one that has possibily been mutated.
        if let current = state.currentStep {
            currentBranchResult.appendStepHistory(with: current.result)
        }
        
        goBack(from: state.currentStep?.node)
    }
    
    private func goBack(from previousNode: Node?) {
        
        // Get the previous node
        let nextNode = currentNavigator.nodeBefore(currentNode: previousNode, branchResult: currentBranchResult)
        guard let node = nextNode.node,
              let nodeState = nodeState(for: node)
        else {
            moveToPreviousSection()
            return
        }

        // Move to the node
        if let stepState = nodeState as? StepState {
            moveTo(nextNode: nextNode, stepState: stepState)
        }
        else if let branchState = nodeState as? BranchState {
            moveInto(branchState: branchState, direction: .backward)
        }
        else {
            assertionFailure("Navigation from this point is undefined for this state machine: \(node)")
            markAsFinished()
        }
    }
    
    private func moveToPreviousSection() {
        guard currentBranchState.id != state.id
        else {
            // If this is the top level then we've moved all the way up and are on the first step.
            return
        }
        let branchNode = currentBranchState.node
        currentBranchState = state
        goBack(from: branchNode)
    }
    
    open func canGoBack(step: Step) -> Bool {
        if shouldHide(.navigation(.goBackward), step: step) {
            return false
        }
        else if currentNavigator.allowBackNavigation(currentNode: step, branchResult: currentBranchResult) {
            return true
        }
        else if state.id != currentBranchState.id, currentNavigator.firstNode()?.identifier == step.identifier {
            return state.navigator.allowBackNavigation(currentNode: currentBranchState.node, branchResult: state.branchNodeResult)
        }
        else {
            return false
        }
    }
    
    open func canPauseAssessment(step: Step) -> Bool {
        guard state.interuptionHandling.canPause &&
              !shouldHide(.navigation(.pause), step: step)
        else {
            return false
        }
        if currentNavigator.canPauseAssessment(currentNode: step, branchResult: currentBranchResult) {
            return true
        }
        else if state.id != currentBranchState.id, currentNavigator.firstNode()?.identifier == step.identifier {
            return state.navigator.canPauseAssessment(currentNode: currentBranchState.node, branchResult: state.branchNodeResult)
        }
        else {
            return false
        }
    }
    
    open func goForwardButtonText(step: Step) -> Text? {
        buttonInfo(.navigation(.goForward), step: step).flatMap { info in
            info.buttonTitle.map {
                Text(.init(stringLiteral: $0), bundle: info.bundle)
            }
        }
    }
        
    open func skipButtonText(step: Step) -> Text? {
        guard !shouldHide(.navigation(.skip), step: step),
              supportsSkipButton(step: step)
        else {
            return nil
        }
        
        if let buttonInfo = buttonInfo(.navigation(.skip), step: step),
           let buttonTitle = buttonInfo.buttonTitle {
            return Text(.init(stringLiteral: buttonTitle), bundle: buttonInfo.bundle)
        }
        else if step is QuestionStep {
            return Text("Skip question", bundle: .module)
        }
        else {
            return Text("Skip step", bundle: .module)
        }
    }
    
    open func supportsSkipButton(step: Step) -> Bool {
        step is QuestionStep
    }
    
    private func shouldHide(_ buttonType: ButtonType, step: Step) -> Bool {
        if let shouldHide = state.assessment.shouldHideButton(buttonType, node: step), shouldHide {
            return true
        }
        else if state.id != currentBranchState.id,
                let shouldHide = currentBranchState.node.shouldHideButton(buttonType, node: step), shouldHide {
            return true
        }
        else {
            return step.shouldHideButton(buttonType, node: step) ?? false
        }
    }
    
    private func buttonInfo(_ buttonType: ButtonType, step: Step) -> ButtonActionInfo? {
        step.button(buttonType, node: step) ??
        currentBranchState.node.button(buttonType, node: step) ??
        (state.id != currentBranchState.id ? state.assessment.button(buttonType, node: step) : nil)
    }
}
