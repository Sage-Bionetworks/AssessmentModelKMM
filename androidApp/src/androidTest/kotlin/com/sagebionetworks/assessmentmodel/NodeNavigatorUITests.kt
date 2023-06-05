package com.sagebionetworks.assessmentmodel

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import junit.framework.TestCase.*
import org.junit.Rule
import org.junit.Test
import org.sagebionetworks.assessmentmodel.navigation.BranchNodeState
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.AssessmentActivity
import org.sagebionetworks.assessmentmodel.sampleapp.ContainerActivity

class NodeNavigatorUITests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ContainerActivity>()
    private lateinit var currentActivity: AssessmentActivity
    private lateinit var assessmentViewModel: BranchNodeState
    private val sampleExpectedStepHistory = mutableListOf(
        "step1",
        "feeling",
        "favoriteColor",
        "otherChoice",
        "checkboxQuestion",
        "stringQuestion",
        "yearQuestion",
        "multipleInputQuestion",
        "completion"
    )
    private val surveyAExpectedStepHistory = mutableListOf(
        "overview",
        "choiceQ1",
        "followupQ",
        "favoriteFood",
        "multipleChoice",
        "completion"
    )
    lateinit var next: String
    lateinit var start: String
    lateinit var exit: String
    lateinit var pause: String
    lateinit var skipQuestion: String
    lateinit var reviewInstructions: String

    @Test
    fun testFullInstructions_sampleAssessment() {
        passThroughSampleAssessment(false)
        passThroughSampleAssessment(true)
    }

    @Test
    fun testFullInstructions_sampleWrapperAssessment() {
        passThroughWrapperAssessment(false)
        passThroughWrapperAssessment(true)
    }

    @Test
    fun testFullInstructions_surveyA() {
        passThroughSurveyA(false)
        passThroughSurveyA(true)
    }

    @Test
    fun testReviewInstructions_surveyA() {
        onView(withText("survey_a")).perform(click())
        setupActivityAndViewModel(false)

        navigateForward(start, 1)
        navigateForward(next, 0)
        navigateForward(skipQuestion, 3)

        val actualStepHistory = assessmentViewModel.currentResult.pathHistoryResults
        val surveyAExpectedStepHistory = mutableListOf(
            "overview",
            "choiceQ1",
            "followupQ",
            "favoriteFood"
        )
        compareStepHistory(actualStepHistory, surveyAExpectedStepHistory)

        composeTestRule.onNodeWithContentDescription(pause).performClick()
        composeTestRule.onNodeWithText(reviewInstructions).performClick()
        assertTrue(assessmentViewModel.showFullInstructions)

        navigateForward(start, 1)
        navigateForward(next, 3)
        navigateForward(skipQuestion, 4)

        insertIntoExpected(
            assessmentViewModel.showFullInstructions,
            surveyAExpectedStepHistory,
            listOf("completion", "multipleChoice", "favoriteFood", "followupQ", "choiceQ1", "step2", "step1", "addedStep1", "overview"),
            4
        )
        compareStepHistory(actualStepHistory, surveyAExpectedStepHistory)
    }

    @Test
    fun testReviewInstructions_sampleWrapperAssessment() {
        onView(withText("sampleWrapperId")).perform(click())
        setupActivityAndViewModel(false)

        navigateForward(next, 1)

        navigateForward(next, 2)
        navigateForward(skipQuestion, 6)

        composeTestRule.onNodeWithContentDescription(pause).performClick()
        composeTestRule.onNodeWithText(reviewInstructions).performClick()
        assertEquals("addedStep1", assessmentViewModel.currentChild?.node?.identifier)
    }


    private fun passThroughSampleAssessment(showFullInstructions: Boolean) {
        onView(withText("sampleId")).perform(click())
        setupActivityAndViewModel(showFullInstructions)

        navigateForward(next, if (assessmentViewModel.showFullInstructions) 2 else 1)
        navigateForward(skipQuestion, 7)

        val actualStepHistory = assessmentViewModel.currentResult.pathHistoryResults
        insertIntoExpected(assessmentViewModel.showFullInstructions, sampleExpectedStepHistory, listOf("step2"), 1)
        compareStepHistory(actualStepHistory, sampleExpectedStepHistory)

        navigateForward(exit, 1)
    }

    private fun passThroughWrapperAssessment(showFullInstructions: Boolean) {
        onView(withText("sampleWrapperId")).perform(click())
        setupActivityAndViewModel(showFullInstructions)

        navigateForward(next, 1)

        // Use the child assessment's view model for this wrapped assessment case
        assessmentViewModel = assessmentViewModel.currentChild as BranchNodeState
        assessmentViewModel.showFullInstructions = showFullInstructions

        navigateForward(next, if (assessmentViewModel.showFullInstructions) 2  else 1)
        navigateForward(skipQuestion, 7)

        val actualStepHistory = assessmentViewModel.currentResult.pathHistoryResults
        insertIntoExpected(assessmentViewModel.showFullInstructions, sampleExpectedStepHistory, listOf("step2"), 1)
        compareStepHistory(actualStepHistory, sampleExpectedStepHistory)

        navigateForward(exit, 1)
    }

    private fun passThroughSurveyA(showFullInstructions: Boolean) {
        onView(withText("survey_a")).perform(click())

        setupActivityAndViewModel(showFullInstructions)

        navigateForward(start, 1)
        navigateForward(next, if (assessmentViewModel.showFullInstructions) 3 else 0)
        navigateForward(skipQuestion, 4)

        val actualStepHistory = assessmentViewModel.currentResult.pathHistoryResults
        insertIntoExpected(assessmentViewModel.showFullInstructions, surveyAExpectedStepHistory, listOf("step2", "step1", "addedStep1"), 1)
        compareStepHistory(actualStepHistory, surveyAExpectedStepHistory)

        navigateForward(exit, 1)
    }

    private fun compareStepHistory(actualStepHistory: MutableList<org.sagebionetworks.assessmentmodel.Result>, expectedStepHistory: MutableList<String>) {
        assertEquals(expectedStepHistory.size, actualStepHistory.size)
        for (ii in 0 until actualStepHistory.size) {
            assertEquals(expectedStepHistory[ii], actualStepHistory[ii].identifier)
        }
    }

    private fun navigateForward(buttonText: String, iterations: Int) {
        for (ii in 0 until iterations) {
            composeTestRule.onNodeWithText(buttonText)
                .assertExists()
                .performClick()
        }
    }

    private fun setupActivityAndViewModel(showFullInstructions: Boolean) {
        currentActivity = ActivityGetter.getActivityInstance() as AssessmentActivity
        assessmentViewModel = currentActivity.viewModel.assessmentNodeState as BranchNodeState
        assessmentViewModel.showFullInstructions = showFullInstructions

        // Setup strings after currentActivity is accessed
        next = getLocalizedString(R.string.next)
        start = getLocalizedString(R.string.start)
        exit = getLocalizedString(R.string.exit)
        pause = getLocalizedString(R.string.pause)
        skipQuestion = getLocalizedString(R.string.skip_question)
        reviewInstructions = getLocalizedString(R.string.review_instructions)
    }

    private fun getLocalizedString(resId: Int): String {
        return currentActivity.getString(resId)
    }

    private fun insertIntoExpected(
        showFullInstructions: Boolean,
        expectedStepHistory: MutableList<String>,
        stepsToAdd: List<String>,
        insertIndex: Int
    ) {
        if (showFullInstructions) {
            for (element in stepsToAdd) {
                expectedStepHistory.add(insertIndex, element)
            }
        }
    }

}