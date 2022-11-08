//
//  PreviewBranchNodeState.swift
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
