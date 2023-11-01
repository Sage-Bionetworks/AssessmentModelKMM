package org.sagebionetworks.assessmentmodel.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Direction {
    /**
     * Move forward through the assessment.
     */
    @SerialName("forward")
    Forward,
    /**
     * Move backward through the assessment.
     */
    @SerialName("backward")
    Backward,
    /**
     * Exit the assessment early. If this direction indicator is set, then the entire assessment run should end.
     */
    @SerialName("exit")
    Exit,
    ;
}