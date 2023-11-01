package org.sagebionetworks.assessmentmodel

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

actual object UUIDGenerator {
    actual fun uuidString() : String = UUID.randomUUID().toString()
}

actual object DateUtils {

    private var timeZoneOverride: TimeZone? = null

    /**
     * For testing purposes
     */
    internal actual fun timeZoneOverride(timeZone: TimeZone) {
        timeZoneOverride = timeZone
    }

    private val iso8601Formatter: DateTimeFormatter = {
        //Would prefer to use DateTimeFormatter.ISO_OFFSET_DATE_TIME, but it is inconsistent with iOS.
        //  ISO_OFFSET_DATE_TIME has a variable number of digits for fractional seconds.
        DateTimeFormatter.ofPattern(DateConstants.BRIDGE_ISO_DATE_TIME_FORMAT, Locale.US)
    }()

    /**
     * Create an ISO_8601 formatted string from the specified [Instant].
     * The format is "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ" which looks like:
     * "2021-03-23T14:58:54.106-07:00"
     */
    actual fun bridgeIsoDateTimeString(instant: Instant): String {
        val jtInstant = java.time.Instant.ofEpochMilli(instant.toEpochMilliseconds())
        val zoneId = timeZoneOverride?.toJavaZoneId() ?: TimeZone.currentSystemDefault().toJavaZoneId()
        return ZonedDateTime.ofInstant(jtInstant, zoneId).format(iso8601Formatter)
    }

    actual fun instantFromBridgeIsoDateTimeString(dateString: String) : Instant {
        val dateTime = ZonedDateTime.from(iso8601Formatter.parse(dateString))
        val jtInstant = dateTime.toInstant()
        return Instant.fromEpochMilliseconds(jtInstant.toEpochMilli())
    }

}
