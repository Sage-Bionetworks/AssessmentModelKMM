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
    AsciiCapableNumberPad;

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

// TODO: syoung 02/06/2020 Figure out what the patten is for including information about why an input is not valid.
//
//
///// A custom text validator that can be used to validate a string.
//var textValidator: RSDTextValidator? { get }
//
///// The localized message presented to the user when invalid input is received.
//var invalidMessage: String? { get }
//
//public protocol RSDTextValidator {
//
//    /// Whether or not the text is considered valid.
//    /// - returns: `true` if the string is valid. Otherwise, returns `false`.
//    /// - throws: Error if the regular expression cannot be instantiated.
//    func isValid(_ string: String) throws -> Bool
//}
//
//public protocol RSDRegExMatchValidator : RSDTextValidator {
//
//    /// A localized custom regular expression that can be used to validate a string.
//    /// - returns: The regular expression to use in validation.
//    /// - throws: Error if the regular expression cannot be instantiated.
//    func regularExpression() throws -> NSRegularExpression
//}
//
//public protocol RSDCodableRegExMatchValidator : RSDRegExMatchValidator {
//
//    /// The regular expression pattern used to create the `NSRegularExpression` object.
//    var regExPattern: String { get }
//}
//
//extension RSDCodableRegExMatchValidator {
//
//    /// A localized custom regular expression that can be used to validate a string.
//    public func regularExpression() throws -> NSRegularExpression {
//        return try NSRegularExpression(pattern: regExPattern, options: [])
//        }
//}
//
//extension RSDRegExMatchValidator {
//
//    /// Method for evaluating a string against the `validationRegex` for a match.
//    /// - paramater string: The string to evaluate.
//    /// - returns: The number of matches found.
//    /// - throws: If the regular expression cannot be created.
//    public func regExMatches(_ string: String) throws -> Int {
//        let expression = try regularExpression()
//        return expression.numberOfMatches(in: string, options: [], range: NSRange(string.startIndex..., in: string))
//    }
//
//    /// Test the string against the validation regular expression and return `true` if there is one or more
//    /// matches to the given string.
//    /// - paramater string: The string to evaluate.
//    /// - returns: Whether or not the string is valid.
//    /// - throws: If the regular expression cannot be created.
//    public func isValid(_ string: String) throws -> Bool {
//        let count = try self.regExMatches(string)
//        return count > 0
//    }
//}
