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
    fun goForward(requestedPermissions: Set<Permission>? = null,
                  asyncActionNavigations: Set<AsyncActionNavigation>? = null)

    /**
     * Method to call when the participant taps the "Back" button or the active step gets a signal to go back to the
     * previous node in the navigation.
     */
    fun goBackward(requestedPermissions: Set<Permission>? = null,
                   asyncActionNavigations: Set<AsyncActionNavigation>? = null)
}

fun NodeState.goIn(direction: NavigationPoint.Direction,
                   requestedPermissions: Set<Permission>?,
                   asyncActionNavigations: Set<AsyncActionNavigation>?) {
    if (direction == NavigationPoint.Direction.Forward) {
        goForward(requestedPermissions, asyncActionNavigations)
    }
    else {
        goBackward(requestedPermissions, asyncActionNavigations)
    }
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
     * The current child that defines the current navigation state.
     */
    val currentChild: NodeState?

    /**
     * Override the [currentResult] to require return of a [BranchNodeResult]
     */
    override val currentResult: BranchNodeResult
}

open class StepNodeStateImpl(override val node: Node, override val parent: BranchNodeState) : NodeState {
    override var currentResult: Result
        get() {
            if (_currentResult == null) {
                _currentResult = node.createResult()
            }
            return _currentResult!!
        }
        set(value) { _currentResult = value }
    private var _currentResult: Result? = null

    override fun goForward(requestedPermissions: Set<Permission>?,
                           asyncActionNavigations: Set<AsyncActionNavigation>?) {
        parent.goForward(requestedPermissions, asyncActionNavigations)
    }

    override fun goBackward(requestedPermissions: Set<Permission>?,
                            asyncActionNavigations: Set<AsyncActionNavigation>?) {
        parent.goBackward(requestedPermissions, asyncActionNavigations)
    }
}

open class BranchNodeStateImpl(override val node: BranchNode, final override val parent: BranchNodeState? = null) : BranchNodeState {

    override var currentResult: BranchNodeResult = node.createResult()
    private val navigator: Navigator = node.getNavigator()

    override var currentChild: NodeState? = null
        protected set

    override var rootNodeController: RootNodeController?
        get() = parent?.rootNodeController ?: _rootNodeController
        set(value) { _rootNodeController = value }
    private var _rootNodeController: RootNodeController? = null

    override fun goForward(requestedPermissions: Set<Permission>?,
                           asyncActionNavigations: Set<AsyncActionNavigation>?) {
        val next = getNextNode()
        unionNavigationSets(next, requestedPermissions, asyncActionNavigations)
        if (next.node != null) {
            // Go to next node if it is not null.
            moveTo(next)
        } else {
            handleFinished(next)
        }
    }

    open fun moveTo(navigationPoint: NavigationPoint) {
        val controller = rootNodeController
        controller?.nodeStateFor(navigationPoint, this)?.let { nodeState ->
            // If the controller returns a node state then it is responsible for showing it. Just set the current
            // child and hand off control to the root node controller.
            currentChild = nodeState
            controller.show(nodeState, navigationPoint)
        } ?: run {
            // Otherwise, see if we have a node to move to.
            getBranchNodeState(navigationPoint)?.let { nodeState ->
                currentChild = nodeState
                nodeState.goIn(navigationPoint.direction, navigationPoint.requestedPermissions, navigationPoint.asyncActionNavigations)
            } ?: run {
                TODO("syoung 02/04/2020 Not implemented. Handle case where a step is skipped by the root controller.")
            }
        }
    }

    open fun handleFinished(navigationPoint: NavigationPoint) {
        if ((navigationPoint.direction == NavigationPoint.Direction.Exit) || (parent == null)) {
            rootNodeController?.handleFinished(navigationPoint, this)
        } else {
            parent.goIn(navigationPoint.direction, navigationPoint.requestedPermissions, navigationPoint.asyncActionNavigations)
        }
    }

    /**
     * Get the next node to show after the current node.
     */
    open fun getNextNode(): NavigationPoint {
        appendChildResultIfNeeded()
        val currentNode = currentChild?.node
        return if (currentNode == null) navigator.start() else navigator.nodeAfter(currentNode, currentResult)
    }

    /**
     * Look to see if the next node to move to is a navigation point that can return a branch node state. The base
     * class looks for conformance to the [NodeContainer] interface and the [Assessment] interface in that order.
     */
    open fun getBranchNodeState(navigationPoint: NavigationPoint): BranchNodeState? {
        return (navigationPoint.node as? BranchNode)?.let { BranchNodeStateImpl(it, this) }
    }

    /**
     * Union the requested permissions and async actions with the returned navigation point.
     */
    open fun unionNavigationSets(navigationPoint: NavigationPoint,
                            requestedPermissions: Set<Permission>?,
                            asyncActionNavigations: Set<AsyncActionNavigation>?) {
        if (requestedPermissions != null) {
            TODO("syoung 02/05/2020 Add unit test")
            navigationPoint.requestedPermissions = requestedPermissions.plus(navigationPoint.requestedPermissions ?: setOf())
        }
        if (asyncActionNavigations != null) {
            TODO("syoung 02/05/2020 Add unit test")
            navigationPoint.asyncActionNavigations = asyncActionNavigations.union(navigationPoint.asyncActionNavigations ?: setOf())
        }
    }

    /**
     * Add the current child result to the end of the path results for this [currentResult]. If the last result has the
     * same identifier as the current child result then this should *not* append. The assumption is that the controller
     * has added the *desired* result but not edited the current child result.
     */
    open fun appendChildResultIfNeeded() {
        currentChild?.currentResult?.let { childResult ->
            if (currentResult.pathHistoryResults.lastOrNull()?.let { it.identifier != childResult.identifier } != false) {
                currentResult.pathHistoryResults.add(childResult)
            }
        }
    }

    override fun goBackward(requestedPermissions: Set<Permission>?,
                            asyncActionNavigations: Set<AsyncActionNavigation>?) {
        TODO("syoung 02/04/2020 Not implemented.")
    }
}