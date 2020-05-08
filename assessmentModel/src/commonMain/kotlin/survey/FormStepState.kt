package org.sagebionetworks.assessmentmodel.survey

import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.BranchNodeState
import org.sagebionetworks.assessmentmodel.navigation.LeafNodeState
import org.sagebionetworks.assessmentmodel.navigation.previousResult

interface FormStepState : LeafNodeState {
    override val node: FormStep
    override val currentResult: CollectionResult

    val fieldStates: List<FieldState>
}

interface FieldState {
    val index: Int
    val node: Node
    val currentResult: Result
}

open class FormStepStateImpl(override val node: FormStep, override val parent: BranchNodeState)
    : FormStepState {

    override val currentResult: CollectionResult by lazy {
        // The question state should look at the path history and pull the last result with a matching result id.
        previousResult() as? CollectionResult ?: this.node.createResult()
    }

    override val fieldStates: List<FieldState> by lazy {
        node.children.mapIndexed { index, node ->  fieldStateFor(index, node) }
    }

    /**
     * Overridable method used to build the [fieldStates].
     */
    protected open fun fieldStateFor(index: Int, node: Node) : FieldState {
        return when (node) {
            is Question -> QuestionFieldStateImpl(index, node, resultFor(node) as AnswerResult)
            else -> AnyNodeFieldStateImpl(index, node, resultFor(node))
        }
    }

    /**
     * Get the result for the node and add to the collection if needed.
     */
    fun resultFor(node: Node) : Result {
        return previousResultFor(node) ?: run {
            val result = node.createResult()
            currentResult.inputResults.add(result)
            result
        }
    }

    protected open fun previousResultFor(node: Node) : Result?
        = currentResult.inputResults.lastOrNull { it.identifier == node.resultId() }
}

data class AnyNodeFieldStateImpl(override val index: Int,
                                 override val node: Node,
                                 override val currentResult: Result) : FieldState

open class QuestionFieldStateImpl(override val index: Int,
                                  override val node: Question,
                                  override val currentResult: AnswerResult) : AbstractQuestionFieldStateImpl()
