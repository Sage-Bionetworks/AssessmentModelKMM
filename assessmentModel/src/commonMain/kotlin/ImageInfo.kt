package org.sagebionetworks.assessmentmodel

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import org.sagebionetworks.assessmentmodel.serialization.ExtendableStringEnum
import org.sagebionetworks.assessmentmodel.serialization.ExtendableStringEnumSerializer
import org.sagebionetworks.assessmentmodel.serialization.StringEnum
import org.sagebionetworks.assessmentmodel.serialization.matching

/**
 * The [ImageInfo] is used to define a placeholder for an image. This could refer to a drawable object as defined by
 * the platform, a url, or the name of an embedded resource.
 */
interface ImageInfo {

    /**
     * A unique identifier that can be used to validate that the image shown in a reusable view is the same image as the
     * one fetched. This can also be used as the string value to fetch an image.
     */
    val imageName: String

    /**
     * A caption or label to display for the image in a localized string.
     */
    val label: String?

    /**
     * The preferred placement of the image. If undefined, then the default placement will depend upon the UI view being
     * used to display the image.
     */
    val imagePlacement: ImagePlacementType?

    /**
     * The size of the image in whatever units are appropriate for the given platform.
     */
    val imageSize: Size?
}

interface AnimatedImageInfo : ImageInfo {

    /**
     * The list of image names for the images to include in this animation.
     */
    val animationImageNames: List<String>

    /**
     * The animation duration for the image animation.
     */
    val animationDuration: Double

    /**
     * This is used to set how many times the animation should be repeated where `0` means infinite.
     */
    val animationRepeatCount: Int?
}

/**
 * An interface that is used to wrap the [name] keyword and allow for an extendable string enum used to give a hint to
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
interface ImagePlacementType : StringEnum

@Serializer(forClass = ImagePlacementType::class)
object ImagePlacementTypeSerializer: ExtendableStringEnumSerializer<ImagePlacementType>("ImagePlacementType", ImagePlacement)

/**
 * String wrapper for describing the image placement of an image. This is used to allow extending the
 * [ImagePlacement.Standard] enum to allow for custom placement typing.
 */
object ImagePlacement : ExtendableStringEnum <ImagePlacementType> {

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
    @Serializable
    enum class Standard : ImagePlacementType {
        IconBefore,
        IconAfter,
        FullSizeBackground,
        TopBackground,
        TopMarginBackground,
        BackgroundBefore,
        BackgroundAfter,
        ;
    }

    @Serializable(with = ImagePlacementTypeSerializer::class)
    data class Custom(override val name: String) : ImagePlacementType

    override fun standardValues(): Array<ImagePlacementType> {
        return Standard.values() as Array<ImagePlacementType>
    }

    override fun custom(name: String): ImagePlacementType {
        return Custom(name)
    }
}

@Serializable
data class Size(val width: Int = 0, val height: Int = 0)