package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.*
import org.sagebionetworks.assessmentmodel.Result
import org.sagebionetworks.assessmentmodel.survey.BaseType
import org.sagebionetworks.assessmentmodel.survey.AnswerType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

open class ResultTest {

    @Serializable
    data class TestResultWrapper(val result: Result)

    private val jsonCoder = Serialization.JsonCoder.default

    /**
     * Collection-type results
     */

    @UnstableDefault
    @Test
    fun testParentNodeResult() {
        val result1 = ResultObject("result1")
        val result2 = ResultObject("result2")
        val result3 = BranchNodeResultObject(identifier = "result3",
                pathHistoryResults = mutableListOf(ResultObject("resultA"), ResultObject("resultB")),
                inputResults = mutableSetOf(ResultObject("asyncResultA"), ResultObject("asyncResultB")))
        val result4 = CollectionResultObject(identifier = "result4",
                inputResults = mutableSetOf(ResultObject("asyncResultA"), ResultObject("asyncResultB")))

        val original = BranchNodeResultObject("testResult", pathHistoryResults = mutableListOf(result1, result2, result3, result4))
        val inputString = """
            {
                "identifier": "testResult",
                "stepHistory": [
                    {"identifier": "result1","type": "base"},
                    {"identifier": "result2","type": "base"},
                    {
                        "identifier": "result3",
                        "type": "task",
                        "stepHistory": [{"identifier": "resultA","type": "base"},{"identifier": "resultB","type": "base"}],
                        "asyncResults": [{"identifier": "asyncResultA","type": "base"},{"identifier": "asyncResultB","type": "base"}]
                    },
                    {
                        "identifier": "result4",
                        "type": "collection",
                        "inputResults": [{"identifier": "asyncResultA","type": "base"},{"identifier": "asyncResultB","type": "base"}]
                    }
                ]
            }   
            """.trimIndent()

        val jsonString = jsonCoder.stringify(BranchNodeResultObject.serializer(), original)
        val restored = jsonCoder.parse(BranchNodeResultObject.serializer(), jsonString)
        val decoded = jsonCoder.parse(BranchNodeResultObject.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        val jsonWrapper = Json.nonstrict.parseJson(jsonString).jsonObject
        assertEquals("testResult", jsonWrapper.getPrimitiveOrNull("identifier")?.content)
        val pathHistory = jsonWrapper.getArrayOrNull("stepHistory")
        assertNotNull(pathHistory)
        assertEquals(4, pathHistory.count())
        val r1 = pathHistory.firstOrNull()?.jsonObject
        assertNotNull(r1)
        assertEquals("result1", r1.getPrimitiveOrNull("identifier")?.content)
        assertEquals("base", r1.getPrimitiveOrNull("type")?.content)
        val r4 = pathHistory.lastOrNull()?.jsonObject
        assertNotNull(r4)
        assertEquals("result4", r4.getPrimitiveOrNull("identifier")?.content)
        assertEquals("collection", r4.getPrimitiveOrNull("type")?.content)
    }

    @UnstableDefault
    @Test
    fun testAssessmentResult() {
        val original = AssessmentResultObject(identifier = "testResult",
                pathHistoryResults = mutableListOf(ResultObject("resultA"), ResultObject("resultB")),
                inputResults = mutableSetOf(ResultObject("asyncResultA"), ResultObject("asyncResultB")),
                runUUIDString = "4cb0580-3cdb-11ea-b77f-2e728ce88125",
                startDateString = "2020-01-21T12:00:00.000+7000",
                endDateString = "2020-01-21T12:05:00.000+7000"
            )
         val inputString = """
            {
                "identifier": "testResult",
                "taskRunUUID": "4cb0580-3cdb-11ea-b77f-2e728ce88125",
                "startDate": "2020-01-21T12:00:00.000+7000",
                "endDate": "2020-01-21T12:05:00.000+7000",
                "stepHistory": [{"identifier": "resultA","type": "base"},{"identifier": "resultB","type": "base"}],
                "asyncResults": [{"identifier": "asyncResultA","type": "base"},{"identifier": "asyncResultB","type": "base"}]
            }
            """.trimIndent()

        val jsonString = jsonCoder.stringify(AssessmentResultObject.serializer(), original)
        val restored = jsonCoder.parse(AssessmentResultObject.serializer(), jsonString)
        val decoded = jsonCoder.parse(AssessmentResultObject.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        val jsonWrapper = Json.nonstrict.parseJson(jsonString).jsonObject
        assertEquals("testResult", jsonWrapper.getPrimitiveOrNull("identifier")?.content)
        assertEquals("4cb0580-3cdb-11ea-b77f-2e728ce88125", jsonWrapper.getPrimitiveOrNull("taskRunUUID")?.content)
        assertEquals("2020-01-21T12:00:00.000+7000", jsonWrapper.getPrimitiveOrNull("startDate")?.content)
        assertEquals("2020-01-21T12:05:00.000+7000", jsonWrapper.getPrimitiveOrNull("endDate")?.content)
        val pathHistory = jsonWrapper.getArrayOrNull("stepHistory")
        assertNotNull(pathHistory)
        assertEquals(2, pathHistory.count())
        val r1 = pathHistory.firstOrNull()?.jsonObject
        assertNotNull(r1)
        assertEquals("resultA", r1.getPrimitiveOrNull("identifier")?.content)
        assertEquals("base", r1.getPrimitiveOrNull("type")?.content)
        val asyncResults = jsonWrapper.getArrayOrNull("asyncResults")
        assertNotNull(asyncResults)
        assertEquals(2, asyncResults.count())
        val ar1 = asyncResults.firstOrNull()?.jsonObject
        assertNotNull(ar1)
        assertEquals("asyncResultA", ar1.getPrimitiveOrNull("identifier")?.content)
        assertEquals("base", ar1.getPrimitiveOrNull("type")?.content)
    }

    /**
     * AnswerResult
     */

    @Test
    fun testAnswerResult_Boolean() {
        val original = TestResultWrapper(AnswerResultObject("foo", AnswerType.BOOLEAN, JsonPrimitive(true)))
        val inputString = """
            { "result": 
                    {
                        "identifier" : "foo",
                        "type" : "answer",
                        "answerType" : {
                            "type": "boolean"
                        },
                        "value" : true
                    }
            }
        """.trimIndent()

        val serializer = TestResultWrapper.serializer()
        val jsonString = jsonCoder.stringify(serializer, original)
        val restored = jsonCoder.parse(serializer, jsonString)
        val decoded = jsonCoder.parse(serializer, inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testAnswerResult_Decimal() {
        val original = TestResultWrapper(AnswerResultObject("foo", AnswerType.DECIMAL, JsonPrimitive(3.2)))
        val inputString = """
                { "result":
                    {
                        "identifier" : "foo",
                        "type" : "answer",
                        "answerType" : {
                            "type": "decimal"
                        },
                        "value" : 3.2
                    }
                }
        """.trimIndent()

        val serializer = TestResultWrapper.serializer()
        val jsonString = jsonCoder.stringify(serializer, original)
        val restored = jsonCoder.parse(serializer, jsonString)
        val decoded = jsonCoder.parse(serializer, inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testAnswerResult_Int() {
        val original = TestResultWrapper(AnswerResultObject("foo", AnswerType.INTEGER, JsonPrimitive(3)))
        val inputString = """
            { "result":
                    {
                        "identifier" : "foo",
                        "type" : "answer",
                        "answerType" : {
                            "type": "integer"
                        },
                        "value" : 3
                    }
            }
        """.trimIndent()

        val serializer = TestResultWrapper.serializer()
        val jsonString = jsonCoder.stringify(serializer, original)
        val restored = jsonCoder.parse(serializer, jsonString)
        val decoded = jsonCoder.parse(serializer, inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testAnswerResult_Map() {
        val originalValue = JsonObject(mapOf("a" to JsonLiteral(3.2), "b" to JsonLiteral("boo")))
        val original = TestResultWrapper(AnswerResultObject("foo", AnswerType.MAP, originalValue))
        val inputString = """
                { "result":
                    {
                        "identifier" : "foo",
                        "type" : "answer",
                        "answerType" : {
                            "type": "map"
                        },
                        "value" : {"a":3.2,"b":"boo"}
                    }
                }
        """.trimIndent()

        val serializer = TestResultWrapper.serializer()
        val jsonString = jsonCoder.stringify(serializer, original)
        val restored = jsonCoder.parse(serializer, jsonString)
        val decoded = jsonCoder.parse(serializer, inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testAnswerResult_String() {
        val originalValue = JsonPrimitive("goo")
        val original = TestResultWrapper(AnswerResultObject("foo", AnswerType.STRING, originalValue))
        val inputString = """
                { "result":
                    {
                        "identifier" : "foo",
                        "type" : "answer",
                        "answerType" : {
                            "type": "string"
                        },
                        "value" : "goo"
                    }
                }
        """.trimIndent()

        val serializer = TestResultWrapper.serializer()
        val jsonString = jsonCoder.stringify(serializer, original)
        val restored = jsonCoder.parse(serializer, jsonString)
        val decoded = jsonCoder.parse(serializer, inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testAnswerResult_DateYearMonth() {
        val originalValue = JsonPrimitive("2020-02")
        val original = TestResultWrapper(AnswerResultObject("foo", AnswerType.DateTime("yyyy-MM"), originalValue))
        val inputString = """
                { "result":
                    {
                        "identifier" : "foo",
                        "type" : "answer",
                        "answerType" : {
                            "type": "dateTime",
                            "codingFormat": "yyyy-MM"
                        },
                        "value" : "2020-02"
                    }
                }
        """.trimIndent()

        val serializer = TestResultWrapper.serializer()
        val jsonString = jsonCoder.stringify(serializer, original)
        val restored = jsonCoder.parse(serializer, jsonString)
        val decoded = jsonCoder.parse(serializer, inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testAnswerResult_ListInt() {
        val originalValue = JsonArray(listOf(JsonPrimitive(2), JsonPrimitive(5)))
        val original = TestResultWrapper(AnswerResultObject("foo", AnswerType.List(BaseType.INTEGER), originalValue))
        val inputString = """
                { "result":
                    {
                        "identifier" : "foo",
                        "type" : "answer",
                        "answerType" : {
                            "type": "list",
                            "baseType": "integer"
                        },
                        "value" : [2,5]
                    }
                }
        """.trimIndent()

        val serializer = TestResultWrapper.serializer()
        val jsonString = jsonCoder.stringify(serializer, original)
        val restored = jsonCoder.parse(serializer, jsonString)
        val decoded = jsonCoder.parse(serializer, inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testAnswerResult_Measurement() {
        val originalValue = JsonPrimitive(10.2)
        val original = TestResultWrapper(AnswerResultObject("foo", AnswerType.Measurement("cm"), originalValue))
        val inputString = """
                { "result":
                    {
                        "identifier" : "foo",
                        "type" : "answer",
                        "answerType" : {
                            "type": "measurement",
                            "unit": "cm"
                        },
                        "value" : 10.2
                    }
                }
        """.trimIndent()

        val serializer = TestResultWrapper.serializer()
        val jsonString = jsonCoder.stringify(serializer, original)
        val restored = jsonCoder.parse(serializer, jsonString)
        val decoded = jsonCoder.parse(serializer, inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }
}