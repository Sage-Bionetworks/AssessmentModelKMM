package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.json
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileResourceInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceBundle
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceInfo

interface TransformableNode : ContentNode {
    val resourceName: String

    override fun unpack(fileLoader: FileLoader, resourceInfo: ResourceInfo, jsonCoder: Json): Node {
        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = fileLoader.loadFile(resourceName, "json", resourceInfo)
        return jsonCoder.parse(serializer, jsonString)
    }
}

data class ResourceInfoObject(override var packageName: String? = null,
                              override var decoderBundle: ResourceBundle? = null,
                              override val bundleIdentifier: String? = null): ResourceInfo

class FileAssessmentProvider(val files: List<TransformableNode>,
                             val fileLoader: FileLoader,
                             val resourceInfo: ResourceInfo): AssessmentProvider {

    var jsonCoder: Json = Serialization.JsonCoder.default

    override fun canLoadAssessment(assessmentIdentifier: String): Boolean = files.any { it.identifier == assessmentIdentifier }

    override fun loadAssessment(assessmentIdentifier: String): Assessment? {
        val serializer = PolymorphicSerializer(Assessment::class)
        return files.firstOrNull { it.identifier == assessmentIdentifier }?.let {
            val jsonString = fileLoader.loadFile(it.resourceName, "json", resourceInfo)
            val assessment = jsonCoder.parse(serializer, jsonString)
            return assessment.unpack(fileLoader, resourceInfo, jsonCoder)
        }
    }
}

