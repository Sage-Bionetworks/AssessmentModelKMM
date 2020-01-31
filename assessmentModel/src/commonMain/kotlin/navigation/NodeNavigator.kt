package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.*

open class NodeNavigator(val rootNode: NodeContainer) : Navigator {

    override fun node(identifier: String): Node?
        = rootNode.children.firstOrNull { it.identifier == identifier }

    override fun start(state: NodeState?): NavigationPoint {
        return NavigationPoint(
                node = rootNode.children.firstOrNull(),
                result = rootNode.createResult())
     }

    override fun runData(result: Result): Any? = null

    override fun nodeAfter(node: Node, result: Result): NavigationPoint {
        val next = nextNode(node, result)
        return NavigationPoint(node = next, result = result, direction = NavigationPoint.Direction.Forward)
    }

    override fun nodeBefore(node: Node, result: Result): NavigationPoint {
        val previous = previousNode(node, result)
        return NavigationPoint(node = previous, result = result, direction = NavigationPoint.Direction.Backward)
    }

    override fun hasNodeAfter(node: Node, result: Result): Boolean {
        return nextNode(node, result) != null
    }

    override fun allowBackNavigation(node: Node, result: Result): Boolean {
        return previousNode(node, result) != null
    }

    override fun progress(node: Node, result: Result): Progress? {
        val state = navigationLevel(node, result)
        val progressMarkers = state.rootNode.progressMarkers ?: return null
        val nodeIdx = state.nodeList.lastIndexOf(node)
        if (nodeIdx == -1) return null
        val nodeList = state.nodeList.subList(0, nodeIdx+1).map { it.identifier }
        val idx = progressMarkers.indexOfLast { nodeList.contains(it) }

        // If the nearest marker before the current node wasn't found return null.
        if (idx == -1) return null
        // If the current node is *after* the last marker return null.
        if ((idx+1 == progressMarkers.count()) && (progressMarkers.last() != node.identifier)) return null
        // Else return the progress.
        return Progress(
                current = idx,
                total = progressMarkers.count(),
                isEstimated = (state.nodeList.count() != state.rootNode.children.count()))
    }

    /**
     * syoung 01/30/2020
     * The private methods below are perhaps overkill right now, but the intention is to eventually add in more
     * complex navigation which will require filtering the node children and possibly handling node navigation within
     * this navigator rather than using a full state handler.
     */

    private data class NavigationLevel(val nodeList: List<Node>, val rootNode: NodeContainer)
    private fun navigationLevel(node: Node, result: Result): NavigationLevel {
        return NavigationLevel(rootNode.children, rootNode)
    }

    private fun nextNode(node: Node, result: Result): Node? {
        val children = navigationLevel(node, result).nodeList
        val idx = children.indexOf(node)
        return if ((idx >= 0) && (idx + 1 < children.count())) children[idx+1] else null
    }

    private fun previousNode(node: Node, result: Result): Node? {
        val children = navigationLevel(node, result).nodeList
        val idx = children.indexOf(node)
        return if (idx-1>=0) children[idx-1] else null
    }
}