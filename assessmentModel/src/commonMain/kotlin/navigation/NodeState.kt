package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.Node
import org.sagebionetworks.assessmentmodel.Result
import org.sagebionetworks.assessmentmodel.CollectionResult

interface RootNodeController {
}

interface NodeState {

    /**
     * The [node] tied to this [NodeState].
     */
    val node: Node

    /**
     * The [parent] (if any) for the node chain.
     */
    val parent: NodeState?

    /**
     * The [Result] associated with [node] for this component in the node chain. This is the result that is added to the
     * path history
     */
    val currentResult: Result

    /**
     * Can this task go forward? If forward navigation is enabled, then the task isn't waiting for a result or a task
     * fetch to enable forward navigation.
     */
    val isForwardEnabled : Boolean

    /**
     * Can the path navigate backward up the chain? This property should be set to [false] if the backwards navigation
     * is blocked by this path component or its child path component.
     */
    val canNavigateBackward : Boolean

    fun goForward()
    fun goBackward()
}

interface ParentNodeState : NodeState {

    /**
     * The current child that defines the current navigation point. This
     */
    val currentChild: NodeState?

    /**
     * A parent node can have child nodes associated with it. The result will therefore always be a collection result.
     */
    val collectionResult: CollectionResult

}
