package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.sagebionetworks.assessmentmodel.Assessment
import org.sagebionetworks.assessmentmodel.AssessmentInfo
import org.sagebionetworks.assessmentmodel.ModuleInfo
import org.sagebionetworks.assessmentmodel.Result
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
    var jsonCoder: Json = Json {
        serializersModule = moduleInfoSerializersModule
        encodeDefaults = true
    }

    override var decoderBundle: Any? = bundle
    override var packageName: String? = null
    override val bundleIdentifier: String?
        get() = null
}

abstract class KotlinDecodable(val decoder: KotlinDecoder) {
    abstract val jsonString: String?
}

// TODO: syoung 01/27/2021 Revisit and get an AssessmentLoader working for iOS. For now, just comment out to unblock Android development.
//
//class AssessmentGroupStringLoader(override val jsonString: String, bundle: NSBundle) : KotlinDecodable(KotlinDecoder(bundle)) {
//    @Throws(Throwable::class)
//    fun decodeObject(): AssessmentGroupWrapper {
//        try {
//            val serializer = PolymorphicSerializer(ModuleInfo::class)
//            val moduleInfo = decoder.jsonCoder.decodeFromString(serializer, jsonString)
//            moduleInfo.resourceInfo.decoderBundle = decoder.decoderBundle
//            val assessments = group.assessments.map { AssessmentLoader(it, decoder) }
//            return AssessmentGroupWrapper(group, assessments)
//        } catch (err: Exception) {
//            throw Throwable(err.message)
//        }
//    }
//}
//data class AssessmentGroupWrapper(val moduleInfo: ModuleInfo, val assessments: List<AssessmentLoader>)
//
//class AssessmentLoader(private val placeholder: Assessment,
//                       private val decoder: KotlinDecoder) : Assessment by placeholder {
//    @Throws(Throwable::class)
//    fun decodeObject(): Assessment {
//        try {
//            return placeholder.unpack(decoder.moduleInfoProvider, decoder, decoder.moduleInfoProvider.getRegisteredJsonDecoder(placeholder) ?: decoder.jsonCoder)
//        } catch (err: Exception) {
//            throw Throwable(err.message)
//        }
//    }
//}
//
//class AssessmentJsonStringLoader(override val jsonString: String, bundle: NSBundle) : KotlinDecodable(KotlinDecoder(bundle)) {
//    @Throws(Throwable::class)
//    fun decodeObject(): Assessment {
//        try {
//            val serializer = PolymorphicSerializer(Assessment::class)
//            val placeholder = decoder.jsonCoder.decodeFromString(serializer, jsonString)
//            return placeholder.unpack(decoder.moduleInfoProvider, decoder, decoder.moduleInfoProvider.getRegisteredJsonDecoder(placeholder) ?: decoder.jsonCoder)
//        } catch (err: Exception) {
//            throw Throwable(err.message)
//        }
//    }
//}

class ResultEncoder(val result: Result) {
    var jsonCoder: Json = Serialization.JsonCoder.default
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

class JsonElementEncoder(val jsonElement: JsonElement) {
    var jsonCoder: Json = Serialization.JsonCoder.default
    @Throws(Throwable::class)
    fun encodeObject(): String {
        try {
            return jsonCoder.encodeToString(JsonElement.serializer(), jsonElement)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}

class JsonElementDecoder(val jsonString: String) {
    var jsonCoder: Json = Serialization.JsonCoder.default
    @Throws(Throwable::class)
    fun decodeObject(): JsonElement {
        try {
            return jsonCoder.parseToJsonElement(jsonString)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}


