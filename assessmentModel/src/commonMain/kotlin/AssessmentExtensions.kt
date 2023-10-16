package org.sagebionetworks.assessmentmodel

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import org.sagebionetworks.assessmentmodel.serialization.Serialization
import org.sagebionetworks.assessmentmodel.survey.BaseType


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
    val type: BaseType,
    val questionTitle: String?
)

private class FlatAnswersDefinitionGenerator(private val assessment: Assessment) {

    private val columns: MutableList<AnswerColumn> = mutableListOf()

    fun build(): List<AnswerColumn> {
        if (columns.isEmpty()) {
            recursiveAdd(assessment)
        }
        return columns.toList()
    }

    private fun recursiveAdd(node: ResultMapElement, stepPath: String? = null) {
        val result = node.createResult()
        val pathSuffix = stepPath?.let { "${it}_" } ?: ""
        val identifier = result.identifier
        var path : String? = "$pathSuffix$identifier"
        if (node is Assessment && stepPath == null) {
            // This is to prevent the assessment identifier from being included
            path = null
        }
        if (node is NodeContainer) {
            for(child in node.children) {
                recursiveAdd(child, path)
            }
        }

        if (result is AnswerResult && result.answerType != null) {
            val colName = path?: result.identifier
            val type = result.answerType!!.baseType
            columns.add(AnswerColumn(colName, type, result.questionText))
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
        Serialization.JsonCoder.default.encodeToString(it.value)
    }
}

/**
 * Get a flattened map of result identifier to result value as a [JsonElement]. The key is defined
 * as <SectionID>_<NodeID>, for most surveys it will just be the identifier of the question.
 * The keys match the column names returned by [Assessment.toFlatAnswersDefinition]
 */
fun AssessmentResult.toFlatAnswersJson(): Map<String, JsonElement> {
    return FlatAnswersGenerator(this).build()
}

private class FlatAnswersGenerator(private val result: Result) {

    private val answers: MutableMap<String, JsonElement> = mutableMapOf()

    fun build(): Map<String, JsonElement> {
        if (answers.isEmpty()) {
            recursiveAdd(result)
        }
        return answers.toMap()
    }

    private fun recursiveAdd(result: Result, stepPath: String? = null) {
        val pathSuffix = stepPath?.let { "${it}_" } ?: ""
        val identifier = result.identifier
        var path : String? = "$pathSuffix$identifier"
        if (result is AssessmentResult && stepPath == null) {
            // This is to prevent the assessment identifier from being included
            path = null
        }

        if (result is BranchNodeResult) {
            recursiveAddResults(result.pathHistoryResults, path)
            recursiveAddResults(result.inputResults, path)
        } else if (result is CollectionResult) {
            recursiveAddResults(result.inputResults, path)
        }
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
        var baseType = result.answerType?.baseType ?: return
        val key = path ?: result.identifier
        var jsonValue = result.jsonValue
        if (baseType == BaseType.ARRAY) {
            baseType = BaseType.STRING
            jsonValue = jsonValue?.jsonArray?.let { array ->
                val answers = array.map { it.toString() }
                JsonPrimitive(answers.joinToString(","))
            }
        }
        if (jsonValue != null) {
            answers[key] = jsonValue
        }
    }

}