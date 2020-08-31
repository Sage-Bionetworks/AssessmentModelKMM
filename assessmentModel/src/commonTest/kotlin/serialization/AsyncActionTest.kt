package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.AsyncActionConfiguration
import org.sagebionetworks.assessmentmodel.recorders.*
import kotlin.test.Test
import kotlin.test.assertEquals

open class AsyncActionTest {

    private val jsonCoder = Serialization.JsonCoder.default

    @Serializable
    data class TestAsyncActionConfigurationWrapper(val config: AsyncActionConfiguration)

    /**
     * DistanceRecorder
     */

    @Test
    fun testDistanceRecorderConfiguration_Serialization() {
        val config = DistanceRecorderConfiguration(
            identifier = "foo",
            motionStepIdentifier = "magoo",
            startStepIdentifier = "baloo",
            stopStepIdentifier = "too",
            shouldDeletePrevious = false,
            usesCSVEncoding = true
        )
        val inputString = """
            {
                "config": {
                    "type": "distance",
                    "identifier": "foo",
                    "startStepIdentifier": "baloo",
                    "stopStepIdentifier": "too",
                    "motionStepIdentifier": "magoo",
                    "shouldDeletePrevious": false,
                    "usesCSVEncoding": true
                }
            }    
            """.trimIndent()

        val original = TestAsyncActionConfigurationWrapper(config)
        val jsonString = jsonCoder.encodeToString(TestAsyncActionConfigurationWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestAsyncActionConfigurationWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestAsyncActionConfigurationWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testDistanceRecord_Serialization() {
        val original = DistanceRecord(
            stepPath = "Foo_task/step1",
            timestampDateString = "2525-01-01T08:00:00Z",
            timestamp = 2.0,
            uptime = 2324342.0,
            timestampUnix = 23242232342.0,
            horizontalAccuracy = 2.0,
            relativeDistance = 0.75,
            verticalAccuracy = 5.0,
            altitude = 0.0,
            totalDistance = 100.0,
            course = 92.0,
            bearingRadians = 5.2,
            speed = 1.2,
            floor = 2
        )
        val inputString = """
            {
                "stepPath": "Foo_task/step1",
                "timestampDate": "2525-01-01T08:00:00Z",
                "timestamp": 2.0,
                "uptime": 2324342.0,
                "timestampUnix": 23242232342.0,
                "horizontalAccuracy": 2.0,
                "relativeDistance": 0.75,
                "verticalAccuracy": 5.0,
                "altitude": 0.0,
                "totalDistance": 100.0,
                "course": 92.0,
                "bearingRadians": 5.2,
                "speed": 1.2,
                "floor": 2
            }    
            """.trimIndent()

        val jsonString = jsonCoder.encodeToString(DistanceRecord.serializer(), original)
        val restored = jsonCoder.decodeFromString(DistanceRecord.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(DistanceRecord.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    /**
     * MotionRecorder
     */

    @Test
    fun testMotionRecorderConfiguration_Serialization() {
        val config = MotionRecorderConfiguration(
            identifier = "foo",
            startStepIdentifier = "baloo",
            stopStepIdentifier = "too",
            requiresBackground = true,
            shouldDeletePrevious = false,
            usesCSVEncoding = true,
            recorderTypes = MotionRecorderType.raw,
            samplingFrequency = 50.0
        )
        val inputString = """
            {
                "config": {
                    "type": "motion",
                    "identifier": "foo",
                    "startStepIdentifier": "baloo",
                    "stopStepIdentifier": "too",
                    "requiresBackgroundAudio": true,
                    "shouldDeletePrevious": false,
                    "usesCSVEncoding": true,
                    "recorderTypes": ["accelerometer", "gyro", "magnetometer"],
                    "frequency": 50.0
                }
            }    
            """.trimIndent()

        val original = TestAsyncActionConfigurationWrapper(config)
        val jsonString = jsonCoder.encodeToString(TestAsyncActionConfigurationWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestAsyncActionConfigurationWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestAsyncActionConfigurationWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testMotionRecord_Serialization() {
        val original = MotionRecord(
            stepPath = "Foo_task/step1",
            timestampDateString = "2525-01-01T08:00:00Z",
            timestamp = 2.0,
            uptime = 2324342.0,
            sensorType = MotionRecorderType.Attitude,
            x = 5.0,
            y = 7.3,
            z = 0.2,
            w = 99.3,
            eventAccuracy = 1,
            referenceCoordinate = AttitudeReferenceFrame.XMagneticNorthZVertical
        )
        val inputString = """
            {
                "stepPath": "Foo_task/step1",
                "timestampDate": "2525-01-01T08:00:00Z",
                "timestamp": 2.0,
                "uptime": 2324342.0,
                "sensorType": "attitude",
                "x": 5.0,
                "y": 7.3,
                "z": 0.2,
                "w": 99.3,
                "eventAccuracy": 1,
                "referenceCoordinate": "North-West-Up"
            }    
            """.trimIndent()

        val jsonString = jsonCoder.encodeToString(MotionRecord.serializer(), original)
        val restored = jsonCoder.decodeFromString(MotionRecord.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(MotionRecord.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }
}
