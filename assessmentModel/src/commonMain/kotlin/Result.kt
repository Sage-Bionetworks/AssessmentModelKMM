package org.sagebionetworks.assessmentmodel

/**
 * A [Result] is any data result that should be included with an [Assessment]. The base level interface only has an
 * [identifier] and does not include any other properties. The [identifier] in this case may be either the
 * [ResultMapElement.resultIdentifier] *or* the [ResultMapElement.identifier] if the result identifier is undefined.
 *
 * TODO: syoung 01/10/2020 figure out a clean-ish way to encode the result and include in the base interface. In Swift, the `RSDResult` conforms to the `Encodable` protocol so it can be encoded to a JSON dictionary. Is there a Kotlin equivalent?
 *
 */
interface Result {

    /**
     * The identifier for the result. This identifier maps to the [ResultMapElement.resultIdentifier] for an associated
     * [Assessment] element.
     */
    val identifier: String
}

/**
 * A [CollectionResult] is used to describe the output of a [Section], [Form], or [Assessment].
 */
interface CollectionResult : Result {

    /**
     * The [inputResults] is a set that contains results that are recorded in parallel to the user-facing node
     * path. This can be the async results of a sensor recorder, a response to a service call, or the results from a
     * form where all the fields are displayed together and the results do not represent a linear path. The results
     * within this set should each have a unique identifier.
     */
    var inputResults: MutableSet<Result>
}

/**
 * The [BranchNodeResult] is the result created for a a given level of navigation of a node tree. The
 * [pathHistoryResults] is additive where each time a node is traversed, it is added to the list.
 */
interface BranchNodeResult : CollectionResult {

    /**
     * The [pathHistoryResults] includes the history of the [Node] results that were traversed as a part of running an
     * [Assessment]. This will only include a subset that is the path defined at this level of the overall [Assessment]
     * hierarchy.
     */
    var pathHistoryResults: MutableList<Result>
}

/**
 * An [AssessmentResult] is the top-level [Result] for an [Assessment].
 */
interface AssessmentResult : BranchNodeResult {

    /**
     * A unique identifier for this run of the assessment. This property is defined as readwrite to allow the
     * controller for the task to set this on the [AssessmentResult] children included in this run.
     */
    var runUUIDString: String

    /**
     * The [versionString] may be a semantic version, timestamp, or sequential revision integer. This should map to the
     * [Assessment.versionString].
     */
    val versionString: String?

    /**
     * The start date timestamp for the result.
     */
    var startDateString: String

    /**
     * The end date timestamp for the result.
     */
    var endDateString: String
}