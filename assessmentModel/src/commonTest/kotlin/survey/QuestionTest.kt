package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.json.*
import org.sagebionetworks.assessmentmodel.AnswerResult
import org.sagebionetworks.assessmentmodel.BranchNode
import org.sagebionetworks.assessmentmodel.BranchNodeResult
import org.sagebionetworks.assessmentmodel.navigation.BranchNodeStateImpl
import org.sagebionetworks.assessmentmodel.navigation.NavigationTestHelper
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
        assertEquals(original.answerType, result.answerType)
    }

    @Test
    fun testChoiceQuestion_Result_WithResultId() {
        val original = ChoiceQuestionObject("foo", listOf(), resultIdentifier = "bar")
        val result = original.createResult()
        assertEquals("bar", result.identifier)
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
        assertEquals(AnswerType.Array(BaseType.INTEGER), original.answerType)
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
        assertEquals(original.answerType, result.answerType)
    }

    @Test
    fun testComboBoxQuestion_Result_WithResultId() {
        val original = ComboBoxQuestionObject("foo", listOf(), resultIdentifier = "bar")
        val result = original.createResult()
        assertEquals("bar", result.identifier)
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
        assertEquals(AnswerType.Array(BaseType.INTEGER), original.answerType)
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
        assertEquals(original.answerType, result.answerType)
    }

    @Test
    fun testMultipleInputQuestion_Result_WithResultId() {
        val original = MultipleInputQuestionObject("foo", inputItems = listOf(StringTextInputItemObject(), StringTextInputItemObject()), resultIdentifier = "bar")
        val result = original.createResult()
        assertEquals("bar", result.identifier)
        assertEquals(original.answerType, result.answerType)
    }

    @Test
    fun testMultipleInputQuestion_AnswerType_WithIndentifiers() {
        val item1 = StringTextInputItemObject("foo")
        val item2 = StringTextInputItemObject("bar")
        val original = MultipleInputQuestionObject("foo", inputItems = listOf(item1, item2))
        assertEquals(AnswerType.OBJECT, original.answerType)
    }

    @Test
    fun testMultipleInputQuestion_AnswerType_NoIdentifiers() {
        val item1 = StringTextInputItemObject()
        val item2 = StringTextInputItemObject()
        val original = MultipleInputQuestionObject("foo", inputItems = listOf(item1, item2))
        assertEquals(AnswerType.OBJECT, original.answerType)
    }

    @Test
    fun testMultipleInputQuestion_AnswerType_MAP_WithSkipCheckbox() {
        val item1 = StringTextInputItemObject("foo")
        val item2 = StringTextInputItemObject("bar")
        val original = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2),
                skipCheckbox = SkipCheckboxInputItemObject("no answer"))
        assertEquals(AnswerType.OBJECT, original.answerType)
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
        assertEquals(original.answerType, result.answerType)
    }

    @Test
    fun testSimpleQuestion_Result_WithResultId() {
        val original = SimpleQuestionObject("foo", inputItem = StringTextInputItemObject(), resultIdentifier = "bar")
        val result = original.createResult()
        assertEquals("bar", result.identifier)
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
        assertEquals(AnswerType.OBJECT, questionState.currentResult.answerType)
        assertEquals(AnswerType.OBJECT, questionState.answerType)
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
        assertEquals(AnswerType.OBJECT, question.answerType)
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
        assertEquals(AnswerType.OBJECT, question.answerType)
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
        assertEquals(AnswerType.OBJECT, question.answerType)
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
        assertEquals(AnswerType.OBJECT, question.answerType)
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
                answerType = AnswerType.Array(BaseType.STRING),
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
                answerType = AnswerType.Array(BaseType.STRING),
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
                answerType = AnswerType.Array(BaseType.STRING),
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
     * QuestionStateImpl - saveAnswer
     */

    @Test
    fun testQuestionStateImpl_SaveAnswer_SimpleQuestion_NoSkipCheckbox() {
        val item1 = StringTextInputItemObject()
        val question = SimpleQuestionObject("foo",
                inputItem = item1)
        val questionState = buildQuestionState(question)

        val jsonValue = JsonPrimitive("baloon")
        val firstItem = questionState.itemStates.first()
        val refresh = questionState.saveAnswer(jsonValue, firstItem)

        assertFalse(refresh)
        assertEquals(jsonValue, firstItem.currentAnswer)
        assertEquals(jsonValue, questionState.currentResult.jsonValue)
    }

    @Test
    fun testQuestionStateImpl_SaveAnswer_MultipleInputQuestion_NoSkipCheckbox() {
        val item1 = StringTextInputItemObject("item1")
        val item2 = StringTextInputItemObject("item2")
        val question = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2))
        val questionState = buildQuestionState(question)

        val jsonValue1 = JsonPrimitive("baloon")
        val firstItem = questionState.itemStates.first()
        val refresh1 = questionState.saveAnswer(jsonValue1, firstItem)

        val map = mutableMapOf<String, JsonElement>("item1" to jsonValue1)

        assertFalse(refresh1)
        assertEquals(jsonValue1, firstItem.currentAnswer)
        assertEquals(JsonObject(map), questionState.currentResult.jsonValue)

        val jsonValue2 = JsonPrimitive("ragu")
        map["item2"] = jsonValue2
        val lastItem = questionState.itemStates.last()
        val refresh2 = questionState.saveAnswer(jsonValue2, lastItem)

        assertFalse(refresh2)
        assertEquals(jsonValue2, lastItem.currentAnswer)
        assertEquals(JsonObject(map), questionState.currentResult.jsonValue)

        val jsonValue1B = JsonPrimitive("moo")
        map["item1"] = jsonValue1B
        val refresh1B = questionState.saveAnswer(jsonValue1B, firstItem)

        assertFalse(refresh1B)
        assertEquals(jsonValue1B, firstItem.currentAnswer)
        assertEquals(JsonObject(map), questionState.currentResult.jsonValue)
    }

    @Test
    fun testQuestionStateImpl_SaveAnswer_MultipleInputQuestion_WithSkipCheckbox() {
        val item1 = StringTextInputItemObject("item1")
        val item2 = StringTextInputItemObject("item2")
        val checkbox = SkipCheckboxInputItemObject("no answer")
        val question = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2),
                skipCheckbox = checkbox)
        val jsonValue = JsonNull
        assertEquals(AnswerType.OBJECT, question.answerType)
        val previousResult = AnswerResultObject("foo", question.answerType, jsonValue)
        val questionState = buildQuestionState(question, previousResult)

        val jsonValue1 = JsonPrimitive("baloon")
        val firstItem = questionState.itemStates.first()
        val refresh = questionState.saveAnswer(jsonValue1, firstItem)

        assertTrue(refresh)
        assertEquals(jsonValue1, firstItem.currentAnswer)
        assertEquals(JsonObject(mapOf("item1" to jsonValue1)), questionState.currentResult.jsonValue)

        val lastItem = questionState.itemStates.last()
        assertTrue(lastItem is ChoiceInputItemState, "$lastItem not of expected type")
        assertFalse(lastItem.selected)
    }

    /**
     * QuestionStateImpl - didChangeSelectionState
     */

    @Test
    fun testQuestionStateImpl_DidChangeSelectionState_SimpleQuestion_WithSkipCheckbox() {
        val item1 = StringTextInputItemObject("item1")
        val checkbox = SkipCheckboxInputItemObject("no answer")
        val question = SimpleQuestionObject("foo",
                inputItem = item1,
                skipCheckbox = checkbox)
        val jsonValue1 = JsonPrimitive("baloon")
        val previousResult = AnswerResultObject("foo", question.answerType, jsonValue1)
        val questionState = buildQuestionState(question, previousResult)

        val firstItem = questionState.itemStates.first() as KeyboardInputItemState<*>
        val lastItem = questionState.itemStates.last() as ChoiceInputItemState
        val refresh1 = questionState.didChangeSelectionState(true, lastItem)

        assertTrue(refresh1)
        assertTrue(lastItem.selected)
        assertNull(firstItem.currentAnswer)
        assertEquals(jsonValue1, firstItem.storedAnswer)
        assertFalse(firstItem.selected)
        assertEquals(JsonNull, questionState.currentResult.jsonValue)

        questionState.didChangeSelectionState(false, lastItem)

        assertFalse(lastItem.selected)
        assertEquals(jsonValue1, firstItem.currentAnswer)
        assertEquals(jsonValue1, firstItem.storedAnswer)
        assertTrue(firstItem.selected)
        assertEquals(jsonValue1, questionState.currentResult.jsonValue)
    }

    @Test
    fun testQuestionStateImpl_DidChangeSelectionState_MultipleInputQuestion_WithSkipCheckbox() {
        val item1 = StringTextInputItemObject("item1")
        val item2 = StringTextInputItemObject("item2")
        val checkbox = SkipCheckboxInputItemObject("no answer")
        val question = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2),
                skipCheckbox = checkbox)
        val jsonValue1 = JsonPrimitive("baloon")
        val initialValue = JsonObject(mapOf("item1" to jsonValue1))
        val previousResult = AnswerResultObject("foo", question.answerType, initialValue)
        val questionState = buildQuestionState(question, previousResult)

        val firstItem = questionState.itemStates.first() as KeyboardInputItemState<*>
        val lastItem = questionState.itemStates.last() as ChoiceInputItemState
        val refresh1 = questionState.didChangeSelectionState(true, lastItem)

        assertTrue(refresh1)
        assertTrue(lastItem.selected)
        assertNull(firstItem.currentAnswer)
        assertEquals(jsonValue1, firstItem.storedAnswer)
        assertFalse(firstItem.selected)
        assertEquals(JsonNull, questionState.currentResult.jsonValue)

        questionState.didChangeSelectionState(false, lastItem)

        assertFalse(lastItem.selected)
        assertEquals(jsonValue1, firstItem.currentAnswer)
        assertEquals(jsonValue1, firstItem.storedAnswer)
        assertTrue(firstItem.selected)
        assertEquals(initialValue, questionState.currentResult.jsonValue)
    }

    @Test
    fun testQuestionStateImpl_DidChangeSelectionState_ChoiceQuestion_Single() {
        val item1 = ChoiceOptionObject(JsonPrimitive("item1"))
        val item2 = ChoiceOptionObject(JsonPrimitive("item2"))
        val item3 = ChoiceOptionObject(JsonPrimitive("item3"))
        val question = ChoiceQuestionObject("foo", listOf(item1, item2, item3), singleAnswer = true)
        val questionState = buildQuestionState(question)

        val firstItem = questionState.itemStates.first() as ChoiceInputItemState
        val secondItem = questionState.itemStates[1] as ChoiceInputItemState
        val thirdItem = questionState.itemStates.last() as ChoiceInputItemState

        val refresh1 = questionState.didChangeSelectionState(true, thirdItem)
        assertFalse(refresh1)
        assertFalse(firstItem.selected)
        assertFalse(secondItem.selected)
        assertTrue(thirdItem.selected)
        assertEquals(JsonPrimitive("item3"), questionState.currentResult.jsonValue)

        val refresh2 = questionState.didChangeSelectionState(true, secondItem)
        assertTrue(refresh2)
        assertFalse(firstItem.selected)
        assertTrue(secondItem.selected)
        assertFalse(thirdItem.selected)
        assertEquals(JsonPrimitive("item2"), questionState.currentResult.jsonValue)

        val refresh3 = questionState.didChangeSelectionState(false, secondItem)
        assertFalse(refresh3)
        assertFalse(firstItem.selected)
        assertFalse(secondItem.selected)
        assertFalse(thirdItem.selected)
        assertEquals(null, questionState.currentResult.jsonValue)
    }

    @Test
    fun testQuestionStateImpl_DidChangeSelectionState_ChoiceQuestion_Multiple() {
        val item1 = ChoiceOptionObject(JsonPrimitive("item1"))
        val item2 = ChoiceOptionObject(JsonPrimitive("item2"))
        val item3 = ChoiceOptionObject(JsonPrimitive("item3"))
        val itemNone = ChoiceOptionObject(exclusive = true)
        val question = ChoiceQuestionObject("foo", listOf(item1, item2, item3, itemNone), singleAnswer = false)
        val questionState = buildQuestionState(question)

        val firstItem = questionState.itemStates.first() as ChoiceInputItemState
        val secondItem = questionState.itemStates[1] as ChoiceInputItemState
        val thirdItem = questionState.itemStates[2] as ChoiceInputItemState
        val noneItem = questionState.itemStates.last() as ChoiceInputItemState

        val refresh1 = questionState.didChangeSelectionState(true, thirdItem)
        assertFalse(refresh1)
        assertFalse(firstItem.selected)
        assertFalse(secondItem.selected)
        assertTrue(thirdItem.selected)
        assertFalse(noneItem.selected)
        val result1 = questionState.currentResult.jsonValue
        assertTrue(result1 is JsonArray)
        assertEquals(setOf(JsonPrimitive("item3")), result1.toSet())

        val refresh2 = questionState.didChangeSelectionState(true, secondItem)
        assertFalse(refresh2)
        assertFalse(firstItem.selected)
        assertTrue(secondItem.selected)
        assertTrue(thirdItem.selected)
        assertFalse(noneItem.selected)
        val result2 = questionState.currentResult.jsonValue
        assertTrue(result2 is JsonArray)
        assertEquals(setOf(JsonPrimitive("item3"), JsonPrimitive("item2")), result2.toSet())

        val refresh3 = questionState.didChangeSelectionState(false, secondItem)
        assertFalse(refresh3)
        assertFalse(firstItem.selected)
        assertFalse(secondItem.selected)
        assertTrue(thirdItem.selected)
        assertFalse(noneItem.selected)
        val result3 = questionState.currentResult.jsonValue
        assertTrue(result3 is JsonArray)
        assertEquals(setOf(JsonPrimitive("item3")), result3.toSet())

        val refresh4 = questionState.didChangeSelectionState(true, noneItem)
        assertTrue(refresh4)
        assertFalse(firstItem.selected)
        assertFalse(secondItem.selected)
        assertFalse(thirdItem.selected)
        assertTrue(noneItem.selected)
        assertEquals(JsonArray(listOf()), questionState.currentResult.jsonValue)
    }

    @Test
    fun testQuestionStateImpl_DidChangeSelectionState_ComboBoxQuestion_Single() {
        val item1 = ChoiceOptionObject(JsonPrimitive("item1"))
        val item2 = ChoiceOptionObject(JsonPrimitive("item2"))
        val item3 = ChoiceOptionObject(JsonPrimitive("item3"))
        val question = ComboBoxQuestionObject("foo", listOf(item1, item2, item3), singleAnswer = true)
        val questionState = buildQuestionState(question)

        val firstItem = questionState.itemStates.first() as ChoiceInputItemState
        val secondItem = questionState.itemStates[1] as ChoiceInputItemState
        val thirdItem = questionState.itemStates[2] as ChoiceInputItemState
        val otherItem = questionState.itemStates.last() as KeyboardInputItemState<*>

        val refresh1 = questionState.didChangeSelectionState(true, thirdItem)
        assertFalse(refresh1)
        assertFalse(firstItem.selected)
        assertFalse(secondItem.selected)
        assertTrue(thirdItem.selected)
        assertFalse(otherItem.selected)
        assertEquals(JsonPrimitive("item3"), questionState.currentResult.jsonValue)

        val refresh2 = questionState.didChangeSelectionState(true, secondItem)
        assertTrue(refresh2)
        assertFalse(firstItem.selected)
        assertTrue(secondItem.selected)
        assertFalse(thirdItem.selected)
        assertFalse(otherItem.selected)
        assertEquals(JsonPrimitive("item2"), questionState.currentResult.jsonValue)

        val refresh3 = questionState.didChangeSelectionState(false, secondItem)
        assertFalse(refresh3)
        assertFalse(firstItem.selected)
        assertFalse(secondItem.selected)
        assertFalse(thirdItem.selected)
        assertFalse(otherItem.selected)
        assertEquals(null, questionState.currentResult.jsonValue)

        val refresh4 = questionState.saveAnswer(JsonPrimitive("baloo"), otherItem)
        assertFalse(refresh4)
        assertFalse(firstItem.selected)
        assertFalse(secondItem.selected)
        assertFalse(thirdItem.selected)
        assertTrue(otherItem.selected)
        assertEquals(JsonPrimitive("baloo"), questionState.currentResult.jsonValue)
    }

    @Test
    fun testQuestionStateImpl_DidChangeSelectionState_ComboBoxQuestion_Multiple() {
        val item1 = ChoiceOptionObject(JsonPrimitive("item1"))
        val item2 = ChoiceOptionObject(JsonPrimitive("item2"))
        val item3 = ChoiceOptionObject(JsonPrimitive("item3"))
        val question = ComboBoxQuestionObject("foo", listOf(item1, item2, item3), singleAnswer = false)
        val questionState = buildQuestionState(question)

        val firstItem = questionState.itemStates.first() as ChoiceInputItemState
        val secondItem = questionState.itemStates[1] as ChoiceInputItemState
        val thirdItem = questionState.itemStates[2] as ChoiceInputItemState
        val otherItem = questionState.itemStates.last() as KeyboardInputItemState<*>

        val refresh1 = questionState.didChangeSelectionState(true, thirdItem)
        assertFalse(refresh1)
        assertFalse(firstItem.selected)
        assertFalse(secondItem.selected)
        assertTrue(thirdItem.selected)
        assertFalse(otherItem.selected)
        val result1 = questionState.currentResult.jsonValue
        assertTrue(result1 is JsonArray)
        assertEquals(setOf(JsonPrimitive("item3")), result1.toSet())

        val refresh2 = questionState.didChangeSelectionState(true, secondItem)
        assertFalse(refresh2)
        assertFalse(firstItem.selected)
        assertTrue(secondItem.selected)
        assertTrue(thirdItem.selected)
        assertFalse(otherItem.selected)
        val result2 = questionState.currentResult.jsonValue
        assertTrue(result2 is JsonArray)
        assertEquals(setOf(JsonPrimitive("item3"), JsonPrimitive("item2")), result2.toSet())

        val refresh3 = questionState.didChangeSelectionState(false, secondItem)
        assertFalse(refresh3)
        assertFalse(firstItem.selected)
        assertFalse(secondItem.selected)
        assertTrue(thirdItem.selected)
        assertFalse(otherItem.selected)
        val result3 = questionState.currentResult.jsonValue
        assertTrue(result3 is JsonArray)
        assertEquals(setOf(JsonPrimitive("item3")), result3.toSet())

        val refresh4 = questionState.saveAnswer(JsonPrimitive("baloo"), otherItem)
        assertFalse(refresh4)
        assertFalse(firstItem.selected)
        assertFalse(secondItem.selected)
        assertTrue(thirdItem.selected)
        assertTrue(otherItem.selected)
        val result4 = questionState.currentResult.jsonValue
        assertTrue(result4 is JsonArray)
        assertEquals(setOf(JsonPrimitive("item3"),JsonPrimitive("baloo")), result4.toSet())
    }

    /**
     * QuestionStateImpl - allAnswersValid
     */

    @Test
    fun testQuestionStateImpl_AllAnswersValid_SimpleQuestion_NotOptional_Valid() {
        val item1 = StringTextInputItemObject()
        val question = SimpleQuestionObject("foo", inputItem = item1)
        question.optional = false
        val jsonValue = JsonPrimitive("baloo")
        val previousResult = AnswerResultObject("foo", question.answerType, jsonValue)
        val questionState = buildQuestionState(question, previousResult)
        assertTrue(questionState.allAnswersValid())
    }

    @Test
    fun testQuestionStateImpl_AllAnswersValid_SimpleQuestion_NotOptional_Skip() {
        val item1 = StringTextInputItemObject()
        val question = SimpleQuestionObject("foo", inputItem = item1, skipCheckbox = SkipCheckboxInputItemObject("skip"))
        question.optional = false
        val jsonValue = JsonNull
        val previousResult = AnswerResultObject("foo", question.answerType, jsonValue)
        val questionState = buildQuestionState(question, previousResult)
        assertTrue(questionState.allAnswersValid())
    }

    @Test
    fun testQuestionStateImpl_AllAnswersValid_SimpleQuestion_NotOptional_Null() {
        val item1 = StringTextInputItemObject()
        val question = SimpleQuestionObject("foo", inputItem = item1)
        question.optional = false
        val questionState = buildQuestionState(question)
        assertFalse(questionState.allAnswersValid())
    }

    @Test
    fun testQuestionStateImpl_AllAnswersValid_SimpleQuestion_Optional_Null() {
        val item1 = StringTextInputItemObject()
        val question = SimpleQuestionObject("foo", inputItem = item1)
        question.optional = true
        val questionState = buildQuestionState(question)
        assertTrue(questionState.allAnswersValid())
    }

    @Test
    fun testQuestionStateImpl_AllAnswersValid_MultipleInputQuestion_WithSkipCheckbox_Checked() {
        val item1 = StringTextInputItemObject("item1")
        item1.optional = false
        val item2 = StringTextInputItemObject("item2")
        val checkbox = SkipCheckboxInputItemObject("no answer")
        val question = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2),
                skipCheckbox = checkbox)
        question.optional = false
        val jsonValue = JsonNull
        assertEquals(AnswerType.OBJECT, question.answerType)
        val previousResult = AnswerResultObject("foo", question.answerType, jsonValue)
        val questionState = buildQuestionState(question, previousResult)
        assertTrue(questionState.allAnswersValid())
    }

    @Test
    fun testQuestionStateImpl_AllAnswersValid_MultipleInputQuestion_WithRequired() {
        val item1 = StringTextInputItemObject("item1")
        item1.optional = false
        val item2 = StringTextInputItemObject("item2")
        val checkbox = SkipCheckboxInputItemObject("no answer")
        val question = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2),
                skipCheckbox = checkbox)
        question.optional = false
        val jsonValue = JsonObject(mapOf("item1" to JsonPrimitive("baloo")))
        val previousResult = AnswerResultObject("foo", question.answerType, jsonValue)
        val questionState = buildQuestionState(question, previousResult)
        assertTrue(questionState.allAnswersValid())
    }

    @Test
    fun testQuestionStateImpl_AllAnswersValid_MultipleInputQuestion_WithoutRequired() {
        val item1 = StringTextInputItemObject("item1")
        item1.optional = false
        val item2 = StringTextInputItemObject("item2")
        val checkbox = SkipCheckboxInputItemObject("no answer")
        val question = MultipleInputQuestionObject("foo",
                inputItems = listOf(item1, item2),
                skipCheckbox = checkbox)
        question.optional = false
        val jsonValue = JsonObject(mapOf("item2" to JsonPrimitive("baloo")))
        val previousResult = AnswerResultObject("foo", question.answerType, jsonValue)
        val questionState = buildQuestionState(question, previousResult)
        assertFalse(questionState.allAnswersValid())
    }

    /**
     * Helper methods
     */

    private fun buildQuestionState(question: Question, previousResult: AnswerResult? = null): QuestionState {
        val nodeList = buildNodeList(3, 1, "step").plus(question).toList()
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