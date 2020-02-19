package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.forms.*
import org.sagebionetworks.assessmentmodel.survey.*

val inputItemSerializersModule = SerializersModule {
    polymorphic(InputItem::class) {
        StringTextInputItemObject::class with StringTextInputItemObject.serializer()
        IntegerTextInputItemObject::class with IntegerTextInputItemObject.serializer()
        YearTextInputItemObject::class with YearTextInputItemObject.serializer()
        DecimalTextInputItemObject::class with DecimalTextInputItemObject.serializer()
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
    override val answerKind: SerialKind
        get() = PrimitiveKind.DOUBLE

    override val textFieldOptions: TextFieldOptionsObject
        get() = TextFieldOptionsObject.DecimalEntryOptions
    override fun getTextValidator(): TextValidator<Double>? = DoubleFormatter(formatOptions)
}

@Serializable
@SerialName("integer")
data class IntegerTextInputItemObject(@SerialName("identifier")
                                      override val resultIdentifier: String? = null,
                                      override var textFieldOptions: TextFieldOptionsObject = TextFieldOptionsObject.NumberEntryOptions,
                                      var formatOptions: IntFormatOptions = IntFormatOptions())
    : InputItemObject(), KeyboardTextInputItem<Int> {
    override val answerKind: SerialKind
        get() = PrimitiveKind.INT

    override fun getTextValidator(): TextValidator<Int>? = IntFormatter(formatOptions)
}

@Serializable
@SerialName("string")
data class StringTextInputItemObject(@SerialName("identifier")
                                     override val resultIdentifier: String? = null,
                                     override var textFieldOptions: TextFieldOptionsObject = TextFieldOptionsObject(),
                                     var regExValidator: RegExValidator? = null)
    : InputItemObject(), KeyboardTextInputItem<String> {
    override val answerKind: SerialKind
        get() = PrimitiveKind.STRING

    override fun getTextValidator(): TextValidator<String>? = regExValidator
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
    override val answerKind: SerialKind
        get() = PrimitiveKind.INT

    override val textFieldOptions: TextFieldOptionsObject
        get() = TextFieldOptionsObject.NumberEntryOptions
    override fun getTextValidator(): TextValidator<Int>? = IntFormatter(formatOptions)
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