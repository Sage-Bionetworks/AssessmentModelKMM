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
                          override val imagePlacement: ImagePlacement? = null,
                          override val size: Size? = null) : ImageInfo, ImageTheme

@Serializable
@SerialName("animated")
data class AnimatedImage(override val imageNames: List<String>,
                         override val animationDuration: Double,
                         override val animationRepeatCount: Int? = null,
                         override val label: String? = null,
                         @SerialName("placementType")
                         override val imagePlacement: ImagePlacement? = null,
                         override val size: Size? = null) : AnimatedImageInfo, ImageTheme {
    override val imageName: String
        get() = imageNames.first()
}

/**
 * [ImageTheme] is a [FrameLayout] that was developed for SageResearch-Apple and uses a serialization strategy where
 * layout is defined by the image placement and size (where applicable) rather than using specific image constraints.
 */
interface ImageTheme : DrawableLayout {
    val imagePlacement: ImagePlacement?
    val size: Size?
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

/**
 * [ImagePlacement] is used to wrap the [name] keyword and allow for an extendable string enum used to give a hint to
 * an [Assessment] developer of the layout to use for a given image. Typically, this is included so that a developer can
 * reuse the same view class where the designer requires different layout constraints for the image depending upon what
 * the image is showing.
 *
 * For example,
 * - An image of a person's midsection will look strange if the image does not "cut off" at the edges of the screen so
 * should be constrained to the edges. This is described using [ImagePlacement.Standard.TopBackground].
 * - An image of a person standing should be constrained to below the phone status bar so that the person is not
 * decapitated. This is described using [ImagePlacement.Standard.TopMarginBackground].
 * - An image of a trophy or smiley face should fit the screen real-estate with a size constraint and margins. This is
 * described using [ImagePlacement.Standard.IconBefore] or [ImagePlacement.Standard.IconAfter].
 *
 */
@Serializable
sealed class ImagePlacement() : StringEnum {

    /**
     * This class defines a set of image placements that are defined within this framework as standard.
     * For all placements that use `background`, the image should use `aspect fill`. For all `icon` placements, the
     * image should use `aspect fit`.
     *
     * - [IconBefore]:
     *      Display the image "before" the content. For a portrait orientation, this would indicate that the image should
     *      be displayed *above* the content. For landscape orientation, this would indicate that for languages that read
     *      left to right, the image should be on the *left*.
     * - [IconAfter]:
     *      Display the image "after" the content. For portrait orientation, this would be *below* the content. For
     *      landscape orientation for languages that read left to right, the image should be on the *right*.
     * - [FullSizeBackground]:
     *      Display the image full size in the background of the view.
     * - [TopBackground]:
     *      Top half of the background constrained to the top of the screen rather than to the safe area. On platforms that
     *      support drawing the image under the status bar and in the area of the "notch", this would draw in that area.
     * - [TopMarginBackground]:
     *      Top half of the background constrained to the safe area.
     * - [BackgroundBefore]:
     *      Display the image "before" the content in the background. In portrait, this is equivalent to
     *      [TopBackground] and in landscape for languages that read left to right, this would be the *left*
     *      half of the view.
     * - [BackgroundAfter]:
     *      Display the image "after" the content in the background. In portrait, the image should display using
     *      [TopBackground], but in landscape for languages that read left to right, this would display on the *right*
     *      half of the view.
     */
    sealed class Standard(override val name: String) : ImagePlacement() {
        object IconBefore : Standard("iconBefore")
        object IconAfter : Standard("iconAfter")
        object FullSizeBackground : Standard("fullSizeBackground")
        object TopBackground : Standard("topBackground")
        object TopMarginBackground : Standard("topMarginBackground")
        object BackgroundBefore : Standard("backgroundBefore")
        object BackgroundAfter : Standard("backgroundAfter")
        companion object : StringEnumCompanion<Standard>{
            override fun values(): Array<Standard>
                    = arrayOf(IconBefore, IconAfter, FullSizeBackground, TopBackground, TopMarginBackground, BackgroundBefore, BackgroundAfter)
        }
    }

    data class Custom(override val name: String) : ImagePlacement()

    // TODO: syoung 01/23/2020 Keep an eye out for improvements to serialization of "inline" values. Currently Kotlin
    //  does not appear to have an equivalent to `RawRepresentable` which results in a lot of boiler plate like the
    //  implementation below.

    @Serializer(forClass = ImagePlacement::class)
    companion object : KSerializer<ImagePlacement> {
        override val descriptor: SerialDescriptor
                = StringDescriptor.withName("ImagePlacement")
        override fun deserialize(decoder: Decoder): ImagePlacement {
            val name = decoder.decodeString()
            return valueOf(name)
        }
        override fun serialize(encoder: Encoder, obj: ImagePlacement) {
            encoder.encodeString(obj.name)
        }
        fun valueOf(name: String): ImagePlacement
                = Standard.valueOf(name) ?: Custom(name)
    }
}

/**
 * Size is a simple serializable data class that includes the [width] and [height] of a drawable element. The dimensions
 * are whatever unit is required by the implementing UI screens.
 */
@Serializable
data class Size(val width: Double, val height: Double)