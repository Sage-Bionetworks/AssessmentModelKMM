package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonLiteral
import kotlinx.serialization.json.JsonNull
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
    exit,

    /**
     * Continue to the next section.
     */
    nextSection,
    ;
}

interface ComparableSurveyRule : SurveyRule {

    /**
     * Expected answer for the rule.
     */
    val matchingAnswer: JsonElement

    /**
     * Optional skip identifier for this rule. If available, this will be used as the skip identifier, otherwise
     * the [skipToIdentifier] will be assumed to be an "exit" identifier.
     */
    val skipToIdentifier: String?

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
        val operator = ruleOperator ?: if (matchingAnswer == JsonNull) SurveyRuleOperator.Skip else SurveyRuleOperator.Equal
        val skipTo = skipToIdentifier ?: ReservedNavigationIdentifier.exit.name
        val jsonValue = answerResult.jsonValue
        if (jsonValue == null) {
            if (operator == SurveyRuleOperator.Skip) skipTo else null
        } else if (jsonValue.compareTo(matchingAnswer, operator, accuracy)) {
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

    /**
     * The rule should always evaluate to true.
     */
    @SerialName("always")
    Always,

    /**
     * Survey rule for checking if the answer was skipped.
     */
    @SerialName("de")
    Skip,
    ;
}

fun JsonElement.compareTo(value: JsonElement?, operator: SurveyRuleOperator, accuracy: Double = 0.00001) : Boolean {
    val value = value ?: JsonNull
    return (value as? JsonLiteral)?.let { value ->
        (this as? JsonLiteral)?.let { it.compareTo(value, operator, accuracy) }
    } ?: when (operator) {
        SurveyRuleOperator.Always -> true
        SurveyRuleOperator.Skip -> this == JsonNull && value == JsonNull
        SurveyRuleOperator.Equal -> this == value
        SurveyRuleOperator.NotEqual -> this != value
        else -> false
    }
}

internal fun JsonLiteral.compareTo(value: JsonLiteral, operator: SurveyRuleOperator, accuracy: Double) : Boolean
        = (this.body as? Comparable<*>)?.let { it.compareTo(value, operator, accuracy) } ?: false

internal fun <T> Comparable<T>.compareTo(value: JsonLiteral, operator: SurveyRuleOperator, accuracy: Double) : Boolean
        = (value.body as? T)?.let { value ->
    val isEqual = this.equalsWithAccuracy(value, accuracy)
    when (operator) {
        SurveyRuleOperator.Always -> true
        SurveyRuleOperator.Skip -> false
        SurveyRuleOperator.NotEqual -> !isEqual
        SurveyRuleOperator.Equal -> isEqual
        SurveyRuleOperator.GreaterThan -> !isEqual && this > value
        SurveyRuleOperator.GreaterThanEqual -> isEqual || this >= value
        SurveyRuleOperator.LessThan -> !isEqual && this < value
        SurveyRuleOperator.LessThanEqual -> isEqual || this <= value
    }
} ?: false

internal fun <T> Comparable<T>.equalsWithAccuracy(value: T, accuracy: Double) : Boolean {
    return (this as? Double)?.let {
        abs(this - (value as Number).toDouble()) <= accuracy
    } ?: run { this == value }
}