package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.json.JsonArray
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.serialization.AnswerResultObject
import org.sagebionetworks.assessmentmodel.serialization.ChoiceItemWrapper

/**
 * A [Question] can be a node in a [FormStep] or [Section] or it might be a stand-alone question.
 *
 * When defining a [Question], the [subtitle] is roughly equivalent to what is often described as a "prompt". It
 * is additional text displayed in a smaller font below [title]. If you need to display a long question, it can work
 * well to keep the title short and put the additional content in the [subtitle] property.
 *
 * Similarly, the [detail] is text to display at the top of the screen in a smaller font to further explain the
 * instructions for the question included on the screen shown to the participant --for example, "Select all that apply".
 */
interface Question : ContentNode {

    /**
     * Can the question be skipped?
     */
    val optional: Boolean

    /**
     * Is there a [singleAnswer] for this [Question] or is the [Question] a composite of multiple choices or input items?
     */
    val singleAnswer: Boolean

    /**
     * The [AnswerType] that is associated with this [Question] or null if this [Question] has custom handling.
     */
    val answerType: AnswerType

    /**
     * A question will always have at least one [InputItem] that is used to define the question. These fields will form
     * a logical grouping for how the [Question] should be presented to the user. For example, the [Question] may be
     * "what is your name" where the fields are given name, family name, title, and a checkbox that says "prefer not to
     * answer". How the fields interact may use custom logic, but they are presented together and do not make sense
     * independently of one another.
     *
     * Typically, the [buildInputItems] function for a [Question] will either be serialized as the same object
     * (returns self) or a list of elements of type [InputItem]. It is defined here as a function to allow for
     * flexibility in how it is stored and displayed.
     */
    fun buildInputItems(): List<InputItem>

    /**
     * Override [createResult] to return an [AnswerResult] by default.
     */
    override fun createResult(): AnswerResult = AnswerResultObject(resultId(), answerType)
}

/**
 * Interface to explicitly define a an optional [skipCheckbox]. This allows for more control over the UI/UX for
 * optional questions that use a [SkipCheckboxInputItem] to define whether or not the field is [optional]. This is
 * not intended to be implemented directly and should be considered an "abstract" interface. Instead, it is used to
 * simplify allowing the [QuestionState] to exclude this input item from the items used to build the non-null answer.
 */
interface SkipCheckboxQuestion : Question {
    val skipCheckbox: SkipCheckboxInputItem?
}

/**
 * A [SimpleQuestion] is used to explicitly define a question with a single [inputItem] and an optional
 * [skipCheckbox]. It is defined here as an interface to allow for a layout that is appropriate for a question where
 * there is only one input field.
 */
interface SimpleQuestion : SkipCheckboxQuestion {
    val inputItem: InputItem

    override val answerType: AnswerType
        get() = inputItem.answerType
    override val singleAnswer: Boolean
        get() = true

    override fun buildInputItems(): List<InputItem> = listOfNotNull(inputItem, skipCheckbox)
}

/**
 * A [MultipleInputQuestion] is used to explicitly define a question with multiple [inputItems] and an optional
 * [skipCheckbox].
 */
interface MultipleInputQuestion : SkipCheckboxQuestion {
    val inputItems: List<InputItem>
    val sequenceSeparator: String?

    override val singleAnswer: Boolean
        get() = false
    override val answerType: AnswerType
        get() =  AnswerType.OBJECT

    override fun buildInputItems(): List<InputItem> = skipCheckbox?.let { inputItems.plus(it) } ?: inputItems
}

/**
 * The [ChoiceQuestion] subtype is used to explicitly call out that this question shows a list of [choices]. This
 * interface will build the [InputItem] elements from the [choices] using a wrapper that takes in the [singleAnswer],
 * [baseType], and [uiHint] that are defined at this level rather than on the individual [choices].
 */
interface ChoiceQuestion : Question {
    val baseType: BaseType
    val choices: List<ChoiceOption>
    var uiHint: UIHint

    /**
     * The default implementation for the answer type is to use the [baseType] and return either a [JsonLiteral]
     * mapping or a [JsonArray] mapping depending upon the value of [singleAnswer].
     */
    override val answerType: AnswerType
        get() = if (singleAnswer) AnswerType.valueFor(baseType) else AnswerType.Array(baseType)

    /**
     * The default implementation for building the input items is to wrap the [choices].
     */
    override fun buildInputItems(): List<InputItem> = choices.map {
        ChoiceItemWrapper(it, singleAnswer, AnswerType.valueFor(baseType), uiHint)
    }
}

/**
 * A [ComboBoxQuestion] extends the [ChoiceQuestion] interface to add an [otherInputItem] choice to the selected
 * choices. Typically, the [otherInputItem] will be a [KeyboardTextInputItem] but it is generically defined to allow
 * for custom implementations.
 */
interface ComboBoxQuestion : ChoiceQuestion {
    val otherInputItem: InputItem

    /**
     * The default implementation for the [baseType] is determined by the [answerType] of the [otherInputItem].
     */
    override val baseType: BaseType
        get() = otherInputItem.answerType.baseType

    /**
     * The default implementation for building the input items is to wrap the [choices] and then add the
     * [otherInputItem] as the last item in the list.
     */
    override fun buildInputItems(): List<InputItem> = choices.map {
        ChoiceItemWrapper(it, singleAnswer, AnswerType.valueFor(baseType), uiHint)
    }.plus(otherInputItem)
}
