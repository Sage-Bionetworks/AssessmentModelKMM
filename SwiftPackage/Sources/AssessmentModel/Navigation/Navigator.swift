//
//  Navigator.swift
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

/// The navigation state is a means to allow circular references where the navigator may require state information
/// from the state machine to initialize.
public protocol NavigationState : AnyObject {
}

/// The ``Navigator`` is used by the assessment view controller or fragment to determine the order of presentation of
/// the ``Node`` elements in an assessment. The navigator is responsible for determining navigation based on the input
/// model, results, and platform context.
///
/// The most common implementation of a ``Navigator`` will include a list of child nodes and rules for navigating the list.
/// However, the ``Navigator`` is defined more generally to allow for custom navigation that may not use a list of nodes.
/// For example, data tracking assessments such as medication tracking do not neatly conform to sequential navigation and
/// as such, use a different set of rules to navigate the assessment.
///
public protocol Navigator {

    /// Returns the ``Node`` associated with the given ``identifier``, if any. This is the ``identifier`` for the
    /// ``Node`` that is local to this level of the node tree.
    func node(identifier: String) -> Node?

    /// Continue to the next node after the current node. If ``currentNode`` is null, then the navigation is moving
    /// forward into this section or assessment.
    func nodeAfter(currentNode: Node?, branchResult: BranchNodeResult) -> NavigationPoint

    /// The node to move *back* to if the participant taps the back button. If ``currentNode`` is null, then the
    /// navigation is moving back into this section or assessment.
    func nodeBefore(currentNode: Node?, branchResult: BranchNodeResult) -> NavigationPoint

    /// Should the controller display a "Next" button or is the given button the last one in the assessment in which case
    /// the button to end the assessment should say "Done"?
    func hasNodeAfter(currentNode: Node, branchResult: BranchNodeResult) -> Bool

    /// Is backward navigation allowed from this ``currentNode`` with the current ``branchResult``?
    func allowBackNavigation(currentNode: Node, branchResult: BranchNodeResult) -> Bool

    /// Returns the ``Progress`` of the assessment from the given ``currentNode`` with the given
    /// ``branchResult``. If `null` then progress should not be shown for this ``currentNode`` of assessment.
    func progress(currentNode: Node, branchResult: BranchNodeResult) -> Progress?

    /// Returns whether or not the ``Assessment`` is completed and ready for the results to be saved
    /// (uploaded to a server, for example). This method should *only* return true if all the results
    /// associated with this ``Assessment`` have been added to the result set.
    func isCompleted(currentNode: Node, branchResult: BranchNodeResult) -> Bool
}

/// The navigation point is a data packet that the ``Navigator`` can use to pass information about how to traverse
/// the assessment.  This class is defined as `open` to allow subclasses to include additional information that is
/// relevant to navigation that may not be included in the branch result.
open class NavigationPoint {
    
    /// The next node to move to in navigating the assessment.
    public let node: Node?
    
    /// The branch result is the result set at this level of navigation. This allows for explicit mutation or copying of a
    /// result into the form that is required by the assessment ``Navigator``.
    public let branchResult: BranchNodeResult
    
    /// The  direction in which to travel the path where the desired navigation may be to go back up the path rather
    /// than moving forward down the path. This can be important for an assessment where the participant is directed
    /// to redo a step and the animation should move backwards to show the user that this is what is happening.
    public let direction: PathMarker.Direction
    
    public init(node: Node?, branchResult: BranchNodeResult, direction: PathMarker.Direction) {
        self.node = node
        self.branchResult = branchResult
        self.direction = direction
    }
}

/// A marker used to indicate progress through the assessment. This includes the ``current`` step (zero indexed), the
/// ``total`` number of steps, and whether or not this progress ``isEstimated``. This can be used to display a progress
/// indicator to the participant.
public struct Progress {
    public let current: Int
    public let total: Int
    public let isEstimated: Bool
    
    public init(current: Int, total: Int, isEstimated: Bool) {
        self.current = current
        self.total = total
        self.isEstimated = isEstimated
    }
}
