package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.survey.FormStepStateImpl
import org.sagebionetworks.assessmentmodel.survey.Question
import org.sagebionetworks.assessmentmodel.survey.QuestionStateImpl

/**
 * The [NodeUIController] is a platform-specific implementation of the UI that is described by the [Assessment] model.
 */
interface NodeUIController {
    /**
     * Does this controller know how to handle the given node?
     *
     * A node may be used to define UI that is not applicable on all platforms. For example, showing a section of a
     * survey on the same screen on a tablet, but across a sequence of screens on a phone where the available space
     * is limited.
     */
    fun canHandle(node: Node): Boolean

    /**
     * Is there a custom [NodeState] for the given [node]? This can be used to return a custom implementation such as
     * a node state that blocks forward progress until a login service call is returned.
     */
    fun customNodeStateFor(node: Node, parent: BranchNodeState): NodeState? = null

    /**
     * Handle going forward to the given [nodeState] with appropriate UI, View, and animations.
     */
    fun handleGoForward(nodeState: NodeState,
                        requestedPermissions: Set<PermissionInfo>? = null,
                        asyncActionNavigations: Set<AsyncActionNavigation>? = null)

    /**
     * Handle going back to the given [nodeState] with appropriate UI, View, and animations.
     */
    fun handleGoBack(nodeState: NodeState,
                     requestedPermissions: Set<PermissionInfo>? = null,
                     asyncActionNavigations: Set<AsyncActionNavigation>? = null)
}

/**
 * The [RootNodeController] is a platform-specific implementation of the UI that is described by the [Assessment] model.
 */
interface RootNodeController {

    /**
     * Handle finishing the [Assessment]. Save state and dismiss the view.
     */
    fun handleFinished(reason: FinishedReason, nodeState: NodeState, error: Error? = null)

    /**
     * Handle saving the results. Typically, this will mean uploading the results to a server.
     * When the [Assessment] finishes normally, this method will be called *before* [handleFinished].
     *
     * *Before* saving the results, the controller should check that any background recorders have
     * been stopped and their results have been added to the result set. Because this can often take
     * time and require hand-off between background and UI threads, this is best handled by the
     * controller rather than the state machine that is managing navigation. Practically speaking,
     * managing UI/UX around sensors is platform-specific and attempting to have the [NodeState]
     * manage this tends to lead to obfuscation and bugs. syoung 11/24/2020
     *
     */
    fun handleReadyToSave(reason: FinishedReason, nodeState: NodeState)
}

enum class FinishedReason {
    Complete, Error, EarlyExit, Discarded, SaveProgress
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
     * Method to call when the participant taps the "Next" button or a timed step is completed.
     */
    fun goForward(requestedPermissions: Set<PermissionInfo>? = null,
                  asyncActionNavigations: Set<AsyncActionNavigation>? = null)

    /**
     * Method to call when the participant taps the "Back" button or the active step gets a signal to go back to the
     * previous node in the navigation.
     */
    fun goBackward(requestedPermissions: Set<PermissionInfo>? = null,
                   asyncActionNavigations: Set<AsyncActionNavigation>? = null)

}

fun NodeState.goIn(direction: NavigationPoint.Direction,
                   requestedPermissions: Set<PermissionInfo>? = null,
                   asyncActionNavigations: Set<AsyncActionNavigation>? = null) {
    if (direction == NavigationPoint.Direction.Forward) {
        goForward(requestedPermissions, asyncActionNavigations)
    }
    else {
        goBackward(requestedPermissions, asyncActionNavigations)
    }
}

fun NodeState.root() :  NodeState {
    var thisPath: NodeState = this
    while (thisPath.parent != null) {
        thisPath = thisPath.parent!!
    }
    return thisPath
}

fun NodeState.previousResult() : Result?
        = parent?.currentResult?.pathHistoryResults?.lastOrNull { it.identifier == node.resultId() }?.copyResult()

fun BranchNodeState.lowestBranch() : BranchNodeState {
    var thisPath: BranchNodeState = this
    while (thisPath.currentChild != null && thisPath.currentChild is BranchNodeState) {
        thisPath = thisPath.currentChild!! as BranchNodeState
    }
    return thisPath
}

