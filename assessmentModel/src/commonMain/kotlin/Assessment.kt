package org.sagebionetworks.assessmentmodel

import org.sagebionetworks.assessmentmodel.navigation.NodeIdentifierPath
import org.sagebionetworks.assessmentmodel.navigation.Navigator
import org.sagebionetworks.assessmentmodel.serialization.AssessmentResultObject
import org.sagebionetworks.assessmentmodel.serialization.BranchNodeResultObject
import org.sagebionetworks.assessmentmodel.serialization.ResultObject

/**
 * A [Session] includes one or more [assessments] that are logically grouped together.
 *
 * The SageResearch equivalent is an `RSDTaskGroup`.
 *
 * TODO: syoung 01/28/2020 Flesh out the interface for a session including how to get its result.
 */
interface Session {
    val assessments: List<Assessment>
}

interface BranchNode : Node {

    // Override the default implementation to return a [BranchNodeResult]
    override fun createResult(): BranchNodeResult
            = BranchNodeResultObject(resultId())
}

/**
 * An [Assessment] is used to define the model information used to gather assessment (measurement) data needed for a
 * given study. It can include both the information needed to display a [Step] sequence to the participant as well as
 * the [AsyncActionConfiguration] data used to set up asynchronous actions such as sensors or web services that can be
 * used to inform the results.
 */
interface Assessment : BranchNode, ContentNode {

    /**
     * The [Navigator] for this assessment. If this is [null] then the [Assessment] will need to implement the
     * [NavigatorLoader] interface to allow for loading the navigator using a callback.
     */
    val navigator: Navigator?

    /**
     * The [versionString] may be a semantic version, timestamp, or sequential revision integer.
     */
    val versionString: String?

    /**
     * The estimated number of minutes that the assessment will take. If `0`, then it is assumed that this value is not
     * defined. Where provided, it can be used by an application to indicate to the participant approximately how
     * long an assessment is expected to take to complete.
     */
    val estimatedMinutes: Int

    // Override the default implementation to return an [AssessmentResult]
    override fun createResult(): AssessmentResult
            = AssessmentResultObject(resultId(), versionString)
}

/**
 * A result map element is an element in the [Assessment] model that defines the expectations for a [Result] associated
 * with this element. It can define a user-facing step, a section (which may or may not map to a view), a background
 * web service, a sensor recorder, or any other piece of data collected by the overall [Assessment].
 *
 * The [identifier] is used to build the input model mapping and the [resultIdentifier] is used to build an output model
 * with the same hierarchy where all the elements of the model conform to the [Result] protocol.
 */
interface ResultMapElement {

    /**
     * The identifier for the node.
     */
    val identifier: String

    /**
     * The identifier to use for the [Result] (if any) associated with this node. If null, the [identifier] will be
     * used instead.
     */
    val resultIdentifier: String?

    /**
     * The [comment] is *not* intended to be user-facing and is a field that allows the [Assessment] designer to add
     * explanatory text describing the purpose of the assessment, section, step, or background action.
     */
    val comment: String?

    /**
     * Create an appropriate instance of a *new* [Result] for this map element.
     */
    fun createResult(): Result
            = ResultObject(resultId())

    /**
     * Convenience method for accessing the result identifier associated with a given node.
     */
    fun resultId() : String
            = resultIdentifier ?: identifier
}

/**
 * A [Node] is any object defined within the structure of an [Assessment] that is used to display a sequence of [Step]
 * nodes. All nodes have an [identifier] string that can be used to uniquely identify the node.
 */
interface Node : ResultMapElement {

    /**
     * List of button actions that should be hidden for this node even if the node subtype typically supports displaying
     * the button on screen. This property can be defined at any level and will default to whichever is the lowest level
     * for which this mapping is defined.
     */
    val hideButtons: List<ButtonAction>

