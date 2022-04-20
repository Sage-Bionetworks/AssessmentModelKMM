package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.*
import kotlinx.serialization.modules.subclass
import org.sagebionetworks.assessmentmodel.*

val imageSerializersModule = SerializersModule {
    polymorphic(ImageInfo::class) {
        subclass(FetchableImage::class)
        subclass(AnimatedImage::class)
        subclass(SageResourceImage::class)
    }
}

// This serialization is included for reverse compatibility to existing JSON for an Overview step. syoung 05/04/2020
@Serializable
data class IconInfoObject(
    @SerialName("icon")
    override val imageName: String,
    @SerialName("title")
    override val label: String? = null,
    override val bundleIdentifier: String? = null,
    override var packageName: String? = null,
    override val rawFileExtension: String? = null,
    @Transient
    override var decoderBundle: Any? = null,
    override val versionString: String? = null
) : ImageInfo

@Serializable
@SerialName("sageResource")
data class SageResourceImage(
    override val imageName: String,
    override val label: String? = null,
) : ImageInfo {
    override val versionString: String?
        get() = null
    override val rawFileExtension: String?
        get() = null
    override var decoderBundle: Any?
        get() = null
        set(value) {}
    override val bundleIdentifier: String?
        get() = "AssessmentModelUI"
    override var packageName: String?
        get() = null
        set(value) {}

    val name: Name?
        get() = Name.values().matching(imageName)

    enum class Name(val tint:Boolean) : StringEnum {
        Survey(true),
        ;
    }
}

@Serializable
@SerialName("fetchable")
data class FetchableImage(override val imageName: String,
                          override val label: String? = null,
                          @SerialName("placementType")
                          val placementHint: String? = null,
                          val size: ImageSize? = null,
                          @Transient
                          override var decoderBundle: Any? = null,
                          override val bundleIdentifier: String? = null,
                          override var packageName: String? = null,
                          override val rawFileExtension: String? = null,
                          override val versionString: String? = null) : ImageInfo

@Serializable
@SerialName("animated")
data class AnimatedImage(override val imageNames: List<String>,
                         override val animationDuration: Double,
                         override val animationRepeatCount: Int? = null,
                         override val label: String? = null,
                         @SerialName("placementType")
                         val placementHint: String? = null,
                         val size: ImageSize? = null,
                         @Transient
                         override var decoderBundle: Any? = null,
                         override val bundleIdentifier: String? = null,
                         override var packageName: String? = null,
                         override val rawFileExtension: String? = null,
                         override val versionString: String? = null,
                         val compositeImageName: String? = null) : AnimatedImageInfo {
    override val imageName: String
        get() = compositeImageName ?: imageNames.first()
}

@Serializer(forClass = FetchableImage::class)
object ImageNameSerializer : KSerializer<FetchableImage> {
    override val descriptor: SerialDescriptor
            = PrimitiveSerialDescriptor("ImageName", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): FetchableImage {
        return FetchableImage(decoder.decodeString())
    }
    override fun serialize(encoder: Encoder, value: FetchableImage) {
        encoder.encodeString(value.imageName)
    }
}

/**
 * Size is a simple serializable data class that includes the [width] and [height] of a drawable element. The dimensions
 * are whatever unit is required by the implementing UI screens.
 */
@Serializable
data class ImageSize(val width: Double, val height: Double)