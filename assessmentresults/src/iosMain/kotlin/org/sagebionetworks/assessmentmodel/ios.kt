package org.sagebionetworks.assessmentmodel

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toNSTimeZone
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeInterval
import platform.Foundation.NSUUID
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.localeWithLocaleIdentifier
import platform.Foundation.timeIntervalSince1970

actual object UUIDGenerator {
    actual fun uuidString(): String = NSUUID.UUID().UUIDString
}

actual object DateUtils {
    
    /**
     * For testing purposes
     */
    internal actual fun timeZoneOverride(timeZone: TimeZone) {
        iso8601Formatter.timeZone = timeZone.toNSTimeZone()
    }

    private val iso8601Formatter: NSDateFormatter = {
        val formatter = NSDateFormatter()
        formatter.dateFormat = DateConstants.BRIDGE_ISO_DATE_TIME_FORMAT
        formatter.locale = NSLocale.localeWithLocaleIdentifier("en_US_POSIX")
        formatter
    }()

    actual fun bridgeIsoDateTimeString(instant: Instant): String {
        val timeInterval: NSTimeInterval = instant.toEpochMilliseconds() / 1000.0
        val date = NSDate.dateWithTimeIntervalSince1970(timeInterval)
        return iso8601Formatter.stringFromDate(date)
    }

    actual fun instantFromBridgeIsoDateTimeString(dateString: String) : Instant {
        val date = iso8601Formatter.dateFromString(dateString)
            ?: throw IllegalArgumentException("Unable to parse date string: $dateString")
        val timeInterval = date.timeIntervalSince1970()
        val milliSec = (timeInterval * 1000).toLong()
        return Instant.fromEpochMilliseconds(milliSec)
    }
    

}
