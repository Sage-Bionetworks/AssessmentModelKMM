package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.sagebionetworks.assessmentmodel.StringEnum
import org.sagebionetworks.assessmentmodel.matching

/**
 * Defines the options that can be used for a text field.
 */
interface KeyboardOptions {

    /**
     * Is the text field for password entry?
     */
    val isSecureTextEntry: Boolean

    /**
     * Auto-capitalization type for the text field.
     */
    val autocapitalizationType: AutoCapitalizationType

    /**
     * Auto-correction type for the text field.
     */
    val autocorrectionType: AutoCorrectionType

    /**
     * Spell checking type for the text field.
     */
    val spellCheckingType: SpellCheckingType

    /**
     * Keyboard type for the text field.
     *
     * For devices that do not support all these keyboard types (or where its use is not applicable) the OS is
     * responsible for determining what is the best mapping.
     */
    val keyboardType: KeyboardType
}

@Serializable
enum class AutoCapitalizationType : StringEnum {
    @SerialName("none")
    None,
    @SerialName("words")
    Words,
    @SerialName("sentences")
    Sentences,
    @SerialName("allCharacters")
    AllCharacters;
}

@Serializable
enum class AutoCorrectionType : StringEnum {
    @SerialName("default")
    Default,
    @SerialName("no")
    No,
    @SerialName("yes")
    Yes;

}

@Serializable
enum class SpellCheckingType : StringEnum {
    @SerialName("default")
    Default,
    @SerialName("no")
    No,
    @SerialName("yes")
    Yes;
}

@Serializable
enum class KeyboardType : StringEnum {
    @SerialName("default")
    Default,
    @SerialName("asciiCapable")
    AsciiCapable,
    @SerialName("numbersAndPunctuation")
    NumbersAndPunctuation,
    @SerialName("URL")
    URL,
    @SerialName("numberPad")
    NumberPad,
    @SerialName("phonePad")
    PhonePad,
    @SerialName("namePhonePad")
    NamePhonePad,
    @SerialName("emailAddress")
    EmailAddress,
    @SerialName("decimalPad")
    DecimalPad,
    @SerialName("twitter")
    Twitter,
    @SerialName("webSearch")
    WebSearch,
    @SerialName("asciiCapableNumberPad")
    AsciiCapableNumberPad,
    ;
}