package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.PrimitiveKind
import kotlin.reflect.KClass

/**
 * A [Range] can be used to compare an entered value to a minimum and maximum value. It can also be used to set up a
 * slider or picker UI element.
 */
interface Range<T : Comparable<T>> {

    /**
     * The minimum allowed number. When the value of this property is `nil`, there is no minimum.
     */
    val minimumValue: T?

    /**
     * The maximum allowed number. When the value of this property is `nil`, there is no maximum.
     */
    val maximumValue: T?

    /**
     * The message to show to the participant if the number entered is less than [minimumValue].
     */
    val minInvalidMessage: InvalidMessage?

    /**
     * The message to show to the participant if the number entered is greater than [maximumValue].
     */
    val maxInvalidMessage: InvalidMessage?

    /**
     * The message to show to the participant if the text entered does not resolve to a valid number.
     */
    val invalidMessage: InvalidMessage

    /**
     * Check the given number against the minimum and maximum values. The default will return the [invalidMessage] if
     * the number is null.
     */
    fun validate(number: T?) : FormattedValue<T> {
        return number?.let { value ->
            when {
                minimumValue?.let { value < it } ?: false -> FormattedValue<T>(invalidMessage = minInvalidMessage
                        ?: invalidMessage)
                maximumValue?.let { value > it } ?: false -> FormattedValue<T>(invalidMessage = maxInvalidMessage
                        ?: invalidMessage)
                else -> FormattedValue(value)
            }
        } ?: run {
            FormattedValue<T>(invalidMessage = invalidMessage)
        }
    }
}

/**
 * A [NumberRange] is assumed to be representable using a number. This could be an integer, double, or time interval.
 */
interface NumberRange<T> : Range<T> where T : Comparable<T>, T : Number {

    /**
     *  A step interval to be used for a slider or picker.
     */
    val stepInterval: T?

    /**
     * The [NumberType] for this [NumberRange].
     */
    val numberType: NumberType
}

enum class NumberType {
    INT, DOUBLE, SHORT, LONG, FLOAT;
}