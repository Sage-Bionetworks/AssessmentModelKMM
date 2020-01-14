package org.sagebionetworks.assessmentmodel

/**
 * An [Assessment] is used to define the model information used to gather assessment (measurement) data needed for a
 * given study. It can include both the information needed to display a [Step] sequence to the participant as well as
 * the [AsyncActionConfiguration] data used to set up asynchronous actions such as sensors or web services that can be
 * used to inform the results.
 */
interface Assessment : NavigationNode {

    /**
     * The [versionString] may be a semantic version, timestamp, or sequential revision integer.
     */
    val versionString: String
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
     * explanatory text describing the purpose of the step, section, or background action.
     */
    val comment: String?

    /**
     * Create an appropriate instance of a *new* [Result] for this map element.
     */
    fun createResult(): Result
}

/**
 * A [Node] is any object defined within the structure of an [Assessment] that is used to display a sequence of [Step]
 * nodes. All nodes have an [identifier] string that can be used to uniquely identify the node.
 */
interface Node : ResultMapElement {

    /**
     * The primary text to display for the node in a localized string. The UI should display this using a larger font.
     */
    val title: String?

    /**
     * A subtitle or label to display for the node in a localized string.
     */
    val label: String?

    /**
     * An image or animation to display with this node.
     */
    val imageInfo: ImageInfo?

    /**
     * Additional detail text to display for the node in a localized string.
     */
    val detail: String?

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

    /**
     * List of button actions that should be hidden for this node even if the node subtype typically supports displaying
     * the button on screen.
     */
    val hideButtons: List<ButtonAction>

    /**
     * A mapping of a [ButtonAction] to a [Button].
     */
    val buttonMap: Map<ButtonAction, Button>
}

/**
 * A [NodeContainer] has a collection of child nodes defined by the [children]. Whether or not these child nodes are
 * presented in a single screen will depend upon the platform and the UI/UX defined by the [Assesment] designers.
 */
interface NodeContainer : Node {

    /**
     * The children contained within this collection.
     */
    val children: List<Node>
}

/**
 * A [NavigationNode] is a node that includes a step navigator that can be used to display a [Step] sequence.
 */
interface NavigationNode : Node {

    /**
     * The step navigator for this task.
     */
    val navigator: Navigator
}

/**
 * The [Navigator] is used by the assessment view controller or fragment to determine the order of presentation of
 * the [Step] elements in an assessment as well as when to start/stop the asynchronous background actions defined by
 * the [AsyncActionConfiguration] elements. The navigator is responsible for determining navigation based on the input
 * model, results, and platform context.
 */
interface Navigator {
// TODO: syoung 01/10/2020 implement.
}

/**
 * A user-interface step in an Assessment.
 *
 * This is the base interface for the steps that can compose an assessment for presentation using a controller
 * appropriate to the device and application. Each [Step] object represents one logical piece of data entry,
 * information, or activity in a larger task.
 *
 * A step can be a question, an active test, or a simple instruction. It is typically paired with a step controller that
 * controls the actions of the [Step].
 */
interface Step : Node

/**
 * A [Section] is used to define a logical subgrouping of nodes and asynchronous background actions such as a section 
 * in a longer survey or an active node that includes an instruction step, countdown step, and activity step.
 * 
 * A [Section] is different from an [Assessment] in that it *always* describes an a subgrouping of nodes that can be
 * displayed sequentially for platforms where the available screen real-estate does not support displaying the nodes
 * on a single view. A [Section] is also different from an [Assessment] in that it is a sub-node and does *not*
 * contain a measurement which, alone, is valuable to a a study designer.
 */
interface Section : NodeContainer

/**
 * A [Form] is a container of other nodes where design of the form *requires* displaying all the components on a single
 * screen.
 *
 * For example, a [Form] may describe entering a participant's demographics data where the study designer wants to
 * display height, weight, gender, and birth year on a single screen.
 */
interface Form : Step, NodeContainer

/**
 * [AsyncActionConfiguration] defines general configuration for an asynchronous background action that should be run in
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