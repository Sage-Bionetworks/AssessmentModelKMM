package org.sagebionetworks.assessmentmodel.forms

import kotlinx.serialization.SerialKind
import kotlinx.serialization.json.JsonPrimitive
import org.sagebionetworks.assessmentmodel.Question

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
     * custom serialization strategy and this property can be ignored.
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
     * The kind of object to expect for the serialization of this [InputItem].
     */
    val answerKind: SerialKind
}

/**
 * A [PickerInputItem] extends the input field for the case where the participant will enter data using a picker to
 * select the answer.
 */
interface PickerInputItem : InputItem {

    /**
     * A [PickerInputItem] maps to a ui hint subtype of [UIHint.Picker].
     */
    override val uiHint: UIHint.Picker
        get() = UIHint.Picker

    // TODO: syoung 01/27/2020 Complete the properties for describing a picker input field.
//    /// Optional picker source for a picker or multiple selection input field.
//    var pickerSource: RSDPickerDataSource
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
//    /// A formatter that is appropriate to the data type. If `nil`, the format will be determined by the UI.
//    /// This is the formatter used to display a previously entered answer to the user or to convert an
//    /// answer entered in a text field into the appropriate value type.
//    ///
//    /// - seealso: `RSDAnswerResultType.BaseType` and `RSDFormStepDataSource`
//    var formatter: Formatter? { get }
//
//    /// A range used by dates and numbers for setting up a picker wheel, slider, or providing text field
//    /// input validation. If not applicable, it will be ignored.
//    var range: RSDRange? { get }
}

/**
 * A choice input field is used to describe a choice that may be part of a larger list of choices or combined with a
 * text field to indicate that the text field should be left empty.
 */
interface ChoiceInputItem : InputItem {

    // TODO: syoung 02/11/2020 Flesh out the choice input item and what it might need.

    /**
     * For a multiple choice option, is this choice mutually exclusive? For example, "none of the above".
     */
    val exclusive: Boolean

    /**
     * A [JsonPrimitive] wraps a String, Boolean, Number, or Null. This is the JSON serializable element selected as
     * one of the possible answers for a [Question]. For certain special cases, the value may depend upon whether or
     * not the item is selected. Since this is used to show fields with a selection state, the value may toggle
     * depending upon whether or not it is selected.
     */
    fun jsonValue(selected: Boolean): JsonPrimitive

    /**
     * A [ChoiceInputItem] maps to a ui hint subtype of [UIHint.Choice].
     */
    override val uiHint: UIHint.Choice
}