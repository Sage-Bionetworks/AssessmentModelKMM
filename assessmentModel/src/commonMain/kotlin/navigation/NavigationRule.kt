package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.BranchNodeResult
import org.sagebionetworks.assessmentmodel.Result
import org.sagebionetworks.assessmentmodel.ResultMapElement
import org.sagebionetworks.assessmentmodel.resultId
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
    fun nextNodeIdentifier(branchResult: BranchNodeResult, isPeeking: Boolean): String?
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

interface ResultNavigationRule : DirectNavigationRule, Result {
    override var nextNodeIdentifier: String?
}