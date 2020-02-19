package org.sagebionetworks.assessmentmodel

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import kotlinx.serialization.modules.SerializersModule
import kotlin.reflect.KClass

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