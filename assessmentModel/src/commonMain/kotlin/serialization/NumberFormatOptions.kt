package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import org.sagebionetworks.assessmentmodel.StringEnum
import org.sagebionetworks.assessmentmodel.matching
import org.sagebionetworks.assessmentmodel.survey.*

@Serializable
abstract class NumberFormatOptions<T> : NumberRange<T> where T : Comparable<T>, T : Number {

    /**
     * The style of the number. Only certain styles are applicable to certain types of numbers.
     */
    abstract val numberStyle: Style

    /**
     * The number of digits to include after the decimal point.
     */
    abstract val maximumFractionDigits: Int

    /**
     * Does the format include using a separator for the number? For example, "1,000" uses a grouping separator while
     * "1000" does not.
     */
    abstract val usesGroupingSeparator: Boolean

    /**
     * Serializable properties of [NumberRange] --[minimumValue], [maximumValue], and [stepInterval] are only
     * serializable as non-generics on the implementing subclass.
     */
    override var minInvalidMessage: InvalidMessageObject? = null
    override var maxInvalidMessage: InvalidMessageObject? = null
    override var invalidMessage: InvalidMessageObject = InvalidMessageObject("The number entered is not valid.")

    /**
     * Hint to use for the formatter.
     */
    @Serializable
    enum class Style : StringEnum {
        None, Decimal, Currency, Percent, Scientific, SpellOut, Ordinal;

        @Serializer(forClass = Style::class)
        companion object : KSerializer<Style> {
            override val descriptor: SerialDescriptor = StringDescriptor.withName("Style")
            override fun deserialize(decoder: Decoder): Style {
                val name = decoder.decodeString()
                return values().matching(name) ?: throw SerializationException("Unknown $name for ${descriptor.name}. Needs to be one of ${values()}")
            }
            override fun serialize(encoder: Encoder, obj: Style) {
                encoder.encodeString(obj.name)
            }
        }
    }
}

/**
 * The [NumberFormatter] is a wrapper that allows for a different implementation for number formatting and localization
 * depending upon the platform.
 *
 * @see [IntFormatter], [DoubleFormatter]
 */
expect abstract class NumberFormatter<T>(formatOptions: NumberFormatOptions<T>)
    : TextValidator<T> where T : Comparable<T>, T : Number

@Serializable
@SerialName("integer")
data class IntFormatOptions(override val numberStyle: Style = Style.None,
                            override val usesGroupingSeparator: Boolean = true) : NumberFormatOptions<Int>() {
    override var stepInterval: Int? = null
    override var minimumValue: Int? = null
    override var maximumValue: Int? = null

    // For an Int, this value is always 0.
    override val maximumFractionDigits: Int
        get() = 0

    override val numberType: NumberType
        get() = NumberType.INT
}

expect class IntFormatter(formatOptions: NumberFormatOptions<Int>) : NumberFormatter<Int>

@Serializable
@SerialName("double")
data class DoubleFormatOptions(override val numberStyle: Style = Style.Decimal,
                               override val usesGroupingSeparator: Boolean = true,
                               override val maximumFractionDigits: Int = 2) : NumberFormatOptions<Double>() {

    override var stepInterval: Double? = null
    override var minimumValue: Double? = null
    override var maximumValue: Double? = null

    override val numberType: NumberType
        get() = NumberType.DOUBLE
}

expect class DoubleFormatter(formatOptions: NumberFormatOptions<Double>) : NumberFormatter<Double>
