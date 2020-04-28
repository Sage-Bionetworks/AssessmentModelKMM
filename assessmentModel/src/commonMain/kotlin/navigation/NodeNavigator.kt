package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.survey.SurveyRule

/**
 * The navigation rule is used to allow the [NodeNavigator] to check if a node has a navigation rule and apply as
 * necessary.
 */
interface NavigationRule {

    /**
     * Identifier for the next step to navigate to based on the current task result and the conditional rule associated
     * with this task. The [branchResult] is the current result for the associated [NodeNavigator]. The variable
     * [isPeeking] equals `true` if this is used in a call to [NodeNavigator.hasNodeAfter] and equals `false` in the
     * call to [NodeNavigator.nodeAfter].
     */
    fun nextNodeIdentifier(branchResult: BranchNodeResult, isPeeking: Boolean) : String?
}

interface DirectNavigationRule : NavigationRule {
    /**
     * The next node to jump to. This is used where direct navigation is required. For example, to allow the
     * task to display information or a question on an alternate path and then exit the task. In that case,
     * the main branch of navigation will need to "jump" over the alternate path step and the alternate path
     * step will need to "jump" to the "exit".
     */
    val nextNodeIdentifier: String?

    override fun nextNodeIdentifier(branchResult: BranchNodeResult, isPeeking: Boolean): String?
            = if (!isPeeking) nextNodeIdentifier else null
}

interface SurveyNavigationRule : DirectNavigationRule, ResultMapElement {

    val surveyRules: List<SurveyRule>?

    override fun nextNodeIdentifier(branchResult: BranchNodeResult, isPeeking: Boolean): String? {
        val nextIdentifier = super.nextNodeIdentifier(branchResult, isPeeking)
        return if (nextIdentifier != null || isPeeking) {
            nextIdentifier
        } else {
            val result = branchResult.pathHistoryResults.lastOrNull { it.identifier == this.resultId() }
            surveyRules?.let { rules ->
                rules.mapNotNull { it.evaluateRuleWith(result) }.firstOrNull() }
        }
    }
}

open class NodeNavigator(val node: NodeContainer) : Navigator {

    override fun node(identifier: String): Node?
        = node.children.firstOrNull { it.identifier == identifier }

    override fun nodeAfter(currentNode: Node?, branchResult: BranchNodeResult): NavigationPoint {
        println("nodeAfter: ${branchResult.path.map { "${it.identifier}.${it.direction.name}" }}")
        val next = nextNode(currentNode, branchResult, false)
        val direction = NavigationPoint.Direction.Forward
        return NavigationPoint(node = next, branchResult = branchResult, direction = direction)
    }

    override fun nodeBefore(currentNode: Node?, branchResult: BranchNodeResult): NavigationPoint {
        println("nodeBefore: ${branchResult.path.map { "${it.identifier}.${it.direction.name}" }}")
        val previous = previousNode(currentNode, branchResult)
        val direction = NavigationPoint.Direction.Backward
        return NavigationPoint(node = previous, branchResult = branchResult, direction = direction)
    }

    override fun hasNodeAfter(currentNode: Node, branchResult: BranchNodeResult): Boolean {
        return nextNode(currentNode, branchResult, true) != null
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

    // MARK: Node navigation

    /**
     * The navigation rule (if any) associated with this node.
     */
    open fun navigationRuleFor(node: Node) : NavigationRule? = (node as? NavigationRule)

    private val children
        get() = node.children

    private fun nextNode(currentNode: Node?, parentResult: BranchNodeResult, isPeeking: Boolean): Node? {
        val nextIdentifier = currentNode?.let { navigationRuleFor(it) }?.nextNodeIdentifier(parentResult, isPeeking)
        return if (nextIdentifier != null) {
            node(nextIdentifier)
        } else if (currentNode == null) {
            children.firstOrNull()
        } else {
            val idx = children.indexOf(currentNode)
            if ((idx >= 0) && (idx + 1 < children.count())) children[idx + 1] else null
        }
    }

    private fun previousNode(currentNode: Node?, parentResult: BranchNodeResult): Node? {
        return when {
            currentNode == null -> {
                parentResult.pathHistoryResults.lastOrNull()?.let { marker ->
                    children.lastOrNull { it.resultId() == marker.identifier }
                } ?: children.firstOrNull()
            }
            parentResult.path.count() == 0 -> {
                val idx = children.indexOf(currentNode)
                if (idx - 1 >= 0) children[idx - 1] else null
            }
            else -> {
                var currentResultIndex = parentResult.path.indexOfLast {
                    it.identifier == currentNode.identifier && it.direction == NavigationPoint.Direction.Forward
                }
                if (currentResultIndex <= 0) null else {
                    val resultBefore = parentResult.path[currentResultIndex - 1]
                    children.lastOrNull { it.identifier == resultBefore.identifier }
                }
            }
        }
    }
}