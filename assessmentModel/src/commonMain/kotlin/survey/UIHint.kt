package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.*
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
     * A choice UI hint maps to a list of one or more [ChoiceInputItem] items. The hint indicates the type of
     * selection indicator that should be used. This hint should be applied to *all* the fields of the
     * [ChoiceInputItem] subtype associated with a given [Question].
     */
    @Serializable
    sealed class Choice(override val name: String) : UIHint() {

        /**
         * List with a checkbox to the left to each item.
         */
        object Checkbox: Choice("checkbox")

        /**
         * List with a checkmark to the right of each item.
         */
        object Checkmark: Choice("checkmark")

        /**
         * List of selectable cells.
         */
        object ListItem: Choice("list")

        /**
         * List of radio buttons. This UI hint should only be used for [DataType.CollectionType.SingleChoice].
         */
        object RadioButton: Choice("radioButton")

        @Serializer(forClass = Choice::class)
        companion object : StringEnumCompanion<Choice>, KSerializer<Choice> {
            override fun values(): Array<Choice>
                    = arrayOf(ListItem, Checkmark, Checkbox, RadioButton)

            override val descriptor: SerialDescriptor = PrimitiveDescriptor("Choice", PrimitiveKind.STRING)
            override fun deserialize(decoder: Decoder): Choice {
                val name = decoder.decodeString()
                return values().matching(name) ?: throw SerializationException("Unknown $name for ${descriptor.serialName}. Needs to be one of ${values()}")
            }
            override fun serialize(encoder: Encoder, value: Choice) {
                encoder.encodeString(value.name)
            }
        }
    }

    /**
     * The [Detail] UI hint type maps to an [InputItem] where the input redirects to another screen or popover that
     * includes a more detailed UI.
     *
     * For example, the participant may be asked to enter details about a doctor's appointment where they fill in the
     * results from that visit. As a part of the form, they are asked to enter the date of the appointment and may be
     * directed to a second screen that shows a calendar. On the form, there would need to be a field that shows the
     * result of the calendar input or [InputItem.placeholder] text if the value has not yet been entered.
     */
    sealed class Detail(override val name: String) : UIHint() {

        /**
         * Input field of a button-style cell that can be used to display a detail view.
         */
        object Button: Detail("button")

        /**
         * Input field of a disclosure arrow cell that can be used to display a detail view.
         */
        object DisclosureArrow: Detail("disclosureArrow")

        /**
         * Input field of a link-style cell that can be used to display a detail view.
         */
        object Link: Detail("link")

        companion object : StringEnumCompanion<Detail> {
            override fun values(): Array<Detail>
                    = arrayOf(DisclosureArrow, Button, Link)
        }
    }

    /**
     * A text field shows a input field that indicates that the user could tap into it and input a value. On certain
     * devices, the field may use a different class of UI component to implement the input field. This is intended to
     * hint at what the preferred layout will be.
     */
    sealed class TextField(override val name: String) : UIHint() {

        /**
         * Default text field with a keyboard.
         */
        object Default: TextField("textfield")

        /**
         * Multiple line text view.
         */
        object MultipleLine: TextField("multipleLine")

        /**
         * Entry using a modal popover box. The popover can contextually use a picker wheel, calendar, text field, etc.
         */
        object Popover: TextField("popover")

        @Serializer(forClass = TextField::class)
        companion object : StringEnumCompanion<TextField>, KSerializer<TextField> {
            override fun values(): Array<TextField>
                    = arrayOf(Default, MultipleLine, Popover)

            override val descriptor: SerialDescriptor = PrimitiveDescriptor("TextField", PrimitiveKind.STRING)
            override fun deserialize(decoder: Decoder): TextField {
                val name = decoder.decodeString()
                return TextField.values().matching(name) ?: throw SerializationException("Unknown $name for ${descriptor.serialName}. Needs to be one of ${values()}")
            }
            override fun serialize(encoder: Encoder, value: TextField) {
                encoder.encodeString(value.name)
            }
        }
    }

    /**
     * Text field with a picker wheel as the keyboard.
     *
     * TODO: syoung 02/12/2020 Decide if we want to deprecate this ui hint. Replacing the keyboard with a picker wheel is so last season.
     */
    object Picker: UIHint() {
        override val name: String
            get() = "picker"
    }

    data class Custom(override val name: String) : UIHint()

    @Serializer(forClass = UIHint::class)
    companion object : KSerializer<UIHint> {
        override val descriptor: SerialDescriptor
                = PrimitiveDescriptor("UIHint", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): UIHint {
            val name = decoder.decodeString()
            return valueOf(name)
        }
        override fun serialize(encoder: Encoder, value: UIHint) {
            encoder.encodeString(value.name)
        }
        fun valueOf(name: String): UIHint {
            return when {
                (name.toLowerCase() == Picker.name.toLowerCase()) -> Picker
                else -> Choice.valueOf(name) ?: Detail.valueOf(name) ?: TextField.valueOf(name) ?: Custom(name)
            }
        }
    }
}
