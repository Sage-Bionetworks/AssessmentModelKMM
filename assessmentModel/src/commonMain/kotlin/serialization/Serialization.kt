package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.plus
import kotlin.reflect.KClass

/**
 * Singleton for the default serializers.
 */
object Serialization {
    object SerializersModule {
        val default =   nodeSerializersModule +
                        buttonSerializersModule +
                        imageSerializersModule +
                        resultSerializersModule +
                        inputItemSerializersModule
    }
    object JsonCoder {
        val default = Json(context = Serialization.SerializersModule.default, configuration = JsonConfiguration.Stable.copy(strictMode = false))
    }
}

/**
 * syoung 02/07/2020 Notes on serialization
 *
 * `configuration = JsonConfiguration.Stable.copy(classDiscriminator = "classType")` - This will set the field that
 * determines the class "type" to "classType" instead of "type".
 *
 *
 *
 *
 */

