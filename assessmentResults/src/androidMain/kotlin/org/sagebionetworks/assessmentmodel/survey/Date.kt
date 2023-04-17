package org.sagebionetworks.assessmentmodel.survey

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal
import java.util.*

internal val serializationLocale = Locale("en", "US")

// TODO: syoung 02/17/2020 Implement for Android. I can't figure out how to create a now() instance of any of the date classes.

actual class DateTime  : Comparable<DateTime> {
    private lateinit var parts: List<DateTimePart>
    private lateinit var formatter: SimpleDateFormat

    private constructor()

    actual constructor(dateTimeReference: DateTimeReference) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual constructor(iso8601Format: String, includeTimeZoneIdentifier: Boolean) : this() {
        this.parts = DateTimePart.partsFor(iso8601Format)
        if (includeTimeZoneIdentifier) {
            this.parts = this.parts.plus(TimeZoneIdentifierPart)
        }
        this.formatter = SimpleDateFormat(iso8601Format, serializationLocale)
    }

    actual constructor(parts: List<DateTimePart>) : this() {
        this.parts = parts
        val iso8601Parts = parts.minus(TimeZoneIdentifierPart)
        val formats: List<String> = ISO8601Format.registeredFormats.mapNotNull {
            if (DateTimePart.partsFor(it.formatString) == iso8601Parts) it.formatString else null
        }
        this.formatter =
        when (formats.count()) {
            0 -> throw NullPointerException("Could not find a valid ISO8601 format for $parts")
            // TODO: syoung 02/17/2020 Revisit this when we no longer need to support Android < 26
            else -> SimpleDateFormat(formats.last(), serializationLocale)
        }
    }

    override fun compareTo(other: DateTime): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual val iso8601Format: String
        get() = formatter.toPattern()

    actual fun getDateTimeParts(): List<DateTimePart> = parts

    actual fun getDateTimeReference(): DateTimeReference {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun getDateTimeComponents(): DateTimeComponents {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}