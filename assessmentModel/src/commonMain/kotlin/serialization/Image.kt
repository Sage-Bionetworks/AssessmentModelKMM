package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.*

// TODO: syoung 01/13/2020 Figure out how to carry the resource bundle as a part of decoding an image and/or how to load an image from a Kotlin resource directory.

val imageSerializersModule = SerializersModule {
    polymorphic(ImageInfo::class) {
        FetchableImage::class with FetchableImage.serializer()
        AnimatedImage::class with AnimatedImage.serializer()
    }
}

@Serializable
@SerialName("fetchable")
data class FetchableImage(override val imageName: String,
                          override val label: String? = null,
                          @SerialName("placementType")
                          @Serializable(with=ImagePlacementTypeSerializer::class)
                          override val imagePlacement: ImagePlacementType? = null,
                          override val imageSize: Size? = null) : ImageInfo

@Serializable
@SerialName("animated")
data class AnimatedImage(override val animationImageNames: List<String>,
                         override val animationDuration: Double,
                         override val animationRepeatCount: Int?,
                         override val label: String? = null,
                         @SerialName("placementType")
                         @Serializable(with=ImagePlacementTypeSerializer::class)
                         override val imagePlacement: ImagePlacementType? = null,
                         override val imageSize: Size? = null) : AnimatedImageInfo {
    override val imageName: String
        get() = animationImageNames.first()
}

@Serializer(forClass = FetchableImage::class)
object ImageNameSerializer : KSerializer<FetchableImage> {
    override val descriptor: SerialDescriptor
            = StringDescriptor.withName("ImageName")
    override fun deserialize(decoder: Decoder): FetchableImage {
        return FetchableImage(decoder.decodeString())
    }
    override fun serialize(encoder: Encoder, obj: FetchableImage) {
        encoder.encodeString(obj.imageName)
    }
}