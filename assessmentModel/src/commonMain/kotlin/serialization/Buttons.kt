@file:UseSerializers(ImageNameSerializer::class)

package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceInfo

val buttonSerializersModule = SerializersModule {
    polymorphic(ButtonActionInfo::class) {
        ButtonActionInfoObject::class with ButtonActionInfoObject.serializer()
        NavigationButtonActionInfoObject::class with NavigationButtonActionInfoObject.serializer()
        ReminderButtonActionInfoObject::class with ReminderButtonActionInfoObject.serializer()
        WebViewButtonActionInfoObject::class with WebViewButtonActionInfoObject.serializer()
        VideoViewButtonActionInfoObject::class with VideoViewButtonActionInfoObject.serializer()
    }
}

@Serializable
abstract class SerializableButtonActionInfo : ButtonActionInfo, ResourceInfo {
    abstract val iconName: String?

    @Transient
    override var decoderBundle: Any? = null

    override val imageInfo: ImageInfo? by lazy {
        iconName?.let {
            FetchableImage(
                imageName = it,
                bundleIdentifier = this.bundleIdentifier,
                packageName = this.packageName,
                decoderBundle = this.decoderBundle
            )
        }
    }
}

@Serializable
@SerialName("default")
data class ButtonActionInfoObject(override val buttonTitle: String? = null,
                                  override val iconName: String? = null,
                                  override var packageName: String? = null,
                                  override var bundleIdentifier: String? = null) : SerializableButtonActionInfo()

@Serializable
@SerialName("navigation")
data class NavigationButtonActionInfoObject(override val buttonTitle: String? = null,
                                            override val iconName: String? = null,
                                            override val skipToIdentifier: String,
                                            override var packageName: String? = null,
                                            override var bundleIdentifier: String? = null) : SerializableButtonActionInfo(), NavigationButtonActionInfo

@Serializable
@SerialName("reminder")
data class ReminderButtonActionInfoObject(override val buttonTitle: String? = "\$remindMeLaterButtonTitle\$", // TODO: syoung 01/21/2020 Implement a localization strategy
                                          override val iconName: String? = null,
                                          override val reminderIdentifier: String,
                                          override val reminderPrompt: String? = null,
                                          override val reminderAlert: String? = null,
                                          override var packageName: String? = null,
                                          override var bundleIdentifier: String? = null) : SerializableButtonActionInfo(), ReminderButtonActionInfo

@Serializable
@SerialName("webView")
data class WebViewButtonActionInfoObject(override val buttonTitle: String? = null,
                                         override val iconName: String? = null,
                                         override val url: String,
                                         override val title: String? = null,
                                         val usesBackButton: Boolean? = null,
                                         val closeButtonTitle: String? = null,
                                         override var packageName: String? = null,
                                         override var bundleIdentifier: String? = null) : SerializableButtonActionInfo(), WebViewButtonActionInfo {
    override val backButtonStyle: ButtonStyle
        get() = closeButtonTitle?.let {
            return ButtonStyle.Footer(it) }
                ?: usesBackButton?.let {
                    return if (it) ButtonStyle.NavigationHeader.Back else ButtonStyle.NavigationHeader.Close }
                ?: super.backButtonStyle
}

@Serializable
@SerialName("videoView")
data class VideoViewButtonActionInfoObject(override val buttonTitle: String? = null,
                                           override val iconName: String? = null,
                                           override val url: String,
                                           override val title: String? = null,
                                           override var packageName: String? = null,
                                           override var bundleIdentifier: String? = null) : SerializableButtonActionInfo(), VideoViewButtonActionInfo
