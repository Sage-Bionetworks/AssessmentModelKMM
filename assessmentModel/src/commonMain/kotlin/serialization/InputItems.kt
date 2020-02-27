package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.StringEnum
import org.sagebionetworks.assessmentmodel.survey.*
import org.sagebionetworks.assessmentmodel.survey.AnswerType

val inputItemSerializersModule = SerializersModule {
    polymorphic(InputItem::class) {
        SkipCheckboxInputItem::class with SkipCheckboxInputItem.serializer()
        DateInputItemObject::class with DateInputItemObject.serializer()
        DecimalTextInputItemObject::class with DecimalTextInputItemObject.serializer()
        IntegerTextInputItemObject::class with IntegerTextInputItemObject.serializer()
        TimeInputItemObject::class with TimeInputItemObject.serializer()
        StringTextInputItemObject::class with StringTextInputItemObject.serializer()
        YearTextInputItemObject::class with YearTextInputItemObject.serializer()
    }
    polymorphic(DateTimeFormatOptions::class) {
        DateFormatOptions::class with DateFormatOptions.serializer()
        TimeFormatOptions::class with TimeFormatOptions.serializer()
    }
}

// TODO: syoung 02/18/2020 Names of fields for serialization have changed from SageResearch to support kotlinx.

/**
 * A [InputItemObject] is intended to implement shared code for serialization of the simple data types. This will
 * work to deserialize some of the existing input items that use the [DataType] to define their type.
 */
@Serializable
abstract class InputItemObject : InputItem {
    @SerialName("prompt")
    override var fieldLabel: String? = null
    override var placeholder: String? = null
    override var optional: Boolean = true
    override var exclusive: Boolean = false
    override var uiHint: UIHint.TextField = UIHint.TextField.Default
}

@Serializable
data class TextFieldOptionsObject(override val isSecureTextEntry: Boolean = false,
                                  override val autocapitalizationType: AutoCapitalizationType = AutoCapitalizationType.None,
                                  override val autocorrectionType: AutoCorrectionType = AutoCorrectionType.Default,
                                  override val spellCheckingType: SpellCheckingType = SpellCheckingType.Default,
                                  override val keyboardType: KeyboardType = KeyboardType.Default) : TextFieldOptions {
    companion object {
        val NumberEntryOptions = TextFieldOptionsObject(
                autocorrectionType = AutoCorrectionType.No,
                spellCheckingType = SpellCheckingType.No,
                keyboardType = KeyboardType.NumberPad)
        val DecimalEntryOptions = TextFieldOptionsObject(
                autocorrectionType = AutoCorrectionType.No,
                spellCheckingType = SpellCheckingType.No,
                keyboardType = KeyboardType.DecimalPad)
        val DateTimeEntryOptions = TextFieldOptionsObject(
                autocorrectionType = AutoCorrectionType.No,
                spellCheckingType = SpellCheckingType.No,
                keyboardType = KeyboardType.NumbersAndPunctuation)
    }
}

/**
 * KeyboardTextInputItem
 */

@Serializable
@SerialName("decimal")
data class DecimalTextInputItemObject(@SerialName("identifier")
                                      override val resultIdentifier: String? = null,
                                      var formatOptions: DoubleFormatOptions = DoubleFormatOptions())
    : InputItemObject(), KeyboardTextInputItem<Double> {
    override val answerType: AnswerType
        get() = AnswerType.DECIMAL

    override val textFieldOptions: TextFieldOptionsObject
        get() = TextFieldOptionsObject.DecimalEntryOptions
    override fun buildTextValidator(): TextValidator<Double>? = DoubleFormatter(formatOptions)
}

@Serializable
@SerialName("integer")
data class IntegerTextInputItemObject(@SerialName("identifier")
                                      override val resultIdentifier: String? = null,
                                      override var textFieldOptions: TextFieldOptionsObject = TextFieldOptionsObject.NumberEntryOptions,
                                      var formatOptions: IntFormatOptions = IntFormatOptions())
    : InputItemObject(), KeyboardTextInputItem<Int> {
    override val answerType: AnswerType
        get() = AnswerType.INTEGER

    override fun buildTextValidator(): TextValidator<Int>? = IntFormatter(formatOptions)
}

@Serializable
@SerialName("string")
data class StringTextInputItemObject(@SerialName("identifier")
                                     override val resultIdentifier: String? = null,
                                     override var textFieldOptions: TextFieldOptionsObject = TextFieldOptionsObject(),
                                     var regExValidator: RegExValidator? = null)
    : InputItemObject(), KeyboardTextInputItem<String> {
    override val answerType: AnswerType
        get() = AnswerType.STRING

    override fun buildTextValidator(): TextValidator<String>? = regExValidator
}

@Serializable
data class RegExValidator(val pattern: String, val invalidMessage: InvalidMessageObject) : TextValidator<String> {
    override fun valueFor(text: String): FormattedValue<String>? {
        val regex = Regex(pattern = pattern)
        return if (regex.matches(text)) FormattedValue(result = text) else FormattedValue(invalidMessage = invalidMessage)
    }
    override fun localizedStringFor(value: String?) = FormattedValue(result = value)
}

