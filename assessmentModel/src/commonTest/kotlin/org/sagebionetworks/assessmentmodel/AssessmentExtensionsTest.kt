package org.sagebionetworks.assessmentmodel

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.sagebionetworks.assessmentmodel.navigation.Direction
import org.sagebionetworks.assessmentmodel.serialization.AssessmentObject
import org.sagebionetworks.assessmentmodel.serialization.Serialization
import org.sagebionetworks.assessmentmodel.survey.AnswerType
import org.sagebionetworks.assessmentmodel.survey.BaseType
import org.sagebionetworks.assessmentmodel.survey.Question
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AssessmentExtensionsTest {

    private val assessmentJson = """
        {
          "type": "assessment",
          "identifier": "qcdkhq",
          "shouldHideActions": [],
          "interruptionHandling": {
            "canResume": true,
            "reviewIdentifier": "beginning",
            "canSkip": true,
            "canSaveForLater": true
          },
          "steps": [
            {
              "type": "overview",
              "identifier": "overview",
              "title": "A Flowery Survey",
              "detail": "This is a new survey",
              "image": {
                "imageName": "mental_health",
                "type": "sageResource"
              }
            },
            {
              "type": "instruction",
              "identifier": "instruction_ccd",
              "title": "Instruction",
              "detail": "Lorem ipsum dolor sit"
            },
            {
              "type": "simpleQuestion",
              "identifier": "time_xmx",
              "title": "Time",
              "inputItem": {
                "type": "time",
                "formatOptions": {
                  "allowFuture": true,
                  "allowPast": true
                }
              }
            },
            {
              "type": "simpleQuestion",
              "identifier": "duration_nwr",
              "title": "Duration",
              "inputItem": {
                "type": "duration"
              }
            },
            {
              "type": "simpleQuestion",
              "identifier": "likert_scale_bjj",
              "title": "Likert Scale",
              "uiHint": "likert",
              "inputItem": {
                "type": "integer",
                "formatOptions": {
                  "minimumLabel": "Foo",
                  "minimumValue": 0,
                  "maximumLabel": "Magoo",
                  "maximumValue": 5
                }
              }
            },
            {
              "type": "simpleQuestion",
              "identifier": "sliderQ_dfjwnf",
              "title": "Slider",
              "uiHint": "slider",
              "inputItem": {
                "type": "integer",
                "formatOptions": {
                  "maximumLabel": "Very",
                  "maximumValue": 100,
                  "minimumLabel": "None",
                  "minimumValue": 0
                }
              }
            },
            {
              "type": "simpleQuestion",
              "identifier": "numericQ_rqmrrj",
              "title": "Integer",
              "uiHint": "textfield",
              "inputItem": {
                "type": "integer"
              }
            },
            {
              "type": "simpleQuestion",
              "identifier": "yearQ_rwphsn",
              "title": "Year",
              "inputItem": {
                "type": "year",
                "placeholder": "YYYY",
                "formatOptions": {
                  "minimumYear": 1900,
                  "allowFuture": false,
                  "maximumYear": 1975,
                  "allowPast": true
                },
                "fieldLabel": "This is a label"
              }
            },
            {
              "type": "simpleQuestion",
              "identifier": "free_text_qfj",
              "title": "Free text",
              "inputItem": {
                "type": "string",
                "placeholder": "(Maximum 250 characters)"
              }
            },
            {
              "type": "choiceQuestion",
              "identifier": "single_select_xnn",
              "subtitle": "",
              "title": "Single Select",
              "detail": "",
              "baseType": "string",
              "singleChoice": true,
              "choices": [
                {
                  "value": "Choice_A",
                  "text": "Choice A"
                },
                {
                  "value": "Choice_B",
                  "text": "Choice B"
                },
                {
                  "text": "Choice C",
                  "value": "Choice_C"
                },
                {
                  "text": "Choice D",
                  "value": "Choice_D"
                }
              ]
            },
            {
              "type": "choiceQuestion",
              "identifier": "multiple_select_rqk",
              "subtitle": "",
              "title": "Multiple Select",
              "detail": "",
              "baseType": "string",
              "singleChoice": false,
              "choices": [
                {
                  "value": "Choice A",
                  "text": "Choice A"
                },
                {
                  "value": "Choice B",
                  "text": "Choice B"
                }
              ]
            },
            {
              "type": "completion",
              "identifier": "completion",
              "title": "Well Done!",
              "detail": "Thank you for being part of our survey",
              "actions": {
                "goForward": {
                  "buttonTitle": "Exit Survey",
                  "type": "default"
                }
              }
            }
          ],
          "webConfig": {
            "skipOption": "CUSTOMIZE"
          }
        }
    """.trimIndent()

    @Test
    fun testToFlatAnswersDefinition() {
        val serializer = PolymorphicSerializer(Assessment::class)
        val assessment: Assessment = Serialization.JsonCoder.default.decodeFromString(serializer, assessmentJson)
        val columns = assessment.toFlatAnswersDefinition()
        val children = (assessment as AssessmentObject).children
        assertEquals(9, columns.size)
        assertEquals(12, children.size) // Includes instruction steps
        for (column in columns) {
            // Check that columnName matches the identifier of a node in the assessment
            val node = children.first { it.resultId() == column.columnName } as Question
            val result = node.createResult() as AnswerResult
            // Check that the column type matches the baseType of the result
            assertEquals(result.answerType, column.type)
            assertEquals(node.title, column.questionTitle)
        }
    }

    @Test
    fun testToFlatAnswersJson() {
        val serializer = PolymorphicSerializer(Assessment::class)
        val assessment: Assessment = Serialization.JsonCoder.default.decodeFromString(serializer, assessmentJson)
        val result = buildResults(assessment as AssessmentObject)
        val answers = result.toFlatAnswers()
        assertEquals(9, answers.size)
        val columns = assessment.toFlatAnswersDefinition()
        assertEquals(9, columns.size)
        for (column in columns) {
            // Verify that the keys to the answers match the column names in the definition
            assertTrue(answers.containsKey(column.columnName))
        }
        val expectedAnswers = mapOf(
            "time_xmx" to "10:15",
            "duration_nwr" to "8.0",
            "likert_scale_bjj" to "1",
            "sliderQ_dfjwnf" to "1",
            "numericQ_rqmrrj" to "1",
            "yearQ_rwphsn" to "1",
            "free_text_qfj" to "Test String",
            "single_select_xnn" to "Test String",
            "multiple_select_rqk" to "Choice 1,Choice 2"
        )
        assertEquals(expectedAnswers, answers)

    }

    private fun buildResults(assessmentObject: AssessmentObject) : AssessmentResult {
        val result = assessmentObject.createResult()
        for (child in assessmentObject.children) {
            val childResult = child.createResult()
            if (childResult is AnswerResult) {
                childResult.jsonValue = when (childResult.answerType!!) {
                    is AnswerType.Measurement -> JsonPrimitive(2.0)
                    is AnswerType.Time -> JsonPrimitive("10:15")
                    is AnswerType.DateTime -> JsonPrimitive("2020-01-21T12:00:00.000-03:00")
                    is AnswerType.Duration -> JsonPrimitive(8.0)
                    is AnswerType.INTEGER -> JsonPrimitive(1)
                    is AnswerType.Decimal -> JsonPrimitive(1.0)
                    is AnswerType.BOOLEAN -> JsonPrimitive(true)
                    is AnswerType.STRING -> JsonPrimitive("Test String")
                    is AnswerType.Array -> {
                        if ((childResult.answerType as AnswerType.Array).baseType == BaseType.INTEGER) {
                            JsonArray(listOf(JsonPrimitive(1), JsonPrimitive(2)))
                        } else {
                            JsonArray(listOf(JsonPrimitive("Choice 1"), JsonPrimitive("Choice 2")))
                        }
                    }
                    is AnswerType.OBJECT -> JsonObject(mapOf(Pair("Test Key", JsonPrimitive("Test value"))))
                    else -> throw UnsupportedOperationException("Unknown AnswerType")
                }
            }
            result.pathHistoryResults.add(childResult)
            result.path.add(PathMarker(child.identifier, Direction.Forward))
        }
        return result
    }

}