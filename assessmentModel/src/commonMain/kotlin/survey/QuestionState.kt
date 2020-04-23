package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.json.*
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.BranchNodeState
import org.sagebionetworks.assessmentmodel.navigation.LeafNodeState
import org.sagebionetworks.assessmentmodel.navigation.previousResult

interface QuestionState : LeafNodeState, QuestionFieldState {
    override val node: Question
    override val currentResult: AnswerResult
}

interface QuestionFieldState : FieldState {
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
     * deselecting other items--for example, a "none of the above" choice.
     *
     * @return Whether or not the change of selection state should trigger a refresh.
     */
    fun didChangeSelectionState(selected: Boolean, forItem: ChoiceInputItemState): Boolean

    /**
     * Save the given answer. It is assumed that the answer in this case has already been validated for the given input
     * item and any errors have been shown to the participant. It is also assumed that the [JsonElement] is of the
     * appropriate type that is expected for this result.
     *
     * The [QuestionState] can then update any other input items for cases where the selection of this item triggers
     * refreshing other items--for example, a custom skip UI/UX where entering a value should uncheck the checkbox.
     *
     * @return Whether or not the updated answer should trigger a refresh.
     */
    fun saveAnswer(answer: JsonElement?, forItem: InputItemState): Boolean
}

open class QuestionStateImpl(override val node: Question, override val parent: BranchNodeState)
    : AbstractQuestionFieldStateImpl(), QuestionState {
    override val index: Int = 0
    override val currentResult: AnswerResult by lazy {
        // The question state should look at the path history and pull the last result with a matching result id.
        previousResult() as? AnswerResult ?: this.node.createResult()
    }
}

abstract class AbstractQuestionFieldStateImpl() : QuestionFieldState {

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
        val resultIdentifier = choice.resultIdentifier ?: "$index"
        return when {
            node.singleAnswer || choice is SkipCheckboxInputItem -> answer == selectedAnswer
            answer is JsonArray -> answer.contains(selectedAnswer)
            answer is JsonObject -> answer[resultIdentifier] == selectedAnswer
            else -> {
                println("WARNING! Cannot interpret answer mapping for $answer")
                false
            }
        }
    }

    /**
     * Overridable method used to build the answer for a given [inputItem] from the current result.
     */
    protected open fun answerFor(index: Int, inputItem: InputItem) : JsonElement? {
        val answer = currentResult.jsonValue ?: return null
        val resultIdentifier = inputItem.resultIdentifier ?: "$index"
        val question = node
        return when {
            answer is JsonNull -> null
            question is ComboBoxQuestion -> {
                val choiceAnswers = question.choices.map { it.jsonValue(true) }
                if (question.singleAnswer) {
                    if (choiceAnswers.contains(answer)) null else answer
                } else if (answer is JsonArray) {
                    answer.minus(choiceAnswers).firstOrNull()
                } else {
                    null
                }
            }
            question.singleAnswer -> answer
            answer is JsonObject -> answer[resultIdentifier]
            answer is JsonArray && index < answer.count() -> answer[index]
            else -> null
        }
    }

    /**
     * -- Answer state handling
     */

    override fun allAnswersValid(): Boolean
            // Return true if the question is optional.
            = node.optional ||
            // `JsonNull` is used as a special placeholder for "chose not to answer".
            currentResult.jsonValue == JsonNull ||
            // Otherwise, there should be a non-null result and all the non-optional items should be selected.
            (currentResult.jsonValue != null && itemStates.none { !it.inputItem.optional && !it.selected })

    override fun didChangeSelectionState(selected: Boolean, forItem: ChoiceInputItemState): Boolean {
        forItem.selected = selected
        return updateAnswerState(forItem)
    }

    override fun saveAnswer(answer: JsonElement?, forItem: InputItemState): Boolean {
        forItem.currentAnswer = answer
        return updateAnswerState(forItem)
    }

    /**
     * Protected overridable method for setting the new value to the current result and updating the question state.
     */
    protected open fun updateAnswerState(changedItem: InputItemState): Boolean {
        var refresh = false

        // If the changed item is selected, then iterate through the collection and deselect other items as needed.
        if (changedItem.selected) {
            val deselectOthers = (changedItem.inputItem.exclusive || node.singleAnswer)
            itemStates.forEach {
                if (it != changedItem && it.selected && (deselectOthers || it.inputItem.exclusive))  {
                    it.selected = false
                    refresh = true
                }
            }
        }

        // Update the result.
        val isSkipCheckbox = changedItem.inputItem is SkipCheckboxInputItem
        if (isSkipCheckbox && changedItem.selected) {
            // For a skip checkbox, use the changeItem's answer to mark the current result.
            currentResult.jsonValue = changedItem.currentAnswer
        } else {
            val map = itemStates.mapNotNull {
                // If the state change to uncheck the skip checkbox, then need to update the selected state
                // for previously stored answers.
                if (isSkipCheckbox && it is AnyInputItemState) {
                    it.selected = (it.storedAnswer != null)
                    refresh = refresh || it.selected
                }
                // Create a mapping of the non-null currentAnswer to the item identifier.
                val itemAnswer = it.currentAnswer
                if (itemAnswer != null && itemAnswer != JsonNull) it.itemIdentifier to itemAnswer else null
            }.toMap()
            currentResult.jsonValue = jsonValue(map)
        }
        return refresh
    }

    /**
     * Protected overridable method for setting the answer value of the result. By default, this will handle only the
     * simple cases of a map, an set made up of unique answers, or setting the value to the first element in the array.
     */
    protected open fun jsonValue(forMap: Map<String, JsonElement>) : JsonElement? = when (val aType = answerType) {
        AnswerType.OBJECT -> JsonObject(forMap)
        is AnswerType.Array -> if (aType.sequenceSeparator == null) {
            JsonArray(forMap.values.toSet().toList())
        } else {
            TODO("Not implemented. syoung 03/03/2020")
        }
        else -> forMap.values.firstOrNull()
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
    var selected: Boolean
}

interface AnyInputItemState : InputItemState {
    var storedAnswer: JsonElement?
    override var currentAnswer: JsonElement?
        get() = if (selected) storedAnswer else null
        set(value) {
            storedAnswer = value
            selected = (value != null)
        }
}

interface ChoiceInputItemState : InputItemState {
    override val inputItem: ChoiceInputItem
    override var currentAnswer: JsonElement?
        get() = inputItem.jsonValue(selected)
        set(value) {
            this.selected = (inputItem.jsonValue(true) == value)
        }
}

interface KeyboardInputItemState<T> : AnyInputItemState {
    val textValidator: TextValidator<T>
    override val inputItem: KeyboardTextInputItem<T>
}

class KeyboardInputItemStateImpl<T>(override val index: Int,
                                    override val inputItem: KeyboardTextInputItem<T>,
                                    override var storedAnswer: JsonElement?) : KeyboardInputItemState<T> {
    override val textValidator = inputItem.buildTextValidator()
    override var selected = (storedAnswer != null)
}

class ChoiceInputItemStateImpl(override val index: Int,
                               override val inputItem: ChoiceInputItem,
                               override var selected: Boolean) : ChoiceInputItemState

class AnyInputItemStateImpl(override val index: Int,
                            override val inputItem: InputItem,
                            override var storedAnswer: JsonElement?) : AnyInputItemState {
    override var selected = (storedAnswer != null)
}
