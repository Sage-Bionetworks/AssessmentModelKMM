package org.sagebionetworks.assessmentmodel

interface AssessmentProvider {

    /**
     * Can this provider load an assessment associated with the given [AssessmentInfo]?
     */
    fun canLoadAssessment(assessmentInfo: AssessmentInfo): Boolean

    /**
     * Load an [Assessment] based on its [AssessmentInfo].
     */
    fun loadAssessment(assessmentInfo: AssessmentInfo): Assessment?
}