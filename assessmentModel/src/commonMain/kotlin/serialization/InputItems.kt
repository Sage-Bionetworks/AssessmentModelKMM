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
        CheckboxInputItemObject::class with CheckboxInputItemObject.serializer()
        DateInputItemObject::class with DateInputItemObject.serializer()
        DecimalTextInputItemObject::class with DecimalTextInputItemObject.serializer()
        IntegerTextInputItemObject::class with IntegerTextInputItemObject.serializer()
        SkipCheckboxInputItemObject::class with SkipCheckboxInputItemObject.serializer()
        StringTextInputItemObject::class with StringTextInputItemObject.serializer()
        TimeInputItemObject::class with TimeInputItemObject.serializer()
        YearTextInputItemObject::class with YearTextInputItemObject.serializer()
    }
    polymorphic(KeyboardTextInputItem::class) {
        DateInputItemObject::class with DateInputItemObject.serializer()
        DecimalTextInputItemObject::class with DecimalTextInputItemObject.serializer()
        IntegerTextInputItemObject::class with IntegerTextInputItemObject.serializer()
        StringTextInputItemObject::class with StringTextInputItemObject.serializer()
        TimeInputItemObject::class with TimeInputItemObject.serializer()
        YearTextInputItemObject::class with YearTextInputItemObject.serializer()
    }
    polymorphic(SkipCheckboxInputItem::class) {
        SkipCheckboxInputItemObject::class with SkipCheckboxInputItemObject.serializer()
    }
    polymorphic(DateTimeFormatOptions::class) {
        DateFormatOptions::class with DateFormatOptions.serializer()
        TimeFormatOptions::class with TimeFormatOptions.serializer()
    }
}

// TODO: syoung 02/18/2020 Names of fields for serialization have changed from SageResearch to support kotlinx.
// "prompt" -> "fieldLabel"

/**
 * A [InputItemObject] is intended to implement shared code for serialization of the simple data types. This will
 * work to deserialize some of the existing input items that use the [DataType] to define their type.
 */
@Serializable
abstract class InputItemObject : InputItem {
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

    override val textFieldOptions: TextFieldOptions
        get() = TextFieldOptionsObject.DecimalEntryOptions
    override fun buildTextValidator(): TextValidator<Double> = DoubleFormatter(formatOptions)
}

@Serializable
@SerialName("integer")
data class IntegerTextInputItemObject(@SerialName("identifier")
                                      override val resultIdentifier: String? = null,
                                      @SerialName("textFieldOptions")
                                      var textOptions: TextFieldOptionsObject = TextFieldOptionsObject.NumberEntryOptions,
                                      var formatOptions: IntFormatOptions = IntFormatOptions())
    : InputItemObject(), KeyboardTextInputItem<Int> {
    override val answerType: AnswerType
        get() = AnswerType.INTEGER

    override val textFieldOptions: TextFieldOptions
        get() = textOptions
    override fun buildTextValidator(): TextValidator<Int> = IntFormatter(formatOptions)
}

@Serializable
@SerialName("string")
data class StringTextInputItemObject(@SerialName("identifier")
                                     override val resultIdentifier: String? = null,
                                     @SerialName("textFieldOptions")
                                     var textOptions: TextFieldOptionsObject = TextFieldOptionsObject(),
                                     var regExValidator: RegExValidator? = null)
    : InputItemObject(), KeyboardTextInputItem<String> {
    override val answerType: AnswerType
        get() = AnswerType.STRING

    override val textFieldOptions: TextFieldOptions
        get() = textOptions

    override fun buildTextValidator(): TextValidator<String> = regExValidator ?: PassThruTextValidator
}

@Serializable
data class RegExValidator(val pattern: String, val invalidMessage: InvalidMessageObject) : TextValidator<String> {
    override fun valueFor(text: String): FormattedValue<String>? {
        val regex = Regex(pattern = pattern)
        return if (regex.matches(text)) FormattedValue(result = text) else FormattedValue(invalidMessage = invalidMessage)
    }
    override fun localizedStringFor(value: String?) = FormattedValue(result = value)
    override fun jsonValueFor(value: String?): JsonElement? = JsonPrimitive(value)
    override fun valueFor(jsonValue: JsonElement?): String? = jsonValue?.toString()
}

@Serializable
@SerialName("year")
data class YearTextInputItemObject(@SerialName("identifier")
                                   override val resultIdentifier: String? = null,
                                   var formatOptions: YearFormatOptions = YearFormatOptions())
    : InputItemObject(), KeyboardTextInputItem<Int> {
    override val answerType: AnswerType
        get() = AnswerType.INTEGER

    override val textFieldOptions: TextFieldOptions
        get() = TextFieldOptionsObject.NumberEntryOptions
    override fun buildTextValidator(): TextValidator<Int> = IntFormatter(formatOptions)
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

@Serializable
@SerialName("skipCheckbox")
data class SkipCheckboxInputItemObject(override val fieldLabel: String,
                                       override val value: JsonElement = JsonNull) : SkipCheckboxInputItem

@Serializable
@SerialName("checkbox")
data class CheckboxInputItemObject(@SerialName("identifier")
                                   override val resultIdentifier: String,
                                   override val fieldLabel: String) : CheckboxInputItem

@Serializable
data class ChoiceOptionObject(val value: JsonElement = JsonNull,
                              @SerialName("text")
                              override val fieldLabel: String? = null,
                              @Serializable(ImageNameSerializer::class)
                              override val icon: FetchableImage? = null,
                              override val exclusive: Boolean = false,
                              override val detail: String? = null) : ChoiceOption {
    override fun jsonValue(selected: Boolean): JsonElement? = if (selected) value else null
}

/**
 * A [ChoiceItemWrapper] is used to wrap serializable [ChoiceOption] items that have a shared [uiHint] and [answerType]
 * for either a [singleChoice] or multiple choice question.
 */
data class ChoiceItemWrapper(val choice: ChoiceOption,
                             val singleChoice: Boolean,
                             override val answerType: AnswerType,
                             override val uiHint: UIHint) : ChoiceInputItem, ChoiceOption by choice {
    override val resultIdentifier: String?
        get() = (choice.jsonValue(true) ?: JsonNull).toString()
    override val exclusive: Boolean
        get() = singleChoice || choice.exclusive
    override val fieldLabel: String?
        get() = choice.fieldLabel
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


