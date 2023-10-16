package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.modules.plus
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.resourcemanagement.*
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfURL

class FileLoaderIOS() : FileLoader {
    override fun loadFile(assetInfo: AssetInfo, resourceInfo: ResourceInfo): String {
        val bundle = resourceInfo.bundle()
        val filename = assetInfo.resourceName
        val ext = assetInfo.rawFileExtension ?: "json"
        return bundle.loadString(filename, ext)
    }
}

fun ResourceInfo.bundle() : NSBundle
        = this.bundleIdentifier?.let { NSBundle.bundleWithIdentifier(it) } ?: (this.decoderBundle as? NSBundle) ?: NSBundle.mainBundle()

@OptIn(ExperimentalForeignApi::class)
internal fun NSBundle.loadString(filename: String, ext: String = "json") : String {
    val url = this.URLForResource(filename, ext) ?: throw NullPointerException("Could not load '$filename' from '${this.bundleIdentifier ?: "null"}'")
    return NSString.stringWithContentsOfURL(url, NSUTF8StringEncoding, null) ?: throw NullPointerException("Could not load string from $filename")
}

open class BundleModuleInfo(bundle: NSBundle, override val assessments: List<TransformableAssessment> = listOf()) : ResourceInfo, EmbeddedJsonModuleInfo {
    override val jsonCoder: Json = Serialization.JsonCoder.default
    override val resourceInfo: ResourceInfo
        get() = this

    override var decoderBundle: Any? = bundle
    override var packageName: String? = null
    override val bundleIdentifier: String? = null
}

class AssessmentBundleModuleInfo(private val resourceName: String, bundle: NSBundle) : BundleModuleInfo(bundle) {
    private var _assessments: List<TransformableAssessment>? = null
    override val assessments: List<TransformableAssessment>
        get() {
            if (_assessments == null) {
                val bundle = resourceInfo.bundle()
                val jsonString = bundle.loadString(resourceName)
                val serializer = ListSerializer(PolymorphicSerializer(TransformableAssessment::class))
                _assessments = jsonCoder.decodeFromString(serializer, jsonString)
            }
            return _assessments!!
        }
}

/**
 * On iOS, the assessment registry can include embedded resources included in a single bundle,
 * assessments that are defined on the server, and assessments that are defined using SageResearch.
 * Therefore, we need to have a way to register modules after the provider is instantiated.
 *
 * Simply put, using the bundle identifier on iOS is very brittle and it's better to use the Bundle
 * directly by using `Bundle.module` when instantiating it and to register modules using code rather
 * than using a JSON file that includes modules housed within different packages.
 */
class AssessmentRegistryProviderIOS(modulesResourceName: String? = null, bundle: NSBundle? = null) :
    EmbeddedJsonAssessmentRegistryProvider(
        fileLoader = FileLoaderIOS(),
        modulesResourceName = modulesResourceName ?: "",
        modulesDecoderBundle = bundle) {

    private val defaultBundle: NSBundle = bundle ?: NSBundle.mainBundle

    private var _modules: MutableList<JsonModuleInfo>? = null
    override val modules: List<JsonModuleInfo>
        get() {
            setupModules()
            return _modules!!
        }

    private fun setupModules() {
        if (_modules == null) {
            _modules = try {
                super.modules.toMutableList()
            } catch (err: Exception) {
                mutableListOf()
            }
        }
    }

    /**
     * Register a module *after* instantiating the registry provider.
     */
    fun registerModuleInfo(moduleInfo: JsonModuleInfo) {
        setupModules()
        _modules!!.add(moduleInfo)
    }

    @Throws(Throwable::class)
    fun loadRegisteredAssessment(identifier: String, version: String? = null): Assessment {
        val placeholder = AssessmentPlaceholderObject(identifier, AssessmentInfoObject(identifier, version))
        try {
            return this.loadAssessment(placeholder)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }

    @Throws(Throwable::class)
    fun loadJsonStringAssessment(jsonString: String): Assessment {
        val loader = AssessmentJsonStringLoader(jsonString, defaultBundle)
        loader.registryProvider = this
        return loader.decodeObject()
    }

    @Throws(Throwable::class)
    fun loadJsonResourceAssessment(resourceName: String): Assessment {
        val loader = AssessmentJsonResourceLoader(resourceName, defaultBundle)
        loader.registryProvider = this
        return loader.decodeObject()
    }
}

abstract class AssessmentLoaderIOS(bundle: NSBundle) : BundleModuleInfo(bundle) {
    abstract val jsonString: String
    var registryProvider: AssessmentRegistryProvider = AssessmentRegistryProviderIOS()

    @Throws(Throwable::class)
    fun decodeObject(): Assessment {
        try {
            val serializer = PolymorphicSerializer(Assessment::class)
            val placeholder = jsonCoder.decodeFromString(serializer, jsonString)
            return placeholder.unpack(placeholder, this, registryProvider)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}

/**
 * Load an assessment from a JSON string.
 */
class AssessmentJsonStringLoader(override val jsonString: String, bundle: NSBundle) : AssessmentLoaderIOS(bundle)

/**
 * Load an assessment from an embedded JSON file.
 */
class AssessmentJsonResourceLoader(private val resourceName: String, bundle: NSBundle) : AssessmentLoaderIOS(bundle) {
    private var _jsonString: String? = null
    override val jsonString: String
        get() {
        if (_jsonString == null) {
            val bundle = resourceInfo.bundle()
            _jsonString = bundle.loadString(resourceName)
        }
        return _jsonString ?: throw NullPointerException("Could not decode UTF8 string from $resourceName")
    }
}

class ResultDecoder(private val jsonString: String) {
    private val jsonCoder: Json = Serialization.JsonCoder.default
    @Throws(Throwable::class)
    fun decodeObject(): Result {
        try {
            val serializer = PolymorphicSerializer(Result::class)
            return jsonCoder.decodeFromString(serializer, jsonString)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}

class ResultEncoder(private val result: Result) {
    private val jsonCoder: Json = Serialization.JsonCoder.default
    @Throws(Throwable::class)
    fun encodeObject(): String {
        try {
            val serializer = PolymorphicSerializer(Result::class)
            return jsonCoder.encodeToString(serializer, result)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}

class JsonElementEncoder(private val jsonElement: JsonElement) {
    private val jsonCoder: Json = Serialization.JsonCoder.default
    @Throws(Throwable::class)
    fun encodeObject(): String {
        try {
            return jsonCoder.encodeToString(JsonElement.serializer(), jsonElement)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}

class JsonElementDecoder(private val jsonString: String) {
    private val jsonCoder: Json = Serialization.JsonCoder.default
    @Throws(Throwable::class)
    fun decodeObject(): JsonElement {
        try {
            return jsonCoder.parseToJsonElement(jsonString)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}


