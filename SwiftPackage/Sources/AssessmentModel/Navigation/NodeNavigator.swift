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

//  TODO: syoung 03/11/2022 Replace stubbed out navigator with rule-based implementation
public final class NodeNavigator : Navigator {
    
    public let identifier: String
    public let nodes: [Node]
    
    public init(identifier: String, nodes: [Node]) {
        self.identifier = identifier
        self.nodes = nodes
    }
    
    public func node(identifier: String) -> Node? {
        self.nodes.first(where: { $0.identifier == identifier })
    }
    
    public func nodeAfter(currentNode: Node?, branchResult: BranchNodeResult) -> NavigationPoint {
        guard let idx = self.nodeIndex(currentNode)
        else {
            return .init(node: nodes.first, branchResult: branchResult, direction: .forward)
        }
        if idx + 1 >= nodes.count {
            return .init(node: nil, branchResult: branchResult, direction: .forward)
        }
        else {
            return .init(node: nodes[idx + 1], branchResult: branchResult, direction: .forward)
        }
    }
    
    public func nodeBefore(currentNode: Node?, branchResult: BranchNodeResult) -> NavigationPoint {
        guard let idx = self.nodeIndex(currentNode)
        else {
            return .init(node: nodes.last, branchResult: branchResult, direction: .backward)
        }
        if idx - 1 <= 0 {
            return .init(node: nil, branchResult: branchResult, direction: .backward)
        }
        else {
            return .init(node: nodes[idx - 1], branchResult: branchResult, direction: .backward)
        }
    }
    
    public func hasNodeAfter(currentNode: Node, branchResult: BranchNodeResult) -> Bool {
        nodeIndex(currentNode).map { $0 + 1 < self.nodes.count } ?? false
    }
    
    public func allowBackNavigation(currentNode: Node, branchResult: BranchNodeResult) -> Bool {
        nodeIndex(currentNode).map { $0 - 1 >= 0 } ?? false
    }
    
    public func progress(currentNode: Node, branchResult: BranchNodeResult) -> Progress? {
        guard let idx = nodeIndex(currentNode) else { return nil }
        return .init(current: idx, total: nodes.count, isEstimated: false)
    }
    
    public func isCompleted(currentNode: Node, branchResult: BranchNodeResult) -> Bool {
        !self.allowBackNavigation(currentNode: currentNode, branchResult: branchResult) && currentNode is CompletionStep
    }
    
    func nodeIndex(_ node: Node?) -> Int? {
        guard let node = node else {
            return nil
        }
        return self.nodes.firstIndex(where: { $0.identifier == node.identifier })
    }
}
