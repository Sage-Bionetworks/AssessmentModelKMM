package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.BranchNode
import org.sagebionetworks.assessmentmodel.Node

/**
 * The [CustomNodeStateProvider] allows [assessment] developers to specify their own
 * [NodeState] implementations to be used when their [assessment] is loaded and run.
 */
interface CustomNodeStateProvider {

    /**
     * Is there a custom [BranchNodeState] for the given [branchNode]? This can be used to return a custom implementation for
     * an [assessment].
     */
    fun customBranchNodeStateFor(node: BranchNode, parent: BranchNodeState?): BranchNodeState? = null

    /**
     * Is there a custom [NodeState] for the given [node]? This can be used to return a custom implementation such as
     * a node state that blocks forward progress until a login service call is returned.
     */
    fun customLeafNodeStateFor(node: Node, parent: BranchNodeState): NodeState? = null

}