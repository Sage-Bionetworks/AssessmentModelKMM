package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.AsyncActionNavigation
import org.sagebionetworks.assessmentmodel.navigation.BranchNodeState
import org.sagebionetworks.assessmentmodel.navigation.NodeState

interface QuestionState : NodeState {
    // TODO: syoung 02/11/2020 Implement. This is a placeholder for the thingy that holds the result state.

    override val node: Question

    // Vend list of input items
    // Look through path history to get any previous results
    // track "indexpath"
}

open class QuestionStateImpl(override val node: Question, override val parent: BranchNodeState) : QuestionState {
    override val currentResult: Result by lazy {
        // By default, the Question should look at the path history and pull the last result with a matching result id.
        parent.currentResult.pathHistoryResults.lastOrNull { it.identifier == node.resultId() } ?: this.node.createResult()
    }

    override fun goForward(requestedPermissions: Set<Permission>?,
                           asyncActionNavigations: Set<AsyncActionNavigation>?) {
        parent.goForward(requestedPermissions, asyncActionNavigations)
    }

    override fun goBackward(requestedPermissions: Set<Permission>?,
                            asyncActionNavigations: Set<AsyncActionNavigation>?) {
        parent.goBackward(requestedPermissions, asyncActionNavigations)
    }

    /**
     * Question state handling
     */

    open val inputItems: List<InputItem> by lazy {
        node.buildInputItems()
    }

    open val answerType : AnswerType by lazy {
        (currentResult as? AnswerResult)?.answerType ?: AnswerType.STRING
    }

    open val itemStates: List<InputItemState> by lazy {
        inputItems.map { itemStateFor(it) }
    }

    open fun itemStateFor(inputItem: InputItem) : InputItemState {
        return when (inputItem) {
            is ChoiceInputItem -> ChoiceInputItemStateImpl(inputItem, selectedFor(inputItem))
            else -> AnyInputItemStateImpl(inputItem, answerFor(inputItem))
        }
    }

    open fun currentAnswer() : JsonElement? = (currentResult as? AnswerResult)?.jsonValue

    open fun selectedFor(choice: ChoiceInputItem) : Boolean {
        val answer = currentAnswer() ?: return false
        val selectedAnswer = choice.jsonValue(true) ?: return false
        val resultIdentifier = choice.resultIdentifier
        return when {
            node.singleAnswer -> answer.toString() == selectedAnswer.toString()
            answer is JsonArray -> answer.contains(selectedAnswer)
            resultIdentifier != null && answer is JsonObject -> answer[resultIdentifier] == selectedAnswer
            else -> false
        }
    }

    open fun answerFor(inputItem: InputItem) : JsonElement? {
        TODO("Implement")
    }
}

interface InputItemState {
    val inputItem: InputItem
    val viewIdentifier: String
        get() = inputItem.uiHint.name
}

interface AnyInputItemState : InputItemState {
    var currentAnswer: JsonElement?
}

interface ChoiceInputItemState : InputItemState {
    override val inputItem: ChoiceInputItem
    var selected: Boolean
}

class KeyboardInputItemStateImpl<T>(override val inputItem: KeyboardTextInputItem<T>, override var currentAnswer: JsonElement?)
    : AnyInputItemState {
    val textValidator = inputItem.buildTextValidator()
}

class ChoiceInputItemStateImpl(override val inputItem: ChoiceInputItem, override var selected: Boolean)
    : ChoiceInputItemState

class AnyInputItemStateImpl(override val inputItem: InputItem, override var currentAnswer: JsonElement?)
    : AnyInputItemState