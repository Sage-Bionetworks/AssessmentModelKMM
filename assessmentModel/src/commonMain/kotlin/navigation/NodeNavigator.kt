package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.*

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

abstract class BranchNodeStateImpl(final override val parent: BranchNodeState? = null) : BranchNodeState {

    abstract val navigator: Navigator

    override var currentResult: BranchNodeResult
        get() {
            if (_currentResult == null) {
                _currentResult = node.createResult()
            }
            return _currentResult!!
        }
        set(value) { _currentResult = value }
    private var _currentResult: BranchNodeResult? = null

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
        }
        else {
            handleFinished(next)
        }
    }

    fun moveTo(navigationPoint: NavigationPoint) {
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
                if (navigationPoint.direction == NavigationPoint.Direction.Forward) {
                    nodeState.goForward(navigationPoint.requestedPermissions, navigationPoint.asyncActionNavigations)
                }
                else {
                    nodeState.goBackward(navigationPoint.requestedPermissions, navigationPoint.asyncActionNavigations)
                }
            } ?: run {
                TODO("not implemented") // syoung 02/04/2020 Implement for the case where a step is skipped by the root controller.
            }
        }
    }

    fun handleFinished(navigationPoint: NavigationPoint) {
        if ((navigationPoint.direction == NavigationPoint.Direction.Exit) || (parent == null)) {
            rootNodeController?.handleFinished(navigationPoint, this)
        }
        else {
            if (navigationPoint.direction == NavigationPoint.Direction.Forward) {
                parent.goForward(navigationPoint.requestedPermissions, navigationPoint.asyncActionNavigations)
            }
            else {
                parent.goBackward(navigationPoint.requestedPermissions, navigationPoint.asyncActionNavigations)
            }
        }
    }

    /**
     * Get the next node to show after the current node.
     */
    fun getNextNode(): NavigationPoint {
        appendChildResultIfNeeded()
        val currentNode = currentChild?.node
        return if (currentNode == null) navigator.start() else navigator.nodeAfter(currentNode, currentResult)
    }

    /**
     * Look to see if the next node to move to is a navigation point that can return a branch node state. The base
     * class looks for conformance to the [NodeContainer] interface and the [Assessment] interface in that order.
     */
    fun getBranchNodeState(navigationPoint: NavigationPoint): BranchNodeState? {
        return when (navigationPoint.node) {
            is NodeContainer -> NodeNavigator(navigationPoint.node, this)
            is Assessment -> AssessmentNavigator(navigationPoint.node, this)
            else -> null
        }
    }

    /**
     * Union the requested permissions and async actions with the returned navigation point.
     */
    fun unionNavigationSets(navigationPoint: NavigationPoint,
                  requestedPermissions: Set<Permission>?,
                  asyncActionNavigations: Set<AsyncActionNavigation>?) {
        if (requestedPermissions != null) {
            navigationPoint.requestedPermissions = requestedPermissions.plus(navigationPoint.requestedPermissions ?: setOf())
        }
        if (asyncActionNavigations != null) {
            navigationPoint.asyncActionNavigations = asyncActionNavigations.union(navigationPoint.asyncActionNavigations ?: setOf())
        }
    }

    /**
     * Add the current child result to the end of the path results for this [currentResult]. If the last result in the
     * path history at this level has the same result identifier as the current child result, then that last result is
     * *replaced* with the new current child result.
     */
    fun appendChildResultIfNeeded() {
        val childResult = currentChild?.currentResult
        childResult?.let {
            val lastResult = currentResult.pathHistoryResults.lastOrNull()
            if ((lastResult == null) || (lastResult.identifier != childResult.identifier)) {
                currentResult.pathHistoryResults.add(childResult)
            }
            else if (lastResult == childResult) {
                // Do nothing
            }
            else {
                // Replace the last result with the new last result
                currentResult.pathHistoryResults.remove(lastResult)
                currentResult.pathHistoryResults.add(childResult)
            }
        }
    }

    override fun goBackward(requestedPermissions: Set<Permission>?,
                            asyncActionNavigations: Set<AsyncActionNavigation>?) {
        TODO("not implemented") // syoung 02/04/2020
    }
}

open class AssessmentNavigator(override val node: Assessment, parent: BranchNodeState? = null) : BranchNodeStateImpl(parent) {
    override val navigator: Navigator
        get() = node.navigator!!
}

open class NodeNavigator(override val node: NodeContainer, parent: BranchNodeState? = null) : BranchNodeStateImpl(parent), Navigator {
    override val navigator: Navigator
        get() = this

    override fun node(identifier: String): Node?
        = node.children.firstOrNull { it.identifier == identifier }

    override fun start(): NavigationPoint {
        return NavigationPoint(
                node = node.children.firstOrNull(),
                branchResult = node.createResult())
     }

    override fun nodeAfter(currentNode: Node, branchResult: BranchNodeResult): NavigationPoint {
        val next = nextNode(currentNode, branchResult)
        return NavigationPoint(node = next, branchResult = branchResult, direction = NavigationPoint.Direction.Forward)
    }

    override fun nodeBefore(currentNode: Node, branchResult: BranchNodeResult): NavigationPoint {
        val previous = previousNode(currentNode, branchResult)
        return NavigationPoint(node = previous, branchResult = branchResult, direction = NavigationPoint.Direction.Backward)
    }

    override fun hasNodeAfter(currentNode: Node, branchResult: BranchNodeResult): Boolean {
        return nextNode(currentNode, branchResult) != null
    }

    override fun allowBackNavigation(currentNode: Node, branchResult: BranchNodeResult): Boolean {
        return previousNode(currentNode, branchResult) != null
    }

    override fun progress(currentNode: Node, branchResult: BranchNodeResult): Progress? {
        val parentNode = node
        val children = node.children
        val isEstimated = false

        val progressMarkers = parentNode.progressMarkers ?: return null
        val nodeIdx = children.lastIndexOf(currentNode)
        if (nodeIdx == -1) return null
        val nodeList = children.subList(0, nodeIdx+1).map { it.identifier }
        val idx = progressMarkers.indexOfLast { nodeList.contains(it) }

        // If the nearest marker before the current node wasn't found return null.
        if (idx == -1) return null
        // If the current node is *after* the last marker return null.
        if ((idx+1 == progressMarkers.count()) && (progressMarkers.last() != currentNode.identifier)) return null
        // Else return the progress.
        return Progress(
                current = idx,
                total = progressMarkers.count(),
                isEstimated = isEstimated)
    }

    /**
     * syoung 01/30/2020 WIP for stubbing out more complex navigation.
     */

    private fun nextNode(currentNode: Node, parentResult: CollectionResult): Node? {
        val children = node.children
        val idx = node.children.indexOf(currentNode)
        return if ((idx >= 0) && (idx + 1 < children.count())) children[idx+1] else null
    }

    private fun previousNode(currentNode: Node, parentResult: CollectionResult): Node? {
        val children = node.children
        val idx = children.indexOf(currentNode)
        return if (idx-1>=0) children[idx-1] else null
    }
}