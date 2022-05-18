package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.BranchNode
import org.sagebionetworks.assessmentmodel.BranchNodeResult
import org.sagebionetworks.assessmentmodel.Node

/**
 * The [CustomNodeStateProvider] allows [assessment] developers to specify their own
 * [NodeState] implementations to be used when their [assessment] is loaded and run.
 */
interface CustomNodeStateProvider {

    /**
     * Is there a custom [BranchNodeState] for the given [node]? This can be used to return a custom implementation for
     * an [Assessment].
     */
    fun customBranchNodeStateFor(node: BranchNode, parent: BranchNodeState?, previousResult: BranchNodeResult?): BranchNodeState? = null

    /**
     * Is there a custom [NodeState] for the given [node]? This can be used to return a custom implementation such as
     * a node state that blocks forward progress until a login service call is returned.
     */
    fun customLeafNodeStateFor(node: Node, parent: BranchNodeState): NodeState? = null

}

/**
 * For apps with more than one [CustomNodeStateProvider], [RootCustomNodeStateProvider] can be used
 * to wrap them all into one.
 */
class RootCustomNodeStateProvider(val providers: List<CustomNodeStateProvider>) :
    CustomNodeStateProvider {

    override fun customBranchNodeStateFor(node: BranchNode, parent: BranchNodeState?, previousResult: BranchNodeResult?): BranchNodeState? {
        for (provider in providers) {
            val state = provider.customBranchNodeStateFor(node, parent, previousResult)
            if (state != null) {
                return state
            }
        }
        return null
    }

    override fun customLeafNodeStateFor(node: Node, parent: BranchNodeState): NodeState? {
        for (provider in providers) {
            val state = provider.customLeafNodeStateFor(node, parent)
            if (state != null) {
                return state
            }
        }
        return null
    }
}