@Serializable
@SerialName("year")
data class YearTextInputItemObject(@SerialName("identifier")
                                   override val resultIdentifier: String? = null,
                                   var formatOptions: YearFormatOptions = YearFormatOptions())
    : InputItemObject(), KeyboardTextInputItem<Int> {
    override val answerType: AnswerType
        get() = AnswerType.INTEGER

    override val textFieldOptions: TextFieldOptionsObject
        get() = TextFieldOptionsObject.NumberEntryOptions
    override fun buildTextValidator(): TextValidator<Int>? = IntFormatter(formatOptions)
}

/**
 * DateTimeInputItem
 */

@Serializable
@SerialName("date")
data class DateInputItemObject(@SerialName("identifier")
                                   override val resultIdentifier: String? = null,
                                   override var formatOptions: DateFormatOptions = DateFormatOptions())
    : InputItemObject(), DateTimeInputItem

@Serializable
@SerialName("time")
data class TimeInputItemObject(@SerialName("identifier")
                                   override val resultIdentifier: String? = null,
                                   override var formatOptions: TimeFormatOptions = TimeFormatOptions())
    : InputItemObject(), DateTimeInputItem

// TODO: syoung 02/18/2020 In SageResearch change "minimumDate" -> "minimumValue" and "maximumDate" -> "maximumValue"

@Serializable
@SerialName("date")
data class DateFormatOptions(override val allowFuture: Boolean = true,
                             override val allowPast: Boolean = true,
                             override val minimumValue: String? = null,
                             override val maximumValue: String? = null,
                             override val codingFormat: String = ISO8601Format.DateOnly.formatString) : DateTimeFormatOptions

@Serializable
@SerialName("time")
data class TimeFormatOptions(override val allowFuture: Boolean = true,
                             override val allowPast: Boolean = true,
                             override val minimumValue: String? = null,
                             override val maximumValue: String? = null,
                             override val codingFormat: String = ISO8601Format.TimeOnly.formatString) : DateTimeFormatOptions

/**
 * ChoiceInputItem
 */

/**
 * A [SkipCheckboxInputItem] is a special case of input item that can be used to define an "or" option for a text field
 * such as asking the participant to answer a question or allowing them to select "I don't know". This item is always
 * shown using a [UIHint.Choice.Checkbox] and always has a [fieldLabel]. It is always [optional] and [exclusive].
 */
@Serializable
@SerialName("skipCheckbox")
data class SkipCheckboxInputItem(@SerialName("prompt")
                                 override val fieldLabel: String,
                                 val value: JsonElement = JsonNull) : ChoiceInputItem {
    override val resultIdentifier: String?
        get() = null
    override val icon: FetchableImage?
        get() = null
    override val optional: Boolean
        get() = true
    override val exclusive: Boolean
        get() = true
    override val answerType: AnswerType
        get() = AnswerType.BOOLEAN
    override val uiHint: UIHint.Choice
        get() = UIHint.Choice.Checkbox

    override fun jsonValue(selected: Boolean): JsonElement? = if (selected) value else null
}

@Serializable
data class ChoiceOptionObject(val value: JsonElement = JsonNull,
                              @SerialName("text")
                              override val fieldLabel: String?,
                              @Serializable(ImageNameSerializer::class)
                              override val icon: FetchableImage? = null,
                              override val exclusive: Boolean = false) : ChoiceOption {
    override fun jsonValue(selected: Boolean): JsonElement? = if (selected) value else null
}

/**
 * A [ChoiceItemWrapper] is used to wrap serializable [ChoiceOption] items that have a shared [uiHint] and [answerType]
 * for either a [singleChoice] or multiple choice question.
 */
data class ChoiceItemWrapper(val choice: ChoiceOption,
                             val singleChoice: Boolean,
                             override val answerType: AnswerType,
                             override val uiHint: UIHint.Choice) : ChoiceInputItem, ChoiceOption by choice {
    override val resultIdentifier: String?
        get() = (choice.jsonValue(true) ?: JsonNull).toString()
    override val optional: Boolean
        get() = true
    override val exclusive: Boolean
        get() = singleChoice || choice.exclusive
}

/**
 * A [OtherChoiceItemWrapper] is used to wrap a serializable [InputItem] that is used to allow a multiple choice
 * question to have an "other" text entry input item.
 */
data class OtherChoiceItemWrapper(val inputItem: InputItem,
                            val singleChoice: Boolean) : InputItem by inputItem {
    override val optional: Boolean
        get() = true
    override val exclusive: Boolean
        get() = singleChoice
}

/**
 * TODO: syoung 02/25/2020 Implement input items for Measurement types.
 */

/**
 * A measurement type is a human-data measurement such as height or weight.
 */
enum class MeasurementType : StringEnum {
    Height, Weight, BloodPressure;
}

/**
 * The measurement range is used to determine units that are appropriate to the size of the person.
 */
enum class MeasurementRange : StringEnum {
    Adult, Child, Infant;
}


