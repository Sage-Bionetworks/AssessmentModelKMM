package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.*
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.BranchNodeState
import org.sagebionetworks.assessmentmodel.navigation.Navigator
import org.sagebionetworks.assessmentmodel.resourcemanagement.*

val moduleInfoSerializersModule = SerializersModule {
    polymorphic(ModuleInfo::class) {
        subclass(ModuleInfoObject::class)
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

@Serializable
@SerialName("assessmentGroupInfo")
data class ModuleInfoObject(override val assessments: List<Assessment>,
                            override var packageName: String? = null,
                            override val bundleIdentifier: String? = null): ResourceInfo,
    ModuleInfo {
    override val resourceInfo: ResourceInfo
        get() = this

    @Transient
    override var decoderBundle: Any? = null


}

/**
 * The [SerializableModuleInfoObject] is a wrapper for a [fileLoader] and [moduleInfo] to allow for unpacking an
 * [Assessment] using a platform-specific resource management strategy.
 */
class SerializableModuleInfoObject(private val moduleInfo: ModuleInfo,
                                   override val jsonCoder: Json = Serialization.JsonCoder.default
)
    : SerializableModuleInfo, ModuleInfo by moduleInfo
