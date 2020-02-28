package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.json.JsonPrimitive
import org.sagebionetworks.assessmentmodel.AnswerResult
import org.sagebionetworks.assessmentmodel.serialization.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QuestionTest {

    /**
     * ChoiceQuestion - interface implementations
     */

    @Test
    fun testChoiceQuestion_Result_NullResultId() {
        val original = ChoiceQuestionObject("foo", listOf())
        val result = original.createResult()
        assertEquals("foo", result.identifier)
        assertTrue(result is AnswerResult)
        assertEquals(original.answerType, result.answerType)
    }

    @Test
    fun testChoiceQuestion_Result_WithResultId() {
        val original = ChoiceQuestionObject("foo", listOf(), resultIdentifier = "bar")
        val result = original.createResult()
        assertEquals("foo", result.identifier)
        assertTrue(result is AnswerResult)
        assertEquals(original.answerType, result.answerType)
    }

    @Test
    fun testChoiceQuestion_AnswerType_MultipleChoice() {
        val original = ChoiceQuestionObject(
                identifier = "foo",
                choices = listOf(
                        ChoiceOptionObject(JsonPrimitive(1), "choice 1"),
                        ChoiceOptionObject(JsonPrimitive(2), "choice 2"),
                        ChoiceOptionObject(JsonPrimitive(3), "choice 3")),
                baseType = BaseType.INTEGER,
                singleAnswer = false)
        assertEquals(AnswerType.List(BaseType.INTEGER), original.answerType)
    }

    @Test
    fun testChoiceQuestion_AnswerType_SingleChoice() {
        val original = ChoiceQuestionObject(
                identifier = "foo",
                choices = listOf(
                        ChoiceOptionObject(JsonPrimitive(1), "choice 1"),
                        ChoiceOptionObject(JsonPrimitive(2), "choice 2"),
                        ChoiceOptionObject(JsonPrimitive(3), "choice 3")),
                baseType = BaseType.INTEGER,
                singleAnswer = true)
        assertEquals(AnswerType.INTEGER, original.answerType)
    }

    @Test
    fun testChoiceQuestion_BuildInputItems() {
        val choices = listOf(
                ChoiceOptionObject(JsonPrimitive(1), "choice 1"),
                ChoiceOptionObject(JsonPrimitive(2), "choice 2"),
                ChoiceOptionObject(JsonPrimitive(3), "choice 3"))
        val original = ChoiceQuestionObject(
                identifier = "foo",
                choices = choices,
                baseType = BaseType.INTEGER,
                singleAnswer = true)
        val items = original.buildInputItems()
        assertEquals(choices.count(), items.count())
        items.forEach {choiceInput ->
            assertTrue(choiceInput is ChoiceItemWrapper)
        }
    }

    /**
     * ComboBoxQuestion - interface implementations
     */

    @Test
    fun testComboBoxQuestion_Result_NullResultId() {
        val original = ComboBoxQuestionObject("foo", listOf())
        val result = original.createResult()
        assertEquals("foo", result.identifier)
        assertTrue(result is AnswerResult)
        assertEquals(original.answerType, result.answerType)
    }

    @Test
    fun testComboBoxQuestion_Result_WithResultId() {
        val original = ComboBoxQuestionObject("foo", listOf(), resultIdentifier = "bar")
        val result = original.createResult()
        assertEquals("foo", result.identifier)
        assertTrue(result is AnswerResult)
        assertEquals(original.answerType, result.answerType)
    }

    @Test
    fun testComboBoxQuestion_AnswerType_MultipleChoice() {
        val original = ComboBoxQuestionObject(
                identifier = "foo",
                choices = listOf(
                        ChoiceOptionObject(JsonPrimitive(1), "choice 1"),
                        ChoiceOptionObject(JsonPrimitive(2), "choice 2"),
                        ChoiceOptionObject(JsonPrimitive(3), "choice 3")),
                otherInputItem = IntegerTextInputItemObject(),
                singleAnswer = false)
        assertEquals(AnswerType.List(BaseType.INTEGER), original.answerType)
    }

    @Test
    fun testComboBoxQuestion_AnswerType_SingleChoice() {
        val original = ComboBoxQuestionObject(
                identifier = "foo",
                choices = listOf(
                        ChoiceOptionObject(JsonPrimitive(1), "choice 1"),
                        ChoiceOptionObject(JsonPrimitive(2), "choice 2"),
                        ChoiceOptionObject(JsonPrimitive(3), "choice 3")),
                otherInputItem = IntegerTextInputItemObject(),
                singleAnswer = true)
        assertEquals(AnswerType.INTEGER, original.answerType)
    }

    @Test
    fun testComboBoxQuestion_BuildInputItems() {
        val choices = listOf(
                ChoiceOptionObject(JsonPrimitive(1), "choice 1"),
                ChoiceOptionObject(JsonPrimitive(2), "choice 2"),
                ChoiceOptionObject(JsonPrimitive(3), "choice 3"))
        val original = ChoiceQuestionObject(
                identifier = "foo",
                choices = choices,
                baseType = BaseType.INTEGER,
                singleAnswer = true)
        val items = original.buildInputItems()
        assertEquals(choices.count() + 1, items.count())
        items.dropLast(1).forEach {choiceInput ->
            assertTrue(choiceInput is ChoiceItemWrapper)
        }
        assertTrue(items.last() is OtherChoiceItemWrapper)
    }

    /**
     * MultipleInputQuestion - interface implementation
     */

    @Test
    fun testMultipleInputQuestion_Result_NullResultId() {
        val original = MultipleInputQuestionObject("foo", inputItems = listOf(StringTextInputItemObject(), StringTextInputItemObject()))
        val result = original.createResult()
        assertEquals("foo", result.identifier)
        assertTrue(result is AnswerResult)
        assertEquals(original.answerType, result.answerType)
    }

    @Test
    fun testMultipleInputQuestion_Result_WithResultId() {
        val original = MultipleInputQuestionObject("foo", inputItems = listOf(StringTextInputItemObject(), StringTextInputItemObject()), resultIdentifier = "bar")
        val result = original.createResult()
        assertEquals("bar", result.identifier)
        assertTrue(result is AnswerResult)
        assertEquals(original.answerType, result.answerType)
    }

    @Test
    fun testMultipleInputQuestion_AnswerType_MAP() {
        val item1 = StringTextInputItemObject("foo")
        val item2 = StringTextInputItemObject("bar")
        val original = MultipleInputQuestionObject("foo", inputItems = listOf(item1, item2))
        assertEquals(AnswerType.MAP, original.answerType)
    }

    @Test
    fun testMultipleInputQuestion_AnswerType_List() {
        val item1 = StringTextInputItemObject()
        val item2 = StringTextInputItemObject()
        val original = MultipleInputQuestionObject("foo", inputItems = listOf(item1, item2))
        assertEquals(AnswerType.List(BaseType.STRING), original.answerType)
    }

    @Test
    fun testMultipleInputQuestion_AnswerType_MAP_WithSkipCheckbox() {
        val item1 = StringTextInputItemObject("foo")
        val item2 = StringTextInputItemObject("bar")
        val original = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2),
                skipCheckbox = SkipCheckboxInputItemObject("no answer"))
        assertEquals(AnswerType.MAP, original.answerType)
    }
}