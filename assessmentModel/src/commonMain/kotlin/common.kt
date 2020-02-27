package org.sagebionetworks.assessmentmodel

expect class Platform() {
    val platform: String
}

expect object UUIDGenerator {
    fun uuidString(): String
}

expect object DateGenerator {
    fun nowString(): String
    fun currentYear(): Int
}