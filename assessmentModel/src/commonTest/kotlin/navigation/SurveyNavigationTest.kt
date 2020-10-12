package org.sagebionetworks.assessmentmodel.navigation

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import org.sagebionetworks.assessmentmodel.serialization.*
import org.sagebionetworks.assessmentmodel.survey.SurveyRuleOperator
import org.sagebionetworks.assessmentmodel.survey.compareTo
import kotlin.test.*

class SurveyNavigationTest : NavigationTestHelper() {

    private val jsonCoder = Serialization.JsonCoder.default

    /**
     * JsonElement.compareTo()
     */

    @Test
    fun testJsonElementComparison_Int() {
        val elementA: JsonElement = JsonPrimitive(3)
        val elementB: JsonElement = JsonPrimitive(3)
        val elementLessThan: JsonElement = JsonPrimitive(2)
        val elementGreaterThan: JsonElement = JsonPrimitive(4)

        assertFalse(elementA.compareTo(null, SurveyRuleOperator.Skip), "compareTo(null, SurveyRuleOperator.skip)")
        assertFalse(elementA.compareTo(JsonNull, SurveyRuleOperator.Skip), "compareTo(JsonNull, SurveyRuleOperator.skip)")

        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.Always), "compareTo(elementB, SurveyRuleOperator.always)")
        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.Equal),"compareTo(elementB, SurveyRuleOperator.equal)")
        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.GreaterThanEqual), "compareTo(elementB, SurveyRuleOperator.greaterThanEqual)")
        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.LessThanEqual), "compareTo(elementB, SurveyRuleOperator.lessThanEqual)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.LessThan), "compareTo(elementB, SurveyRuleOperator.lessThan)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.GreaterThan), "compareTo(elementB, SurveyRuleOperator.greaterThan)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.NotEqual), "compareTo(elementB, SurveyRuleOperator.notEqual)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.Skip), "compareTo(elementB, SurveyRuleOperator.skip)")

        assertTrue(elementA.compareTo(elementLessThan, SurveyRuleOperator.Always), "compareTo(elementLessThan, SurveyRuleOperator.always)")
        assertFalse(elementA.compareTo(elementLessThan, SurveyRuleOperator.Equal), "compareTo(elementLessThan, SurveyRuleOperator.equal)")
        assertFalse(elementA.compareTo(elementLessThan, SurveyRuleOperator.LessThanEqual),"compareTo(elementLessThan, SurveyRuleOperator.lessThanEqual)")
        assertFalse(elementA.compareTo(elementLessThan, SurveyRuleOperator.LessThan), "compareTo(elementLessThan, SurveyRuleOperator.lessThan)")
        assertTrue(elementA.compareTo(elementLessThan, SurveyRuleOperator.GreaterThanEqual), "compareTo(elementLessThan, SurveyRuleOperator.greaterThanEqual)")
        assertTrue(elementA.compareTo(elementLessThan, SurveyRuleOperator.GreaterThan), "compareTo(elementLessThan, SurveyRuleOperator.greaterThan)")
        assertTrue(elementA.compareTo(elementLessThan, SurveyRuleOperator.NotEqual), "compareTo(elementLessThan, SurveyRuleOperator.notEqual)")
        assertFalse(elementA.compareTo(elementLessThan, SurveyRuleOperator.Skip), "compareTo(elementLessThan, SurveyRuleOperator.skip)")

        assertTrue(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.Always), "compareTo(elementGreaterThan, SurveyRuleOperator.always)")
        assertFalse(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.Equal), "compareTo(elementGreaterThan, SurveyRuleOperator.equal)")
        assertTrue(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.LessThanEqual), "compareTo(elementGreaterThan, SurveyRuleOperator.lessThanEqual)")
        assertTrue(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.LessThan), "compareTo(elementGreaterThan, SurveyRuleOperator.lessThan)")
        assertFalse(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.GreaterThanEqual), "compareTo(elementGreaterThan, SurveyRuleOperator.greaterThanEqual)")
        assertFalse(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.GreaterThan), "compareTo(elementGreaterThan, SurveyRuleOperator.greaterThan)")
        assertTrue(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.NotEqual), "compareTo(elementGreaterThan, SurveyRuleOperator.notEqual)")
        assertFalse(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.Skip), "compareTo(elementGreaterThan, SurveyRuleOperator.skip)")
    }

    @Test
    fun testJsonElementComparison_Double() {
        val elementA: JsonElement = JsonPrimitive(3.100000000)
        val elementB: JsonElement = JsonPrimitive(3.100000001)
        val elementLessThan: JsonElement = JsonPrimitive(2.100000000)
        val elementGreaterThan: JsonElement = JsonPrimitive(4.100000000)

        assertFalse(elementA.compareTo(null, SurveyRuleOperator.Skip), "compareTo(null, SurveyRuleOperator.skip)")
        assertFalse(elementA.compareTo(JsonNull, SurveyRuleOperator.Skip), "compareTo(JsonNull, SurveyRuleOperator.skip)")

        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.Always), "compareTo(elementB, SurveyRuleOperator.always)")
        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.Equal),"compareTo(elementB, SurveyRuleOperator.equal)")
        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.GreaterThanEqual), "compareTo(elementB, SurveyRuleOperator.greaterThanEqual)")
        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.LessThanEqual), "compareTo(elementB, SurveyRuleOperator.lessThanEqual)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.LessThan), "compareTo(elementB, SurveyRuleOperator.lessThan)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.GreaterThan), "compareTo(elementB, SurveyRuleOperator.greaterThan)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.NotEqual), "compareTo(elementB, SurveyRuleOperator.notEqual)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.Skip), "compareTo(elementB, SurveyRuleOperator.skip)")

        assertTrue(elementA.compareTo(elementLessThan, SurveyRuleOperator.Always), "compareTo(elementLessThan, SurveyRuleOperator.always)")
        assertFalse(elementA.compareTo(elementLessThan, SurveyRuleOperator.Equal), "compareTo(elementLessThan, SurveyRuleOperator.equal)")
        assertFalse(elementA.compareTo(elementLessThan, SurveyRuleOperator.LessThanEqual),"compareTo(elementLessThan, SurveyRuleOperator.lessThanEqual)")
        assertFalse(elementA.compareTo(elementLessThan, SurveyRuleOperator.LessThan), "compareTo(elementLessThan, SurveyRuleOperator.lessThan)")
        assertTrue(elementA.compareTo(elementLessThan, SurveyRuleOperator.GreaterThanEqual), "compareTo(elementLessThan, SurveyRuleOperator.greaterThanEqual)")
        assertTrue(elementA.compareTo(elementLessThan, SurveyRuleOperator.GreaterThan), "compareTo(elementLessThan, SurveyRuleOperator.greaterThan)")
        assertTrue(elementA.compareTo(elementLessThan, SurveyRuleOperator.NotEqual), "compareTo(elementLessThan, SurveyRuleOperator.notEqual)")
        assertFalse(elementA.compareTo(elementLessThan, SurveyRuleOperator.Skip), "compareTo(elementLessThan, SurveyRuleOperator.skip)")

        assertTrue(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.Always), "compareTo(elementGreaterThan, SurveyRuleOperator.always)")
        assertFalse(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.Equal), "compareTo(elementGreaterThan, SurveyRuleOperator.equal)")
        assertTrue(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.LessThanEqual), "compareTo(elementGreaterThan, SurveyRuleOperator.lessThanEqual)")
        assertTrue(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.LessThan), "compareTo(elementGreaterThan, SurveyRuleOperator.lessThan)")
        assertFalse(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.GreaterThanEqual), "compareTo(elementGreaterThan, SurveyRuleOperator.greaterThanEqual)")
        assertFalse(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.GreaterThan), "compareTo(elementGreaterThan, SurveyRuleOperator.greaterThan)")
        assertTrue(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.NotEqual), "compareTo(elementGreaterThan, SurveyRuleOperator.notEqual)")
        assertFalse(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.Skip), "compareTo(elementGreaterThan, SurveyRuleOperator.skip)")
    }

    @Test
    fun testJsonElementComparison_String() {
        val elementA: JsonElement = JsonPrimitive("foo")
        val elementB: JsonElement = JsonPrimitive("foo")
        val elementLessThan: JsonElement = JsonPrimitive("ba")
        val elementGreaterThan: JsonElement = JsonPrimitive("lalala")

        assertFalse(elementA.compareTo(null, SurveyRuleOperator.Skip), "compareTo(null, SurveyRuleOperator.skip)")
        assertFalse(elementA.compareTo(JsonNull, SurveyRuleOperator.Skip), "compareTo(JsonNull, SurveyRuleOperator.skip)")

        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.Always), "compareTo(elementB, SurveyRuleOperator.always)")
        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.Equal),"compareTo(elementB, SurveyRuleOperator.equal)")
        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.GreaterThanEqual), "compareTo(elementB, SurveyRuleOperator.greaterThanEqual)")
        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.LessThanEqual), "compareTo(elementB, SurveyRuleOperator.lessThanEqual)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.LessThan), "compareTo(elementB, SurveyRuleOperator.lessThan)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.GreaterThan), "compareTo(elementB, SurveyRuleOperator.greaterThan)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.NotEqual), "compareTo(elementB, SurveyRuleOperator.notEqual)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.Skip), "compareTo(elementB, SurveyRuleOperator.skip)")

        assertTrue(elementA.compareTo(elementLessThan, SurveyRuleOperator.Always), "compareTo(elementLessThan, SurveyRuleOperator.always)")
        assertFalse(elementA.compareTo(elementLessThan, SurveyRuleOperator.Equal), "compareTo(elementLessThan, SurveyRuleOperator.equal)")
        assertFalse(elementA.compareTo(elementLessThan, SurveyRuleOperator.LessThanEqual),"compareTo(elementLessThan, SurveyRuleOperator.lessThanEqual)")
        assertFalse(elementA.compareTo(elementLessThan, SurveyRuleOperator.LessThan), "compareTo(elementLessThan, SurveyRuleOperator.lessThan)")
        assertTrue(elementA.compareTo(elementLessThan, SurveyRuleOperator.GreaterThanEqual), "compareTo(elementLessThan, SurveyRuleOperator.greaterThanEqual)")
        assertTrue(elementA.compareTo(elementLessThan, SurveyRuleOperator.GreaterThan), "compareTo(elementLessThan, SurveyRuleOperator.greaterThan)")
        assertTrue(elementA.compareTo(elementLessThan, SurveyRuleOperator.NotEqual), "compareTo(elementLessThan, SurveyRuleOperator.notEqual)")
        assertFalse(elementA.compareTo(elementLessThan, SurveyRuleOperator.Skip), "compareTo(elementLessThan, SurveyRuleOperator.skip)")

        assertTrue(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.Always), "compareTo(elementGreaterThan, SurveyRuleOperator.always)")
        assertFalse(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.Equal), "compareTo(elementGreaterThan, SurveyRuleOperator.equal)")
        assertTrue(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.LessThanEqual), "compareTo(elementGreaterThan, SurveyRuleOperator.lessThanEqual)")
        assertTrue(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.LessThan), "compareTo(elementGreaterThan, SurveyRuleOperator.lessThan)")
        assertFalse(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.GreaterThanEqual), "compareTo(elementGreaterThan, SurveyRuleOperator.greaterThanEqual)")
        assertFalse(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.GreaterThan), "compareTo(elementGreaterThan, SurveyRuleOperator.greaterThan)")
        assertTrue(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.NotEqual), "compareTo(elementGreaterThan, SurveyRuleOperator.notEqual)")
        assertFalse(elementA.compareTo(elementGreaterThan, SurveyRuleOperator.Skip), "compareTo(elementGreaterThan, SurveyRuleOperator.skip)")
    }

    @Test
    fun testJsonElementComparison_Boolean() {
        val elementA: JsonElement = JsonPrimitive(true)
        val elementB: JsonElement = JsonPrimitive(true)
        val elementNotEqual: JsonElement = JsonPrimitive(false)

        assertFalse(elementA.compareTo(null, SurveyRuleOperator.Skip), "compareTo(null, SurveyRuleOperator.skip)")
        assertFalse(elementA.compareTo(JsonNull, SurveyRuleOperator.Skip), "compareTo(JsonNull, SurveyRuleOperator.skip)")

        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.Always), "compareTo(elementB, SurveyRuleOperator.always)")
        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.Equal),"compareTo(elementB, SurveyRuleOperator.equal)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.NotEqual), "compareTo(elementB, SurveyRuleOperator.notEqual)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.Skip), "compareTo(elementB, SurveyRuleOperator.skip)")

        assertTrue(elementA.compareTo(elementNotEqual, SurveyRuleOperator.Always), "compareTo(elementB, SurveyRuleOperator.always)")
        assertFalse(elementA.compareTo(elementNotEqual, SurveyRuleOperator.Equal),"compareTo(elementB, SurveyRuleOperator.equal)")
        assertTrue(elementA.compareTo(elementNotEqual, SurveyRuleOperator.NotEqual), "compareTo(elementB, SurveyRuleOperator.notEqual)")
        assertFalse(elementA.compareTo(elementNotEqual, SurveyRuleOperator.Skip), "compareTo(elementB, SurveyRuleOperator.skip)")
    }

    @Test
    fun testJsonElementComparison_Null() {
        val elementA: JsonElement = JsonNull

        assertTrue(elementA.compareTo(JsonNull, SurveyRuleOperator.Always), "compareTo(elementLessThan, SurveyRuleOperator.always)")
        assertTrue(elementA.compareTo(JsonNull, SurveyRuleOperator.Equal), "compareTo(elementLessThan, SurveyRuleOperator.equal)")
        assertTrue(elementA.compareTo(JsonNull, SurveyRuleOperator.LessThanEqual),"compareTo(elementLessThan, SurveyRuleOperator.lessThanEqual)")
        assertFalse(elementA.compareTo(JsonNull, SurveyRuleOperator.LessThan), "compareTo(elementLessThan, SurveyRuleOperator.lessThan)")
        assertTrue(elementA.compareTo(JsonNull, SurveyRuleOperator.GreaterThanEqual), "compareTo(elementLessThan, SurveyRuleOperator.greaterThanEqual)")
        assertFalse(elementA.compareTo(JsonNull, SurveyRuleOperator.GreaterThan), "compareTo(elementLessThan, SurveyRuleOperator.greaterThan)")
        assertFalse(elementA.compareTo(JsonNull, SurveyRuleOperator.NotEqual), "compareTo(elementLessThan, SurveyRuleOperator.notEqual)")
        assertTrue(elementA.compareTo(JsonNull, SurveyRuleOperator.Skip), "compareTo(elementLessThan, SurveyRuleOperator.skip)")

        assertTrue(elementA.compareTo(null, SurveyRuleOperator.Always), "compareTo(elementLessThan, SurveyRuleOperator.always)")
        assertTrue(elementA.compareTo(null, SurveyRuleOperator.Equal), "compareTo(elementLessThan, SurveyRuleOperator.equal)")
        assertTrue(elementA.compareTo(null, SurveyRuleOperator.LessThanEqual),"compareTo(elementLessThan, SurveyRuleOperator.lessThanEqual)")
        assertFalse(elementA.compareTo(null, SurveyRuleOperator.LessThan), "compareTo(elementLessThan, SurveyRuleOperator.lessThan)")
        assertTrue(elementA.compareTo(null, SurveyRuleOperator.GreaterThanEqual), "compareTo(elementLessThan, SurveyRuleOperator.greaterThanEqual)")
        assertFalse(elementA.compareTo(null, SurveyRuleOperator.GreaterThan), "compareTo(elementLessThan, SurveyRuleOperator.greaterThan)")
        assertFalse(elementA.compareTo(null, SurveyRuleOperator.NotEqual), "compareTo(elementLessThan, SurveyRuleOperator.notEqual)")
        assertTrue(elementA.compareTo(null, SurveyRuleOperator.Skip), "compareTo(elementLessThan, SurveyRuleOperator.skip)")
    }

    @Test
    fun testJsonElementComparison_Array() {
        val elementA: JsonElement = JsonArray(listOf(JsonPrimitive(2), JsonPrimitive(4)))
        val elementB: JsonElement = JsonArray(listOf(JsonPrimitive(2), JsonPrimitive(4)))
        val elementOtherThan: JsonElement = JsonArray(listOf(JsonPrimitive(0), JsonPrimitive(3)))

        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.Always), "compareTo(elementLessThan, SurveyRuleOperator.always)")
        assertTrue(elementA.compareTo(elementB, SurveyRuleOperator.Equal), "compareTo(elementLessThan, SurveyRuleOperator.equal)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.NotEqual), "compareTo(elementLessThan, SurveyRuleOperator.notEqual)")
        assertFalse(elementA.compareTo(elementB, SurveyRuleOperator.Skip), "compareTo(elementLessThan, SurveyRuleOperator.skip)")

        assertTrue(elementA.compareTo(elementOtherThan, SurveyRuleOperator.Always), "compareTo(elementLessThan, SurveyRuleOperator.always)")
        assertFalse(elementA.compareTo(elementOtherThan, SurveyRuleOperator.Equal), "compareTo(elementLessThan, SurveyRuleOperator.equal)")
        assertTrue(elementA.compareTo(elementOtherThan, SurveyRuleOperator.NotEqual), "compareTo(elementLessThan, SurveyRuleOperator.notEqual)")
        assertFalse(elementA.compareTo(elementOtherThan, SurveyRuleOperator.Skip), "compareTo(elementLessThan, SurveyRuleOperator.skip)")
    }

    /**
     * SurveyRuleOperator - serialization
     */

    @Serializable
    data class TestSurveyRuleOperatorWrapper(val operators: List<SurveyRuleOperator>)

    @Test
    fun testSurveyRuleOperator_serialization() {
        val operators = listOf(
            SurveyRuleOperator.Always,
            SurveyRuleOperator.Equal,
            SurveyRuleOperator.GreaterThan,
            SurveyRuleOperator.GreaterThanEqual,
            SurveyRuleOperator.LessThan,
            SurveyRuleOperator.LessThanEqual,
            SurveyRuleOperator.NotEqual,
            SurveyRuleOperator.Skip
        )
        val original = TestSurveyRuleOperatorWrapper(operators)
        val inputString = """{"operators":["always","eq","gt","ge","lt","le","ne","de"]}"""

        val serializer = TestSurveyRuleOperatorWrapper.serializer()
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
        assertEquals(inputString, jsonString)
    }

    /**
     * Question navigation
     */

    @Test
    fun testSimpleQuestion_Navigation() {
        val step = SimpleQuestionObject(
            identifier = "foo",
            inputItem = YearTextInputItemObject())
        step.surveyRules = listOf(
            ComparableSurveyRuleObject(JsonPrimitive(2525),"equal"),
            ComparableSurveyRuleObject(JsonPrimitive(2525),"greaterThan", SurveyRuleOperator.GreaterThan),
            ComparableSurveyRuleObject(JsonPrimitive(2525),"lessThan", SurveyRuleOperator.LessThan),
            ComparableSurveyRuleObject(JsonNull,"skip")
        )

        val answerResult = step.createResult()

        val branchResult = BranchNodeResultObject("baloo", mutableListOf(answerResult))

        answerResult.jsonValue = JsonPrimitive(2525)
        assertNull(step.nextNodeIdentifier(branchResult, true))
        assertEquals("equal", step.nextNodeIdentifier(branchResult, false))

        answerResult.jsonValue = JsonPrimitive(2526)
        assertEquals("greaterThan", step.nextNodeIdentifier(branchResult, false))

        answerResult.jsonValue = JsonPrimitive(2524)
        assertEquals("lessThan", step.nextNodeIdentifier(branchResult, false))

        answerResult.jsonValue = JsonNull
        assertEquals("skip", step.nextNodeIdentifier(branchResult, false))
    }
}

