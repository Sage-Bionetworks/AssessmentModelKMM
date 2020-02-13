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
    polymorphic(NumberFormatOptions::class) {
        IntFormatOptions::class with IntFormatOptions.serializer()
        DoubleFormatOptions::class with DoubleFormatOptions.serializer()
    }
}

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
    override val exclusive: Boolean = false
    override var uiHint: UIHint.TextField = UIHint.TextField.Default
}

@Serializable
@SerialName("string")
data class StringTextInputItemObject(
        @SerialName("identifier")
        override val resultIdentifier: String? = null) : InputItemObject(), KeyboardTextInputItem<String> {
    override var textFieldOptions: TextFieldOptionsObject = TextFieldOptionsObject()
    var regExValidator: RegExValidator? = null

    override val answerKind: SerialKind
        get() = PrimitiveKind.STRING

    override fun getTextValidator(): TextValidator<String>? = regExValidator
}

@Serializable
@SerialName("integer")
data class IntegerTextInputItemObject(@SerialName("identifier")
                                  override val resultIdentifier: String? = null) : InputItemObject(), KeyboardTextInputItem<Int> {
    override var textFieldOptions: TextFieldOptionsObject = TextFieldOptionsObject.NumberEntryOptions
    var formatOptions: IntFormatOptions = IntFormatOptions()

    override val answerKind: SerialKind
        get() = PrimitiveKind.INT

    override fun getTextValidator(): TextValidator<Int>? = IntFormatter(formatOptions)
}

@Serializable
@SerialName("year")
data class YearTextInputItemObject(@SerialName("identifier")
                                  override val resultIdentifier: String? = null) : InputItemObject(), KeyboardTextInputItem<Int> {
    override val answerKind: SerialKind
        get() = PrimitiveKind.INT

    // Text field options and the validator are not read/write or serializable.
    override val textFieldOptions: TextFieldOptionsObject
        get() = TextFieldOptionsObject.NumberEntryOptions
    override fun getTextValidator(): TextValidator<Int>? = IntFormatter(IntFormatOptions(usesGroupingSeparator = false))
}

@Serializable
@SerialName("decimal")
data class DecimalTextInputItemObject(@SerialName("identifier")
                                  override val resultIdentifier: String? = null) : InputItemObject(), KeyboardTextInputItem<Double> {
    override var textFieldOptions: TextFieldOptionsObject = TextFieldOptionsObject.NumberEntryOptions
    var formatOptions: DoubleFormatOptions = DoubleFormatOptions()

    override val answerKind: SerialKind
        get() = PrimitiveKind.INT

    override fun getTextValidator(): TextValidator<Double>? = DoubleFormatter(formatOptions)
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
        val NameEntryOptions = TextFieldOptionsObject(
                autocorrectionType = AutoCorrectionType.No,
                spellCheckingType = SpellCheckingType.No)
    }
}

@Serializable
data class RegExValidator(val pattern: String, val invalidMessage: InvalidMessageObject) : TextValidator<String> {
    override fun valueFor(text: String): FormattedValue<String>? {
        val regex = Regex(pattern = pattern)
        return if (regex.matches(text)) FormattedValue(result = text) else FormattedValue(invalidMessage = invalidMessage)
    }
    override fun localizedStringFor(value: String?) = FormattedValue(result = value)
}