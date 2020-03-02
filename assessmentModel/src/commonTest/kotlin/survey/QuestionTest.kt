package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.sagebionetworks.assessmentmodel.AnswerResult
import org.sagebionetworks.assessmentmodel.BranchNode
import org.sagebionetworks.assessmentmodel.BranchNodeResult
import org.sagebionetworks.assessmentmodel.navigation.BranchNodeStateImpl
import org.sagebionetworks.assessmentmodel.navigation.NavigationTestHelper
import org.sagebionetworks.assessmentmodel.navigation.NodeNavigator
import org.sagebionetworks.assessmentmodel.serialization.*
import kotlin.test.*

class QuestionTest : NavigationTestHelper() {

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
        assertEquals("bar", result.identifier)
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
        assertEquals("bar", result.identifier)
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
        val otherItem = IntegerTextInputItemObject()
        val original = ComboBoxQuestionObject(
                identifier = "foo",
                choices = choices,
                otherInputItem = otherItem)
        val items = original.buildInputItems()
        assertEquals(4, items.count())
        items.dropLast(1).forEach {choiceInput ->
            assertTrue(choiceInput is ChoiceItemWrapper)
        }
        assertEquals(otherItem, items.last())
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
    fun testMultipleInputQuestion_AnswerType_WithIndentifiers() {
        val item1 = StringTextInputItemObject("foo")
        val item2 = StringTextInputItemObject("bar")
        val original = MultipleInputQuestionObject("foo", inputItems = listOf(item1, item2))
        assertEquals(AnswerType.MAP, original.answerType)
    }

