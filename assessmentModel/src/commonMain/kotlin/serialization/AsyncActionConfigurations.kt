package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.AsyncActionConfiguration
import org.sagebionetworks.assessmentmodel.recorders.DistanceRecorderConfiguration
import org.sagebionetworks.assessmentmodel.recorders.MotionRecorderConfiguration

/**
 * The distance and motion recorders are included within this package to allow for standardized serialization of these
 * recorders. In addition to these recorders, others may be added at a future date.
 */
val asyncActionSerializersModule = SerializersModule {
    polymorphic(AsyncActionConfiguration::class) {
        DistanceRecorderConfiguration::class with DistanceRecorderConfiguration.serializer()
        MotionRecorderConfiguration::class with MotionRecorderConfiguration.serializer()
    }
}
