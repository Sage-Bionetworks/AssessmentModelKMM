package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.BranchNode

interface CustomBranchNodeStateProvider {

    fun hasCustomNodeState(node: BranchNode): Boolean

    fun customNodeStateFor(node: BranchNode, parent: BranchNodeState?): BranchNodeState?

}