package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.Node

/**
 * This a simple interface for keeping track of node state.
 *
 * Typically, this is used to allow for fine-grain navigation through an [Assessment] where the UI/UX might require a
 * more involved design that the straight-forward sequential display typical of the task-step model. For example, the
 * state may be used to allow a question to show a modal flow when the participant taps on an input field that cannot
 * easily be answered inline, such as setting up a calendar-based schedule.
 *
 * - Note: syoung 01/30/2020 This may, at some point, be replaced by a concrete implementation, but early research into
 * Kotlin/Native suggests that memory management is less performant that it is for native implementations on iOS that
 * ARC (automatic reference counting) and that the behavior will differ from what an iOS app developer is expecting.
 * While the parent node *could* be implemented using a [WeakReference] and delegation, this apparently is not
 * translated directly into an Objective-C weak reference, meaning that the life cycle will not conform to the expected
 * retain/release patterns with which an Obj-c developer is familiar.
 */
interface NodeState {

    /**
     * The [node] tied to this [NodeState].
     */
    val node: Node

    /**
     * The [parentNode] (if any) for the node chain.
     */
    val parentNode: Node?

    /**
     * The [previousRunData] is data stored by the application from a previous run of the same [Assessment].
     */
    val previousRunData: Any?
}