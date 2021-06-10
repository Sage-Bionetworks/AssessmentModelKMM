//
//  PreviewBranchNodeState.swift
//
//  Copyright © 2021 Sage Bionetworks. All rights reserved.
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
import AssessmentModel

/// This class is used by SwiftUI to allow for testing the UI w/o showing permissions or async actions.
class PreviewBranchNodeState : BranchNodeState {

    var customNodeStateProvider: CustomNodeStateProvider? = nil
    var nodeUIController: NodeUIController? = nil
    var rootNodeController: RootNodeController? = nil
    
    var parent: BranchNodeState? = nil
    var currentChild: NodeState? = nil
    var currentResult: Result { branchNodeResult }
    
    let branchNode: NodeContainer
    let branchNodeResult: BranchNodeResult
    
    var currentIndex: Int
    
    init(_ branchNode: NodeContainer, initialIndex: Int = 0) {
        self.currentIndex = initialIndex
        self.branchNode = branchNode
        self.branchNodeResult = branchNode.createResult() as! BranchNodeResult
        self.currentChild = nextNodeState()
    }
    
    var node: Node {
        branchNode
    }
    
    func allowBackNavigation() -> Bool {
        currentIndex > 0
    }
    
    func exitEarly(finishedReason: FinishedReason, asyncActionNavigations: Set<AsyncActionNavigation>?) {
        finish(finishedReason: finishedReason)
    }
    
    func moveToNextNode(direction: NavigationPoint.Direction, requestedPermissions: Set<AnyHashable>?, asyncActionNavigations: Set<AsyncActionNavigation>?) {
        switch direction {
        case .exit:
            finish(finishedReason: .Complete())
        case .backward:
            guard currentIndex > 0 else { return }
            currentIndex -= 1
            let nodeState = nextNodeState()
            currentChild = nodeState
            self.nodeUIController?.handleGoBack(nodeState: nodeState, requestedPermissions: requestedPermissions, asyncActionNavigations: asyncActionNavigations)
        case .forward:
            guard currentIndex + 1 < branchNode.children.count else { return }
            if currentChild != nil {
                currentIndex += 1
            }
            let nodeState = nextNodeState()
            currentChild = nodeState
            self.nodeUIController?.handleGoForward(nodeState: nodeState, requestedPermissions: requestedPermissions, asyncActionNavigations: asyncActionNavigations)
        default:
            break  // Should not exist as an option but Kotlin is weird. syoung 05/24/2021
        }
    }
    
    func nextNodeState() -> NodeState {
        let node = branchNode.children[self.currentIndex]
        if let nodeState = self.customNodeStateProvider?.customLeafNodeStateFor(node: node, parent: self) {
            return nodeState
        }
        else if let question = node as? Question {
            return QuestionStateImpl(node: question, parent: self)
        }
        else {
            return LeafNodeStateImpl(node: node, parent: self)
        }
    }
    
    func progress() -> AssessmentModel.Progress? {
        AssessmentModel.Progress(current: Int32(currentIndex), total: Int32(branchNode.children.count), isEstimated: false)
    }
    
    func goBackward(requestedPermissions: Set<AnyHashable>?, asyncActionNavigations: Set<AsyncActionNavigation>?) {
        self.moveToNextNode(direction: .backward, requestedPermissions: requestedPermissions, asyncActionNavigations: asyncActionNavigations)
    }
    
    func goForward(requestedPermissions: Set<AnyHashable>?, asyncActionNavigations: Set<AsyncActionNavigation>?) {
        self.moveToNextNode(direction: .forward, requestedPermissions: requestedPermissions, asyncActionNavigations: asyncActionNavigations)
    }
    
    func finish(finishedReason: FinishedReason) {
        self.rootNodeController?.handleFinished(reason: finishedReason, nodeState: self)
    }
}
