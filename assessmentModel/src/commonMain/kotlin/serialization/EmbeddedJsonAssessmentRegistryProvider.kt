package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.plus
import org.sagebionetworks.assessmentmodel.AssessmentRegistryProvider
import org.sagebionetworks.assessmentmodel.ModuleInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.AssetResourceInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader
import org.sagebionetworks.assessmentmodel.resourcemanagement.StandardResourceAssetType
import org.sagebionetworks.assessmentmodel.serialization.moduleInfoSerializersModule
import org.sagebionetworks.assessmentmodel.serialization.nodeSerializersModule

/**
 * An implementation of [AssessmentRegistryProvider] that loads list of [ModuleInfo] objects from a json file.
 * To specify custom [Json] coders, you could add to the [moduleInfoSerializersModule] a different
 * decoding for each module and use the "type" to define the coder for the module.
 */
class EmbeddedJsonAssessmentRegistryProvider(override val fileLoader: FileLoader,
                                             private val modulesResourceName: String,
                                             private val moduleJsonCoder: Json = Json {
                                        serializersModule = nodeSerializersModule + moduleInfoSerializersModule
                                    }
                                    ): AssessmentRegistryProvider {

    private var _modules: List<ModuleInfo>? = null
    override val modules: List<ModuleInfo>
        get() {
            if (_modules == null) {
                val modulesFile = object : AssetResourceInfo {
                    override val resourceAssetType = StandardResourceAssetType.RAW
                    override var decoderBundle: Any? = null
                    override val bundleIdentifier: String? = null
                    override val rawFileExtension = "json"
                    override val versionString: String? = null
                    override var packageName: String? = null
                    override val resourceName = modulesResourceName

                }
                val jsonString = fileLoader.loadFile(assetInfo = modulesFile, resourceInfo = modulesFile)
                val serializer = ListSerializer(PolymorphicSerializer(ModuleInfo::class))
                _modules = moduleJsonCoder.decodeFromString(serializer, jsonString)
            }
            return _modules?: throw AssertionError("Set to null by another thread")
        }

}