interface BranchNodeState : NodeState {

    var customBranchNodeStateProvider: CustomBranchNodeStateProvider?

    var nodeUIController: NodeUIController?

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

    /**
     * Called from the child node to send the call back up the chain to the parent.
     *
     * WARNING: This method should *only* be called by the [currentChild].
     */
    fun moveToNextNode(direction: NavigationPoint.Direction,
                       requestedPermissions: Set<PermissionInfo>? = null,
                       asyncActionNavigations: Set<AsyncActionNavigation>? = null)

    /**
     * Allow for chaining up to the top node state when the navigation should end early.
     *
     * WARNING: This method should *only* be called by the [currentChild].
     */
    fun exitEarly(asyncActionNavigations: Set<AsyncActionNavigation>?)

    open fun finish(navigationPoint: NavigationPoint)
}

interface LeafNodeState : NodeState {
    override val parent: BranchNodeState

    override fun goForward(requestedPermissions: Set<PermissionInfo>?,
                           asyncActionNavigations: Set<AsyncActionNavigation>?) {
        parent.moveToNextNode(NavigationPoint.Direction.Forward, requestedPermissions, asyncActionNavigations)
    }

    override fun goBackward(requestedPermissions: Set<PermissionInfo>?,
                            asyncActionNavigations: Set<AsyncActionNavigation>?) {
        parent.moveToNextNode(NavigationPoint.Direction.Backward, requestedPermissions, asyncActionNavigations)
    }
}

class LeafNodeStateImpl(override val node: Node, override val parent: BranchNodeState) : LeafNodeState {
    override val currentResult: Result = node.createResult()
}

open class BranchNodeStateImpl(override val node: BranchNode, final override val parent: BranchNodeState? = null) : BranchNodeState {
    private val navigator: Navigator by lazy { node.getNavigator(this) }

    override val currentResult: BranchNodeResult by lazy {
        // If this node has previously been shown, use that to determine the current state.
        previousResult() as? BranchNodeResult ?: this.node.createResult()
    }

    override var currentChild: NodeState? = null
        protected set

    override var rootNodeController: RootNodeController?
        get() = parent?.rootNodeController ?: _rootNodeController
        set(value) { _rootNodeController = value }
    private var _rootNodeController: RootNodeController? = null

    override var nodeUIController: NodeUIController?
        //If we have a NodeUIController use it, otherwise defer to our parent.
        get() =  _nodeUiController?: parent?.nodeUIController
        set(value) { _nodeUiController = value }
    private var _nodeUiController: NodeUIController? = null

    override var customBranchNodeStateProvider: CustomBranchNodeStateProvider?
        get() = parent?.customBranchNodeStateProvider?: _customBranchNodeStateProvider
        set(value) {_customBranchNodeStateProvider = value}
    private var _customBranchNodeStateProvider: CustomBranchNodeStateProvider? = null

    override fun goForward(requestedPermissions: Set<PermissionInfo>?,
                           asyncActionNavigations: Set<AsyncActionNavigation>?) {
        lowestBranch().moveToNextNode(NavigationPoint.Direction.Forward, requestedPermissions, asyncActionNavigations)
    }

    override fun goBackward(requestedPermissions: Set<PermissionInfo>?,
                            asyncActionNavigations: Set<AsyncActionNavigation>?) {
        lowestBranch().moveToNextNode(NavigationPoint.Direction.Backward, requestedPermissions, asyncActionNavigations)
    }

    override fun moveToNextNode(direction: NavigationPoint.Direction,
                                requestedPermissions: Set<PermissionInfo>?,
                                asyncActionNavigations: Set<AsyncActionNavigation>?) {
        val next = getNextNode(direction) ?: return
        unionNavigationSets(next, requestedPermissions, asyncActionNavigations)
        // Before moving to the next node (or ending the task), mark the end data for the current node.
        currentChild?.currentResult?.endDateString = DateGenerator.nowString()
        if (next.node != null) {
            // Go to next node if it is not null.
            moveTo(next)
        } else {
            finish(next)
        }
    }

