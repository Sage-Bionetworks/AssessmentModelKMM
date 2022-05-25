package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.sagebionetworks.assessmentmodel.StringEnum
import org.sagebionetworks.assessmentmodel.matching
import org.sagebionetworks.assessmentmodel.serialization.*

/**
 * An [InputItem] describes a "part" of a [Question] representing a single answer.
 *
 * For example, if a question is "what is your name" then the input items may include "given name" and "family name"
 * where separate text fields are used to allow the participant to enter their first and last name, and the question
 * may also include a list of titles from which to choose.
 *
 * In another example, the input item could be a single cell in a list that shows the possible choices for a question.
 * In essence, this is akin to a single cell in a table view though the actual implementation may differ.
 */
interface InputItem {

    /**
     * The result identifier is an optional value that can be used to help in building the serializable answer result
     * from this [InputItem]. If null, then it is assumed that the [Question] that holds this [InputItem] has some
     * custom serialization strategy or only contains a single answer and this property can be ignored.
     */
    val resultIdentifier: String?

    /**
     * The kind of object to expect for the serialization of the answer associated with this [InputItem]. Typically,
     * this will be a [BaseType], but it is possible for the [InputItem] to translate to an object rather than a
     * primitive.
     *
     * For example, the question could be about blood pressure where the participant answers the question with a string
     * of "120/70" but the [QuestionState] is responsible for translating that into a data class with systolic and
     * diastolic as properties that are themselves numbers.
     */
    val answerType: AnswerType

    /**
     * Can the input field be left blank? This is subtly different from the [Question.optional] property since a
     * [Question] can be described as a composite of [InputItem] results.
     *
     * For example, if the designer wants to ask "What year did you first start having symptoms?", the participant may
     * be allowed to enter a number in a text field that conforms to the [KeyboardTextInputItem] interface *or* select
     * a checkbox that says "I don't know" which conforms to the [ChoiceInputItem] interface. Both of those input items
     * are [optional] but the union of them is not.
     *
     * However, if the [Question] requires entering your blood pressure using two fields for the systolic and
     * diastolic readings, then the question may be [optional] but the input items are not. (If you enter a value for
     * one, then you need a value for the other in order to have a valid answer.)
     */
    val optional: Boolean

    /**
     * Does filling in or selecting this [InputItem] mean that the other [InputItem] objects that as a collectively
     * define the [Question] should be deselected or disabled?
     *
     * For example, if the [Question] asks "When were you diagnosed?" with a text field for entering the year and a
     * checkbox that says "I don't remember", then the UI should disable the year field or otherwise indicate to the
     * user when they check the box that the year field is ignored.
     *
     * In another example, this can be used in a multiple selection question to allow for a "none of the above" input
     * item that is exclusive to the other items.
     */
    val exclusive: Boolean
}

/**
 * -- ChoiceInputItem
 */

/**
 * A choice input field is used to describe a choice that may be part of a larger list of choices or
 * combined with a text field to indicate that the text field should be left empty.
 */
interface ChoiceInputItem : InputItem, ChoiceOption {

    /**
     * A choice input is always optional because it is either a checkbox yes/no input where the
     * difference between "not selected" and "not answered" is indeterminate, or it is a choice in a
     * list of other choices.
     */
    override val optional: Boolean
        get() = true

    override val exclusive: Boolean
        get() = (selectorType == ChoiceSelectorType.Exclusive)
}

/**
 * A choice option is a light-weight interface for a list of choices that should be displayed to the participant.
 */
interface ChoiceOption {

    /**
     * This is the JSON serializable element selected as one of the possible answers for a [Question]. For certain
     * special cases, the value may depend upon whether or not the item is selected.
     *
     * For example, a boolean [Question] may be displayed using choice items of "Yes" and "No" in a list. The choices
     * would both be [exclusive] and the [jsonValue] for "Yes" could be `true` if [selected] and `null` if not while
     * for the "No", it could be `false` if [selected] and `null` if not.
     *
     * While Kotlin does not allow for extending a final class to allow implementing an interface, it is assumed that
     * the returned value is something that the associated result can encode to JSON.
     */
    fun jsonValue(selected: Boolean): JsonElement?

    /**
     * A localized label (ie. title or text) to display for this choice.
     */
    val label: String

    /**
     * Additional detail shown below the [label].
     */
    val detail: String?
        get() = null

    /**
     * The name of the image to use for this choice.
     */
    val iconName: String?

