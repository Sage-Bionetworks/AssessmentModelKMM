package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.*

// TODO: syoung 02/17/2020 WIP. Currently blocked from supporting date ranges on text field b/c I can't figure out the date thing on Android.

@Serializable
data class ISO8601Format(val formatString: String, val regexPattern: String) {
    companion object {
        val Timestamp = ISO8601Format("yyyy-MM-ddTHH:mm:SS.SSSZZZZZ","""\d{4}(-)\d{2}(-)\d{2}(T)\d{2}(:)\d{2}(:)\d{2}(.)\d{3}""")
        // Older Android devices do not support the newer timestamp format.
        val TimestampAndroid = ISO8601Format("yyyy-MM-ddTHH:mm:SS.SSSZ", """\d{4}(-)\d{2}(-)\d{2}(T)\d{2}(:)\d{2}(:)\d{2}(.)\d{3}""")
        val DateOnly = ISO8601Format("yyyy-MM-dd", """\d{4}(-)\d{2}(-)\d{2}$""")
        val TimeOnly = ISO8601Format("HH:mm:SS.SSS", """\d{2}(:)\d{2}(:)\d{2}(.)\d{3}$""")
        val YearFormat = ISO8601Format("yyyy", """\d{4}$""")
        val MonthYearFormat = ISO8601Format("yyyy-MM", """\d{4}(-)\d{2}$""")
        val MonthDayFormat = ISO8601Format("MM-dd", """\d{2}(-)\d{2}$""")
        val HourMinuteFormat = ISO8601Format("HH:mm", """\d{2}(:)\d{2}$""")
        val registeredFormats = mutableListOf(
                Timestamp,
                TimestampAndroid,
                DateOnly,
                TimeOnly,
                YearFormat,
                MonthYearFormat,
                MonthDayFormat,
                HourMinuteFormat
        )
    }
}

sealed class DateTimePart(val isoKey: String) {
    companion object {
        fun timestampParts(): List<DateTimePart> = isoParts().plus(TimeZoneIdentifierPart)
        fun isoParts(): List<DateTimePart> = DatePart.values().plus(TimePart.values()).plus(GMTOffsetPart)
        fun partsFor(formatString: String): List<DateTimePart> {
            var ignoreCase = true
            val list: List<DateTimePart> = when {
                formatString.contains("T") -> isoParts()
                formatString.contains(DatePart.separator) -> DatePart.values()
                formatString.contains(TimePart.separator) -> TimePart.values()
                else -> {
                    ignoreCase = false
                    isoParts()
                }
            }
            return list.filter { formatString.contains(it.isoKey, ignoreCase) }
        }
    }
}

sealed class DatePart(isoKey: String) : DateTimePart(isoKey) {
    object Year: DatePart("yyyy")
    object Month: DatePart("MM")
    object Day: DatePart("dd")
    companion object {
        val separator: String = "-"
        fun values(): List<DatePart> = listOf(Year, Month, Day)
    }
}

sealed class TimePart(isoKey: String) : DateTimePart(isoKey) {
    object Hour: TimePart("HH")
    object Minute: TimePart("mm")
    object Second: TimePart("SS.SSS")
    companion object {
        val separator: String = ":"
        fun values(): List<TimePart> = listOf(Hour, Minute, Second)
    }
}

object GMTOffsetPart : DateTimePart("Z")
object TimeZoneIdentifierPart : DateTimePart("[]")

data class DateTimeComponents(val year: Int? = null,
                              val month: Int? = null,
                              val day: Int? = null,
                              val hour: Int? = null,
                              val minute: Int? = null,
                              val second: Int? = null,
                              val gmtHourOffset: Double? = null,
                              val timezoneIdentifier: String? = null)

@Serializable
data class DateTimeReference(val dateTimeString: String, val timezoneIdentifier: String? = null)


expect class DateTime : Comparable<DateTime> {
    constructor(dateTimeReference: DateTimeReference)
    constructor(iso8601Format: String, includeTimeZoneIdentifier: Boolean = false)
    constructor(parts: List<DateTimePart>)
    val iso8601Format: String
    fun getDateTimeParts(): List<DateTimePart>
    fun getDateTimeReference(): DateTimeReference
    fun getDateTimeComponents(): DateTimeComponents
}