    /**
     * Move to the given node.
     *
     * Warning: If you override this method, you should still call through to super to allow the
     * base class to manage its internal navigation state.
     *
     * Throws: [NullPointerException] if the [NavigationPoint.node] or [rootNodeController] are null.
     */
    open fun moveTo(navigationPoint: NavigationPoint) {
        val controller = nodeUIController ?: throw NullPointerException("Unexpected null nodeUiController")
        val node = navigationPoint.node ?: throw NullPointerException("Unexpected null navigationPoint.node")
        val pathMarker = PathMarker(node.identifier, navigationPoint.direction)
        if (currentResult.path.lastOrNull() != pathMarker) {
            currentResult.path.add(pathMarker)
        }
        if (controller.canHandle(node)) {
            //controller should also have the option of handling a branch node and returning a custom BranchNodeState.
                //

            // If the controller can handle the node state then it is responsible for showing it. Just set the current
            // child and hand off control to the root node controller.
            getLeafNodeState(navigationPoint)?.let { nodeState ->
                currentChild = nodeState
                // Mark the start/end timestamps before displaying the node.
                nodeState.currentResult.startDateString = DateGenerator.nowString()
                nodeState.currentResult.endDateString = null
                // Check if the "ready-to-save" state should be changed.
                if (navigator.isCompleted(nodeState.node, currentResult)) {
                    callUpReadyToSaveChain()
                }
                if (navigationPoint.direction == NavigationPoint.Direction.Forward) {
                    controller.handleGoForward(nodeState, navigationPoint.requestedPermissions, navigationPoint.asyncActionNavigations)
                } else {
                    controller.handleGoBack(nodeState, navigationPoint.requestedPermissions, navigationPoint.asyncActionNavigations)
                }
            } ?: run {
                TODO("syoung 02/10/2020 Not implemented. Handle case where a step is skipped by the branch state.")
            }
        } else {
            // Otherwise, see if we have a node to move to.
            getBranchNodeState(navigationPoint)?.let { nodeState ->
                currentChild = nodeState
                nodeState.goIn(navigationPoint.direction, navigationPoint.requestedPermissions, navigationPoint.asyncActionNavigations)
            } ?: run {
                TODO("syoung 02/04/2020 Not implemented. Handle case where a step is skipped by the root controller.")
            }
        }
    }

    /**
     * Mark the final result end date timestamp for *this* level of the node state. Then check if
     * this is the root node state and if it is, handle calling "readyToSave" on the
     * [rootNodeController]. Otherwise, check to see if the parent node state is "done" with it's
     * navigation (ie. this node is the last in the chain) and if so, call up the chain recursively.
     *
     * Note: This is marked as private b/c I don't want developers to call it directly from their
     * subclass implementations. Because it is recursive *and* uses a flag to ensure that the
     * "readyToSave" message is only called once by the root, it is brittle if called in a way that
     * is not how I intend for it to be used. If you break the chain by *not* using a subclass of
     * this implementation of [BranchNodeState] or by overriding one of the methods that calls this
     * and not calling through to super, then it is up to the developer who subclasses this to
     * handle calling the "readyToSave" method to ensure that the [rootNodeController] cleans up
     * and uploads the results. ie. There is no Kotlin equivalent to "fileprivate". syoung 11/24/2020
     */
    private fun callUpReadyToSaveChain() {
        appendChildResultIfNeeded()
        markFinalResultIfNeeded()
        if (parent == null) {
            callReadyToSaveIfNeeded(FinishedReason.Complete)
        } else if ((parent is BranchNodeStateImpl) &&
            !parent.navigator.hasNodeAfter(this.node, parent.currentResult)) {
            parent.callUpReadyToSaveChain()
        }
    }

    /**
     * Call the [rootNodeController.handleReadyToSave()] method.
     *
     * Note: This method is marked as private because it uses a flag to ensure that the "readyToSave"
     * message is only sent *once*. Additionally, it should only be called by the top-level node
     * state. This makes it a bit brittle and so I don't want it called outside of this code file.
     * syoung 11/24/2020
     */
    private fun callReadyToSaveIfNeeded(reason: FinishedReason) {
        if (parent != null) throw Exception("Invalid assumption. This method should only be called on the root node.")
        if (hasCalledReadyToSave) return
        this.hasCalledReadyToSave = true
        rootNodeController?.handleReadyToSave(reason, this)
    }
    private var hasCalledReadyToSave = false

