package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.*

interface RootNodeController {
    fun nodeStateFor(navigationPoint: NavigationPoint, parent: BranchNodeState): NodeState?
    fun show(nodeState: NodeState, navigationPoint: NavigationPoint)
    fun handleFinished(navigationPoint: NavigationPoint, parent: BranchNodeState)
}

interface NodeState {

    /**
     * The [node] tied to *this* [NodeState]. For any given [NodeState], there is one and only one [Node] associated
     * with that state.
     */
    val node: Node

    /**
     * The [parent] (if any) for the node chain.
     */
    val parent: BranchNodeState?

    /**
     * The [Result] associated with [node] for this component in the node chain. This is the result that is added to the
     * path history. Since this is a pointer to an object, that object might be mutable.
     */
    val currentResult: Result

    /**
     * Method to call when the participant taps the "Next" button or a timed step is completed. The [navigationPoint]
     * carries information about the current state of the navigation.
     */
    fun goForwardWith(requestedPermissions: Set<Permission>? = null,
                      asyncActionNavigations: Set<AsyncActionNavigation>? = null)

    /**
     * Method to call when the participant taps the "Back" button or the active step gets a signal to go back to the
     * previous node in the navigation.
     */
    fun goBackwardWith(requestedPermissions: Set<Permission>? = null,
                       asyncActionNavigations: Set<AsyncActionNavigation>? = null)
}

interface BranchNodeState : NodeState {

    /**
     * The controller for running the full flow of steps and nodes.
     */
    var rootNodeController: RootNodeController?

    /**
     * Override the [node] to require return of a [BranchNode].
     */
    override val node: BranchNode

    /**
     * The current child that defines the current navigation point. This
     */
    val currentChild: NodeState?

    /**
     * Override the [currentResult] to require return of a [BranchNodeResult]
     */
    override val currentResult: BranchNodeResult
}
