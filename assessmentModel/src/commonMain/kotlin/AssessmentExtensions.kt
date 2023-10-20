package org.sagebionetworks.assessmentmodel

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import org.sagebionetworks.assessmentmodel.survey.AnswerType
import org.sagebionetworks.assessmentmodel.survey.BaseType
import org.sagebionetworks.assessmentmodel.survey.ChoiceQuestion
import org.sagebionetworks.assessmentmodel.survey.ChoiceSelectorType


/**
 * Get a flattened list of answers definitions. This provides a list of column names and types
 * for the results returned by [AssessmentResult.toFlatAnswers]. Column name is defined
 * as <SectionID>_<NodeID>, for most surveys it will just be the identifier of the question.
 * The column names match the keys returned by [AssessmentResult.toFlatAnswers]
 */
fun Assessment.toFlatAnswersDefinition(): List<AnswerColumn> {
    return FlatAnswersDefinitionGenerator(this).build()
}

data class AnswerColumn(
    val columnName: String,
    val answerType: AnswerType,
    val questionTitle: String?,
    val choices: Map<String, String>? = null,
)

/**
 * Use a shared base class so that the column name and path are built using the same rules for
 * both the answers and the column headers.
 */
internal abstract class BaseFlatAnswersGenerator {
    protected fun appendedPath(isAssessment: Boolean, result: Result, stepPath: String? = null): String? {
        val pathSuffix = stepPath?.let { "${it}_" } ?: ""
        val identifier = result.identifier
        return if (isAssessment && stepPath == null) null else "$pathSuffix$identifier"
    }

    protected fun columnName(result: Result, path: String? = null): String {
        return path ?: result.identifier
    }
}

private class FlatAnswersDefinitionGenerator(private val assessment: Assessment) : BaseFlatAnswersGenerator() {

    private val columns: MutableList<AnswerColumn> = mutableListOf()

    fun build(): List<AnswerColumn> {
        if (columns.isEmpty()) {
            recursiveAdd(assessment)
        }
        return columns.toList()
    }

    private fun recursiveAdd(node: ResultMapElement, stepPath: String? = null) {
        val result = node.createResult()
        val path : String? = appendedPath(node is Assessment, result, stepPath)
        if (node is NodeContainer) {
            for(child in node.children) {
                recursiveAdd(child, path)
            }
        }

        if (result is AnswerResult && result.answerType != null) {
            var choices: Map<String, String>? = null
            if (node is ChoiceQuestion) {
                choices = node.choices.mapNotNull {
                    val value = it.jsonValue(true)
                    if (it.selectorType == ChoiceSelectorType.Default && value != null && value !is JsonNull) {
                        Pair(value.content, it.label)
                    } else null
                }.toMap()
            }
            val colName = columnName(result, path)
            columns.add(AnswerColumn(colName, result.answerType!!, result.questionText, choices))
        }

    }
}

/**
 * Get a flattened map of result identifier to result value as a [String]. The key is defined
 * as <SectionID>_<NodeID>, for most surveys it will just be the identifier of the question.
 * The keys match the column names returned by [Assessment.toFlatAnswersDefinition]
 */
fun AssessmentResult.toFlatAnswers(): Map<String, String> {
    return FlatAnswersGenerator(this).build().mapValues{
        return FlatAnswersGenerator(this).build()
    }
}

private class FlatAnswersGenerator(private val result: Result)  : BaseFlatAnswersGenerator() {

    private val answers: MutableMap<String, String> = mutableMapOf()

    fun build(): Map<String, String> {
        if (answers.isEmpty()) {
            recursiveAdd(result)
        }
        return answers.toMap()
    }

    private fun recursiveAdd(result: Result, stepPath: String? = null) {
        val path = appendedPath(result is AssessmentResult, result, stepPath)

        if (result is BranchNodeResult) {
            recursiveAddResults(result.pathHistoryResults, path)
            recursiveAddResults(result.inputResults, path)
        } else if (result is CollectionResult) {
            recursiveAddResults(result.inputResults, path)
        }
        // In theory a BranchNodeResult or CollectionResult could also implement AnswerResult
        if (result is AnswerResult) {
            addAnswerResult(result, path)
        }
    }

    private fun recursiveAddResults(results: Collection<Result>, stepPath: String? = null)  {
        results.forEach {
            recursiveAdd(it, stepPath)
        }
    }

    private fun addAnswerResult(result: AnswerResult, path: String?) {
        val jsonType = result.answerType?.jsonType ?: return
        val key = columnName(result, path)
        val jsonValue = result.jsonValue
        var stringValue: String? = null
        if (jsonValue != null) {
            stringValue = when (jsonType) {
                BaseType.ARRAY -> {
                    jsonValue.jsonArray.let { array ->
                        val answers = array.map { (it as? JsonPrimitive)?.content?.replace(",", "_") }
                        answers.joinToString(",")
                    }
                }
                BaseType.OBJECT -> {
                    jsonValue.toString()
                }
                else -> {
                    // Using content field so as to get string without quotes
                    (jsonValue as? JsonPrimitive)?.content
                }
            }
        }
        if (stringValue != null) {
            answers[key] = stringValue
        }
    }

}