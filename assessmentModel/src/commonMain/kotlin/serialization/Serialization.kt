package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import org.sagebionetworks.assessmentmodel.Assessment
import org.sagebionetworks.assessmentmodel.AssessmentInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceInfo
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
                fileProviderSerializersModule +
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
 * The [ModuleInfoProvider] provides methods to lookup the necessary [ResourceInfo] and [Json] to
 * load an [Assessment] using the provide the platform specific [FileLoader].
 */
interface ModuleInfoProvider {

    val fileLoader: FileLoader

    fun getRegisteredResourceInfo(assessmentInfo: AssessmentInfo): ResourceInfo?

    fun getRegisteredJsonDecoder(assessmentInfo: AssessmentInfo): Json?

    /**
     * Allows the provider to return an [Assessment] (for the given [assessmentInfo]) that is not
     * encoded as a JSON serialized object.
     */
    fun getRegisteredAssessment(assessmentInfo: AssessmentInfo): Assessment? = null
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

