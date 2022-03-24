package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import org.sagebionetworks.assessmentmodel.*
import kotlin.math.abs

/**
 * [SurveyRule] defines an evaluation rule and returns a step identifier if appropriate.
 */
interface SurveyRule {

    /**
     * For a given [result] (if any), what is the identifier for the step that the survey should go to next?
     */
    fun evaluateRuleWith(result: Result?) : String?
}

enum class ReservedNavigationIdentifier {
    /**
     * Exit the activity.
     */
    Exit,

    /**
     * Continue to the next section.
     */
    NextSection,
    ;

    fun matching(identifier: String?) = (name.compareTo(identifier ?: "", true) == 0)
}

interface ComparableSurveyRule : SurveyRule {

    /**
     * Expected answer for the rule.
     */
    val matchingAnswer: JsonElement

    /**
     * Skip identifier for this rule.
     */
    val skipToIdentifier: String

    /**
     * The rule operator to apply. If `null`, `.equal` will be assumed unless the [matchingAnswer] is [JsonNull], in
     * which case `.skip` will be assumed.
     */
    val ruleOperator: SurveyRuleOperator?

    /**
     * The accuracy to use if this is a double.
     */
    val accuracy: Double
        get() = 0.00001

    override fun evaluateRuleWith(result: Result?) : String? = (result as? AnswerResult)?.let {  answerResult ->
        val operator = ruleOperator ?: SurveyRuleOperator.Equal
        val skipTo = skipToIdentifier
        val jsonValue = answerResult.jsonValue ?: JsonNull
        if (jsonValue.compareTo(matchingAnswer, operator, accuracy)) {
            skipTo
        } else {
            null
        }
    }
}

/**
 * List of operators to use in comparing [AnswerResult] values to a matching rule.
 */
@Serializable
enum class SurveyRuleOperator : StringEnum {

    /**
     * The answer value is equal to the matching answer.
     */
    @SerialName("eq")
    Equal,

    /**
     * The answer value is *not* equal to the matching answer.
     */
    @SerialName("ne")
    NotEqual,

    /// The answer value is less than the matching answer.
    @SerialName("lt")
    LessThan,

    /// The answer value is greater than the matching answer.
    @SerialName("gt")
    GreaterThan,

    /// The answer value is less than or equal to the matching answer.
    @SerialName("le")
    LessThanEqual,

    /// The answer value is greater than or equal to the matching answer.
    @SerialName("ge")
    GreaterThanEqual,
    ;
}

fun JsonElement.compareTo(value: JsonElement?, operator: SurveyRuleOperator, accuracy: Double = 0.00001) : Boolean {
    val jsonValue = value ?: JsonNull
    return (jsonValue as? JsonPrimitive)?.let { jsonLiteral ->
        (this as? JsonPrimitive)?.compareTo(jsonLiteral, operator, accuracy)
    } ?: when (operator) {
        SurveyRuleOperator.Equal -> this == jsonValue
        SurveyRuleOperator.NotEqual -> this != jsonValue
        else -> false
    }
}

internal fun JsonPrimitive.compareTo(value: JsonPrimitive, operator: SurveyRuleOperator, accuracy: Double) : Boolean {
    var isEqual = this.content == value.content
    this.doubleOrNull?.let {
        value.doubleOrNull?.let { v ->
            isEqual = abs(it - v) <= accuracy
        }
    }
    return when (operator) {
        SurveyRuleOperator.NotEqual -> !isEqual
        SurveyRuleOperator.Equal -> isEqual
        SurveyRuleOperator.GreaterThan -> !isEqual && this.content > value.content
        SurveyRuleOperator.GreaterThanEqual -> isEqual || this.content >= value.content
        SurveyRuleOperator.LessThan -> !isEqual && this.content < value.content
        SurveyRuleOperator.LessThanEqual -> isEqual || this.content <= value.content
    }
}