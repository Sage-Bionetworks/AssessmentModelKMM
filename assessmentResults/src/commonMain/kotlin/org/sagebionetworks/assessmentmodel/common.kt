package org.sagebionetworks.assessmentmodel

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

expect object UUIDGenerator {
    fun uuidString(): String
}

object DateConstants {
    const val BRIDGE_ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ"
}

expect object DateUtils {

    /**
     * For testing purposes
     */
    internal fun timeZoneOverride(timeZone: TimeZone)

    /**
     * Create an ISO_8601 formatted string from the specified [Instant].
     * The format is "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ" which looks like:
     * "2021-03-23T14:58:54.106-07:00"
     */
    fun bridgeIsoDateTimeString(instant: Instant): String

    /**
     * Parse an ISO_8601 string of format "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ" into an [Instant].
     */
    fun instantFromBridgeIsoDateTimeString(dateString: String) : Instant

}

expect class Product {
    val user: String
}

expect object Factory {
    fun create(config: Map<String, String>): Product
    val platform: String
}
