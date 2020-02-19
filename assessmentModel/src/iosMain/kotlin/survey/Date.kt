package org.sagebionetworks.assessmentmodel.survey

import platform.CoreFoundation.*
import platform.Foundation.*

fun serializableDateFormatter(): NSDateFormatter {
    val formatter = NSDateFormatter()
    formatter.locale = NSLocale("en_US_POSIX")
    return formatter
}

actual class DateTime  : Comparable<DateTime> {
    lateinit var dateBehind: NSDate
    var timeZone: NSTimeZone? = NSTimeZone.new()
        private set
    private var formatter = serializableDateFormatter()
    private lateinit var parts: List<DateTimePart>

    private constructor()

    actual constructor(dateTimeReference: DateTimeReference) : this() {
        this.dateBehind = ISO8601Format.registeredFormats.mapNotNull { format ->
            if (!Regex(format.regexPattern).matches(dateTimeReference.dateTimeString)) null
            else {
                this.formatter.dateFormat = format.formatString
                this.formatter.dateFromString(dateTimeReference.dateTimeString)
            }
        }.firstOrNull() ?: throw NullPointerException("Could not find a valid ISO8601 format for ${ dateTimeReference.dateTimeString }")
        this.parts = DateTimePart.partsFor(formatter.dateFormat)
        dateTimeReference.timezoneIdentifier?.let {
            this.timeZone = NSTimeZone.timeZoneWithName(it)
            this.parts = this.parts.plus(TimeZoneIdentifierPart)
        }
    }

    actual constructor(iso8601Format: String, includeTimeZoneIdentifier: Boolean) : this() {
        dateBehind = NSDate.new()!!
        this.parts = DateTimePart.partsFor(iso8601Format)
        if (includeTimeZoneIdentifier) {
            this.parts = this.parts.plus(TimeZoneIdentifierPart)
        }
        this.formatter.dateFormat = iso8601Format

    }

    actual constructor(parts: List<DateTimePart>) : this() {
        dateBehind = NSDate.new()!!
        this.parts = parts
        val iso8601Parts = parts.minus(TimeZoneIdentifierPart)
        this.formatter.dateFormat = ISO8601Format.registeredFormats.firstOrNull {
            DateTimePart.partsFor(it.formatString) == iso8601Parts
        }?.formatString ?: throw NullPointerException("Could not find a valid ISO8601 format for $parts")
    }

    actual val iso8601Format: String
        get() = formatter.dateFormat

    actual fun getDateTimeReference(): DateTimeReference
        = formatter.stringFromDate(dateBehind)?.let { DateTimeReference(it, timeZone?.name) }

    @ExperimentalUnsignedTypes
    actual fun getDateTimeComponents(): DateTimeComponents {
        val calendar = NSCalendar(NSCalendarIdentifierISO8601)
        val zeroUnit: CFCalendarUnit = 0u
        val unitFlags: CFCalendarUnit = parts.fold(zeroUnit){ result, value -> result + value.calendarUnit() }
        val components = calendar.components(unitFlags, dateBehind)
        return DateTimeComponents(
            year = if (parts.contains(DatePart.Year)) components.year.toInt() else null,
            month = if (parts.contains(DatePart.Month)) components.month.toInt() else null,
            day = if (parts.contains(DatePart.Day)) components.day.toInt() else null,
            hour = if (parts.contains(TimePart.Hour)) components.hour.toInt() else null,
            minute = if (parts.contains(TimePart.Minute)) components.minute.toInt() else null,
            second = if (parts.contains(TimePart.Second)) components.second.toInt() else null,
            gmtHourOffset = if (parts.contains(GMTOffsetPart) && (timeZone != null)) timeZone!!.secondsFromGMT.toDouble() / 3600.0 else null,
            timezoneIdentifier = if (parts.contains(TimeZoneIdentifierPart)) timeZone?.name else null
        )
    }

    actual fun getDateTimeParts(): List<DateTimePart> = DateTimePart.partsFor(iso8601Format)

    override fun compareTo(other: DateTime): Int = this.dateBehind.compare(other.dateBehind).toInt()
}

@ExperimentalUnsignedTypes
fun DateTimePart.calendarUnit(): CFCalendarUnit
    = when (this) {
        DatePart.Year -> kCFCalendarUnitYear
        DatePart.Month -> kCFCalendarUnitMonth
        DatePart.Day -> kCFCalendarUnitDay
        TimePart.Hour -> kCFCalendarUnitHour
        TimePart.Minute -> kCFCalendarUnitMinute
        TimePart.Second -> kCFCalendarUnitSecond
        else -> 0u
}