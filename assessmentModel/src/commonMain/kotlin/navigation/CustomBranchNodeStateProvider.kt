package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.BranchNode

/**
 * The [CustomBranchNodeStateProvider] allows [assessment] developers to specify their own
 * [BranchNodeState] implementation to be used when their [assessment] is loaded and run.
 */
interface CustomBranchNodeStateProvider {

    fun customNodeStateFor(node: BranchNode, parent: BranchNodeState?): BranchNodeState?

}