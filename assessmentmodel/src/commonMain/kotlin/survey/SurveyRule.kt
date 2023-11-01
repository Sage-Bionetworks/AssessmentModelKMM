package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import org.sagebionetworks.assessmentmodel.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt

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

    /**
     * Go to the beginning of the assessment.
     */
    Beginning,
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

    override fun evaluateRuleWith(result: Result?) : String? = (result as? AnswerResult)?.let {  answerResult ->
        val operator = ruleOperator ?: SurveyRuleOperator.Equal
        val skipTo = skipToIdentifier
        val digits: Int? = (answerResult.answerType as? AnswerType.Decimal)?.significantDigits
        val significantDigits = digits ?: if (answerResult.answerType == AnswerType.INTEGER) 0 else 5
        val jsonValue = answerResult.jsonValue ?: JsonNull
        if (jsonValue.compareTo(matchingAnswer, operator, significantDigits)) {
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

fun JsonElement.compareTo(value: JsonElement?, operator: SurveyRuleOperator, significantDigits: Int = 5) : Boolean {
    val jsonValue = value ?: JsonNull
    return (jsonValue as? JsonPrimitive)?.let { jsonLiteral ->
        (this as? JsonPrimitive)?.compareTo(jsonLiteral, operator, significantDigits)
    } ?: when (operator) {
        SurveyRuleOperator.Equal -> this == jsonValue
        SurveyRuleOperator.NotEqual -> this != jsonValue
        else -> false
    }
}

internal fun JsonPrimitive.compareTo(value: JsonPrimitive, operator: SurveyRuleOperator, significantDigits: Int) : Boolean {
    var isEqual = this.content == value.content
    if ((this is JsonNull) || (value is JsonNull)) {
        return when (operator) {
            SurveyRuleOperator.NotEqual -> !isEqual
            SurveyRuleOperator.Equal -> isEqual
            else -> false
        }
    }
    this.doubleOrNull?.let {
        value.doubleOrNull?.let { v ->
            val roundToValue = 10.0.pow(significantDigits)
            isEqual = round(it * roundToValue) == round(v * roundToValue)
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