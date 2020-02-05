package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.*

open class NodeNavigator(val node: NodeContainer) : Navigator {

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