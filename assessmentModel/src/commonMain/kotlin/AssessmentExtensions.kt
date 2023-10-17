package org.sagebionetworks.assessmentmodel

import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import org.sagebionetworks.assessmentmodel.survey.AnswerType
import org.sagebionetworks.assessmentmodel.survey.BaseType
import org.sagebionetworks.assessmentmodel.survey.ChoiceQuestion


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
    val type: AnswerType,
    val questionTitle: String?,
    val choices: Map<JsonPrimitive, String>? = null,
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
        val path : String? = if (node is Assessment && stepPath == null) null else "$pathSuffix$identifier"
        if (node is NodeContainer) {
            for(child in node.children) {
                recursiveAdd(child, path)
            }
        }

        if (result is AnswerResult && result.answerType != null) {
            var choices: Map<JsonPrimitive, String>? = null
            if (node is ChoiceQuestion) {
                choices = node.choices.associate { Pair(it.jsonValue(true)!!, it.label) }
            }
            val colName = path?: result.identifier
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

private class FlatAnswersGenerator(private val result: Result) {

    private val answers: MutableMap<String, String> = mutableMapOf()

    fun build(): Map<String, String> {
        if (answers.isEmpty()) {
            recursiveAdd(result)
        }
        return answers.toMap()
    }

    private fun recursiveAdd(result: Result, stepPath: String? = null) {
        val pathSuffix = stepPath?.let { "${it}_" } ?: ""
        val identifier = result.identifier
        val path : String? = if (result is AssessmentResult && stepPath == null) null else "$pathSuffix$identifier"

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
        val key = path ?: result.identifier
        val jsonValue = result.jsonValue
        var stringValue: String? = null
        if (jsonValue != null) {
            stringValue = when (jsonType) {
                BaseType.ARRAY -> {
                    jsonValue.jsonArray.let { array ->
                        val answers = array.map { (it as? JsonPrimitive)?.content }
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