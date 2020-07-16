package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.sagebionetworks.assessmentmodel.Assessment
import org.sagebionetworks.assessmentmodel.Result
import org.sagebionetworks.assessmentmodel.resourcemanagement.AssetInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceInfo
import platform.Foundation.*

class FileLoaderIOS() : FileLoader {
    override fun loadFile(assetInfo: AssetInfo, resourceInfo: ResourceInfo): String {
        val bundle = resourceInfo.bundle()
        val filename = assetInfo.resourceName
        val ext = assetInfo.rawFileExtension ?: "json"
        val url = bundle.URLForResource(filename, ext) ?: return ""
        return NSString.stringWithContentsOfURL(url, NSUTF8StringEncoding, null) ?: return ""
    }
}

fun ResourceInfo.bundle() : NSBundle
        = (this.decoderBundle as? NSBundle) ?: this.bundleIdentifier?.let { NSBundle.bundleWithIdentifier(it) } ?: NSBundle.mainBundle()

/**
 * syoung 03/18/2020
 * Kotlin/Native to iOS does not allow for using generics or error handling except for the case where there is a single
 * return value so I've tried to organize these interfaces to allow for a fairly flexible implementation for decoding
 * an assessment and/or an assessment group.
 */
class KotlinDecoder(bundle: NSBundle) : ResourceInfo {
    var fileLoader: FileLoader = FileLoaderIOS()
    var jsonCoder: Json = Serialization.JsonCoder.default

    override var decoderBundle: Any? = bundle
    override var packageName: String? = null
    override val bundleIdentifier: String?
        get() = null
}

abstract class KotlinDecodable(val decoder: KotlinDecoder) {
    abstract val jsonString: String?
}

class AssessmentGroupStringLoader(override val jsonString: String, bundle: NSBundle) : KotlinDecodable(KotlinDecoder(bundle)) {
    @Throws
    fun decodeObject(): AssessmentGroupWrapper {
        try {
            val serializer = PolymorphicSerializer(AssessmentGroupInfo::class)
            val group = decoder.jsonCoder.parse(serializer, jsonString)
            group.resourceInfo.decoderBundle = decoder.decoderBundle
            val assessments = group.assessments.map { AssessmentLoader(it, decoder) }
            return AssessmentGroupWrapper(group, assessments)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}
data class AssessmentGroupWrapper(val assessmentGroupInfo: AssessmentGroupInfo, val assessments: List<AssessmentLoader>)

class AssessmentLoader(private val placeholder: Assessment,
                       private val decoder: KotlinDecoder) : Assessment by placeholder {
    @Throws
    fun decodeObject(): Assessment {
        try {
            return placeholder.unpack(decoder.fileLoader, decoder, decoder.jsonCoder)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}

class AssessmentJsonStringLoader(override val jsonString: String, bundle: NSBundle) : KotlinDecodable(KotlinDecoder(bundle)) {
    @Throws
    fun decodeObject(): Assessment {
        try {
            val serializer = PolymorphicSerializer(Assessment::class)
            val placeholder = decoder.jsonCoder.parse(serializer, jsonString)
            return placeholder.unpack(decoder.fileLoader, decoder, decoder.jsonCoder)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}

class ResultEncoder(val result: Result) {
    var jsonCoder: Json = Serialization.JsonCoder.default
    @Throws
    fun encodeObject(): String {
        try {
            val serializer = PolymorphicSerializer(Result::class)
            return jsonCoder.stringify(serializer, result)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}

class JsonElementEncoder(val jsonElement: JsonElement) {
    var jsonCoder: Json = Serialization.JsonCoder.default
    @Throws
    fun encodeObject(): String {
        try {
            return jsonCoder.stringify(JsonElement.serializer(), jsonElement)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}

class JsonElementDecoder(val jsonString: String) {
    var jsonCoder: Json = Serialization.JsonCoder.default
    @Throws
    fun decodeObject(): JsonElement {
        try {
            return jsonCoder.parseJson(jsonString)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}


