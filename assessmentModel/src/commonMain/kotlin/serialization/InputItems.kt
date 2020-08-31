package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.modules.*
import kotlinx.serialization.modules.subclass
import org.sagebionetworks.assessmentmodel.StringEnum
import org.sagebionetworks.assessmentmodel.survey.*
import org.sagebionetworks.assessmentmodel.survey.AnswerType

val inputItemSerializersModule = SerializersModule {
    polymorphic(InputItem::class) {
        subclass(CheckboxInputItemObject::class)
        subclass(DateInputItemObject::class)
        subclass(DecimalTextInputItemObject::class)
        subclass(IntegerTextInputItemObject::class)
        subclass(SkipCheckboxInputItemObject::class)
        subclass(StringTextInputItemObject::class)
        subclass(TimeInputItemObject::class)
        subclass(YearTextInputItemObject::class)
    }
    polymorphic(KeyboardTextInputItem::class) {
        subclass(DateInputItemObject::class)
        subclass(DecimalTextInputItemObject::class)
        subclass(IntegerTextInputItemObject::class)
        subclass(StringTextInputItemObject::class)
        subclass(TimeInputItemObject::class)
        subclass(YearTextInputItemObject::class)
    }
    polymorphic(SkipCheckboxInputItem::class) {
        subclass(SkipCheckboxInputItemObject::class)
    }
    polymorphic(DateTimeFormatOptions::class) {
        subclass(DateFormatOptions::class)
        subclass(TimeFormatOptions::class)
    }
}

/**
 * An [InputItemObject] is intended to implement shared code for serialization of the simple data types.
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
data class KeyboardOptionsObject(override val isSecureTextEntry: Boolean = false,
                                 override val autocapitalizationType: AutoCapitalizationType = AutoCapitalizationType.None,
                                 override val autocorrectionType: AutoCorrectionType = AutoCorrectionType.Default,
                                 override val spellCheckingType: SpellCheckingType = SpellCheckingType.Default,
                                 override val keyboardType: KeyboardType = KeyboardType.Default) : KeyboardOptions {
    companion object {
        val NumberEntryOptions = KeyboardOptionsObject(
                autocorrectionType = AutoCorrectionType.No,
                spellCheckingType = SpellCheckingType.No,
                keyboardType = KeyboardType.NumberPad)
        val DecimalEntryOptions = KeyboardOptionsObject(
                autocorrectionType = AutoCorrectionType.No,
                spellCheckingType = SpellCheckingType.No,
                keyboardType = KeyboardType.DecimalPad)
        val DateTimeEntryOptions = KeyboardOptionsObject(
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

    override val keyboardOptions: KeyboardOptions
        get() = KeyboardOptionsObject.DecimalEntryOptions
    override fun buildTextValidator(): TextValidator<Double> = DoubleFormatter(formatOptions)
}

@Serializable
@SerialName("integer")
data class IntegerTextInputItemObject(@SerialName("identifier")
                                      override val resultIdentifier: String? = null,
                                      @SerialName("keyboardOptions")
                                      var textOptions: KeyboardOptionsObject = KeyboardOptionsObject.NumberEntryOptions,
                                      var formatOptions: IntFormatOptions = IntFormatOptions())
    : InputItemObject(), KeyboardTextInputItem<Int> {
    override val answerType: AnswerType
        get() = AnswerType.INTEGER

    override val keyboardOptions: KeyboardOptions
        get() = textOptions
    override fun buildTextValidator(): TextValidator<Int> = IntFormatter(formatOptions)
}

@Serializable
@SerialName("string")
data class StringTextInputItemObject(@SerialName("identifier")
                                     override val resultIdentifier: String? = null,
                                     @SerialName("keyboardOptions")
                                     var textOptions: KeyboardOptionsObject = KeyboardOptionsObject(),
                                     var regExValidator: RegExValidator? = null)
    : InputItemObject(), KeyboardTextInputItem<String> {
    override val answerType: AnswerType
        get() = AnswerType.STRING

    override val keyboardOptions: KeyboardOptions
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

    override val keyboardOptions: KeyboardOptions
        get() = KeyboardOptionsObject.NumberEntryOptions
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

///**
// * A measurement type is a human-data measurement such as height or weight.
// */
//enum class MeasurementType : StringEnum {
//    Height, Weight, BloodPressure;
//}
//
///**
// * The measurement range is used to determine units that are appropriate to the size of the person.
// */
//enum class MeasurementRange : StringEnum {
//    Adult, Child, Infant;
//}

