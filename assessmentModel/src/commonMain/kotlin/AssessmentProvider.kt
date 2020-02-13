package org.sagebionetworks.assessmentmodel

interface AssessmentProvider {

    /**
     * Load an [Assessment] based on its identifier.
     */
    fun loadAssessment(assssmentIdentifier: String): Assessment?

}