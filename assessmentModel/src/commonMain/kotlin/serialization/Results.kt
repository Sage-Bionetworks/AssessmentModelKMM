package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.ResultNavigationRule
import org.sagebionetworks.assessmentmodel.survey.AnswerType

val resultSerializersModule = SerializersModule {
    polymorphic(Result::class) {
        AnswerResultObject::class with AnswerResultObject.serializer()
        AssessmentResultObject::class with AssessmentResultObject.serializer()
        BranchNodeResultObject::class with BranchNodeResultObject.serializer()
        CollectionResultObject::class with CollectionResultObject.serializer()
        ResultObject::class with ResultObject.serializer()
    }
}

@Serializable
@SerialName("answer")
data class AnswerResultObject(override val identifier: String,
                              override var answerType: AnswerType? = null,
                              @SerialName("value")
                              override var jsonValue: JsonElement? = null) : AnswerResult {
    override fun copyResult(identifier: String): AnswerResult = this.copy(identifier = identifier)
}

@Serializable
@SerialName("assessment")
data class AssessmentResultObject(override val identifier: String,
                                  override val versionString: String? = null,
                                  @SerialName("stepHistory")
                                  override var pathHistoryResults: MutableList<Result> = mutableListOf(),
                                  @SerialName("asyncResults")
                                  override var inputResults: MutableSet<Result> = mutableSetOf(),
                                  @SerialName("taskRunUUID")
                                  override var runUUIDString: String = UUIDGenerator.uuidString(),
                                  @SerialName("startDate")
                                  override var startDateString: String = DateGenerator.nowString(),
                                  @SerialName("endDate")
                                  override var endDateString: String = DateGenerator.nowString(),
                                  override val path: MutableList<PathMarker> = mutableListOf(),
                                  @SerialName("skipToIdentifier")
                                  override var nextNodeIdentifier: String? = null)
    : AssessmentResult, ResultNavigationRule {
    override fun copyResult(identifier: String): AssessmentResult = this.copy(
            identifier = identifier,
            pathHistoryResults = pathHistoryResults.copyResults(),
            inputResults = inputResults.copyResults())
}

@Serializable
@SerialName("base")
data class ResultObject(override val identifier: String,
                        @SerialName("skipToIdentifier")
                        override var nextNodeIdentifier: String? = null) : Result, ResultNavigationRule {
    override fun copyResult(identifier: String): Result = this.copy(identifier = identifier)
}

@Serializable
@SerialName("collection")
data class CollectionResultObject(override val identifier: String,
                                  override var inputResults: MutableSet<Result> = mutableSetOf(),
                                  @SerialName("skipToIdentifier")
                                  override var nextNodeIdentifier: String? = null) : CollectionResult, ResultNavigationRule {
    override fun copyResult(identifier: String): CollectionResult = this.copy(
            identifier = identifier,
            inputResults = inputResults.copyResults())
}

@Serializable
@SerialName("task")
data class BranchNodeResultObject(override val identifier: String,
                                  @SerialName("stepHistory")
                                  override var pathHistoryResults: MutableList<Result> = mutableListOf(),
                                  @SerialName("asyncResults")
                                  override var inputResults: MutableSet<Result> = mutableSetOf(),
                                  override val path: MutableList<PathMarker> = mutableListOf(),
                                  @SerialName("skipToIdentifier")
                                  override var nextNodeIdentifier: String? = null) : BranchNodeResult, ResultNavigationRule {
    override fun copyResult(identifier: String): BranchNodeResult = this.copy(
            identifier = identifier,
            pathHistoryResults = pathHistoryResults.copyResults(),
            inputResults = inputResults.copyResults())
}


