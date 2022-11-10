package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.modules.*
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.survey.AnswerType

val resultSerializersModule = SerializersModule {
    polymorphic(Result::class) {
        subclass(AnswerResultObject::class)
        subclass(AssessmentResultObject::class)
        subclass(BranchNodeResultObject::class)
        subclass(CollectionResultObject::class)
        subclass(ResultObject::class)
    }
    polymorphic(AssessmentResult::class) {
        subclass(AssessmentResultObject::class)
    }
}

@Serializable
@SerialName("answer")
data class AnswerResultObject(override val identifier: String,
                              override var answerType: AnswerType? = null,
                              @SerialName("value")
                              override var jsonValue: JsonElement? = null,
                              @SerialName("startDate")
                              @Serializable(with = InstantSerializer::class)
                              override var startDateTime: Instant = Clock.System.now(),
                              @SerialName("endDate")
                              @Serializable(with = InstantSerializer::class)
                              override var endDateTime: Instant? = null,
                              override val questionText: String? = null,
                              val questionData: JsonElement? = null)
    : AnswerResult {
    override fun copyResult(identifier: String): AnswerResult = this.copy(identifier = identifier)
}

@Serializable
@SerialName("assessment")
data class AssessmentResultObject(override val identifier: String,
                                  override val assessmentIdentifier: String? = null,
                                  override val schemaIdentifier: String? = null,
                                  override val versionString: String? = null,
                                  @SerialName("stepHistory")
                                  override var pathHistoryResults: MutableList<Result> = mutableListOf(),
                                  @SerialName("asyncResults")
                                  override var inputResults: MutableSet<Result> = mutableSetOf(),
                                  @SerialName("taskRunUUID")
                                  override var runUUIDString: String = UUIDGenerator.uuidString(),
                                  @SerialName("startDate")
                                  @Serializable(with = InstantSerializer::class)
                                  override var startDateTime: Instant = Clock.System.now(),
                                  @SerialName("endDate")
                                  @Serializable(with = InstantSerializer::class)
                                  override var endDateTime: Instant? = null,
                                  override val path: MutableList<PathMarker> = mutableListOf())
    : AssessmentResult {
    override fun copyResult(identifier: String): AssessmentResult = this.copy(
            identifier = identifier,
            pathHistoryResults = pathHistoryResults.copyResults(),
            inputResults = inputResults.copyResults())
}

@Serializable
@SerialName("base")
data class ResultObject(override val identifier: String,
                        @SerialName("startDate")
                        @Serializable(with = InstantSerializer::class)
                        override var startDateTime: Instant = Clock.System.now(),
                        @SerialName("endDate")
                        @Serializable(with = InstantSerializer::class)
                        override var endDateTime: Instant? = null)
    : Result {
    override fun copyResult(identifier: String): Result = this.copy(identifier = identifier)
}

@Serializable
@SerialName("collection")
data class CollectionResultObject(override val identifier: String,
                                  @SerialName("children")
                                  override var inputResults: MutableSet<Result> = mutableSetOf(),
                                  @SerialName("startDate")
                                  @Serializable(with = InstantSerializer::class)
                                  override var startDateTime: Instant = Clock.System.now(),
                                  @SerialName("endDate")
                                  @Serializable(with = InstantSerializer::class)
                                  override var endDateTime: Instant? = null)
    : CollectionResult {
    override fun copyResult(identifier: String): CollectionResult = this.copy(
            identifier = identifier,
            inputResults = inputResults.copyResults())
}

@Serializable
@SerialName("section")
data class BranchNodeResultObject(override val identifier: String,
                                  @SerialName("stepHistory")
                                  override var pathHistoryResults: MutableList<Result> = mutableListOf(),
                                  @SerialName("asyncResults")
                                  override var inputResults: MutableSet<Result> = mutableSetOf(),
                                  @SerialName("startDate")
                                  @Serializable(with = InstantSerializer::class)
                                  override var startDateTime: Instant = Clock.System.now(),
                                  @SerialName("endDate")
                                  @Serializable(with = InstantSerializer::class)
                                  override var endDateTime: Instant? = null,
                                  override val path: MutableList<PathMarker> = mutableListOf())
    : BranchNodeResult {
    override fun copyResult(identifier: String): BranchNodeResult = this.copy(
            identifier = identifier,
            pathHistoryResults = pathHistoryResults.copyResults(),
            inputResults = inputResults.copyResults())
}

//Could use this if we want to switch "startDate" and "endDate" to being an Instant
object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(output: Encoder, obj: Instant) {
        output.encodeString(DateUtils.bridgeIsoDateTimeString(obj))
    }

    override fun deserialize(input: Decoder): Instant {
        return DateUtils.instantFromBridgeIsoDateTimeString(input.decodeString())
    }
}


