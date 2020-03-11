package org.sagebionetworks.assessmentmodel

interface AssessmentProvider {

    /**
     * Can this provider load an assessment associated with the given identifier?
     */
    fun canLoadAssessment(assessmentIdentifier: String): Boolean

    /**
     * Load an [Assessment] based on its identifier.
     */
    fun loadAssessment(assessmentIdentifier: String): Assessment?
}