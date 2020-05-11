package org.sagebionetworks.assessmentmodel.navigation

import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.*

/**
 * The [Navigator] is used by the assessment view controller or fragment to determine the order of presentation of
 * the [Step] elements in an assessment as well as when to start/stop the asynchronous background actions defined by
 * the [AsyncActionConfiguration] elements. The navigator is responsible for determining navigation based on the input
 * model, results, and platform context.
 *
 * The most common implementation of a [Navigator] will include a list of child nodes and rules for navigating the list.
 * However, the [Navigator] is defined more generally to allow for custom navigation that may not use a list of nodes.
 * For example, data tracking assessments such as medication tracking do not neatly conform to sequential navigation and
 * as such, use a different set of rules to navigate the assessment.
 */
interface Navigator {

    /**
     * Returns the [Node] associated with the given [identifier], if any. This is the [identifier] for the [Node] that
     * is local to this level of the node tree.
     */
    fun node(identifier: String): Node?

    /**
     * Continue to the next node after the current node. If [currentNode] is null, then the navigation is moving
     * forward into this section or assessment.
     */
    fun nodeAfter(currentNode: Node?, branchResult: BranchNodeResult): NavigationPoint

    /**
     * The node to move *back* to if the participant taps the back button. If [currentNode] is null, then the
     * navigation is moving back into this section or assessment.
     */
    fun nodeBefore(currentNode: Node?, branchResult: BranchNodeResult): NavigationPoint

    /**
     * Should the controller display a "Next" button or is the given button the last one in the assessment in which case
     * the button to end the assessment should say "Done"?
     */
    fun hasNodeAfter(currentNode: Node, branchResult: BranchNodeResult): Boolean

    /**
     * Is backward navigation allowed from this [currentNode] with the current [branchResult]?
     */
    fun allowBackNavigation(currentNode: Node, branchResult: BranchNodeResult): Boolean

    /**
     * Returns the [Progress] of the assessment from the given [currentNode] with the given [branchResult]. If `null`
     * then progress should not be shown for this [currentNode] of assessment.
     */
    fun progress(currentNode: Node, branchResult: BranchNodeResult): Progress?
}

/**
 * The [NavigationPoint] is a tuple that allows the [Navigator] to return additional information about how to traverse
 * the assessment.
 *
 * The [node] is the next node to move to in navigating the assessment.
 *
 * The [direction] returns the direction in which to travel the path where the desired navigation may be to go back up
 * the path rather than moving forward down the path. This can be important for an assessment where the participant is
 * directed to redo a step and the animation should move backwards to show the user that this is what is happening.
 *
 * The [branchResult] is the result set at this level of navigation. This allows for explicit mutation or copying of a
 * result into the form that is required by the assessment [Navigator].
 *
 * The [requestedPermissions] are the permissions to request *before* transitioning to the next node. Typically, these
 * are permissions that are required to run an async action.
 */
data class NavigationPoint(val node: Node?,
                           val branchResult: BranchNodeResult,
                           val direction: Direction = Direction.Forward,
                           var requestedPermissions: Set<PermissionInfo>? = null,
                           var asyncActionNavigations: Set<AsyncActionNavigation>? = null) {
    @Serializable
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
 * A container for the async actions to start/stop and any permissions to request.
 *
 * The [sectionIdentifier] is an optional identifier to allow for repeating a section with async actions on that
 * section. This is an identifier that can be used organizationally to differentiate between recorders within an
 * assessment. How it is used is up to the assessment developer.
 *
 * The [startAsyncActions] lists the async actions to start *after* transitioning to the next node.
 *
 * The [stopAsyncActions] lists the async actions to stop *before* transitioning to the next node.
 */
data class AsyncActionNavigation(val sectionIdentifier: String? = null,
                                 val startAsyncActions: Set<AsyncActionConfiguration>? = null,
                                 val stopAsyncActions: Set<AsyncActionConfiguration>? = null) {
    fun isEmpty(): Boolean
            = ((startAsyncActions == null) && (stopAsyncActions == null))
}

/**
 * Merge the two sets together. The start actions are the previous start actions plus the new start actions minus the
 * new stop actions (don't start it if the later navigation stops it). The stop actions are the previous stop actions
 * plus the new stop actions minus the new start actions (don't stop it if the later navigation starts it).
 */
fun Set<AsyncActionNavigation>.union(values: Set<AsyncActionNavigation>): Set<AsyncActionNavigation> {
    val ret = this.toMutableSet()
    values.filter { !it.isEmpty() }.forEach { value ->
        val existing = this.firstOrNull { it.sectionIdentifier == value.sectionIdentifier }
        if (existing == null) {
            ret.add(value)
        } else {
            val existingStart = existing.startAsyncActions ?: setOf()
            val existingStop = existing.stopAsyncActions ?: setOf()
            val valueStart = value.startAsyncActions ?: setOf()
            val valueStop = value.stopAsyncActions ?: setOf()
            val start = existingStart.plus(valueStart).minus(valueStop)
            val stop = existingStop.plus(valueStop).minus(valueStart)
            ret.add(AsyncActionNavigation(value.sectionIdentifier,
                    if (start.count() > 0) start else null,
                    if (stop.count() > 0) stop else null))
        }
    }
    return ret
}

/**
 * A marker used to indicate progress through the assessment. This includes the [current] step (zero indexed), the
 * [total] number of steps, and whether or not this progress [isEstimated]. This can be used to display a progress
 * indicator to the participant.
 */
data class Progress(val current: Int, val total: Int, val isEstimated: Boolean)