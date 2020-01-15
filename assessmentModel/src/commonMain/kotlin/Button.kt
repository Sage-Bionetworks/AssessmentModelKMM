package org.sagebionetworks.assessmentmodel

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import org.sagebionetworks.assessmentmodel.serialization.ExtendableStringEnum
import org.sagebionetworks.assessmentmodel.serialization.ExtendableStringEnumSerializer
import org.sagebionetworks.assessmentmodel.serialization.StringEnum

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
 * The [ButtonActionType] is used to wrap a string keyword (extendable enum) that can be used to describe a mapping of
 * UI buttons to the image and/or text that should be displayed on the button.
 */
interface ButtonActionType : StringEnum

@Serializer(forClass = ButtonActionType::class)
object ButtonActionTypeSerializer: ExtendableStringEnumSerializer<ButtonActionType>("ButtonActionType", ButtonAction)

/**
 * The [ButtonAction] enum describes standard navigation actions that are common to a given UI step. It is extendable
 * using the custom field.
 */
object ButtonAction : ExtendableStringEnum<ButtonActionType> {

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

    @Serializable(with = ButtonActionTypeSerializer::class)
    data class Custom(override val name: String) : ButtonActionType

    override fun standardValues(): Array<ButtonActionType> {
        return Navigation.values() as Array<ButtonActionType>
    }

    override fun custom(name: String): ButtonActionType {
        return Custom(name)
    }
}

