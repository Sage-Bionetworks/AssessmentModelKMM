package org.sagebionetworks.assessmentmodel

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

expect class Platform() {
    val platform: String
}

expect object UUIDGenerator {
    fun uuidString(): String
}

expect object DateUtils {
    /**
     * Create an ISO_8601 formatted string from the specified [Instant].
     * The format is "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ" which looks like:
     * "2021-03-23T14:58:54.106-07:00"
     */
    fun bridgeIsoDateTimeString(instant: Instant, timeZone: TimeZone = TimeZone.currentSystemDefault()): String

    /**
     * Parse an ISO_8601 string of format "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ" into an [Instant].
     */
    fun instantFromBridgeIsoDateTimeString(dateString: String) : Instant
    fun nowString(): String
    fun currentYear(): Int
}

expect class Product {
    val user: String
}

expect object Factory {
    fun create(config: Map<String, String>): Product
    val platform: String
}
