//
//  Navigator.swift
//  
//

import Foundation
import JsonModel
import ResultModel

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
    
    /// Returns the first node in the navigator or `nil` if navigating back to the start is not supported.
    func firstNode() -> Node?

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
    
    /// Can the assessment be "paused"?
    func canPauseAssessment(currentNode: Node, branchResult: BranchNodeResult) -> Bool

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
    
    /// The  direction in which to travel the path where the desired navigation may be to go back up the path rather
    /// than moving forward down the path. This can be important for an assessment where the participant is directed
    /// to redo a step and the animation should move backwards to show the user that this is what is happening.
    public let direction: PathMarker.Direction
    
    public init(node: Node?, direction: PathMarker.Direction) {
        self.node = node
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
