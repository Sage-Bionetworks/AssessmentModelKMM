@file:OptIn(ExperimentalSerializationApi::class)

package org.sagebionetworks.assessmentmodel.navigation

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
enum class Direction {
    /**
     * Move forward through the assessment.
     */
    @SerialName("forward")
    @JsonNames("forward", "Forward")
    Forward,
    /**
     * Move backward through the assessment.
     */
    @SerialName("backward")
    @JsonNames("backward", "Backward")
    Backward,
    /**
     * Exit the assessment early. If this direction indicator is set, then the entire assessment run should end.
     */
    @SerialName("exit")
    @JsonNames("exit", "Exit")
    Exit,
    ;
}