    /**
     * A mapping of a [ButtonAction] to a [Button].
     *
     * For example, this mapping can be used to define the url for a [ButtonAction.Navigation.LearnMore] link or to
     * customize the title of the [ButtonAction.Navigation.GoForward] button. It can also define the title, icon, etc.
     * on a custom button as long as the application knows how to interpret the custom action.
     *
     * Finally, a mapping can be used to explicitly mark a button as "should display" even if the overall assessment or
     * section includes the button action in the list of hidden buttons. For example, an assessment may define the
     * skip button as hidden but a lower level step within that assessment's hierarchy can return a mapping for the
     * skip button. The lower level mapping should be respected and the button should be displayed for that step only.
     */
    val buttonMap: Map<ButtonAction, Button>
}

/**
 * A [ContentNode] contains additional content that may, under certain circumstances and where screen real estate
 * allows, be displayed to the participant to help them understand the intended purpose of the part of the assessment
 * described by this [Node].
 */
interface ContentNode : Node {

    /**
     * The primary text to display for the node in a localized string. The UI should display this using a larger font.
     */
    val title: String?

    /**
     * A [subtitle] to display for the node in a localized string.
     */
    val subtitle: String?

    /**
     * Detail text to display for the node in a localized string.
     */
    val detail: String?

    /**
     * An image or animation to display with this node.
     */
    val imageInfo: ImageInfo?

    /**
     *
     * Additional text to display for the node in a localized string at the bottom of the view.
     *
     * The footnote is intended to be displayed in a smaller font at the bottom of the screen. It is intended to be
     * used in order to include disclaimer, copyright, etc. that is important to display to the participant but should
     * not distract from the main purpose of the [Step] or [Assessment].
     */
    val footnote: String?
        get() = null
}

/**
 * A [NodeContainer] has a collection of child nodes defined by the [children]. Whether or not these child nodes are
 * presented in a single screen will depend upon the platform and the UI/UX defined by the [Assesment] designers.
 */
interface NodeContainer : BranchNode {

    /**
     * The children contained within this collection.
     */
    val children: List<Node>

    /**
     * A list of the node identifiers to include in showing progress through the section or assessment. This is used
     * by a [NodeNavigator] to calculate progress.
     */
    val progressMarkers: List<String>?

    /**
     * Convenience method for mapping the child nodes to their identifier.
     */
    fun allNodeIdentifiers(): List<String> = children.map { it.identifier }
}

interface NavigatorLoader : Assessment {
    // TODO: syoung 01/28/2020 implement.
}

/**
 * [AsyncActionConfiguration] defines general configuration for an asynchronous action that should be run in
 * the background. Depending upon the parameters and how the action is set up, this could be something that is run
 * continuously or else is paused or reset based on a timeout interval.
 */
interface AsyncActionConfiguration : ResultMapElement

/**
 * An [AsyncActionContainer] is a node that contains the model description for asynchronous background actions that
 * should be started when this [Node] in the [Assessment] is presented to the user.
 */
interface AsyncActionContainer : Node {

    /**
     * A list of the [AsyncActionConfiguration] elements used to describe the configuration for background actions
     * (such as a sensor recorder or web service) that should should be started when this [Node] in the [Assessment] is
     * presented to the user.
     */
    val backgroundActions: List<AsyncActionConfiguration>
}

/**
 * A [Section] is used to define a logical sub-grouping of nodes and asynchronous background actions such as a section
 * in a longer survey or an active node that includes an instruction step, countdown step, and activity step.
 *
 * A [Section] is different from an [Assessment] in that it *always* describes a subgrouping of nodes that can be
 * displayed sequentially for platforms where the available screen real-estate does not support displaying the nodes
 * on a single view. A [Section] is also different from an [Assessment] in that it is a sub-node and does *not*
 * contain a measurement which, alone, is valuable to a study designer.
 */
interface Section : NodeContainer, ContentNode

/**
 * A user-interface step in an [Assessment].
 *
 * This is the base interface for the steps that can compose an assessment for presentation using a controller
 * appropriate to the device and application. Each [Step] object represents one logical piece of data entry,
 * information, or activity in a larger assessment.
 *
 * A step can be a question, an active test, or a simple instruction. It is typically paired with a step controller that
 * controls the actions of the [Step].
 */
