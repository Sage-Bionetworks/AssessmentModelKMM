package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.plus

/**
 * Singleton for the default serializers.
 */
object Serialization {
    object SerializersModule {
        val default =   buttonSerializersModule +
                        imageSerializersModule +
                        resultSerializersModule
    }
    object JsonCoder {
        val default = Json(context = Serialization.SerializersModule.default)
    }
}



