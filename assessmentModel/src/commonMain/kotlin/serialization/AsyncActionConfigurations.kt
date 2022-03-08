package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.modules.*
import org.sagebionetworks.assessmentmodel.AsyncActionConfiguration

/**
 * The distance and motion recorders are included within this package to allow for standardized serialization of these
 * recorders. In addition to these recorders, others may be added at a future date.
 */
val asyncActionSerializersModule = SerializersModule {
    polymorphic(AsyncActionConfiguration::class) {
    }
}
