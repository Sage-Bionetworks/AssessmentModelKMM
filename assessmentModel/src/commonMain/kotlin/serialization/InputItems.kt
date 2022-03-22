package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.modules.*
import kotlinx.serialization.modules.subclass
import org.sagebionetworks.assessmentmodel.survey.*
import org.sagebionetworks.assessmentmodel.survey.AnswerType

val inputItemSerializersModule = SerializersModule {
    polymorphic(InputItem::class) {
        subclass(CheckboxInputItemObject::class)
        subclass(DateInputItemObject::class)
        subclass(DoubleTextInputItemObject::class)
        subclass(IntegerTextInputItemObject::class)
        subclass(StringTextInputItemObject::class)
        subclass(TimeInputItemObject::class)
        subclass(YearTextInputItemObject::class)
    }
    polymorphic(KeyboardTextInputItem::class) {
        subclass(DateInputItemObject::class)
        subclass(DoubleTextInputItemObject::class)
        subclass(IntegerTextInputItemObject::class)
        subclass(StringTextInputItemObject::class)
        subclass(TimeInputItemObject::class)
        subclass(YearTextInputItemObject::class)
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
abstract class InputItemObject<T> : KeyboardTextInputItem<T> {
    override var fieldLabel: String? = null
    override var placeholder: String? = null
    override var optional: Boolean = true
    override var exclusive: Boolean = false
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
@SerialName("number")
data class DoubleTextInputItemObject(@SerialName("identifier")
                                      override val resultIdentifier: String? = null,
                                     var formatOptions: DoubleFormatOptions = DoubleFormatOptions())
    : InputItemObject<Double>() {
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
    : InputItemObject<Int>() {
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
    : InputItemObject<String>() {
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
    : InputItemObject<Int>() {
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
    : InputItemObject<String>(), DateTimeInputItem

@Serializable
@SerialName("time")
data class TimeInputItemObject(@SerialName("identifier")
                                   override val resultIdentifier: String? = null,
                                   override var formatOptions: TimeFormatOptions = TimeFormatOptions())
    : InputItemObject<String>(), DateTimeInputItem

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
@SerialName("checkbox")
data class CheckboxInputItemObject(@SerialName("identifier")
                                   override val resultIdentifier: String,
                                   override val label: String) : CheckboxInputItem

@Serializable
data class JsonChoiceObject(val value: JsonElement = JsonNull,
                            val text: String? = null,
                            @SerialName("icon")
                            override val iconName: String? = null,
                            override val selectorType: ChoiceSelectorType = ChoiceSelectorType.Default,
                            override val detail: String? = null) : ChoiceOption {
    override val label: String
        get() = text ?: value.toString()
    override fun jsonValue(selected: Boolean): JsonElement? = if (selected) value else null
}

/**
 * A [ChoiceItemWrapper] is used to wrap serializable [ChoiceOption] items that have a shared
 * [answerType] for either a single or multiple choice question.
 *
 * This is used to work around a limitation of [JsonElement] where it does not have a json type
 * defined for the element. In other words, it does not conform to the Json Schema Draft 7 rules
 * used throughout this library for defining JSON serialization. syoung 03/10/2022
 */
data class ChoiceItemWrapper(val choice: ChoiceOption,
                             override val answerType: AnswerType) : ChoiceInputItem, ChoiceOption by choice {
    override val resultIdentifier: String?
        get() = (choice.jsonValue(true) ?: JsonNull).toString()
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

