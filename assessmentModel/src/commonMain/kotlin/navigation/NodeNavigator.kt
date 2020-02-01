package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.*


open class NodeNavigator(val rootNode: NodeContainer) : Navigator {

    override fun node(identifier: String): Node?
        = rootNode.children.firstOrNull { it.identifier == identifier }

    override fun start(state: NodeState?): NavigationPoint {
        return NavigationPoint(
                node = rootNode.children.firstOrNull(),
                parentResult = rootNode.createResult())
     }

    override fun runData(parentResult: CollectionResult): Any? = null

    override fun nodeAfter(node: Node, parentResult: CollectionResult): NavigationPoint {
        val next = nextNode(node, parentResult)
        return NavigationPoint(node = next, parentResult = parentResult, direction = NavigationPoint.Direction.Forward)
    }

    override fun nodeBefore(currentNode: Node, parentResult: CollectionResult): NavigationPoint {
        val previous = previousNode(currentNode, parentResult)
        return NavigationPoint(node = previous, parentResult = parentResult, direction = NavigationPoint.Direction.Backward)
    }

    override fun hasNodeAfter(currentNode: Node, parentResult: CollectionResult): Boolean {
        return nextNode(currentNode, parentResult) != null
    }

    override fun allowBackNavigation(currentNode: Node, parentResult: CollectionResult): Boolean {
        return previousNode(currentNode, parentResult) != null
    }

    override fun progress(currentNode: Node, parentResult: CollectionResult): Progress? {
        val parentNode = rootNode
        val children = rootNode.children
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
        val children = rootNode.children
        val idx = rootNode.children.indexOf(currentNode)
        return if ((idx >= 0) && (idx + 1 < children.count())) children[idx+1] else null
    }

    private fun previousNode(currentNode: Node, parentResult: CollectionResult): Node? {
        val children = rootNode.children
        val idx = children.indexOf(currentNode)
        return if (idx-1>=0) children[idx-1] else null
    }
}