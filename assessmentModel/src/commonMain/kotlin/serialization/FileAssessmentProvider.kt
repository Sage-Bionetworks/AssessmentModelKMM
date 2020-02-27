package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.Assessment
import org.sagebionetworks.assessmentmodel.AssessmentProvider
import org.sagebionetworks.assessmentmodel.serialization.FileLoader
import org.sagebionetworks.assessmentmodel.Node

/**
 * An [AssessmentProvider] for loading Assessments from json files included as part of the app.
 */
class FileAssessmentProvider(val fileLoader: FileLoader): AssessmentProvider {

    data class ResourceInfo(val fileName: String, val packageName: String)

    //TODO: Need a more configurable mapping of Assessment identifiers to files -nbrown 02/10/2020
    val fileMap = mapOf<String, ResourceInfo>(Pair("test_json", ResourceInfo("sample_assessment", "org.sagebionetworks.assessmentmodel.sampleapp")))

    override fun loadAssessment(assssmentIdentifier: String): Assessment? {
        fileMap.get(assssmentIdentifier)?.let {
            val jsonString = fileLoader.loadFile(it.fileName, it.packageName)
            return Serialization.JsonCoder.default.parse(AssessmentObject.serializer(), jsonString)
        }

        return null
    }
}