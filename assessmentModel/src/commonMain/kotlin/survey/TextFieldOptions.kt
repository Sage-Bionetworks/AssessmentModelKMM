package org.sagebionetworks.assessmentmodel.forms

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import org.sagebionetworks.assessmentmodel.StringEnum
import org.sagebionetworks.assessmentmodel.matching

/**
 * Defines the options that can be used for a text field.
 */
interface TextFieldOptions {

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
    None, Words, Sentences, AllCharacters;

    @Serializer(forClass = AutoCapitalizationType::class)
    companion object : KSerializer<AutoCapitalizationType>{
        override val descriptor: SerialDescriptor = StringDescriptor.withName("AutoCapitalizationType")
        override fun deserialize(decoder: Decoder): AutoCapitalizationType {
            val name = decoder.decodeString()
            return values().matching(name) ?: throw SerializationException("Unknown $name for ${descriptor.name}. Needs to be one of ${values()}")
        }
        override fun serialize(encoder: Encoder, obj: AutoCapitalizationType) {
            encoder.encodeString(obj.name)
        }
    }
}

@Serializable
enum class AutoCorrectionType : StringEnum {
    Default, No, Yes;

    @Serializer(forClass = AutoCorrectionType::class)
    companion object : KSerializer<AutoCorrectionType>{
        override val descriptor: SerialDescriptor = StringDescriptor.withName("AutoCorrectionType")
        override fun deserialize(decoder: Decoder): AutoCorrectionType {
            val name = decoder.decodeString()
            return values().matching(name) ?: throw SerializationException("Unknown $name for ${descriptor.name}. Needs to be one of ${values()}")
        }
        override fun serialize(encoder: Encoder, obj: AutoCorrectionType) {
            encoder.encodeString(obj.name)
        }
    }
}

@Serializable
enum class SpellCheckingType : StringEnum {
    Default, No, Yes;

    @Serializer(forClass = SpellCheckingType::class)
    companion object : KSerializer<SpellCheckingType>{
        override val descriptor: SerialDescriptor = StringDescriptor.withName("KeyboardType")
        override fun deserialize(decoder: Decoder): SpellCheckingType {
            val name = decoder.decodeString()
            return values().matching(name) ?: throw SerializationException("Unknown $name for ${descriptor.name}. Needs to be one of ${values()}")
        }
        override fun serialize(encoder: Encoder, obj: SpellCheckingType) {
            encoder.encodeString(obj.name)
        }
    }
}

@Serializable
enum class KeyboardType : StringEnum {
    Default,
    AsciiCapable,
    NumbersAndPunctuation,
    URL,
    NumberPad,
    PhonePad,
    NamePhonePad,
    EmailAddress,
    DecimalPad,
    Twitter,
    WebSearch,
    AsciiCapableNumberPad,
    ;

    @Serializer(forClass = KeyboardType::class)
    companion object : KSerializer<KeyboardType>{
        override val descriptor: SerialDescriptor = StringDescriptor.withName("KeyboardType")
        override fun deserialize(decoder: Decoder): KeyboardType {
            val name = decoder.decodeString()
            return values().matching(name) ?: throw SerializationException("Unknown $name for ${descriptor.name}. Needs to be one of ${ values() }")
        }
        override fun serialize(encoder: Encoder, obj: KeyboardType) {
            encoder.encodeString(obj.name)
        }
    }
}