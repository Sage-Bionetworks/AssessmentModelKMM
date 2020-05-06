package org.sagebionetworks.assessmentmodel

import kotlinx.serialization.*

/**
 * A generic configuration object with information about a given permission. The permission can be used by the
 * app to handle gracefully requesting authorization from the user for access to sensors, services, and hardware
 * required by the app.
 */
interface PermissionInfo {

    /**
     * The permission type associated with this request. This can be used to generalize requesting certain permissions
     * if the common UX for a given platform recognizes the permission type.
     */
    val permissionType: PermissionType

    /**
     * Is the permission optional for a given task?
     *
     * - example:
     *
     * Test A requires the motion sensors to calculate the results, in which case this permission should be required
     * and the participant should be blocked from performing the task if the permission is not included.
     *
     * Test B uses the motion sensors (if available) to inform the results but can still receive valuable information
     * about the participant without them. In this case, the permission is optional and the participant should be
     * allowed to continue without permission to access the motion sensors.
     *
     */
    val optional: Boolean

    /**
     * Whether or not the permission requires accessing the sensors or services in the background.
     */
    val requiresBackground: Boolean

    /**
     * A localized string to display (when appropriate) that includes a reason why this assessment may want permission
     * to access the sensor or run in the background.
     */
    val reason: String?

    /**
     * The localized message to show when displaying an alert the participant that the application cannot run an
     * assessment because access to a required sensor is restricted.
     */
    val restrictedMessage: String?

    /**
     * The localized message to show when displaying an alert the participant that the application cannot run an
     * assessment because access to a required sensor is denied.
     */
    val deniedMessage: String?
}

/**
 * The [PermissionType] is used to wrap a string keyword (extendable enum) that can be used by the application to
 * determine the "type" of permission being requested. In some cases, this permission type is all that is needed to
 * set up the authorization, and in other cases, more information may be required on a subclass of the [PermissionInfo]
 * associated with this permission.
 */
@Serializable
sealed class PermissionType() : StringEnum {

    /**
     * A list of standard sensors.
     */
    sealed class Standard(override val name: String) : PermissionType() {
        object Camera : Standard("camera")
        object Location : Standard("location")
        object Microphone : Standard("microphone")
        object Motion : Standard("motion")
        object PhotoLibrary : Standard("photoLibrary")

        companion object : StringEnumCompanion<Standard> {
            override fun values(): Array<Standard>
                    = arrayOf(Camera, Location, Microphone, Motion, PhotoLibrary)
        }
    }

    data class Custom(override val name: String) : PermissionType()

    // TODO: syoung 01/23/2020 Keep an eye out for improvements to serialization of "inline" values. Currently Kotlin
    //  does not appear to have an equivalent to `RawRepresentable` which results in a lot of boiler plate like the
    //  implementation below.

    @Serializer(forClass = PermissionType::class)
    companion object : KSerializer<PermissionType> {
        override val descriptor: SerialDescriptor
                = PrimitiveDescriptor("SensorType", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): PermissionType {
            val name = decoder.decodeString()
            return valueOf(name)
        }
        override fun serialize(encoder: Encoder, value: PermissionType) {
            encoder.encodeString(value.name)
        }
        fun valueOf(name: String): PermissionType
                = Standard.valueOf(name) ?: Custom(name)
    }
}