interface Step : Node {

    /**
     * A mapping of the localized text that represents an instructional voice prompt to the time marker for speaking
     * the instruction. Time markers can be defined by a set of key words or as time intervals. Any step *could* include
     * a time marker on the step, though typically, this will only apply to active steps.
     *
     * - Example:
     * ```
     * {
     *      "start": "Start moving",
     *      "10": "Keep going",
     *      "halfway": "Halfway there",
     *      "countdown": "5",
     *      "end": "Stop moving"
     * }
     * ```
     */
    val spokenInstructions: Map<String, String>?  // TODO: syoung 01/27/2020 replace String with a sealed class
}

/**
 * [OverviewStep] extends [Step] to include general overview information about an [Assessment].
 */
interface OverviewStep : StandardPermissionsStep {

    /**
     * Detail text to display for the node in a localized string. For an overview step, the detail is readwrite.
     */
    override var detail: String?

    /**
     * The learn more button for the assessment that this overview step is describing. This is defined as readwrite so
     * that researchers who are using the [Assessment] as a part of their application can define a custom learn more
     * action.
     */
    var learnMore: Button?

    /**
     * The [icons] that are used to define the list of things you will need for an active assessment.
     */
    val icons: List<ImageInfo>?
}

/**
 * A generic configuration object with information about a given permission. The permission can be used by the
 * app to handle gracefully requesting authorization from the user for access to sensors, services, and hardware
 * required by the app.
 */
interface Permission {
    // TODO: syoung 01/27/2020 implement the class that describes permissions.
}

/**
 * [StandardPermissionsStep] extends the [Step] to include information about an activity including what permissions are
 * required by this step or assessment. Without these preconditions, the [Assessment] cannot measure or collect the data
 * needed for this assessment.
 */
interface StandardPermissionsStep : Step, ContentNode {
    // TODO: syoung 01/27/2020 implement the class that describes permissions.
}

/**
 * An [OptionalStep] is a subclass of step where the step should be displayed if and only if the [fullInstructionsOnly]
 * flag has been set for displaying the full instructions. This is used to allow the assessment designer to show more
 * detailed instructions only to users who are not already familiar with the assessment rather than showing a full set
 * of instructions every time.
 */
interface OptionalStep : Step {

    /**
     * Should this step only be displayed when showing the full instruction sequence?
     */
    val fullInstructionsOnly: Boolean
}

/**
 * An [InstructionStep] is a UI [Step] that includes detailed text with instructions. By design, there is *only* one
 * text label in an instruction step with the intention that the amount of text will be short enough to be readable on
 * a single screen.
 */
interface InstructionStep : OptionalStep, ContentNode

/**
 * [ActiveStep] extends the [Step] to include a [duration] and [commands]. This is used for the case where a step has
 * an action such as "start walking", "tap the screen", or "get ready".
 */
interface ActiveStep : Step {

    /**
     * The duration of time to run the step. If `0`, then this value is ignored.
     */
    val duration: Double

    /**
     * The set of commands to apply to this active step. These indicate actions to fire at the beginning and end of
     * the step such as playing a sound as well as whether or not to automatically start and finish the step.
     */
    val commands: Set<String> // TODO: syoung 01/27/2020 replace String with a data class

    /**
     * A mapping of the localized text shown in an instruction label to the time marker for when the instruction label
     * should be set to the given text. Time markers can be defined by a set of key words or as time intervals.
     *
     * For example, an active step may show the user a progress indicator with text prompts that are timed to match
     * a set of spoken instructions or add a prompt to tell the participant what to do.
     *
     * - Example:
     * ```
     * {
     *      "start": "Walk forward",
     *      "10": "Keep walking",
     *      "halfway": "Turn around and walk back",
     *      "20": "Almost done",
     *      "end": "All done!"
     * }
     * ```
     */
    val instructions: Map<String, String>?  // TODO: syoung 01/27/2020 replace String key with a sealed class

