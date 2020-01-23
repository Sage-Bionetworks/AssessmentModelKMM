package org.sagebionetworks.assessmentmodel

import kotlinx.serialization.*
//import org.sagebionetworks.assessmentmodel.serialization.ExtendableStringEnum
//import org.sagebionetworks.assessmentmodel.serialization.ExtendableStringEnumSerializer
import org.sagebionetworks.assessmentmodel.serialization.StringEnum
import org.sagebionetworks.assessmentmodel.serialization.matching

/**
 * A [Button] can be used to customize the title and image displayed for a given action of the UI. This is the view
 * model for a UI element.
 */
interface Button {

    /**
     * The localized title to display on the button view associated with this action.
     */
    val buttonTitle: String?

    /**
     * The icon to display on the button view associated with this action.
     */
    val imageInfo: ImageInfo?
}

/**
 * The action of this [Button] is to set up navigation to a different step. That navigation should happen immediately
 * without waiting for normal completion of any activity associated with this step. The behavior is similar to a "skip"
 * button where timers and spoken instructions are ignored.
 */
interface NavigationButton : Button {

    /**
     * The identifier for the step to skip to if the action is called.
     */
    val skipToIdentifier: String
}

/**
 * [ReminderButton] is used to associate a button with the action of setting up a local notification for the
 * participant. Currently, the reminder notification only supports setting up a reminder that uses a time interval from
 * now rather than being scheduled at a specific time.
 */
interface ReminderButton : Button {

    /**
     * The identifier to use for the notification request.
     */
    val reminderIdentifier: String

    /**
     * A localized string to display when asking the participant when and if they want to set up a reminder.
     */
    val reminderPrompt: String?

    /**
     * A localized string to display when showing the participant the reminder.
     */
    val reminderAlert: String?
}

/**
 * A modal view button links to showing a modal view. Typically, this is used to define a web view or a video view with
 * limited UI outside of the URL that it is intended to display.
 */
interface ModalViewButton : Button {

    /**
     * What style of back button should be used? Should it use a back arrow icon, localized text with "Back" or "Close",
     * A close "X" icon, or custom text in a footer?
     *
     * @Note: This is only applicable to devices that use a back button or close button. Otherwise, it is ignored.
     */
    val backButtonStyle: ButtonStyle
        get() = ButtonStyle.NavigationHeader.Close

    /**
     * The title to show in a title bar or header.
     */
    val title: String?
}

/**
 * [WebViewButton] implements an extension of the base protocol where the action includes a pointer to a [url] that can
 * display in a web view. The url can either be fully qualified or point to an embedded resource.
 */
interface WebViewButton : ModalViewButton {

    /**
     * The url to load in the web view. If this is not a fully qualified url string, then it is assumed to refer to an
     * embedded resource.
     */
    val url: String
}

/**
 * [VideoViewButton] implements an extension of the base protocol where the action includes a pointer to a [url] that can
 * display in a video. The url can either be fully qualified or point to an embedded resource.
 */
interface VideoViewButton : ModalViewButton {

    /**
     * The url to load in the video view. If this is not a fully qualified url string, then it is assumed to refer to an
     * embedded resource.
     */
    val url: String
}

/**
 * The [ButtonStyle] defines a general "style" for the button to close a modal view. If the design calls for a back
 * button or a close button, then the style of the button is defined by the application. Whether or not to use text
 * ("Back" or "Close") or an icon (back arrow or close X) should be consistent throughout the application. Additionally,
 * whether or not a "close" button should be displayed on the left or the right of the header should also be defined
 * to be consistent throughout the application and is therefore, not included here.
 *
 * @note: This is currently a sealed class which means that it acts like a Swift enum where the enum does *not* directly
 * implement the `RawRepresentable` protocol, but can contain only those implementations defined by the framework and
 * cannot be subclassed or extended.
 */
@Serializable
sealed class ButtonStyle  {

    @Serializable
    @SerialName("header")
    sealed class NavigationHeader(val name: String) : ButtonStyle() {
        object Close: NavigationHeader("close")
        object Back: NavigationHeader("back")
    }

    @Serializable
    @SerialName("footer")
    data class Footer(val buttonTitle: String) : ButtonStyle()
}

/**
 * The [ButtonActionType] is used to wrap a string keyword (extendable enum) that can be used to describe a mapping of
 * UI buttons to the image and/or text that should be displayed on the button.
 */
interface ButtonActionType : StringEnum

/**
 * A list of button actions defined within this module. These actions have special meaning that is used to support
 * task navigation.
 *
 * - [GoForward]: Navigate to the next step.
 * - [GoBackward]: Navigate to the previous step.
 * - [Skip]: Skip the step and immediately go forward.
 * - [Cancel]: Cancel the task.
 * - [LearnMore]: Display additional information about the step.
 * - [ReviewInstructions]: Go back in the navigation to review the instructions.
 *
 */
@Serializable
enum class Navigation : ButtonActionType {
    GoForward,
    GoBackward,
    Skip,
    Cancel,
    LearnMore,
    ReviewInstructions,
    ;
}

//@Serializer(forClass = ButtonActionType::class)
//object ButtonActionTypeSerializer: ExtendableStringEnumSerializer<ButtonActionType>("ButtonActionType", ButtonAction)
//
///**
// * The [ButtonAction] enum describes standard navigation actions that are common to a given UI step. It is extendable
// * using the custom field.
// */
//object ButtonAction : ExtendableStringEnum<ButtonActionType> {
//
//    /**
//     * A list of button actions defined within this module. These actions have special meaning that is used to support
//     * task navigation.
//     *
//     * - [GoForward]: Navigate to the next step.
//     * - [GoBackward]: Navigate to the previous step.
//     * - [Skip]: Skip the step and immediately go forward.
//     * - [Cancel]: Cancel the task.
//     * - [LearnMore]: Display additional information about the step.
//     * - [ReviewInstructions]: Go back in the navigation to review the instructions.
//     *
//     */
//    @Serializable
//    enum class Navigation : ButtonActionType {
//        GoForward,
//        GoBackward,
//        Skip,
//        Cancel,
//        LearnMore,
//        ReviewInstructions,
//        ;
//    }
//
//    @Serializable(with = ButtonActionTypeSerializer::class)
//    data class Custom(override val name: String) : ButtonActionType
//
//    override fun standardValues(): Array<ButtonActionType> {
//        return Navigation.values() as Array<ButtonActionType>
//    }
//
//    override fun custom(name: String): ButtonActionType {
//        return Custom(name)
//    }
//}

