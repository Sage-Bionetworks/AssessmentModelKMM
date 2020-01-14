package org.sagebionetworks.assessmentmodel

/**
 * A [Button] can be used to customize the title and image displayed for a given action of the UI. This is the view
 * model for an UI element.
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
 * [ButtonAction] is used to define the "action" of a button.
 */
interface ButtonAction {
    val name: String
}

/**
 * A list of button actions defined within this module. These actions have special meaning that is used to support
 * task navigation.
 */
enum class NavigationButtonAction : ButtonAction {

    /**
     * Navigate to the next step.
     */
    goForward,

    /**
     * Navigate to the previous step.
     */
    goBackward,

    /**
     * Skip the step and immediately go forward.
     */
    skip,

    /**
     * Cancel the task.
     */
    cancel,

    /**
     * Display additional information about the step.
     */
    learnMore,

    /**
     * Go back in the navigation to review the instructions.
     */
    reviewInstructions
}

/**
 * A custom button action that serves as a placeholder for the button name.
 */
data class CustomButtonAction(override val name: String) : ButtonAction