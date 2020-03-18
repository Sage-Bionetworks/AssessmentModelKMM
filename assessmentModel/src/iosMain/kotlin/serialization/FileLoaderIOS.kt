package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import org.sagebionetworks.assessmentmodel.Assessment
import org.sagebionetworks.assessmentmodel.Node
import org.sagebionetworks.assessmentmodel.resourcemanagement.AssetInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.StandardResourceAssetType
import platform.Foundation.*

class FileLoaderIOS() : FileLoader {
    override fun loadFile(assetInfo: AssetInfo, resourceInfo: ResourceInfo): String {
        val bundle = resourceInfo.bundle()
        val filename = assetInfo.resourceName
        val ext = assetInfo.rawFileExtension ?: "json"
        val url = bundle.URLForResource(filename, ext) ?: return ""
        return NSString.stringWithContentsOfURL(url, 4u, null) ?: return ""
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
interface KotlinDecoder : ResourceInfo {
    val fileLoader: FileLoader
    val jsonCoder: Json
}

interface KotlinDecodable {
    val decoder: KotlinDecoder
    fun jsonString(): String
}

abstract class KotlinDecodableIOSWrapper : KotlinDecoder, KotlinDecodable {
    abstract var bundle: NSBundle
    override var fileLoader: FileLoader = FileLoaderIOS()
    override var jsonCoder = Serialization.JsonCoder.default

    override val decoder: KotlinDecoder
        get() = this

    override var decoderBundle: Any?
        get() = bundle
        set(value) {
            if (value is NSBundle)
                bundle = value
        }

    override val bundleIdentifier: String?
        get() = null

    override var packageName: String? = null
}

abstract class JsonFile : KotlinDecodableIOSWrapper(), AssetInfo {
    override val rawFileExtension: kotlin.String?
        get() = "json"
    override val resourceAssetType: kotlin.String?
        get() = StandardResourceAssetType.RAW
    override val versionString: String?
        get() = null

    override fun jsonString() = fileLoader.loadFile(this, this)
}

class AssessmentGroupFileLoader(override val resourceName: String, override var bundle: NSBundle) : JsonFile() {
    @Throws
    fun decodeObject(): AssessmentGroupWrapper {
        try {
            val serializer = PolymorphicSerializer(AssessmentGroupInfo::class)
            val group = decoder.jsonCoder.parse(serializer, jsonString())
            group.resourceInfo.decoderBundle = decoder.decoderBundle
            val assessments = group.files.map { AssessmentLoader(it, decoder) }
            return AssessmentGroupWrapper(group, assessments)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}
data class AssessmentGroupWrapper(val assessmentGroupInfo: AssessmentGroupInfo, val assessments: List<AssessmentLoader>)

class AssessmentLoader(private val placeholder: Assessment,
                       private val decoder: KotlinDecoder) : Assessment by placeholder, KotlinDecoder by decoder {
    @Throws
    fun decodeObject(): Assessment {
        try {
            return placeholder.unpack(decoder.fileLoader, decoder, decoder.jsonCoder)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}