    /**
     * Mark the result with the end timestamp.
     *
     * Note: This method is marked as private because it uses a flag to ensure that the end date
     * timestamp is only marked *once*. This makes it a bit brittle and so I don't want it called
     * outside of this code file. syoung 11/24/2020
     */
    private fun markFinalResultIfNeeded() {
        if (hasMarkedFinalResult) return
        this.hasMarkedFinalResult = true
        currentResult.endDateString = DateGenerator.nowString()
        didMarkFinalResult()
    }
    private var hasMarkedFinalResult = false

    protected open fun didMarkFinalResult() {
    }

    /**
     * Finish any navigation required at this level. This will check to see if the returned navigation point requires
     * exiting the entire run or just that this section is finished.
     *
     * Warning: If you override this method, you should still call through to super to allow the
     * base class to manage its internal navigation state.
     */
    override fun finish(navigationPoint: NavigationPoint) {
        // When finishing, mark the end date for the current result.
        markFinalResultIfNeeded()
        when {
            navigationPoint.direction == NavigationPoint.Direction.Exit ->
                exitEarly(navigationPoint.asyncActionNavigations)
            parent == null -> {
                callReadyToSaveIfNeeded(FinishedReason.Complete)
                rootNodeController?.handleFinished(FinishedReason.Complete, this)
            }
            else ->
                parent.moveToNextNode(navigationPoint.direction, navigationPoint.requestedPermissions, navigationPoint.asyncActionNavigations)
        }
    }

    /**
     * Exit the [Assessment] early.
     *
     * Warning: If you override this method, you should still call through to super to allow the
     * base class to manage its internal navigation state.
     */
    override fun exitEarly(asyncActionNavigations: Set<AsyncActionNavigation>?) {
        appendChildResultIfNeeded()
        markFinalResultIfNeeded()
        if (parent == null) {
            callReadyToSaveIfNeeded(FinishedReason.EarlyExit)
            rootNodeController?.handleFinished(FinishedReason.EarlyExit, this)
        } else {
            parent.exitEarly(asyncActionNavigations)
        }
    }

    /**
     * Get the next node to show after the current node.
     */
    open fun getNextNode(inDirection: NavigationPoint.Direction): NavigationPoint? {
        appendChildResultIfNeeded()
        val currentNode = currentChild?.node
        return if (inDirection == NavigationPoint.Direction.Forward) {
            navigator.nodeAfter(currentNode, currentResult)
        } else {
            navigator.nodeBefore(currentNode, currentResult)
        }
    }

    /**
     * Get the leaf node for the given [navigationPoint]. This method assumes that the [NavigationPoint.node] is not
     * null.
     */
    open fun getLeafNodeState(navigationPoint: NavigationPoint): NodeState? {
        val node = navigationPoint.node ?: throw NullPointerException("Unexpected null navigationPoint.node")
        return nodeUIController?.customNodeStateFor(node, this) ?: when (node) {
            is Question -> QuestionStateImpl(node, this)
            is FormStep -> FormStepStateImpl(node, this)
            else -> LeafNodeStateImpl(node, this)
        }
    }

    /**
     * Look to see if the next node to move to is a navigation point that can return a branch node state. The base
     * class looks for conformance to the [BranchNode] interface.
     */
    open fun getBranchNodeState(navigationPoint: NavigationPoint): BranchNodeState? {
        return (navigationPoint.node as? BranchNode)?.let {
            customBranchNodeStateProvider?.customNodeStateFor(it, this)?: BranchNodeStateImpl(it, this)
        }
    }

    /**
     * Union the requested permissions and async actions with the returned navigation point.
     */
    open fun unionNavigationSets(navigationPoint: NavigationPoint,
                                 requestedPermissions: Set<PermissionInfo>?,
                                 asyncActionNavigations: Set<AsyncActionNavigation>?) {
        if (requestedPermissions != null) {
            navigationPoint.requestedPermissions = requestedPermissions.plus(navigationPoint.requestedPermissions ?: setOf())
        }
        if (asyncActionNavigations != null) {
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
}