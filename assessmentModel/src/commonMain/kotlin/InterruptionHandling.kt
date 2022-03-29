package org.sagebionetworks.assessmentmodel

import kotlinx.serialization.Serializable

interface InterruptionHandling {
    /// Once interrupted, can the associated assessment be resumed?
    val canResume: Boolean

    /// Can partial results for this assessment be saved and the assessment resumed at some indeterminate time in the future?
    val canSaveForLater: Boolean

    /// Can this assessment be skipped or is it required for subsequent assessments that rely upon this one?
    val canSkip: Boolean

    /// An assessment might be designed to allow reviewing the instructions for that assessment, with direct navigation to
    /// the first "instruction" node. If so, that identifier can be defined for the assessment using this identifier.
    val reviewIdentifier: String?
}

@Serializable
data class InterruptionHandlingObject(
    override val reviewIdentifier: String? = null,
    override val canResume: Boolean = true,
    override val canSaveForLater: Boolean = true,
    override val canSkip: Boolean = true,
): InterruptionHandling