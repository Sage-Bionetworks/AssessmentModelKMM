package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceBundle
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceInfo

/**
 * A [TransformableNode] is a special node that allows for two-part unpacking of deserialization. This is used to allow
 * a "placeholder" to be replaced with a different node.
 */
interface TransformableNode : ContentNode {
    val resourceName: String

    override fun unpack(fileLoader: FileLoader, resourceInfo: ResourceInfo, jsonCoder: Json): Node {
        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = fileLoader.loadFile(resourceName, "json", resourceInfo)
        val unpackedNode = jsonCoder.parse(serializer, jsonString)
        return unpackedNode.unpack(fileLoader, resourceInfo, jsonCoder)
    }
}

/**
 * The [AssessmentGroupInfo] is a way of collecting a group of assessments that are shared within a given module where
 * they should use the same [resourceInfo] to load any of the assessments within the [files] included in this group.
 */
interface AssessmentGroupInfo {
    val files: List<TransformableNode>
    val resourceInfo: ResourceInfo
}

interface ResourceAssessmentProvider : AssessmentGroupInfo, AssessmentProvider {
    val fileLoader: FileLoader
    val jsonCoder: Json

    override fun canLoadAssessment(assessmentIdentifier: String): Boolean =
            files.any { it.identifier == assessmentIdentifier }

    override fun loadAssessment(assessmentIdentifier: String): Assessment? {
        val serializer = PolymorphicSerializer(Assessment::class)
        return files.firstOrNull { it.identifier == assessmentIdentifier }?.let {
            val jsonString = fileLoader.loadFile(it.resourceName, "json", resourceInfo)
            val assessment = jsonCoder.parse(serializer, jsonString)
            return assessment.unpack(fileLoader, resourceInfo, jsonCoder)
        }
    }
}

@Serializable
data class AssessmentGroupInfoObject(override val files: List<TransformableNode>,
                                     override var packageName: String? = null,
                                     override var decoderBundle: ResourceBundle? = null,
                                     override val bundleIdentifier: String? = null): ResourceInfo, AssessmentGroupInfo {
    override val resourceInfo: ResourceInfo
        get() = this
}

/**
 * The [FileAssessmentProvider] is a wrapper for a [fileLoader] and [assessmentGroupInfo] to allow for unpacking an
 * [Assessment] using a platform-specific resource management strategy.
 */
class FileAssessmentProvider(override val fileLoader: FileLoader,
                             private val assessmentGroupInfo: AssessmentGroupInfo)
    : ResourceAssessmentProvider, AssessmentGroupInfo by assessmentGroupInfo {
    override var jsonCoder: Json = Serialization.JsonCoder.default
}

