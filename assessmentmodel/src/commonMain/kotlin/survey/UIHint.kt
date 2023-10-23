package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.sagebionetworks.assessmentmodel.*

/**
 * The [UIHint] enum is a key word that can be used to describe the preferred UI for an [InputItem]. This is intended as
 * a "hint" that the designers and developers can use to indicate the preferred input display style. Not all UI hints
 * are applicable to all data types or devices, and therefore the UI hint may be ignored by the application displaying
 * the [InputItem] to the user.
 *
 * On iOS this will typically map to the `reuseIdentifier` for a `UITableViewCell`.
 */
@Serializable
sealed class UIHint : StringEnum {

    /**
     * UI hints for showing choices that are supported by this library.
     */
    sealed class Choice(override val name: String) : UIHint() {

        /**
         * List with a checkbox to the left to each item.
         */
        object Checkbox: Choice("checkbox")

        /**
         * List with a radio buttons to the left of each item.
         */
        object RadioButton: Choice("radioButton")

        companion object : StringEnumCompanion<Choice> {
            override fun values(): Array<Choice>
                    = arrayOf(Checkbox, RadioButton)
        }
    }

    /**
     * UI hints for entering a number that are supported by this library.
     */
    sealed class NumberField(override val name: String) : UIHint() {

        /**
         * Show a number slider.
         */
        object Slider: NumberField("slider")

        /**
         * Show a Likert scale.
         */
        object Likert: NumberField("likert")

        companion object : StringEnumCompanion<NumberField> {
            override fun values(): Array<NumberField>
                    = arrayOf(Slider, Likert)
        }
    }

    /**
     * UI hints for entering text that are supported by this library.
     */
    sealed class StringField(override val name: String) : UIHint() {

        /**
         * Multiple line text view.
         */
        object MultipleLine: StringField("multipleLine")

        companion object : StringEnumCompanion<StringField> {
            override fun values(): Array<StringField>
                    = arrayOf(MultipleLine)
        }
    }

    /**
     * Default text field with a keyboard.
     */
    object TextField: UIHint() {
        override val name: String
            get() = "textfield"
    }

    data class Custom(override val name: String) : UIHint()

    @Serializer(forClass = UIHint::class)
    companion object : KSerializer<UIHint> {
        override val descriptor: SerialDescriptor
                = PrimitiveSerialDescriptor("UIHint", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): UIHint {
            val name = decoder.decodeString()
            return valueOf(name)
        }
        override fun serialize(encoder: Encoder, value: UIHint) {
            encoder.encodeString(value.name)
        }
        fun valueOf(name: String): UIHint {
            return when {
                (name.lowercase() == TextField.name.lowercase()) -> TextField
                else -> Choice.valueOf(name) ?: StringField.valueOf(name) ?: NumberField.valueOf(name) ?: Custom(name)
            }
        }
    }
}
