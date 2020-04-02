@file:UseSerializers(ImageNameSerializer::class)

package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.*

val buttonSerializersModule = SerializersModule {
    polymorphic(Button::class) {
        ButtonObject::class with ButtonObject.serializer()
        NavigationButtonObject::class with NavigationButtonObject.serializer()
        ReminderButtonObject::class with ReminderButtonObject.serializer()
        WebViewButtonObject::class with WebViewButtonObject.serializer()
        VideoViewButtonObject::class with VideoViewButtonObject.serializer()
    }
}

interface SerializableButton : Button {
    val icon: FetchableImage?
    override val imageInfo: ImageInfo?
        get() = icon
}

@Serializable
@SerialName("default")
data class ButtonObject(override val buttonTitle: String? = null,
                        @SerialName("iconName")
                        override val icon: FetchableImage? = null) : SerializableButton

@Serializable
@SerialName("navigation")
data class NavigationButtonObject(override val buttonTitle: String? = null,
                                  @SerialName("iconName")
                                  override val icon: FetchableImage? = null,
                                  override val skipToIdentifier: String) : SerializableButton, NavigationButton

@Serializable
@SerialName("reminder")
data class ReminderButtonObject(override val buttonTitle: String? = "\$remindMeLaterButtonTitle\$", // TODO: syoung 01/21/2020 Implement a localization strategy
                                @SerialName("iconName")
                                override val icon: FetchableImage? = null,
                                override val reminderIdentifier: String,
                                override val reminderPrompt: String? = null,
                                override val reminderAlert: String? = null) : SerializableButton, ReminderButton

@Serializable
@SerialName("webView")
data class WebViewButtonObject(override val buttonTitle: String? = null,
                               @SerialName("iconName")
                               override val icon: FetchableImage? = null,
                               override val url: String,
                               override val title: String? = null,
                               val usesBackButton: Boolean? = null,
                               val closeButtonTitle: String? = null) : SerializableButton, WebViewButton {
    override val backButtonStyle: ButtonStyle
        get() = closeButtonTitle?.let {
            return ButtonStyle.Footer(it) }
                ?: usesBackButton?.let {
                    return if (it) ButtonStyle.NavigationHeader.Back else ButtonStyle.NavigationHeader.Close }
                ?: super.backButtonStyle
}

@Serializable
@SerialName("videoView")
data class VideoViewButtonObject(override val buttonTitle: String? = null,
                                 @SerialName("iconName")
                                 override val icon: FetchableImage? = null,
                                 override val url: String,
                                 override val title: String? = null) : SerializableButton, VideoViewButton
