package org.sagebionetworks.assessmentmodel.navigation

import kotlinx.datetime.Clock
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
     * Handle going forward to the given [nodeState] with appropriate UI, View, and animations.
     */
    fun handleGoForward(nodeState: NodeState)

    /**
     * Handle going back to the given [nodeState] with appropriate UI, View, and animations.
     */
    fun handleGoBack(nodeState: NodeState)
}

/**
 * The [RootNodeController] is a platform-specific implementation of the save and finish logic
 * associated with running the [Assessment] model.
 */
interface RootNodeController {

    /**
     * Handle finishing the [Assessment]. Save state and dismiss the view.
     */
    fun handleFinished(reason: FinishedReason, nodeState: NodeState)

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

enum class SaveResults {
    /**
     * Save and upload results immediately.
     */
    Now,

    /**
     * Wait until scheduled session has expired before uploading results. This allows the participant
     * to come back and finish the assessment.
     */
    WhenSessionExpires,

    /**
     * Discard any results.
     */
    Never
}

sealed class FinishedReason( val saveResult: SaveResults, val markFinished: Boolean, val declined: Boolean) {
    object Complete : FinishedReason(SaveResults.Now, true, declined = false)
    data class Failed(val error: Error) : FinishedReason(SaveResults.Never, false, declined = false)
    class Incomplete(saveResult: SaveResults, markFinished: Boolean, declined: Boolean) : FinishedReason(saveResult, markFinished, declined)
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
    fun goForward()

