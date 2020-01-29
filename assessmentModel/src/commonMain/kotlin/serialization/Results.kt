package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.*

val resultSerializersModule = SerializersModule {
    polymorphic(Result::class) {
        CollectionResultObject::class with CollectionResultObject.serializer()
        AssessmentResultObject::class with AssessmentResultObject.serializer()
        ResultObject::class with ResultObject.serializer()
        WebViewButtonObject::class with WebViewButtonObject.serializer()
        VideoViewButtonObject::class with VideoViewButtonObject.serializer()
    }
}


@Serializable
@SerialName("collection")
data class CollectionResultObject(override val identifier: String,
                                  @SerialName("stepHistory")
                                  override var pathHistoryResults: MutableList<Result> = mutableListOf(),
                                  @SerialName("asyncResults")
                                  override var asyncActionResults: MutableSet<Result> = mutableSetOf()) : CollectionResult
@Serializable
@SerialName("assessment")
data class AssessmentResultObject(override val identifier: String,
                                  override val versionString: String? = null,
                                  @SerialName("stepHistory")
                                  override var pathHistoryResults: MutableList<Result> = mutableListOf(),
                                  @SerialName("asyncResults")
                                  override var asyncActionResults: MutableSet<Result> = mutableSetOf(),
                                  @SerialName("taskRunUUID")
                                  override var runUUIDString: String = UUIDGenerator.uuidString(),
                                  @SerialName("startDate")
                                  override var startDateString: String = DateGenerator.nowString(),
                                  @SerialName("endDate")
                                  override var endDateString: String = DateGenerator.nowString()) : AssessmentResult

@Serializable
@SerialName("base")
data class ResultObject(override val identifier: String) : Result