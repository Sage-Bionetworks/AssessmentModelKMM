package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.plus
import org.sagebionetworks.assessmentmodel.AssessmentPlaceholder
import org.sagebionetworks.assessmentmodel.AssessmentRegistryProvider
import org.sagebionetworks.assessmentmodel.JsonModuleInfo
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
open class EmbeddedJsonAssessmentRegistryProvider(override val fileLoader: FileLoader,
                                                  private val modulesResourceName: String,
                                                  private val moduleJsonCoder: Json = Serialization.JsonCoder.default,
                                                  private val modulesDecoderBundle: Any? = null,
                                                  private val modulesPackageName: String? = null
                                    ): AssessmentRegistryProvider {

    private var _modules: List<JsonModuleInfo>? = null
    override val modules: List<JsonModuleInfo>
        get() {
            if (_modules == null) {
                val modulesFile = object : AssetResourceInfo {
                    override val resourceAssetType = StandardResourceAssetType.RAW
                    override var decoderBundle: Any? = modulesDecoderBundle
                    override val bundleIdentifier: String? = null
                    override val rawFileExtension = "json"
                    override val versionString: String? = null
                    override var packageName: String? = modulesPackageName
                    override val resourceName = modulesResourceName
                }
                val jsonString = fileLoader.loadFile(assetInfo = modulesFile, resourceInfo = modulesFile)
                val serializer = ListSerializer(PolymorphicSerializer(JsonModuleInfo::class))
                val decodedModules = moduleJsonCoder.decodeFromString(serializer, jsonString)
                decodedModules.forEach {
                    if (it.resourceInfo.bundleIdentifier == null) {
                        it.resourceInfo.decoderBundle = modulesDecoderBundle
                    }
                    if (it.resourceInfo.packageName == null) {
                        it.resourceInfo.packageName = modulesPackageName
                    }
                }
                _modules = decodedModules
            }
            return _modules?: throw AssertionError("Set to null by another thread")
        }

    override fun getJsonCoder(assessmentPlaceholder: AssessmentPlaceholder): Json {
        val moduleInfo = modules.find { it.hasAssessment(assessmentPlaceholder) }
        return moduleInfo?.jsonCoder
            ?: throw IllegalStateException("This version of the application cannot load " + {assessmentPlaceholder.assessmentInfo.identifier})
    }

}