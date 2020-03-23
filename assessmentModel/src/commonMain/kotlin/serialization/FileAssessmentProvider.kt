package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.Navigator
import org.sagebionetworks.assessmentmodel.resourcemanagement.*

val fileProviderSerializersModule = SerializersModule {
    polymorphic(AssessmentGroupInfo::class) {
        AssessmentGroupInfoObject::class with AssessmentGroupInfoObject.serializer()
    }
}

/**
 * A [TransformableNode] is a special node that allows for two-part unpacking of deserialization. This is used to allow
 * a "placeholder" to be replaced with a different node.
 */
interface TransformableNode : ContentNode, AssetInfo {
    override val resourceAssetType: String?
        get() = StandardResourceAssetType.RAW
    override val rawFileExtension: String?
        get() = "json"

    override fun unpack(fileLoader: FileLoader, resourceInfo: ResourceInfo, jsonCoder: Json): Node {
        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = fileLoader.loadFile(this, resourceInfo)
        val unpackedNode = jsonCoder.parse(serializer, jsonString)
        return unpackedNode.unpack(fileLoader, resourceInfo, jsonCoder)
    }
}

/**
 * A [TransformableAssessment] is a placeholder that can be used to contain just enough information about an
 * [Assessment] to allow referencing the resource information needed to load the actual [Assessment] on demand. This
 * allows using a "placeholder" that can be vended from a different source from the actual assessment. For example,
 * an active task may be defined using a hardcoded JSON file and included in a module but requested via a
 * [TransformableAssessment] that is vended from a server.
 */
interface TransformableAssessment : Assessment, TransformableNode {
    override fun unpack(fileLoader: FileLoader, resourceInfo: ResourceInfo, jsonCoder: Json): Assessment {
        val serializer = PolymorphicSerializer(Assessment::class)
        val jsonString = fileLoader.loadFile(this, resourceInfo)
        val unpackedNode = jsonCoder.parse(serializer, jsonString)
        return unpackedNode.unpack(fileLoader, resourceInfo, jsonCoder)
    }

    override fun getNavigator(): Navigator = throw IllegalStateException("Attempted to get a navigator without first unpacking the Assessment.")
}

/**
 * The [AssessmentGroupInfo] is a way of collecting a group of assessments that are shared within a given module where
 * they should use the same [resourceInfo] to load any of the assessments within the [assessments] included in this group.
 */
interface AssessmentGroupInfo {
    val assessments: List<Assessment>
    val resourceInfo: ResourceInfo
}

interface ResourceAssessmentProvider : AssessmentGroupInfo, AssessmentProvider {
    val fileLoader: FileLoader
    val jsonCoder: Json

    override fun canLoadAssessment(assessmentIdentifier: String): Boolean =
            assessments.any { it.identifier == assessmentIdentifier }

    override fun loadAssessment(assessmentIdentifier: String): Assessment? {
        return assessments.firstOrNull { it.identifier == assessmentIdentifier }?.let {
            return it.unpack(fileLoader, resourceInfo, jsonCoder)
        }
    }
}

@Serializable
@SerialName("assessmentGroupInfo")
data class AssessmentGroupInfoObject(override val assessments: List<Assessment>,
                                     override var packageName: String? = null,
                                     override val bundleIdentifier: String? = null): ResourceInfo, AssessmentGroupInfo {
    override val resourceInfo: ResourceInfo
        get() = this

    @Transient
    override var decoderBundle: Any? = null

    fun jsonString(): String =
            Serialization.JsonCoder.default.stringify(AssessmentGroupInfoObject.serializer(), this)
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