    /**
     * Method to call when the participant taps the "Back" button or the active step gets a signal to go back to the
     * previous node in the navigation.
     */
    fun goBackward()

}

fun NodeState.goIn(direction: NavigationPoint.Direction) {
    if (direction == NavigationPoint.Direction.Forward) {
        goForward()
    }
    else {
        goBackward()
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

    /**
     * Used to optionally provide custom [NodeState] implementations for a custom [Node].
     */
    var customNodeStateProvider: CustomNodeStateProvider?

    /**
     * The controller for running the full flow of steps and nodes.
     */
    var nodeUIController: NodeUIController?

    /**
     * The controller for finishing and saving the full flow of steps and nodes.
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
    fun moveToNextNode(direction: NavigationPoint.Direction)

    /**
     * Allow for chaining up to the top node state when the navigation should end early.
     *
     * WARNING: This method should *only* be called by the [currentChild].
     */
    fun exitEarly(finishedReason: FinishedReason)

    /**
     * Returns the [Progress] of the assessment based on the [currentChild] and [currentResult]. If `null`
     * then progress should not be shown for this [currentChild] of assessment.
     */
    fun progress(): Progress?

    /**
     * Is backward navigation allowed from this [currentChild] with the current [currentResult]?
     */
    fun allowBackNavigation(): Boolean
}

interface LeafNodeState : NodeState {
    override val parent: BranchNodeState

    override fun goForward() {
        parent.moveToNextNode(NavigationPoint.Direction.Forward)
    }

    override fun goBackward() {
        parent.moveToNextNode(NavigationPoint.Direction.Backward)
    }
}

class LeafNodeStateImpl(override val node: Node, override val parent: BranchNodeState) : LeafNodeState {
    override val currentResult: Result = node.createResult()
}

open class BranchNodeStateImpl(
    override val node: BranchNode,
    final override val parent: BranchNodeState? = null,
    //Previous result should only be used for top level assessments where the parent is null.
    private val previousResult: BranchNodeResult? = null
) : BranchNodeState {
    private val navigator: Navigator by lazy { node.createNavigator(this) }

    override val currentResult: BranchNodeResult by lazy {
        // If this node has previously been shown, use that to determine the current state.
        previousResult ?: previousResult() as? BranchNodeResult ?: this.node.createResult()
    }

    override var currentChild: NodeState? = null
        protected set

    override var rootNodeController: RootNodeController?
        get() = parent?.rootNodeController ?: _rootNodeController
        set(value) { _rootNodeController = value }
    private var _rootNodeController: RootNodeController? = null

    override var nodeUIController: NodeUIController?
        //If we have a NodeUIController use it, otherwise defer to our parent.
        get() =  _nodeUiController ?: parent?.nodeUIController
        set(value) { _nodeUiController = value }
    private var _nodeUiController: NodeUIController? = null

    override var customNodeStateProvider: CustomNodeStateProvider?
        get() = parent?.customNodeStateProvider ?: _customNodeStateProvider
        set(value) {_customNodeStateProvider = value}
    private var _customNodeStateProvider: CustomNodeStateProvider? = null

    override fun goForward() {
        lowestBranch().moveToNextNode(NavigationPoint.Direction.Forward)
    }

    override fun goBackward() {
        lowestBranch().moveToNextNode(NavigationPoint.Direction.Backward)
    }

    override fun moveToNextNode(direction: NavigationPoint.Direction) {
        val next = getNextNode(direction) ?: return
        // Before moving to the next node (or ending the task), mark the end data for the current node.
        currentChild?.currentResult?.endDateTime = Clock.System.now()
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
            // If the controller can handle the node state then it is responsible for showing it. Just set the current
            // child and hand off control to the root node controller.
            getLeafNodeState(navigationPoint)?.let { nodeState ->
                currentChild = nodeState
                // Mark the start/end timestamps before displaying the node.
                nodeState.currentResult.startDateTime = Clock.System.now()
                nodeState.currentResult.endDateTime = null
                // Check if the "ready-to-save" state should be changed.
                if (navigator.isCompleted(nodeState.node, currentResult)) {
                    callUpReadyToSaveChain()
                }
                if (navigationPoint.direction == NavigationPoint.Direction.Forward) {
                    controller.handleGoForward(nodeState)
                } else {
                    controller.handleGoBack(nodeState)
                }
            } ?: run {
                TODO("syoung 02/10/2020 Not implemented. Handle case where a step is skipped by the branch state.")
            }
        } else {
            // Otherwise, see if we have a node to move to.
            getBranchNodeState(navigationPoint)?.let { nodeState ->
                currentChild = nodeState
                nodeState.goIn(navigationPoint.direction)
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
        currentResult.endDateTime = Clock.System.now()
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
    fun finish(navigationPoint: NavigationPoint) {
        // When finishing, mark the end date for the current result.
        markFinalResultIfNeeded()
        when {
            navigationPoint.direction == NavigationPoint.Direction.Exit ->
                exitEarly(FinishedReason.Incomplete(SaveResults.Never, markFinished = false, declined = false))
            parent == null -> {
                callReadyToSaveIfNeeded(FinishedReason.Complete)
                rootNodeController?.handleFinished(FinishedReason.Complete, this)
            }
            else ->
                parent.moveToNextNode(navigationPoint.direction)
        }
    }

    /**
     * Exit the [Assessment] early.
     *
     * Warning: If you override this method, you should still call through to super to allow the
     * base class to manage its internal navigation state.
     */
    override fun exitEarly(finishedReason: FinishedReason) {
        appendChildResultIfNeeded()
        markFinalResultIfNeeded()
        if (parent == null) {
            callReadyToSaveIfNeeded(finishedReason)
            rootNodeController?.handleFinished(finishedReason, this)
        } else {
            parent.exitEarly(finishedReason)
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
        return customNodeStateProvider?.customLeafNodeStateFor(node, this) ?: when (node) {
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
            customNodeStateProvider?.customBranchNodeStateFor(it, this, null) ?: BranchNodeStateImpl(it, this)
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

    /**
     * Returns the [Progress] of the assessment based on the [currentChild] and [currentResult]. If `null`
     * then progress should not be shown for this [currentChild] of assessment.
     */
    override fun progress(): Progress? {
        return currentChild?.node?.let {
            navigator.progress(it, currentResult)
        }
    }

    override fun allowBackNavigation(): Boolean {
        return currentChild?.node?.let {
            navigator.allowBackNavigation(it, currentResult)
        } == true
    }
}
