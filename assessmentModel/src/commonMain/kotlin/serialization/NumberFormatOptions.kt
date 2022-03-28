package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.sagebionetworks.assessmentmodel.DateUtils
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
     * A label for the minimum value.
     */
    var minimumLabel: String? = null

    /**
     * A label for the maximum value.
     */
    var maximumLabel: String? = null

    /**
     * Hint to use for the formatter.
     */
    @Serializable
    enum class Style(override val serialName: String? = null) : StringEnum {
        None,
        Decimal,
        Currency,
        Percent,
        Scientific,
        SpellOut,
        OrdinalNumber("ordinal"),
        ;

        @Serializer(forClass = Style::class)
        companion object : KSerializer<Style> {
            override val descriptor: SerialDescriptor =
                PrimitiveSerialDescriptor("Style", PrimitiveKind.STRING)
            override fun deserialize(decoder: Decoder): Style {
                val name = decoder.decodeString()

                return values().matching(name) ?: throw SerializationException("Unknown $name for ${descriptor.serialName}. Needs to be one of ${values()}")
            }
            override fun serialize(encoder: Encoder, value: Style) {
                encoder.encodeString(value.serialName ?: value.name)
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
data class IntFormatOptions(override val numberStyle: Style = Style.None,
                            override val usesGroupingSeparator: Boolean = true,
                            override var minimumValue: Int? = null,
                            override var maximumValue: Int? = null,
                            override var stepInterval: Int? = 1) : NumberFormatOptions<Int>() {
    // For an Int, this value is always 0.
    override val maximumFractionDigits: Int
        get() = 0

    override val numberType: NumberType
        get() = NumberType.INT
}

@Serializable
data class YearFormatOptions(var allowFuture: Boolean = true,
                             var allowPast: Boolean = true,
                             var minimumYear: Int? = null,
                             var maximumYear: Int? = null) : NumberFormatOptions<Int>() {

    override val minimumValue: Int?
        get() = minimumYear ?: if (allowPast) 1000 else _currentYear    // Require the year to be yyyy

    override val maximumValue: Int?
        get() = maximumYear ?: if (allowFuture) null else _currentYear

    private val _currentYear =  Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year

    override val stepInterval: Int?
        get() = 1
    override val usesGroupingSeparator: Boolean
        get() = false
    override val numberStyle: Style
        get() = Style.None
    override val maximumFractionDigits: Int
        get() = 0

    override val numberType: NumberType
        get() = NumberType.INT
}

expect class IntFormatter(formatOptions: NumberFormatOptions<Int>) : NumberFormatter<Int>

@Serializable
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