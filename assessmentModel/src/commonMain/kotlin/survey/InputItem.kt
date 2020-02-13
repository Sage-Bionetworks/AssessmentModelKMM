package org.sagebionetworks.assessmentmodel.forms

import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerialKind
import kotlinx.serialization.json.JsonPrimitive
import org.sagebionetworks.assessmentmodel.Question
import org.sagebionetworks.assessmentmodel.survey.QuestionState

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
     * A UI hint for how the study would prefer that the [InputItem] is displayed to the user.
     */
    val uiHint: UIHint

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
     * Can the input field be left blank? This is subtly different from the [Question.optional] property since a
     * [Question] can be described as a composite of [InputItem] results.
     *
     * For example, if the designer wants to ask "What year did you first start having symptoms?", the participant may
     * be allowed to enter a number in a text field that conforms to the [TextKeyboardInputItem] interface *or* select
     * a checkbox that says "I don't know" which conforms to the [ChoiceInputItem] interface where the
     * [ChoiceInputItem.uiHint] equals [UIHint.Choice.Checkmark], then the [Question.getInputItems] method will
     * return 2 input items. Both of those input items are [optional] but the union of them is not.
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

    /**
     * The kind of object to expect for the serialization of the answer associated with this [InputItem]. Typically,
     * this will be a [PrimitiveKind] of some type, but it is possible for the [InputItem] to translate to an object
     * rather than a primitive.
     *
     * For example, the question could be about blood pressure where the participant answers the question with a string
     * of "120/70" but the [QuestionState] is responsible for translating that into a data class with systolic and
     * diastolic as properties that are themselves numbers.
     */
    val answerKind: SerialKind
}

/**
 * A [TextKeyboardInputItem] extends the input field for the case where the participant will enter data into a text
 * field.
 */
interface TextKeyboardInputItem : InputItem {

    /**
     * A [TextKeyboardInputItem] maps to a ui hint subtype of [UIHint.TextField].
     */
    override val uiHint: UIHint.TextField

    /**
     * Options for displaying a text field. This is only applicable for certain types of UI hints and data types. If
     * not applicable, it will be ignored.
     */
    val textFieldOptions: TextFieldOptions

    // TODO: syoung 01/27/2020 Complete the properties for describing a text field input field.
//
//    /// Optional picker source for a picker or multiple selection input field.
//    var pickerSource: RSDPickerDataSource
}

/**
 * A choice input field is used to describe a choice that may be part of a larger list of choices or combined with a
 * text field to indicate that the text field should be left empty.
 */
interface ChoiceInputItem : InputItem {

    // TODO: syoung 02/11/2020 Flesh out the choice input item and what it might need.

    /**
     * A [JsonPrimitive] wraps a String, Boolean, Number, or Null. This is the JSON serializable element selected as
     * one of the possible answers for a [Question]. For certain special cases, the value may depend upon whether or
     * not the item is selected. For example, a boolean [Question] may be displayed using choice items of "Yes" and
     * "No" in a list. The choices would both be [exclusive] and the [jsonValue] for "Yes" could be `true` if [selected]
     * and `null` if not while for the "No", it could be `false` if [selected] and `null` if not.
     */
    fun jsonValue(selected: Boolean): JsonPrimitive

    /**
     * A [ChoiceInputItem] maps to a ui hint subtype of [UIHint.Choice].
     */
    override val uiHint: UIHint.Choice
}