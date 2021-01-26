package org.sagebionetworks.assessmentmodel.resourcemanagement

import org.sagebionetworks.assessmentmodel.Assessment
import org.sagebionetworks.assessmentmodel.AssessmentInfo

/**
 * The [ModuleInfo] is a way of collecting a group of assessments that are shared within a given module where
 * they should use the same [resourceInfo] to load any of the assessments within the [assessments] included in this group.
 */
interface ModuleInfo {
    val assessments: List<Assessment>
    val resourceInfo: ResourceInfo

    /**
     * Get the assessment for the given [AssessmentInfo]?
     */
    fun getAssessment(assessmentInfo: AssessmentInfo): Assessment? {
        return assessments.first {
            it.identifier == assessmentInfo.identifier
        }
    }

    /**
     * Does this [ModuleInfo] include the assessment for the given [AssessmentInfo]?
     */
    fun hasAssessment(assessmentInfo: AssessmentInfo): Boolean {
        return getAssessment(assessmentInfo) != null
    }
}