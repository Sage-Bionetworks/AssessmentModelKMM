package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.json.*
import org.sagebionetworks.assessmentmodel.survey.FormattedValue
import org.sagebionetworks.assessmentmodel.survey.NumberType
import org.sagebionetworks.assessmentmodel.survey.TextValidator
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter

// TODO: syoung 02/13/2020 Write unit tests for these formatters.

@ExperimentalUnsignedTypes
actual abstract class NumberFormatter<T> actual constructor(formatOptions: NumberFormatOptions<T>)
    : TextValidator<T> where T : Comparable<T>, T : Number  {

    private val options: NumberFormatOptions<T> = formatOptions
    private val formatter: NSNumberFormatter = NSNumberFormatter()

    init {
        formatter.maximumFractionDigits = formatOptions.maximumFractionDigits.toULong()
        formatter.generatesDecimalNumbers = when(formatOptions.numberType) {
            NumberType.DOUBLE, NumberType.FLOAT -> true
            else -> false
        }
        formatter.numberStyle = formatOptions.numberStyle.ordinal.toULong()
        formatter.usesGroupingSeparator = formatOptions.usesGroupingSeparator
    }

    override fun valueFor(text: String): FormattedValue<T>?
            = if (text.isEmpty()) FormattedValue() else options.validate(toType(formatter.numberFromString(text)))

    override fun localizedStringFor(value: T?): FormattedValue<String>
            = FormattedValue(value?.let { formatter.stringFromNumber(toNSNumber(it)) })

    abstract fun toNSNumber(value: T): NSNumber
    abstract fun toType(value: NSNumber?): T?
}

@ExperimentalUnsignedTypes
actual class IntFormatter actual constructor(formatOptions: NumberFormatOptions<Int>)
    : NumberFormatter<Int>(formatOptions = formatOptions) {
    override fun toNSNumber(value: Int): NSNumber = NSNumber(value)
    override fun toType(value: NSNumber?): Int? = value?.intValue
    override fun jsonValueFor(value: Int?): JsonElement? = JsonPrimitive(value)
    override fun valueFor(jsonValue: JsonElement?): Int? = jsonValue?.int
}

@ExperimentalUnsignedTypes
actual class DoubleFormatter actual constructor(formatOptions: NumberFormatOptions<Double>)
    : NumberFormatter<Double>(formatOptions = formatOptions) {
    override fun toNSNumber(value: Double): NSNumber = NSNumber(value)
    override fun toType(value: NSNumber?): Double? = value?.doubleValue
    override fun jsonValueFor(value: Double?): JsonElement? = JsonPrimitive(value)
    override fun valueFor(jsonValue: JsonElement?): Double? = jsonValue?.double
}