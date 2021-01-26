package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import org.sagebionetworks.assessmentmodel.survey.answerTypeSerializersModule

/**
 * Singleton for the default serializers.
 */
object Serialization {
    object SerializersModule {
        val default =
                answerTypeSerializersModule +
                asyncActionSerializersModule +
                buttonSerializersModule +
                inputItemSerializersModule +
                imageSerializersModule +
                nodeSerializersModule +
                resultSerializersModule +
                moduleInfoSerializersModule +
                SerializersModule {}    // Marker for the end of the list. Used to make github read more cleanly.

    }
    object JsonCoder {
        val default = Json{
                serializersModule = SerializersModule.default
                encodeDefaults = true
        }
    }
}

/**
 * Notes on serialization
 *
 * syoung 02/07/2020
 * `configuration = JsonConfiguration.Stable.copy(classDiscriminator = "classType")` - This will set the field that
 * determines the class "type" to "classType" instead of "type".
 *
 * syoung 02/25/2020
 * Throughout this codebase, @SerialName markers use camel-case. This is to reduce required changes to the
 * human-readable JSON previously defined in SageResearch.
 */

