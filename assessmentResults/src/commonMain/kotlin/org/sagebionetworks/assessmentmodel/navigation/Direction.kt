package org.sagebionetworks.assessmentmodel.navigation

import kotlinx.serialization.Serializable

@Serializable
enum class Direction {
    /**
     * Move forward through the assessment.
     */
    Forward,
    /**
     * Move backward through the assessment.
     */
    Backward,
    /**
     * Exit the assessment early. If this direction indicator is set, then the entire assessment run should end.
     */
    Exit,
    ;
}