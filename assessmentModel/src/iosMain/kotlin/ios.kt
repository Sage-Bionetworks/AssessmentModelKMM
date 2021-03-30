package org.sagebionetworks.assessmentmodel

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toNSTimeZone
import org.sagebionetworks.assessmentmodel.survey.ISO8601Format
import platform.Foundation.*
import platform.posix.uname
import platform.posix.utsname
import kotlin.reflect.KClass

actual class Platform actual constructor() {
    actual val platform: String = "iOS"
}

actual class Product(actual val user: String) {
    val model: String = memScoped {
        val systemInfo = alloc<utsname>()
        uname(systemInfo.ptr)
        systemInfo.machine.toKString()
    }

    fun iosSpecificOperation() {
        println("I am $model")
    }

    override fun toString() = "iOS product of $user for $model"
}

actual object Factory {
    actual fun create(config: Map<String, String>) =
        Product(config["user"]!!)

    actual val platform: String = "ios"
}

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
        val formatter = NSDateFormatter.new()!!
        formatter.dateFormat = DateConstants.BRIDGE_ISO_DATE_TIME_FORMAT
        formatter.locale = NSLocale.localeWithLocaleIdentifier("en_US_POSIX")
        formatter
    }()

    actual fun currentYear(): Int = NSCalendar(NSISO8601Calendar).component(NSCalendarUnitYear, NSDate.now).toInt()

    actual fun bridgeIsoDateTimeString(instant: Instant): String {
        val timeInterval: NSTimeInterval = instant.toEpochMilliseconds() / 1000.0
        val date = NSDate.dateWithTimeIntervalSince1970(timeInterval)
        return iso8601Formatter.stringFromDate(date)
    }

    actual fun instantFromBridgeIsoDateTimeString(dateString: String) : Instant {
        val date = iso8601Formatter.dateFromString(dateString)
        if (date == null) {
            throw IllegalArgumentException("Unable to parse date string: $dateString")
        }
        val timeInterval = date!!.timeIntervalSince1970()
        val milliSec = (timeInterval * 1000).toLong()
        return Instant.fromEpochMilliseconds(milliSec)
    }
    

}
