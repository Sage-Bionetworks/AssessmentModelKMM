package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.sagebionetworks.assessmentmodel.ButtonAction
import org.sagebionetworks.assessmentmodel.ButtonStyle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

open class ResultTest {

    val jsonCoder = Serialization.JsonCoder.default

    @Test
    fun testCollectionResult() {
        val result1 = ResultObject("result1")
        val result2 = ResultObject("result2")
        val result3 = CollectionResultObject(identifier = "result3",
                pathHistoryResults = mutableListOf(ResultObject("resultA"), ResultObject("resultB")),
                asyncActionResults = mutableSetOf(ResultObject("asyncResultA"), ResultObject("asyncResultB")))
        val result4 = CollectionResultObject(identifier = "result4",
                pathHistoryResults = mutableListOf(ResultObject("resultA"), ResultObject("resultB")),
                asyncActionResults = mutableSetOf(ResultObject("asyncResultA"), ResultObject("asyncResultB")))

        val original = CollectionResultObject("testResult", pathHistoryResults = mutableListOf(result1, result2, result3, result4))
        val inputString = """
            {
                "identifier": "testResult",
                "stepHistory": [
                    {"identifier": "result1","type": "base"},
                    {"identifier": "result2","type": "base"},
                    {
                        "identifier": "result3",
                        "type": "collection",
                        "stepHistory": [{"identifier": "resultA","type": "base"},{"identifier": "resultB","type": "base"}],
                        "asyncResults": [{"identifier": "asyncResultA","type": "base"},{"identifier": "asyncResultB","type": "base"}]
                    },
                    {
                        "identifier": "result4",
                        "type": "collection",
                        "stepHistory": [{"identifier": "resultA","type": "base"},{"identifier": "resultB","type": "base"}],
                        "asyncResults": [{"identifier": "asyncResultA","type": "base"},{"identifier": "asyncResultB","type": "base"}]
                    }
                ]
            }   
            """.trimIndent()

        val jsonString = jsonCoder.stringify(CollectionResultObject.serializer(), original)
        val restored = jsonCoder.parse(CollectionResultObject.serializer(), jsonString)
        val decoded = jsonCoder.parse(CollectionResultObject.serializer(), inputString)

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

    @Test
    fun testAssessmentResult() {
        val original = AssessmentResultObject(identifier = "testResult",
                pathHistoryResults = mutableListOf(ResultObject("resultA"), ResultObject("resultB")),
                asyncActionResults = mutableSetOf(ResultObject("asyncResultA"), ResultObject("asyncResultB")),
                taskRunUUIDString = "4cb0580-3cdb-11ea-b77f-2e728ce88125",
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
}