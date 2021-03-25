package org.sagebionetworks.assessmentmodel

import android.os.Build
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

actual class Platform actual constructor() {
    actual val platform: String = "Android"
}

actual class Product(actual val user: String) {
    fun androidSpecificOperation() {
        println("I am ${Build.MODEL} by ${Build.MANUFACTURER}")
    }

    override fun toString() = "Android product of $user for ${Build.MODEL}"
}

actual object Factory {
    actual fun create(config: Map<String, String>) =
        Product(config["user"]!!)

    actual val platform: String = "android"
}

actual object UUIDGenerator {
    actual fun uuidString() : String = UUID.randomUUID().toString()
}

actual object DateUtils {
    actual fun nowString(): String = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    
    actual fun currentYear(): Int = ZonedDateTime.now().year

    actual fun bridgeIsoDateTimeString(instant: Instant, timeZone: TimeZone): String {
        val jtInstant = java.time.Instant.ofEpochMilli(instant.toEpochMilliseconds())
        val zoneId = ZoneId.of(timeZone.id)
        return ZonedDateTime.ofInstant(jtInstant, zoneId).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    actual fun instantFromBridgeIsoDateTimeString(dateString: String) : Instant {
        val dateTime = ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(dateString))
        val jtInstant = dateTime.toInstant()
        return Instant.fromEpochMilliseconds(jtInstant.toEpochMilli())
    }
}
