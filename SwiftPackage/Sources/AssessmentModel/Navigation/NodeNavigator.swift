//
//  NodeNavigator.swift
//  
//
//  Copyright © 2017-2022 Sage Bionetworks. All rights reserved.
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

/// The navigation rule is used to allow the assessment navigator to check if a node has a navigation rule and
/// apply as necessary.
public protocol NavigationRule {

    /// Identifier for the next step to navigate to based on the current task result and the conditional rule associated
    /// with this task. The ``branchResult`` is the current result for the associated navigator. The variable
    /// ``isPeeking`` equals `true` if this is used in a call that sets up button state and equals `false` in a
    /// call used to navigate to the next node.
    func nextNodeIdentifier(branchResult: BranchNodeResult, isPeeking: Bool) -> NavigationIdentifier?
}

//  TODO: syoung 03/21/2022 refactor navigation tests to move from SageResearch/Kotlin implementations to this framework.

public final class NodeNavigator : Navigator {
    
    public let identifier: String
    public let nodes: [Node]
    
    public init(identifier: String, nodes: [Node]) throws {
        guard Set(nodes.map { $0.identifier }).count == nodes.count else {
            throw NodeNavigatorError.notUniqueIdentifiers
        }
        self.identifier = identifier
        self.nodes = nodes
    }
    
    enum NodeNavigatorError : Error {
        case notUniqueIdentifiers
    }
    
    public func node(identifier: String) -> Node? {
        self.nodes.first(where: { $0.identifier == identifier })
    }
    
    public func nodeAfter(currentNode: Node?, branchResult: BranchNodeResult) -> NavigationPoint {
        guard let currentNode = currentNode, let idx = self.nodeIndex(currentNode) else {
            return .init(node: nodes.first, direction: .forward)
        }

        if let navId = self.nextNodeIdentifier(currentNode: currentNode, branchResult: branchResult, isPeeking: false) {
            switch navId {
            case .reserved(let reservedKey):
                return .init(node: nil, direction: reservedKey == .exit ? .exit : .forward)
            case .node(let identifier):
                // This implementation does not support switching the direction of navigation so always forward.
                return .init(node: self.node(identifier: identifier), direction: .forward)
            }
        }
        else if idx + 1 >= nodes.count {
            return .init(node: nil, direction: .forward)
        }
        else {
            return .init(node: nodes[idx + 1], direction: .forward)
        }
    }
    
    public func nodeBefore(currentNode: Node?, branchResult: BranchNodeResult) -> NavigationPoint {
        return .init(node: self.previousNode(currentNode: currentNode, branchResult: branchResult),
                     direction: .backward)
    }
    
    public func hasNodeAfter(currentNode: Node, branchResult: BranchNodeResult) -> Bool {
        if let navId = self.nextNodeIdentifier(currentNode: currentNode, branchResult: branchResult, isPeeking: true) {
            switch navId {
            case .reserved(_):
                return false
            case .node(let identifier):
                // This implementation does not support switching the direction of navigation so always forward.
                return self.nodes.contains(where: { $0.identifier == identifier })
            }
        }
        else {
            return nodeIndex(currentNode).map { $0 + 1 < self.nodes.count } ?? false
        }
    }
    
    public func allowBackNavigation(currentNode: Node, branchResult: BranchNodeResult) -> Bool {
        self.previousNode(currentNode: currentNode, branchResult: branchResult) != nil
    }
    
    public func canPauseAssessment(currentNode: Node, branchResult: BranchNodeResult) -> Bool {
        guard let idx = nodeIndex(currentNode) else { return false }
        return idx > 0 && !isCompleted(currentNode: currentNode, branchResult: branchResult)
    }
    
    public func progress(currentNode: Node, branchResult: BranchNodeResult) -> Progress? {
        guard let idx = nodeIndex(currentNode) else { return nil }
        
        return .init(current: idx, total: nodes.count, isEstimated: true)
    }
    
    public func isCompleted(currentNode: Node, branchResult: BranchNodeResult) -> Bool {
        !self.allowBackNavigation(currentNode: currentNode, branchResult: branchResult) && currentNode is CompletionStep
    }
    
    // MARK: Node navigation
    
    private func nextNodeIdentifier(currentNode: Node, branchResult: BranchNodeResult, isPeeking: Bool) -> NavigationIdentifier? {
        (currentNode as? NavigationRule)?.nextNodeIdentifier(branchResult: branchResult, isPeeking: isPeeking)
    }
    
    func nodeIndex(_ node: Node?) -> Int? {
        guard let node = node else {
            return nil
        }
        return self.nodes.firstIndex(where: { $0.identifier == node.identifier })
    }
    
    func previousNode(currentNode: Node?, branchResult: BranchNodeResult) -> Node? {
        // If the current node is nil then return the last node in the step history (if any).
        guard let currentNode = currentNode,
              let currentIdx = nodes.firstIndex(where: { $0.identifier == currentNode.identifier })
        else {
            return branchResult.stepHistory.last.flatMap { self.node(identifier: $0.identifier) }
        }
        // If this branch state handler does not support path markers then return the index before the current node.
        guard branchResult.path.count > 0 else {
            return self.nodeIndex(currentNode).flatMap { idx in
                idx - 1 >= 0 ? self.nodes[idx - 1] : nil
            }
        }
        // If the path index (going forward) for the current node is not found or it's the first node
        // then return nil.
        guard let previousIdx = findIndexBefore(currentNode, in: branchResult.path, findLast: true)
        else {
            return nil
        }

        // If the path is looping, then need to check that the previously shown node
        // is *after* the current node sequentially.
        if previousIdx < currentIdx {
            // Exit early if the previous shown is *before* this node.
            return self.nodes[previousIdx]
        }
        
        // Otherwise, look to see that the first time the node was shown has a "before" node
        // and move to that node.
        return findIndexBefore(currentNode, in: branchResult.path, findLast: false).map {
            self.nodes[$0]
        }
    }
    
    private func findIndexBefore(_ currentNode: Node, in path: [PathMarker], findLast: Bool) -> Int? {
        func matching(_ marker: PathMarker) -> Bool {
            marker.identifier == currentNode.identifier && marker.direction == .forward
        }
        guard let index = findLast ? path.lastIndex(where: {matching($0)}) : path.firstIndex(where: {matching($0)}),
                index > 0
        else {
            return nil
        }
        let markerBeforeLast = path[index - 1]
        return self.nodes.firstIndex(where: { markerBeforeLast.identifier == $0.identifier })
    }
}

