package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.json.*
import org.sagebionetworks.assessmentmodel.survey.FormattedValue
import org.sagebionetworks.assessmentmodel.survey.NumberType
import org.sagebionetworks.assessmentmodel.survey.TextValidator
import java.text.NumberFormat
import java.text.ParseException

//TODO: syoung 02/13/2020 implment the other styles of formatting. (Spellout, Scientific, Ordinal)

actual abstract class NumberFormatter<T> actual constructor(formatOptions: NumberFormatOptions<T>)
    : TextValidator<T> where T : Comparable<T>, T : Number  {

    private val options: NumberFormatOptions<T> = formatOptions
    private val formatter: NumberFormat = when {
        formatOptions.numberStyle == NumberFormatOptions.Style.Percent -> NumberFormat.getPercentInstance()
        formatOptions.numberStyle == NumberFormatOptions.Style.Currency -> NumberFormat.getCurrencyInstance()
        formatOptions.numberType == NumberType.INT -> NumberFormat.getIntegerInstance()
        else -> NumberFormat.getNumberInstance()
    }

    init {
        formatter.isGroupingUsed = formatOptions.usesGroupingSeparator
        formatter.maximumFractionDigits = formatOptions.maximumFractionDigits
    }

    override fun valueFor(text: String): FormattedValue<T>?
            = if (text.isEmpty()) FormattedValue() else options.validate(toType(parseText(text)))

    override fun jsonValueFor(value: T?): JsonElement? = JsonPrimitive(value)

    override fun localizedStringFor(value: T?): FormattedValue<String>
            = FormattedValue(value?.let { formatter.format(it) })

    private fun parseText(text: String) = try { formatter.parse(text) } catch (err: ParseException) { null }

    abstract fun toType(value: Number?): T?
}

actual class IntFormatter actual constructor(formatOptions: NumberFormatOptions<Int>) : NumberFormatter<Int>(formatOptions = formatOptions) {
    override fun toType(value: Number?): Int? = value?.toInt()
    override fun valueFor(jsonValue: JsonElement?): Int? = jsonValue?.jsonPrimitive?.intOrNull
}

actual class DoubleFormatter actual constructor(formatOptions: NumberFormatOptions<Double>) : NumberFormatter<Double>(formatOptions = formatOptions) {
    override fun toType(value: Number?): Double? = value?.toDouble()
    override fun valueFor(jsonValue: JsonElement?): Double? = jsonValue?.jsonPrimitive?.doubleOrNull
}