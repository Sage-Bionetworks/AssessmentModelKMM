package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import org.sagebionetworks.assessmentmodel.Assessment
import org.sagebionetworks.assessmentmodel.Node
import org.sagebionetworks.assessmentmodel.resourcemanagement.AssetInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.StandardResourceAssetType
import platform.CoreFoundation.kCFStringEncodingUTF8
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

interface KotlinDecoder : AssetInfo, ResourceInfo {
    val fileLoader: FileLoader
    var jsonCoder: Json
}

abstract class JsonFile() : KotlinDecoder {
    abstract var bundle: NSBundle
    override val fileLoader = FileLoaderIOS()
    override var jsonCoder = Serialization.JsonCoder.default

    @Throws
    abstract fun load()

    override val rawFileExtension: kotlin.String?
        get() = "json"
    override val resourceAssetType: kotlin.String?
        get() = StandardResourceAssetType.RAW
    override val versionString: String?
        get() = null

    fun decodeAssessmentGroupInfo(): AssessmentGroupInfo {
        val inputString = fileLoader.loadFile(this, this)
        val serializer = PolymorphicSerializer(AssessmentGroupInfo::class)
        val group = jsonCoder.parse(serializer, inputString)
        group.resourceInfo.decoderBundle = bundle
        return group
    }

    override var decoderBundle: Any?
        get() = bundle
        set(value) {
            if (value is NSBundle)
            bundle = value
        }

    override val bundleIdentifier: String?
        get() = null

    override var packageName: String?
        get() = throw NotImplementedError("Not used on iOS")
        set(value) {}
}

class AssessmentGroupLoader(override val resourceName: String, override var bundle: NSBundle) : JsonFile() {
    var assessmentGroupInfo: AssessmentGroupInfo? = null
        private set
    var assessments: List<AssessmentLoader> = listOf()
        private set

    @Throws
    override fun load() {
        try {
            val inputString = fileLoader.loadFile(this, this)
            val serializer = PolymorphicSerializer(AssessmentGroupInfo::class)
            val group = jsonCoder.parse(serializer, inputString)
            group.resourceInfo.decoderBundle = bundle
            assessmentGroupInfo = group
            assessments = group.files.map { AssessmentLoader(it, this) }
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}

class AssessmentLoader(private val placeholder: Assessment,
                       private val decoder: KotlinDecoder) : Assessment by placeholder {
    @Throws
    fun loadAssessment(): Assessment {
        try {
            return placeholder.unpack(decoder.fileLoader, decoder, decoder.jsonCoder)
        } catch (err: Exception) {
            throw Throwable(err.message)
        }
    }
}

