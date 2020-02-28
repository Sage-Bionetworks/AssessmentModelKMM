package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.json.*
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.AsyncActionNavigation
import org.sagebionetworks.assessmentmodel.navigation.BranchNodeState
import org.sagebionetworks.assessmentmodel.navigation.NodeState

interface QuestionState : NodeState {
    override val node: Question
    override val currentResult: AnswerResult

    /**
     * The answerType for this [Question].
     */
    val answerType : AnswerType
        get() = currentResult.answerType ?: AnswerType.STRING

    /**
     * The list of [InputItemState] objects built from the [Question.buildInputItems] that also hold the current
     * answer state for the input item.
     */
    val itemStates: List<InputItemState>

    /**
     * Determine if all answers are valid. Also checks the case where answers are required but one has not been provided.
     */
    fun allAnswersValid(): Boolean

    /**
     * This method should be called by the controller when the view associated with this input item is tapped on by the
     * participant. For the case where this is a single choice [Question] or a skip checkbox, selecting this item may
     * result in deselecting other items. Additionally, the selection state for the view may be to remain selected or
     * to deselect the view.
     *
     * [SelectionState] is a tuple that carries the information necessary to update the view where
     * [SelectionState.selected] indicates whether or not the new state of the item is "selected" and
     * [SelectionState.reloadQuestion] indicates whether or not the other input items associated with this question
     * should be reloaded to update their state.
     */
    fun didSelect(item: InputItemState): SelectionState
    data class SelectionState(val selected: Boolean, val reloadQuestion: Boolean)

    /**
     * Save the given answer. It is assumed that the answer in this case has already been validated for the given input
     * item and any errors have been shown to the participant. It is also assumed that the [JsonElement] is of the
     * appropriate type that is expected for this result.
     */
    fun saveAnswer(answer: JsonElement?, forItem: InputItemState)
}

/**
 * Return the state of the [SkipCheckboxInputItem] for this question or nil if there is no skip checkbox.
 */
val QuestionState.skipCheckbox : ChoiceInputItemState?
    get() = itemStates.first { it is ChoiceInputItemState && it.inputItem is SkipCheckboxInputItem } as ChoiceInputItemState

open class QuestionStateImpl(override val node: Question, override val parent: BranchNodeState) : QuestionState {
    final override val currentResult: AnswerResult = {
        // The question state should look at the path history and pull the last result with a matching result id.
        parent.currentResult.pathHistoryResults.lastOrNull { it.identifier == node.resultId() } as? AnswerResult
                ?: this.node.createResult()
    }()

    override fun goForward(requestedPermissions: Set<Permission>?,
                           asyncActionNavigations: Set<AsyncActionNavigation>?) {
        // TODO: syoung 02/27/2020 Decide when it is best to update the result and who owns doing so.
        parent.goForward(requestedPermissions, asyncActionNavigations)
    }

    override fun goBackward(requestedPermissions: Set<Permission>?,
                            asyncActionNavigations: Set<AsyncActionNavigation>?) {
        // TODO: syoung 02/27/2020 Decide when it is best to update the result and who owns doing so.
        parent.goBackward(requestedPermissions, asyncActionNavigations)
    }

    /**
     * -- Initialization
     */

    override val answerType : AnswerType by lazy {
        currentResult.answerType ?: AnswerType.STRING
    }

    override val itemStates: List<InputItemState> by lazy {
        node.buildInputItems().mapIndexed { index, inputItem ->  itemStateFor(index, inputItem) }
    }

    /**
     * Overridable method used to build the [itemStates].
     */
    protected open fun itemStateFor(index: Int, inputItem: InputItem) : InputItemState {
        return when (inputItem) {
            is ChoiceInputItem -> ChoiceInputItemStateImpl(index, inputItem, selectedFor(index, inputItem))
            is KeyboardTextInputItem<*> -> KeyboardInputItemStateImpl(index, inputItem, answerFor(index, inputItem))
            else -> AnyInputItemStateImpl(index, inputItem, answerFor(index, inputItem))
        }
    }

    /**
     * Overridable method used to build the selection state for a given [choice] from the current result.
     */
    protected open fun selectedFor(index: Int, choice: ChoiceInputItem) : Boolean {
        val answer = currentResult.jsonValue ?: return false
        val selectedAnswer = choice.jsonValue(true) ?: return false
        val resultIdentifier = choice.resultIdentifier
        return when {
            node.singleAnswer || choice is SkipCheckboxInputItem -> answer == selectedAnswer
            resultIdentifier != null && answer is JsonObject -> answer[resultIdentifier] == selectedAnswer
            answer is JsonArray -> answer.contains(selectedAnswer)
            else -> false
        }
    }

    /**
     * Overridable method used to build the answer for a given [inputItem] from the current result.
     */
    protected open fun answerFor(index: Int, inputItem: InputItem) : JsonElement? {
        val answer = currentResult.jsonValue ?: return null
        val resultIdentifier = inputItem.resultIdentifier
        return when {
            answer is JsonNull -> null
            node is SimpleQuestion -> answer
            resultIdentifier != null && answer is JsonObject -> answer[resultIdentifier]
            answer is JsonArray && index < answer.count() -> answer[index]
            else -> null
        }
    }

    /**
     * -- Answer state handling
     */

    override fun allAnswersValid(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun didSelect(item: InputItemState): QuestionState.SelectionState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveAnswer(answer: JsonElement?, forItem: InputItemState) {
        when (answerType) {
            AnswerType.MAP -> updateJsonObject(answer, forItem)
            is AnswerType.List -> updateJsonArray(answer, forItem)
            is AnswerType.DateTime -> throw NotImplementedError("AnswerType.DateTime has not been implemented. syoung 02/28/2020")
            is AnswerType.Measurement -> throw NotImplementedError("AnswerType.Measurement has not been implemented. syoung 02/28/2020")
            else -> updateJsonValue(answer, forItem)
        }
    }

    open protected fun updateJsonObject(answer: JsonElement?, forItem: InputItemState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    open protected fun updateJsonArray(answer: JsonElement?, forItem: InputItemState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    open protected fun updateJsonValue(answer: JsonElement?, forItem: InputItemState) {
        currentResult.jsonValue = answer
    }
}

interface InputItemState {
    val index: Int
    val inputItem: InputItem
    val viewIdentifier: String
        get() = inputItem.uiHint.name
    val itemIdentifier: String
        get() = inputItem.resultIdentifier ?: "[$index]"
    var currentAnswer: JsonElement?
}

interface ChoiceInputItemState : InputItemState {
    override val inputItem: ChoiceInputItem
    var selected: Boolean
}

interface KeyboardInputItemState<T> : InputItemState {
    val textValidator: TextValidator<T>
}

open class KeyboardInputItemStateImpl<T>(override val index: Int,
                                         override val inputItem: KeyboardTextInputItem<T>,
                                         override var currentAnswer: JsonElement?) : KeyboardInputItemState<T> {
    override val textValidator = inputItem.buildTextValidator()
}

class ChoiceInputItemStateImpl(override val index: Int,
                               override val inputItem: ChoiceInputItem,
                               override var selected: Boolean) : ChoiceInputItemState {
    override var currentAnswer: JsonElement?
        get() = inputItem.jsonValue(selected)
        set(value) {
            this.selected = (inputItem.jsonValue(true) == value)
        }
}

class AnyInputItemStateImpl(override val index: Int,
                            override val inputItem: InputItem,
                            override var currentAnswer: JsonElement?) : InputItemState