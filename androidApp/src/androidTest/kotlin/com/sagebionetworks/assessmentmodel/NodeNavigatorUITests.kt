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
    fun testFullInstructions_surveyA_reviewInstructions() {
        onView(withText("survey_a")).perform(click())
        setupActivityAndViewModel(false)

        navigateForward("Start", 1)
        navigateForward("Next", 0)
        navigateForward("Skip question", 3)

        val actualStepHistory = assessmentViewModel.currentResult.pathHistoryResults
        val surveyAExpectedStepHistory = mutableListOf(
            "overview",
            "choiceQ1",
            "followupQ",
            "favoriteFood"
        )
        compareStepHistory(actualStepHistory, surveyAExpectedStepHistory)

        // Navigate to step before completion and click "review instructions"
        composeTestRule.onNodeWithContentDescription("Pause").performClick()
        composeTestRule.onNodeWithText("Review Instructions").performClick()
        assertTrue(assessmentViewModel.showFullInstructions)

        navigateForward("Start", 1)
        navigateForward("Next", 3)
        navigateForward("Skip question", 4)

        insertIntoExpected(
            assessmentViewModel.showFullInstructions,
            surveyAExpectedStepHistory,
            listOf("completion", "multipleChoice", "favoriteFood", "followupQ", "choiceQ1", "step2", "step1", "addedStep1", "overview"),
            4
        )
        compareStepHistory(actualStepHistory, surveyAExpectedStepHistory)
    }


    private fun passThroughSampleAssessment(showFullInstructions: Boolean) {
        onView(withText("sampleId")).perform(click())
        setupActivityAndViewModel(showFullInstructions)

        navigateForward("Next", if (assessmentViewModel.showFullInstructions) 2 else 1)
        navigateForward("Skip question", 7)

        val actualStepHistory = assessmentViewModel.currentResult.pathHistoryResults
        insertIntoExpected(assessmentViewModel.showFullInstructions, sampleExpectedStepHistory, listOf("step2"), 1)
        compareStepHistory(actualStepHistory, sampleExpectedStepHistory)

        navigateForward("Exit", 1)
    }

    private fun passThroughWrapperAssessment(showFullInstructions: Boolean) {
        onView(withText("sampleWrapperId")).perform(click())
        setupActivityAndViewModel(showFullInstructions)

        navigateForward("Next", 1)

        // Use the child assessment's view model for this wrapped assessment case
        assessmentViewModel = assessmentViewModel.currentChild as BranchNodeState
        assessmentViewModel.showFullInstructions = showFullInstructions

        navigateForward("Next", if (assessmentViewModel.showFullInstructions) 2  else 1)
        navigateForward("Skip question", 7)

        val actualStepHistory = assessmentViewModel.currentResult.pathHistoryResults
        insertIntoExpected(assessmentViewModel.showFullInstructions, sampleExpectedStepHistory, listOf("step2"), 1)
        compareStepHistory(actualStepHistory, sampleExpectedStepHistory)

        navigateForward("Exit", 1)
    }

    private fun passThroughSurveyA(showFullInstructions: Boolean) {
        onView(withText("survey_a")).perform(click())

        setupActivityAndViewModel(showFullInstructions)

        navigateForward("Start", 1)
        navigateForward("Next", if (assessmentViewModel.showFullInstructions) 3 else 0)
        navigateForward("Skip question", 4)

        val actualStepHistory = assessmentViewModel.currentResult.pathHistoryResults
        insertIntoExpected(assessmentViewModel.showFullInstructions, surveyAExpectedStepHistory, listOf("step2", "step1", "addedStep1"), 1)
        compareStepHistory(actualStepHistory, surveyAExpectedStepHistory)

        navigateForward("Exit", 1)
    }

    private fun compareStepHistory(actualStepHistory: MutableList<org.sagebionetworks.assessmentmodel.Result>, expectedStepHistory: MutableList<String>) {
        assertEquals(actualStepHistory.size, expectedStepHistory.size)
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