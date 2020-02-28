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
     * Determine if all answers are valid. Also checks the case where answers are required but one has not been
     * provided. This can be used to update the enabled state of a "Next" button.
     */
    fun allAnswersValid(): Boolean

    /**
     * This method should be called by the controller when the view associated with a [ChoiceInputItemState] is
     * tapped on by the user. The controller is responsible for updating the [selected] value to the new value.
     * Typically, this is handled either in code with a toggle or with a UI button that renders a selection state and
     * then passes the change event to the controller.
     *
     * The [QuestionState] can then update any other input items for cases where the selection of this item triggers
     * deselecting other items --for example, a "none of the above" choice.
     *
     * @return Whether or not the change of selection state should trigger a refresh.
     */
    fun didChangeSelectionState(selected: Boolean, forItem: ChoiceInputItemState): Boolean

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
    override val currentResult: AnswerResult by lazy {
        // The question state should look at the path history and pull the last result with a matching result id.
        parent.currentResult.pathHistoryResults.lastOrNull { it.identifier == node.resultId() } as? AnswerResult
                ?: this.node.createResult()
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

    override fun didChangeSelectionState(selected: Boolean, forItem: ChoiceInputItemState): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveAnswer(answer: JsonElement?, forItem: InputItemState) {
        forItem.currentAnswer = when (answerType) {
            AnswerType.MAP -> updateJsonObject(answer, forItem)
            is AnswerType.List -> updateJsonArray(answer, forItem)
            else -> updateJsonValue(answer, forItem)
        }
    }

    /**
     * Overridable method for adding or removing the [answer] from the [JsonObject] that is used to back the
     * [AnswerResult.jsonValue]. The returned value is then set on the [forItem] in the [saveAnswer] method.
     * By default, the return value is [answer].
     */
    protected open fun updateJsonObject(answer: JsonElement?, forItem: InputItemState): JsonElement? {
        val map: MutableMap<String, JsonElement> = when (val currentValue = currentResult.jsonValue) {
            is JsonObject -> currentValue.toMutableMap()
            else -> mutableMapOf()
        }
        if (answer != null) map[forItem.itemIdentifier] = answer else map.remove(forItem.itemIdentifier)
        currentResult.jsonValue = JsonObject(map)
        return answer
    }

    /**
     * Overridable method for adding or removing the [answer] from the [JsonArray] that is used to back the
     * [AnswerResult.jsonValue]. The returned value is then set on the [forItem] in the [saveAnswer] method.
     * By default, the return value is [answer].
     */
    protected open fun updateJsonArray(answer: JsonElement?, forItem: InputItemState): JsonElement? {
        val set: MutableSet<JsonElement> = when (val currentValue = currentResult.jsonValue) {
            is JsonArray -> currentValue.toMutableSet()
            else -> mutableSetOf()
        }
        if (answer != null) {
            set.add(answer)
        } else if (forItem.currentAnswer != null) {
            set.remove(forItem.currentAnswer!!)
        }
        currentResult.jsonValue = JsonArray(set.toList())
        return answer
    }

    /**
     * Overridable method for updating the current result to use set the [answer] as the [AnswerResult.jsonValue].
     * The returned value is then set on the [forItem] in the [saveAnswer] method. By default, the return value is
     * [answer].
     */
    protected open fun updateJsonValue(answer: JsonElement?, forItem: InputItemState): JsonElement? {
        currentResult.jsonValue = answer
        return answer
    }
}

interface InputItemState {
    val index: Int
    val inputItem: InputItem
    val viewIdentifier: String
        get() = inputItem.uiHint.name
    val itemIdentifier: String
        get() = inputItem.resultIdentifier ?: "$index"
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