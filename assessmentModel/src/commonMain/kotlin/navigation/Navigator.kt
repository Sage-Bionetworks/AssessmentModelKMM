package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.*

/**
 * The [Navigator] is used by the assessment view controller or fragment to determine the order of presentation of
 * the [Step] elements in an assessment as well as when to start/stop the asynchronous background actions defined by
 * the [AsyncActionConfiguration] elements. The navigator is responsible for determining navigation based on the input
 * model, results, and platform context.
 *
 * The most common implementation of a [Navigator] will include a list of child nodes and rules for navigating the list.
 * However, the [Navigator] is defined more generally to allow for custom navigation that may not use a list of nodes.
 * For example, data tracking assessments such as medication tracking do not neatly conform to sequential navigation and as
 * such, use a different set of rules to navigate the assessment.
 *
 * The input model and platform context can be provided by the [state] parameter passed into the [start] method. Since
 * this parameter is optional and defines only a very simple interface, the [Navigator] may need to be paired with a
 * custom state handler in which case it should fail gracefully if the [NodeState] is null or not of the subclass that
 * is required by the navigator.
 */
interface Navigator {

    /**
     * Returns the [Node] associated with the given [identifier], if any. This is the [identifier] for the [Node] that
     * is local to this level of the node tree.
     */
    fun node(identifier: String): Node?

    /**
     * Start the assessment. This should return the first [NavigationPoint] for this assessment.
     */
    fun start(state: NodeState? = null): NavigationPoint

    /**
     * The data to store for the assessment run described by the given [result]. While this can be any object, the
     * navigator will need to return something that the application will know how to store.
     *
     * - Warning: If the [state] is retained by the navigator, the navigator is responsible for managing memory in a
     * way that is appropriate to the supported platforms.
     */
    fun runData(result: Result): Any?

    /**
     * Continue to the next node after the current node. This should return the next node (if any), the current
     * result state for the assessment, as well as the direction and any async actions that should be started or stopped.
     */
    fun nodeAfter(node: Node, result: Result): NavigationPoint

    /**
     * The node to move *back* to if the participant taps the back button.
     *
     * The [NavigationPoint.direction] and the [NavigationPoint.requestedPermissions] are ignored by the controller for
     * this return.
     */
    fun nodeBefore(node: Node, result: Result): NavigationPoint

    /**
     * Should the controller display a "Next" button or is the given button the last one in the assessment in which case the
     * button to end the assessment should say "Done"?
     */
    fun hasNodeAfter(node: Node, result: Result): Boolean

    /**
     * Is backward navigation allowed from this [node] with the current [result]?
     */
    fun allowBackNavigation(node: Node, result: Result): Boolean

    /**
     * Returns the [Progress] of the assessment from the given [node] with the given [result]. If [null] then progress
     * should not be shown for this [node] of assessment.
     */
    fun progress(node: Node, result: Result): Progress?
}

/**
 * The [NavigationPoint] is a tuple that allows the [Navigator] to return additional information about how to traverse
 * the assessment.
 *
 * The [node] is the next node to move to in navigating the assessment.
 *
 * The [direction] returns the direction in which to travel the path where the desired navigation may be to go back up
 * the path rather than moving forward down the path. This can be important for an assessment where the participant is directed to redo a step and the animation
 * should move backwards to show the user that this is what is happening.
 *
 * The [result] is the result set at this level of navigation. This allows for explicit mutation or copying of a result
 * into the form that is required by the assessment [Navigator].
 *
 * The [requestedPermissions] are the permissions to request *before* transitioning to the next node. Typically, these
 * are permissions that are required to run an async action.
 *
 * The [startAsyncActions] lists the async actions to start *after* transitioning to the next node.
 *
 * The [stopAsyncActions] lists the async actions to stop *before* transitioning to the next node.
 */
data class NavigationPoint(val node: Node?,
                           val result: Result,
                           val direction: Direction = Direction.Forward,
                           val requestedPermissions: List<Permission>? = null,
                           val startAsyncActions: List<AsyncActionConfiguration>? = null,
                           val stopAsyncActions: List<AsyncActionConfiguration>? = null) {
    enum class Direction {
        /**
         * Move forward through the assessment.
         */
        Forward,
        /**
         * Move backward through the assessment.
         */
        Backward,
        /**
         * Exit the assessment early. If this direction indicator is set, then the entire assessment run should end.
         */
        Exit,
        ;
    }
}

/**
 * A marker used to indicate progress through the assessment. This includes the [current] step (zero indexed), the
 * [total] number of steps, and whether or not this progress [isEstimated]. This can be used to display a progress
 * indicator to the participant.
 */
data class Progress(val current: Int, val total: Int, val isEstimated: Boolean)