    /**
     * Does selecting this choice mean that the other options should be deselected or selected as
     * well?
     *
     * For example, this can be used in a multiple selection question to allow for a "none of the above"
     * choice that is `exclusive` to the other items or an "all of the above" choice that should
     * select `all` other choices as well (except those that are marked as `exclusive`).
     */
    val selectorType: ChoiceSelectorType
        get() = ChoiceSelectorType.Default

}

/**
 * A [CheckboxInputItem] is intended as a simplified input item that can be included in a compound [Question] where
 * the goal is to build a mapping of answers to identifiers. As such, [resultIdentifier] and [fieldLabel] are required
 * fields.
 */
interface CheckboxInputItem : ChoiceInputItem {
    override val resultIdentifier: String
    override val answerType: AnswerType
        get() = AnswerType.BOOLEAN
    override val iconName: String?
        get() = null
    override fun jsonValue(selected: Boolean): JsonElement?
            = if (selected) JsonPrimitive(true) else JsonPrimitive(false)
}

@Serializable
enum class ChoiceSelectorType : StringEnum {
    Default, Exclusive, All;

    @Serializer(forClass = ChoiceSelectorType::class)
    companion object : KSerializer<ChoiceSelectorType> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("ChoiceSelectorType", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): ChoiceSelectorType {
            val name = decoder.decodeString()
            return values().matching(name) ?: throw SerializationException("Unknown $name for ${descriptor.serialName}. Needs to be one of ${values()}")
        }
        override fun serialize(encoder: Encoder, value: ChoiceSelectorType) {
            encoder.encodeString(value.name.lowercase())
        }
    }
}

/**
 * -- KeyboardTextInputItem
 */

/**
 * A [KeyboardTextInputItem] extends the input field for the case where the participant will enter data into a text
 * field.
 */
interface KeyboardTextInputItem<T> : InputItem {

    /**
     * A localized string that displays a short text offering a hint to the user of the data to be entered for this
     * field.
     */
    val fieldLabel: String?

    /**
     * A localized string that displays placeholder information for the [InputItem].
     *
     * You can display placeholder text in a text field or text area to help users understand how to answer the item's
     * question. If the input field brings up another view to enter the answer, this could also be used at the button
     * title.
     */
    val placeholder: String?

    /**
     * Options for displaying a text field. This is only applicable for certain types of UI hints and data types. If
     * not applicable, it will be ignored.
     */
    val keyboardOptions: KeyboardOptions

    /**
     * This can be used to return a class used to format and/or validate the text input.
     */
    fun buildTextValidator(): TextValidator<T>
}

/**
 * Dates and times are not directly serializable and should always be serialized as a [String].
 */
interface TimeInputItem : KeyboardTextInputItem<String> {

    /**
     * For date and time input entry, the format options will always be constrained for a date or time range and coding.
     */
    val formatOptions: TimeFormatOptions
        get() = TimeFormatOptions()

    override val answerType: AnswerType
        get() = AnswerType.Time()

    override val keyboardOptions: KeyboardOptions
        get() = KeyboardOptionsObject.DateTimeEntryOptions

    // TODO: syoung 02/18/2020 Revisit this. I couldn't figure out Android date formatting.
    override fun buildTextValidator(): TextValidator<String> = PassThruTextValidator
}

@Serializable
data class TimeFormatOptions(val allowFuture: Boolean = true,
                             val allowPast: Boolean = true,
                             val minimumValue: String? = null,
                             val maximumValue: String? = null)

object PassThruTextValidator : TextValidator<String> {
    override fun valueFor(text: String): FormattedValue<String> = FormattedValue(text)
    override fun localizedStringFor(value: String?): FormattedValue<String> = FormattedValue(value)
    override fun jsonValueFor(value: String?): JsonElement = JsonPrimitive(value)
    override fun valueFor(jsonValue: JsonElement?): String? = jsonValue?.jsonPrimitive?.content
}

interface DurationInputItem : InputItem {
    val displayUnits: List<DurationUnit>
        get() = DurationUnit.defaultUnits

    override val answerType: AnswerType
        get() = AnswerType.Duration(displayUnits)
}

@Serializable
enum class DurationUnit {
    @SerialName("hour")
    Hour,
    @SerialName("minute")
    Minute,
    @SerialName("second")
    Second;

    val secondsMultiplier: Double
        get() = when (this) {
            Hour -> 60.0 * 60.0
            Minute -> 60.0
            Second -> 1.0
        }

    companion object {
        val defaultUnits = listOf(Hour, Minute)
    }
}
