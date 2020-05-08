package org.sagebionetworks.assessmentmodel.recorders

import kotlinx.serialization.*
import org.sagebionetworks.assessmentmodel.*

/**
 * The [MotionRecorderConfiguration] is used to define the configuration properties for motion sensors. The actual
 * implementation of the motion recorder is platform-specific and is not defined within this package.
 */
@Serializable
@SerialName("motion")
data class MotionRecorderConfiguration(
    override val identifier: String,
    override val resultIdentifier: String? = null,
    @SerialName("description")
    override val comment: String? = null,
    override val reason: String? = null,
    override val optional: Boolean = false,
    override val startStepIdentifier: String? = null,
    override val stopStepIdentifier: String? = null,
    @SerialName("requiresBackgroundAudio")
    override val requiresBackground: Boolean = false,
    override val shouldDeletePrevious: Boolean = true,
    override val usesCSVEncoding : Boolean = false,
    val recorderTypes: Set<MotionRecorderType> = setOf(MotionRecorderType.Accelerometer, MotionRecorderType.Gyro),
    @SerialName("frequency")
    val samplingFrequency: Double? = null
) : RecorderConfiguration {
    @Transient
    override val permissions: List<PermissionInfo>? = listOf(
        PermissionType.Standard.Motion.createPermissionInfo(false, requiresBackground, reason))
}

/**
 * [MotionRecorderType] is used to enumerate the sensors and calculated measurements that should be recorded for a
 * given assessment.
 *
 * Splitting the device motion into components in this manner stores the data using a consistent JSON schema that
 * can represent the sensor data returned by both iOS and Android devices. Additionally, this allows for representing
 * the data using a single table which can then be  filtered by type to perform calculations and DSP on the input
 * sources.
 *
 * The recorder types are split into two categories of [raw] sensor data and [calculated] values. The [calculated]
 * values are are black box values calculated by the platform libraries and may vary between devices.
 *
 * - Raw data:
 *   - [Accelerometer]: Raw accelerometer reading.
 *   - [Gyro]: Raw gyroscope reading.
 *   - [Magnetometer]: Raw magnetometer reading.
 *
 * - Calculated values:
 *   - [Attitude]: Calculated orientation of the device using the gyro and magnetometer (if appropriate).
 *   - [Gravity]: Calculated vector for the direction of gravity in the coordinates of the device.
 *   - [MagneticField]: The magnetic field vector with respect to the device for devices with a magnetometer.
 *   - [RotationRate]: The rotation rate of the device for devices with a gyro.
 *   - [UserAcceleration]: Calculated vector for the participant's acceleration in the coordinates of the device.
 *
 */
@Serializable
enum class MotionRecorderType : StringEnum {
    Accelerometer, Gyro, Magnetometer,
    Attitude, Gravity, MagneticField, RotationRate, UserAcceleration;

    @Serializer(forClass = MotionRecorderType::class)
    companion object : KSerializer<MotionRecorderType> {
        override val descriptor: SerialDescriptor = PrimitiveDescriptor("MotionRecorderType", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): MotionRecorderType {
            val name = decoder.decodeString()
            return values().matching(name) ?: throw SerializationException("Unknown $name for ${descriptor.serialName}. Needs to be one of ${values()}")
        }
        override fun serialize(encoder: Encoder, value: MotionRecorderType) {
            encoder.encodeString(value.name.decapitalize())
        }

        val raw = setOf(
            Accelerometer,
            Gyro,
            Magnetometer
        )
        val calculated = setOf(
            Attitude,
            Gravity,
            MagneticField,
            RotationRate,
            UserAcceleration
        )
    }
}

/**
 * A [MotionRecord] is a serializable implementation of a [SampleRecord] that can be used to record a sample from one
 * of the motion sensors or calculated vectors. All properties on the record ard defined as `null` to allow using this
 * same serialization object to define both path markers that are added to the data stream to mark step transitions
 * and the results of the sensor.
 *
 * If the [sensorType] is `null` then this is a path marker. Other values included here are only applicable to certain
 * sensors. This data structure allows for defining all records within a single file.
 *
 * Vector properties:
 * - [x]: The `x` component of the vector measurement for this sensor sample.
 * - [y]: The `y` component of the vector measurement for this sensor sample.
 * - [z]: The `z` component of the vector measurement for this sensor sample.
 * - [w]: The `w` component of the vector measurement for this sensor sample. (Used by the attitude quaternion.)
 *
 * Additional properties:
 * - [eventAccuracy]: A number marking the sensor accuracy of the magnetic field sensor.
 * - [referenceCoordinate]: Used for an [MotionRecorderType.Attitude] record type to describe the reference frame.
 * - [heading]: The angle in the range [0,360) degrees with respect to [AttitudeReferenceFrame.XMagneticNorthZVertical].
 *
 */
@Serializable
data class MotionRecord(
    override val stepPath: String? = null,
    @SerialName("timestampDate")
    override val timestampDateString: String? = null,
    override val timestamp: Double? = null,
    val uptime: Double? = null,
    val sensorType: MotionRecorderType? = null,
    val x: Double? = null,
    val y: Double? = null,
    val z: Double? = null,
    val w: Double? = null,
    val eventAccuracy: Int? = null,
    val referenceCoordinate: AttitudeReferenceFrame? = null,
    val heading: Double? = null
) : SampleRecord

@Serializable
enum class AttitudeReferenceFrame  {
     /**
      * Describes a reference frame in which the Z axis is vertical and the X axis points in an arbitrary direction in
      * the horizontal plane.
      */
     @SerialName("Z-Up") XArbitraryZVertical,

     /**
      * Describes a reference frame in which the Z axis is vertical and the X axis points toward magnetic north.
      *
      * - note: Using this reference frame may require user interaction to calibrate the magnetometer.
      */
     @SerialName("North-West-Up") XMagneticNorthZVertical;
}