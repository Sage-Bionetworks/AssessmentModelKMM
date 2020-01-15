package org.sagebionetworks.assessmentmodel

/**
 * The [ImageInfo] is used to define a placeholder for an image. This could refer to a drawable object as defined by
 * the platform, a url, or the name of an embedded resource. The [height] and [width], if defined, describe the image
 * size and can be defined in either pixels or points. The [imageIdentifier] is used to uniquely identify this image
 * (or set of images) so that the client platform can fetch the image data. The [label] may be displayed as a caption
 * for the image or set as the accessibility label for the image on platforms that implement user accessibility on
 * images.
 */
interface ImageInfo {

    /**
     * A unique identifier that can be used to validate that the image shown in a reusable view is the same image as the
     * one fetched.
     */
    val imageIdentifier: String

    /**
     * A caption or label to display for the image in a localized string.
     */
    val label: String?

    /**
     * The preferred placement of the image.
     */
    val placement: ImagePlacement
        get() = ImagePlacement.iconBefore

    /**
     * The height of the image.
     */
    val height: Int
        get() = 0

    /**
     * The width of the image.
     */
    val width: Int
        get() = 0
}

enum class ImagePlacement {
    iconBefore, iconAfter, fullsizeBackground, topBackground, topMarginBackground
}
