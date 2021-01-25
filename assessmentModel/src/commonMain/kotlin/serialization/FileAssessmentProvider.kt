package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.*
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.BranchNodeState
import org.sagebionetworks.assessmentmodel.navigation.Navigator
import org.sagebionetworks.assessmentmodel.resourcemanagement.*

val fileProviderSerializersModule = SerializersModule {
    polymorphic(AssessmentGroupInfo::class) {
        subclass(AssessmentGroupInfoObject::class)
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

    override fun unpack(moduleInfoProvider: ModuleInfoProvider, resourceInfo: ResourceInfo, jsonCoder: Json): Node {
        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = moduleInfoProvider.fileLoader.loadFile(this, resourceInfo)
        val unpackedNode = jsonCoder.decodeFromString(serializer, jsonString)
        return unpackedNode.unpack(moduleInfoProvider, resourceInfo, jsonCoder)
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

    override fun unpack(moduleInfoProvider: ModuleInfoProvider, resourceInfo: ResourceInfo, jsonCoder: Json): Assessment {
        val assessment = moduleInfoProvider.getRegisteredAssessment(this)
        return if (assessment != null) assessment else {
            val curResourceInfo = moduleInfoProvider.getRegisteredResourceInfo(this) ?: resourceInfo
            val serializer = PolymorphicSerializer(Assessment::class)
            val jsonString = moduleInfoProvider.fileLoader.loadFile(this, resourceInfo)
            val curJsonCoder = moduleInfoProvider.getRegisteredJsonDecoder(this) ?: jsonCoder
            val unpackedNode = curJsonCoder.decodeFromString(serializer, jsonString)
            unpackedNode.unpack(moduleInfoProvider, curResourceInfo, curJsonCoder)
        }
    }

    override fun getNavigator(nodeState: BranchNodeState): Navigator = throw IllegalStateException("Attempted to get a navigator without first unpacking the Assessment.")
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
    val moduleInfoProvider: ModuleInfoProvider
    val jsonCoder: Json

    override fun canLoadAssessment(assessmentInfo: AssessmentInfo): Boolean =
            assessments.any { it.identifier == assessmentInfo.identifier && it.versionString == assessmentInfo.versionString }

    override fun loadAssessment(assessmentInfo: AssessmentInfo): Assessment? {
        return assessments.firstOrNull { it.identifier == assessmentInfo.identifier && it.versionString == assessmentInfo.versionString}?.let {
            return it.unpack(moduleInfoProvider, resourceInfo, jsonCoder)
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
}

/**
 * The [FileAssessmentProvider] is a wrapper for a [fileLoader] and [assessmentGroupInfo] to allow for unpacking an
 * [Assessment] using a platform-specific resource management strategy.
 */
class FileAssessmentProvider(override val moduleInfoProvider: ModuleInfoProvider,
                             private val assessmentGroupInfo: AssessmentGroupInfo,
                             override var jsonCoder: Json
)
    : ResourceAssessmentProvider, AssessmentGroupInfo by assessmentGroupInfo