    /**
     * Whether or not the step uses audio, such as the speech synthesizer, that should play whether or not the user
     * has the mute switch turned on.
     */
    val requiresBackgroundAudio: Boolean

    /**
     * Should the assessment end early if the assessment is interrupted by a phone call?
     */
    val shouldEndOnInterrupt : Boolean
}

/**
 * A [CountdownStep] is a subtype of the [ActiveStep] that may only be displayed when showing the full instructions.
 * Typically, this type of step is shown using a label that displays a countdown to displaying the [ActiveStep] that
 * follows it.
 */
interface CountdownStep : OptionalStep, ActiveStep

/**
 * A [FormStep] is a container of other nodes where design of the form *requires* displaying all the components on a
 * single screen.
 *
 * For example, a [FormStep] may describe entering a participant's demographics data where the study designer wants to
 * display height, weight, gender, and birth year on a single screen.
 */
interface FormStep : Step, NodeContainer, ContentNode

/**
 * A [QuestionStep] can either be a [FormStep] or can describe the drill-down to display for an [InputField].
 */
interface QuestionStep : Step {

    /**
     * Additional text to display for the [FormStep] in a localized string.
     *
     * The additional text is often displayed in a smaller font below [title]. If you need to display a long question,
     * it can work well to keep the title short and put the additional content in the [prompt] property.
     */
    val prompt: String?

    /**
     * Additional text to display at the top of the screen in a smaller font to further explain the instructions for the
     * questions included on the screen shown to the participant.
     *
     * For example, "Select all that apply".
     */
    val promptDetail: String?

    /**
     * A question will always have at least one input field that is used to define the question. These fields will form
     * a logical grouping for how the [QuestionStep] should be presented to the user. For example, the question may be
     * "what is your name" where the fields are given name, family name, title, and a checkbox that says "prefer not to
     * answer". How the fields interact may use custom logic, but they are presented together and do not make sense
     * independently of one another.
     */
    val inputFields: List<InputField>

    /**
     * Can the question be skipped?
     */
    val optional: Boolean
}

/**
 * An [InputField] describes a "part" of a [QuestionStep] representing a single answer. For example, if a question is
 * "what is your name" then the input fields may include "given name" and "family name" where separate text fields
 * are used to allow the participant to enter their first and last name, and the question may also include a list of
 * titles from which to choose.
 */
interface InputField : Node {

    /**
     * The data type for this input field. The data type can have an associated ui hint.
     */
    val dataType: String    // TODO: syoung 01/27/2020 replace String with a sealed class

    /**
     * A UI hint for how the study would prefer that the input field is displayed to the user.
     */
    val inputUIHint: String? // TODO: syoung 01/27/2020 replace String with a sealed class

    /**
     * A localized string that displays a short text offering a hint to the user of the data to be entered for this
     * field.
     */
    val fieldLabel: String?

    /**
     * A localized string that displays placeholder information for the input field.
     *
     * You can display placeholder text in a text field or text area to help users understand how to answer the item's
     * question.
     */
    val placeholder: String?

    /**
     * Can the input field be left blank?
     */
    val optional: Boolean

    // TODO: syoung 01/27/2020 Complete the properties for describing an input field. See `RSDInputField`
}

/**
 * A result summary step is used to display a result that is calculated or measured earlier in the [Assessment].
 */
interface ResultSummaryStep : Step, ContentNode {

    /**
     * Text to display as the title above the result.
     */
    val resultTitle: String?

    /**
     * The localized unit to display for this result.
     */
    val unitText: String?

    /**
     * A link list that describes the path in an [AssessmentResult] down which to look for the result to use as the
     * answer to the result. If [null], then the application UI must define a custom presentation for showing the
     * result.
     */
    val scoringResultNodeIdentifier: NodeIdentifierPath?
}
