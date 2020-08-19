package org.sagebionetworks.assessmentmodel.recorders

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.sagebionetworks.assessmentmodel.*

/**
 * The [DistanceRecorderConfiguration] is used to define the configuration for a recorder used to track the distance
 * a person travelled. Typically, this is used by an assessment to measure the participant's cardiorespiratory fitness.
 *
 * If included, the [motionStepIdentifier] is used to tell the distance recorder which step to mark as the "in motion"
 * step. If non-null, then the recorder will calculate [DistanceRecord.totalDistance] travelled only while this step is
 * the current step by summing the [DistanceRecord.relativeDistance] values. All other steps included in the recorder
 * are assumed to be steps where the participant is directed to stand still and that the recorder is being used to
 * measure compliance.
 */
@Serializable
@SerialName("distance")
data class DistanceRecorderConfiguration(
    override val identifier: String,
    @SerialName("description")
    override val comment: String? = null,
    override val reason: String? = null,
    override val optional: Boolean = false,
    val motionStepIdentifier: String? = null,
    override val startStepIdentifier: String? = null,
    override val stopStepIdentifier: String? = null,
    override val shouldDeletePrevious: Boolean = true,
    override val usesCSVEncoding : Boolean = false
) : TableRecorderConfiguration {
    override val requiresBackground: Boolean
        get() = true
    @Transient
    override val permissions: List<PermissionInfo>? = listOf(
        PermissionType.Standard.Location.createPermissionInfo(optional = false, requiresBackground = true, reason = reason),
        PermissionType.Standard.Motion.createPermissionInfo(optional = true, requiresBackground = true, reason = reason))
}

/**
 * A [DistanceRecord] is a serializable object that can be used to record normalized GPS samples for use in determining
 * distance travelled.
 *
 * Record Properties:
 *   - [timestampUnix]: The Unix timestamp (seconds since 1970-01-01T00:00:00.000Z) when the measurement was taken.
 *   - [horizontalAccuracy]: The horizontal accuracy of the location in meters; `null` if the lateral location is invalid.
 *   - [relativeDistance]: The lateral distance between the current location and the previous location in meters.
 *   - [verticalAccuracy]: The vertical accuracy of the location in meters; `null` if the altitude is invalid.
 *   - [altitude]: The altitude of the location in meters. Can be positive (above sea level) or negative (below sea level).
 *   - [totalDistance]: Sum of the relative distance measurements if the participant is supposed to be moving.
 *   - [course]: The course of the location in degrees true North; `null` if course is invalid.
 *   - [bearingRadians]: The bearing to the location from the previous location in radians (clockwise from) true North.
 *   - [speed]: The speed of the location in meters/second; `null` if speed is invalid.
 *   - [floor]: The floor of the building where the location was recorded; `null` if floor is not available.
 *
 * Additionally, the record *may* include [latitude] and [longitude], but if and only if either this is a record for
 * a test user or if appropriate consent has been given by the participant to include sensitive location data.
 *
 */
@Serializable
data class DistanceRecord(
    override val stepPath: String? = null,
    @SerialName("timestampDate")
    override val timestampDateString: String? = null,
    override val timestamp: Double? = null,
    val uptime: Double? = null,
    val timestampUnix: Double? = null,
    val horizontalAccuracy: Double? = null,
    val relativeDistance: Double? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val verticalAccuracy: Double? = null,
    val altitude: Double? = null,
    val totalDistance: Double? = null,
    val course: Double? = null,
    val bearingRadians: Double? = null,
    val speed: Double? = null,
    val floor: Int? = null
) : SampleRecord