    @Test
    fun testMultipleInputQuestion_AnswerType_NoIdentifiers() {
        val item1 = StringTextInputItemObject()
        val item2 = StringTextInputItemObject()
        val original = MultipleInputQuestionObject("foo", inputItems = listOf(item1, item2))
        assertEquals(AnswerType.MAP, original.answerType)
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

    @Test
    fun testMultipleInputQuestion_BuildItems_NoSkipCheckbox() {
        val item1 = StringTextInputItemObject("item1")
        val item2 = StringTextInputItemObject("item2")
        val original = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2))
        val items = original.buildInputItems()
        assertEquals(2, items.count())
        assertEquals(item1, items.first())
        assertEquals(item2, items.last())
    }

    @Test
    fun testMultipleInputQuestion_BuildItems_WithSkipCheckbox() {
        val item1 = StringTextInputItemObject("foo")
        val item2 = StringTextInputItemObject("bar")
        val checkbox = SkipCheckboxInputItemObject("no answer")
        val original = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2),
                skipCheckbox = checkbox)
        val items = original.buildInputItems()
        assertEquals(3, items.count())
        assertEquals(item1, items.first())
        assertEquals(checkbox, items.last())
    }

    /**
     * SimpleQuestion - interface implementation
     */

    @Test
    fun testSimpleQuestion_Result_NullResultId() {
        val original = SimpleQuestionObject("foo", inputItem = StringTextInputItemObject())
        val result = original.createResult()
        assertEquals("foo", result.identifier)
        assertTrue(result is AnswerResult)
        assertEquals(original.answerType, result.answerType)
    }

    @Test
    fun testSimpleQuestion_Result_WithResultId() {
        val original = SimpleQuestionObject("foo", inputItem = StringTextInputItemObject(), resultIdentifier = "bar")
        val result = original.createResult()
        assertEquals("bar", result.identifier)
        assertTrue(result is AnswerResult)
        assertEquals(original.answerType, result.answerType)
    }

    @Test
    fun testSimpleQuestion_AnswerType_STRING_WithIdentifiers() {
        val item1 = StringTextInputItemObject("foo")
        val original = SimpleQuestionObject("foo", inputItem = item1)
        assertEquals(AnswerType.STRING, original.answerType)
    }

    @Test
    fun testSimpleQuestion_AnswerType_STRING_NoIdentifiers() {
        val item1 = StringTextInputItemObject()
        val original = SimpleQuestionObject("foo", inputItem = item1)
        assertEquals(AnswerType.STRING, original.answerType)
    }

    @Test
    fun testSimpleQuestion_AnswerType_INTEGER_NoIdentifiers() {
        val item1 = IntegerTextInputItemObject()
        val original = SimpleQuestionObject("foo", inputItem = item1)
        assertEquals(AnswerType.INTEGER, original.answerType)
    }

    @Test
    fun testSimpleQuestion_AnswerType_WithSkipCheckbox() {
        val item1 = IntegerTextInputItemObject()
        val original = SimpleQuestionObject("foo",
                inputItem = item1,
                skipCheckbox = SkipCheckboxInputItemObject("no answer"))
        assertEquals(AnswerType.INTEGER, original.answerType)
    }

    @Test
    fun testSimpleQuestion_BuildItems_NoSkipCheckbox() {
        val item1 = StringTextInputItemObject()
        val original = SimpleQuestionObject("foo", inputItem = item1)
        val items = original.buildInputItems()
        assertEquals(1, items.count())
        assertEquals(item1, items.first())
    }

    @Test
    fun testSimpleQuestion_BuildItems_WithSkipCheckbox() {
        val item1 = StringTextInputItemObject()
        val checkbox = SkipCheckboxInputItemObject("no answer")
        val original = SimpleQuestionObject("foo",
                inputItem = item1,
                skipCheckbox = checkbox)
        val items = original.buildInputItems()
        assertEquals(2, items.count())
        assertEquals(item1, items.first())
        assertEquals(checkbox, items.last())
    }

    /**
     * QuestionStateImpl - Initial state
     * syoung 03/02/2020 Including with the [Question] tests b/c changes to one will impact the other.
     */

    @Test
    fun testQuestionStateImpl_Init_SimpleQuestion() {
        val item1 = StringTextInputItemObject()
        val question = SimpleQuestionObject("foo", inputItem = item1)
        val questionState = buildQuestionState(question)

        // Check expectations for the initial item state.
        assertEquals(1, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertTrue(firstItem is KeyboardInputItemState<*>, "$firstItem not of expected type")
        assertNull(firstItem.currentAnswer)
        assertEquals(item1, firstItem.inputItem)

        // Check expectations for the result and answer type.
        assertNull(questionState.currentResult.jsonValue)
        assertEquals(AnswerType.STRING, questionState.currentResult.answerType)
        assertEquals(AnswerType.STRING, questionState.answerType)
    }

    @Test
    fun testQuestionStateImpl_Init_SimpleQuestion_PreviousResult() {
        val item1 = StringTextInputItemObject()
        val question = SimpleQuestionObject("foo", inputItem = item1)
        val jsonValue = JsonPrimitive("baloo")
        val previousResult = AnswerResultObject("foo", question.answerType, jsonValue)
        val questionState = buildQuestionState(question, previousResult)

        // Check expectations for the initial item state.
        assertEquals(1, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertEquals(jsonValue, firstItem.currentAnswer)

        // Check expectations for the result and answer type.
        assertEquals(previousResult, questionState.currentResult)
    }

    @Test
    fun testQuestionStateImpl_Init_SimpleQuestion_WithSkipCheckbox() {
        val item1 = StringTextInputItemObject()
        val checkbox = SkipCheckboxInputItemObject("no answer")
        val question = SimpleQuestionObject("foo",
                inputItem = item1,
                skipCheckbox = checkbox)
        val questionState = buildQuestionState(question)

        // Check expectations for the initial item state.
        assertEquals(2, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertNull(firstItem.currentAnswer)
        assertEquals(item1, firstItem.inputItem)
        val lastItem = questionState.itemStates.last()
        assertTrue(lastItem is ChoiceInputItemState, "$lastItem not of expected type")
        assertFalse(lastItem.selected)
        assertEquals(checkbox, lastItem.inputItem)

        // Check expectations for the result and answer type.
        assertNull(questionState.currentResult.jsonValue)
        assertEquals(AnswerType.STRING, questionState.currentResult.answerType)
        assertEquals(AnswerType.STRING, questionState.answerType)
    }

    @Test
    fun testQuestionStateImpl_Init_SimpleQuestion_WithSkipCheckbox_PreviousResult() {
        val item1 = StringTextInputItemObject()
        val checkbox = SkipCheckboxInputItemObject("no answer")
        val question = SimpleQuestionObject("foo",
                inputItem = item1,
                skipCheckbox = checkbox)
        val jsonValue = JsonPrimitive("baloo")
        val previousResult = AnswerResultObject("foo", question.answerType, jsonValue)
        val questionState = buildQuestionState(question, previousResult)

        // Check expectations for the initial item state.
        assertEquals(2, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertEquals(jsonValue, firstItem.currentAnswer)
        val lastItem = questionState.itemStates.last()
        assertTrue(lastItem is ChoiceInputItemState, "$lastItem not of expected type")
        assertFalse(lastItem.selected)

        // Check expectations for the result and answer type.
        assertEquals(previousResult, questionState.currentResult)
    }

    @Test
    fun testQuestionStateImpl_Init_SimpleQuestion_WithSkipCheckbox_PreviousResultChecked() {
        val item1 = StringTextInputItemObject()
        val checkbox = SkipCheckboxInputItemObject("no answer")
        val question = SimpleQuestionObject("foo",
                inputItem = item1,
                skipCheckbox = checkbox)
        val jsonValue = JsonNull
        val previousResult = AnswerResultObject("foo", question.answerType, jsonValue)
        val questionState = buildQuestionState(question, previousResult)

        // Check expectations for the initial item state.
        assertEquals(2, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertNull(firstItem.currentAnswer)
        val lastItem = questionState.itemStates.last()
        assertTrue(lastItem is ChoiceInputItemState, "$lastItem not of expected type")
        assertTrue(lastItem.selected)

        // Check expectations for the result and answer type.
        assertEquals(previousResult, questionState.currentResult)
    }

    @Test
    fun testQuestionStateImpl_Init_MultipleInputQuestion() {
        val item1 = StringTextInputItemObject("item1")
        val item2 = StringTextInputItemObject("item2")
        val question = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2))
        val questionState = buildQuestionState(question)

        // Check expectations for the initial item state.
        assertEquals(2, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertTrue(firstItem is KeyboardInputItemState<*>, "$firstItem not of expected type")
        assertNull(firstItem.currentAnswer)
        assertEquals(item1, firstItem.inputItem)

        // Check expectations for the result and answer type.
        assertNull(questionState.currentResult.jsonValue)
        assertEquals(AnswerType.MAP, questionState.currentResult.answerType)
        assertEquals(AnswerType.MAP, questionState.answerType)
    }

    @Test
    fun testQuestionStateImpl_Init_MultipleInputQuestion_PreviousResult_WithResultIdentifiers() {
        val item1 = StringTextInputItemObject("item1")
        val item2 = StringTextInputItemObject("item2")
        val question = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2))
        val a1 = JsonPrimitive("baloo")
        val a2 = JsonPrimitive("ragu")
        val jsonValue = JsonObject(mapOf("item1" to a1, "item2" to a2))
        assertEquals(AnswerType.MAP, question.answerType)
        val previousResult = AnswerResultObject("foo", question.answerType, jsonValue)
        val questionState = buildQuestionState(question, previousResult)

        // Check expectations for the initial item state.
        assertEquals(2, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertEquals(a1, firstItem.currentAnswer)
        val lastItem = questionState.itemStates.last()
        assertEquals(a2, lastItem.currentAnswer)

        // Check expectations for the result and answer type.
        assertEquals(previousResult, questionState.currentResult)
    }

    @Test
    fun testQuestionStateImpl_Init_MultipleInputQuestion_PreviousResult_NoResultIdentifiers() {
        val item1 = StringTextInputItemObject()
        val item2 = StringTextInputItemObject()
        val question = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2))
        val a1 = JsonPrimitive("baloo")
        val a2 = JsonPrimitive("ragu")
        val jsonValue = JsonObject(mapOf("0" to a1, "1" to a2))
        assertEquals(AnswerType.MAP, question.answerType)
        val previousResult = AnswerResultObject("foo", question.answerType, jsonValue)
        val questionState = buildQuestionState(question, previousResult)

        // Check expectations for the initial item state.
        assertEquals(2, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertEquals(a1, firstItem.currentAnswer)
        val lastItem = questionState.itemStates.last()
        assertEquals(a2, lastItem.currentAnswer)

        // Check expectations for the result and answer type.
        assertEquals(previousResult, questionState.currentResult)
    }

    @Test
    fun testQuestionStateImpl_Init_MultipleInputQuestion_WithSkipCheckbox_NotChecked() {
        val item1 = StringTextInputItemObject("item1")
        val item2 = StringTextInputItemObject("item2")
        val checkbox = SkipCheckboxInputItemObject("no answer")
        val question = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2),
                skipCheckbox = checkbox)
        val a1 = JsonPrimitive("baloo")
        val a2 = JsonPrimitive("ragu")
        val jsonValue = JsonObject(mapOf("item1" to a1, "item2" to a2))
        assertEquals(AnswerType.MAP, question.answerType)
        val previousResult = AnswerResultObject("foo", question.answerType, jsonValue)
        val questionState = buildQuestionState(question, previousResult)

        // Check expectations for the initial item state.
        assertEquals(3, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertEquals(a1, firstItem.currentAnswer)
        val lastItem = questionState.itemStates.last()
        assertTrue(lastItem is ChoiceInputItemState, "$lastItem not of expected type")
        assertFalse(lastItem.selected)

        // Check expectations for the result and answer type.
        assertEquals(previousResult, questionState.currentResult)
    }

    @Test
    fun testQuestionStateImpl_Init_MultipleInputQuestion_WithSkipCheckbox_Checked() {
        val item1 = StringTextInputItemObject("item1")
        val item2 = StringTextInputItemObject("item2")
        val checkbox = SkipCheckboxInputItemObject("no answer")
        val question = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2),
                skipCheckbox = checkbox)
        val jsonValue = JsonNull
        assertEquals(AnswerType.MAP, question.answerType)
        val previousResult = AnswerResultObject("foo", question.answerType, jsonValue)
        val questionState = buildQuestionState(question, previousResult)

        // Check expectations for the initial item state.
        assertEquals(3, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertEquals(null, firstItem.currentAnswer)
        val lastItem = questionState.itemStates.last()
        assertTrue(lastItem is ChoiceInputItemState, "$lastItem not of expected type")
        assertTrue(lastItem.selected)

        // Check expectations for the result and answer type.
        assertEquals(previousResult, questionState.currentResult)
    }

    @Test
    fun testQuestionStateImpl_Init_ChoiceQuestion() {
        val item1 = ChoiceOptionObject(JsonPrimitive("item1"))
        val item2 = ChoiceOptionObject(JsonPrimitive("item2"))
        val item3 = ChoiceOptionObject(JsonPrimitive("item3"))
        val question = ChoiceQuestionObject("foo", listOf(item1, item2, item3))
        val questionState = buildQuestionState(question)

        // Check expectations for the initial item state.
        assertEquals(3, questionState.itemStates.count())
        questionState.itemStates.forEach {
            assertTrue(it is ChoiceInputItemState, "$it not of expected type")
            assertFalse(it.selected)
        }

        // Check expectations for the result and answer type.
        assertNull(questionState.currentResult.jsonValue)
        assertEquals(question.answerType, questionState.currentResult.answerType)
        assertEquals(question.answerType, questionState.answerType)
    }

    @Test
    fun testQuestionStateImpl_Init_ChoiceQuestion_PreviousResult_SingleAnswer() {
        val item1 = ChoiceOptionObject(JsonPrimitive("item1"))
        val item2 = ChoiceOptionObject(JsonPrimitive("item2"))
        val item3 = ChoiceOptionObject(JsonPrimitive("item3"))
        val question = ChoiceQuestionObject("foo", listOf(item1, item2, item3), singleAnswer = true)
        val previousResult = AnswerResultObject("foo", AnswerType.STRING, JsonPrimitive("item2"))
        val questionState = buildQuestionState(question, previousResult)

        // Check expectations for the initial item state.
        assertEquals(3, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertTrue(firstItem is ChoiceInputItemState, "$firstItem not of expected type")
        assertFalse(firstItem.selected)
        val secondItem = questionState.itemStates[1]
        assertTrue(secondItem is ChoiceInputItemState, "$secondItem not of expected type")
        assertTrue(secondItem.selected)
        val lastItem = questionState.itemStates.last()
        assertTrue(lastItem is ChoiceInputItemState, "$lastItem not of expected type")
        assertFalse(lastItem.selected)

        // Check expectations for the result and answer type.
        assertEquals(previousResult, questionState.currentResult)
    }

    @Test
    fun testQuestionStateImpl_Init_ChoiceQuestion_PreviousResult_MultipleAnswer() {
        val item1 = ChoiceOptionObject(JsonPrimitive("item1"))
        val item2 = ChoiceOptionObject(JsonPrimitive("item2"))
        val item3 = ChoiceOptionObject(JsonPrimitive("item3"))
        val question = ChoiceQuestionObject("foo", listOf(item1, item2, item3), singleAnswer = false)
        val previousResult = AnswerResultObject("foo",
                answerType = AnswerType.List(BaseType.STRING),
                jsonValue = JsonArray(listOf(JsonPrimitive("item1"), JsonPrimitive("item2"))))
        val questionState = buildQuestionState(question, previousResult)

        // Check expectations for the initial item state.
        assertEquals(3, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertTrue(firstItem is ChoiceInputItemState, "$firstItem not of expected type")
        assertTrue(firstItem.selected)
        val secondItem = questionState.itemStates[1]
        assertTrue(secondItem is ChoiceInputItemState, "$secondItem not of expected type")
        assertTrue(secondItem.selected)
        val lastItem = questionState.itemStates.last()
        assertTrue(lastItem is ChoiceInputItemState, "$lastItem not of expected type")
        assertFalse(lastItem.selected)

        // Check expectations for the result and answer type.
        assertEquals(previousResult, questionState.currentResult)
    }

    @Test
    fun testQuestionStateImpl_Init_ComboBoxQuestion() {
        val item1 = ChoiceOptionObject(JsonPrimitive("item1"))
        val item2 = ChoiceOptionObject(JsonPrimitive("item2"))
        val item3 = ChoiceOptionObject(JsonPrimitive("item3"))
        val question = ComboBoxQuestionObject("foo", listOf(item1, item2, item3))
        val questionState = buildQuestionState(question)

        // Check expectations for the initial item state.
        assertEquals(4, questionState.itemStates.count())
        questionState.itemStates.dropLast(1).forEach {
            assertTrue(it is ChoiceInputItemState, "$it not of expected type")
            assertFalse(it.selected)
        }
        val lastItem = questionState.itemStates.last()
        assertTrue(lastItem is KeyboardInputItemState<*>, "$lastItem not of expected type")
        assertNull(lastItem.currentAnswer)

        // Check expectations for the result and answer type.
        assertNull(questionState.currentResult.jsonValue)
        assertEquals(question.answerType, questionState.currentResult.answerType)
        assertEquals(question.answerType, questionState.answerType)
    }

    @Test
    fun testQuestionStateImpl_Init_ComboBoxQuestion_PreviousResult_SingleAnswer() {
        val item1 = ChoiceOptionObject(JsonPrimitive("item1"))
        val item2 = ChoiceOptionObject(JsonPrimitive("item2"))
        val item3 = ChoiceOptionObject(JsonPrimitive("item3"))
        val question = ComboBoxQuestionObject("foo", listOf(item1, item2, item3), singleAnswer = true)
        val previousResult = AnswerResultObject("foo", AnswerType.STRING, JsonPrimitive("item2"))
        val questionState = buildQuestionState(question, previousResult)

        // Check expectations for the initial item state.
        assertEquals(4, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertTrue(firstItem is ChoiceInputItemState, "$firstItem not of expected type")
        assertFalse(firstItem.selected)
        val secondItem = questionState.itemStates[1]
        assertTrue(secondItem is ChoiceInputItemState, "$secondItem not of expected type")
        assertTrue(secondItem.selected)
        val thirdItem = questionState.itemStates[2]
        assertTrue(thirdItem is ChoiceInputItemState, "$thirdItem not of expected type")
        assertFalse(thirdItem.selected)
        val lastItem = questionState.itemStates.last()
        assertTrue(lastItem is KeyboardInputItemState<*>, "$lastItem not of expected type")
        assertNull(lastItem.currentAnswer)

        // Check expectations for the result and answer type.
        assertEquals(previousResult, questionState.currentResult)
    }

    @Test
    fun testQuestionStateImpl_Init_ComboBoxQuestion_PreviousResult_SingleAnswer_Other() {
        val item1 = ChoiceOptionObject(JsonPrimitive("item1"))
        val item2 = ChoiceOptionObject(JsonPrimitive("item2"))
        val item3 = ChoiceOptionObject(JsonPrimitive("item3"))
        val question = ComboBoxQuestionObject("foo", listOf(item1, item2, item3), singleAnswer = true)
        val jsonValue = JsonPrimitive("ragu")
        val previousResult = AnswerResultObject("foo", AnswerType.STRING, jsonValue)
        val questionState = buildQuestionState(question, previousResult)

        // Check expectations for the initial item state.
        assertEquals(4, questionState.itemStates.count())
        questionState.itemStates.dropLast(1).forEach {
            assertTrue(it is ChoiceInputItemState, "$it not of expected type")
            assertFalse(it.selected)
        }
        val lastItem = questionState.itemStates.last()
        assertTrue(lastItem is KeyboardInputItemState<*>, "$lastItem not of expected type")
        assertEquals(jsonValue, lastItem.currentAnswer)

        // Check expectations for the result and answer type.
        assertEquals(previousResult, questionState.currentResult)
    }


    @Test
    fun testQuestionStateImpl_Init_ComboBoxQuestion_PreviousResult_MultipleAnswer() {
        val item1 = ChoiceOptionObject(JsonPrimitive("item1"))
        val item2 = ChoiceOptionObject(JsonPrimitive("item2"))
        val item3 = ChoiceOptionObject(JsonPrimitive("item3"))
        val question = ComboBoxQuestionObject("foo", listOf(item1, item2, item3), singleAnswer = false)
        val previousResult = AnswerResultObject("foo",
                answerType = AnswerType.List(BaseType.STRING),
                jsonValue = JsonArray(listOf(JsonPrimitive("item2"))))
        val questionState = buildQuestionState(question, previousResult)

        // Check expectations for the initial item state.
        assertEquals(4, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertTrue(firstItem is ChoiceInputItemState, "$firstItem not of expected type")
        assertFalse(firstItem.selected)
        val secondItem = questionState.itemStates[1]
        assertTrue(secondItem is ChoiceInputItemState, "$secondItem not of expected type")
        assertTrue(secondItem.selected)
        val thirdItem = questionState.itemStates[2]
        assertTrue(thirdItem is ChoiceInputItemState, "$thirdItem not of expected type")
        assertFalse(thirdItem.selected)
        val lastItem = questionState.itemStates.last()
        assertTrue(lastItem is KeyboardInputItemState<*>, "$lastItem not of expected type")
        assertNull(lastItem.currentAnswer)

        // Check expectations for the result and answer type.
        assertEquals(previousResult, questionState.currentResult)
    }

    @Test
    fun testQuestionStateImpl_Init_ComboBoxQuestion_PreviousResult_MultipleAnswer_Other() {
        val item1 = ChoiceOptionObject(JsonPrimitive("item1"))
        val item2 = ChoiceOptionObject(JsonPrimitive("item2"))
        val item3 = ChoiceOptionObject(JsonPrimitive("item3"))
        val question = ComboBoxQuestionObject("foo", listOf(item1, item2, item3), singleAnswer = false)
        val otherAnswer = JsonPrimitive("ragu")
        val previousResult = AnswerResultObject("foo",
                answerType = AnswerType.List(BaseType.STRING),
                jsonValue = JsonArray(listOf(JsonPrimitive("item2"), otherAnswer)))
        val questionState = buildQuestionState(question, previousResult)

        // Check expectations for the initial item state.
        assertEquals(4, questionState.itemStates.count())
        val firstItem = questionState.itemStates.first()
        assertTrue(firstItem is ChoiceInputItemState, "$firstItem not of expected type")
        assertFalse(firstItem.selected)
        val secondItem = questionState.itemStates[1]
        assertTrue(secondItem is ChoiceInputItemState, "$secondItem not of expected type")
        assertTrue(secondItem.selected)
        val thirdItem = questionState.itemStates[2]
        assertTrue(thirdItem is ChoiceInputItemState, "$thirdItem not of expected type")
        assertFalse(thirdItem.selected)
        val lastItem = questionState.itemStates.last()
        assertTrue(lastItem is KeyboardInputItemState<*>, "$lastItem not of expected type")
        assertEquals(otherAnswer, lastItem.currentAnswer)

        // Check expectations for the result and answer type.
        assertEquals(previousResult, questionState.currentResult)
    }

    /**
     * Helper methods
     */

    fun buildQuestionState(question: Question, previousResult: AnswerResult? = null): QuestionState {
        val nodeList = buildNodeList(3, 1, "step").toList()
        val assessmentObject = AssessmentObject("parent", nodeList)
        val result = buildResult(assessmentObject, 2)
        if (previousResult != null) {
            result.pathHistoryResults.plusAssign(previousResult)
            result.pathHistoryResults.plusAssign(ResultObject("step3"))
        }
        val parent = TestBranchNodeStateImpl(assessmentObject, result)
        return QuestionStateImpl(question, parent)
    }

    class TestBranchNodeStateImpl(node: BranchNode, result: BranchNodeResult) : BranchNodeStateImpl(node) {
        override val currentResult: BranchNodeResult = result
    }
}