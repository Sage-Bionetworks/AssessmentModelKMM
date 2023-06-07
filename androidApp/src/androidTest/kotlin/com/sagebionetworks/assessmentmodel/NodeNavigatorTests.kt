package com.sagebionetworks.assessmentmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.sagebionetworks.assessmentmodel.navigation.BranchNodeState
import org.sagebionetworks.assessmentmodel.navigation.LeafNodeState
import org.sagebionetworks.assessmentmodel.presentation.AssessmentViewModel
import org.sagebionetworks.assessmentmodel.presentation.RootAssessmentViewModel
import org.sagebionetworks.assessmentmodel.serialization.AssessmentInfoObject
import org.sagebionetworks.assessmentmodel.serialization.AssessmentPlaceholderObject
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class NodeNavigatorTests: KoinComponent {

    lateinit var viewModel: RootAssessmentViewModel
    lateinit var assessmentViewModel: AssessmentViewModel

    // First level within assessments for linear navigation
    lateinit var nodeState: BranchNodeState

    // Second level within assessments in wrapped assessments/non-linear navigation
    lateinit var branchNodeState: BranchNodeState

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    /**
     * Test the linear assessment: survey_a
     */

    @Test
    fun testFullInstructions_surveyA_showFullInstructions() {
        initializeViewModel("survey_a")
        startAndAssert("overview")

        // Test that default showFullInstructions flag in the nodeStates blocks skipping instructions
        goForward(3)
        assertEquals("step2", nodeState.currentChild?.node?.identifier)
    }

    @Test
    fun testFullInstructions_surveyA_doNotShowFullInstructions() {
        initializeViewModel("survey_a")
        startAndAssert("overview")

        // Test that assessments skip instructions when an OptionalStep's fullInstructionsOnly = true,
        // and showFullInstructions = false
        nodeState.showFullInstructions = false
        goForward(1)
        assertEquals("choiceQ1", nodeState.currentChild?.node?.identifier)
    }

    @Test
    fun testFullInstructions_surveyA_reviewInstructions() {
        initializeViewModel("survey_a")
        startAndAssert("overview")

        nodeState.showFullInstructions = false

        goForward(1)
        assertEquals("choiceQ1", nodeState.currentChild?.node?.identifier)
        skip(4)
        assertEquals("completion", nodeState.currentChild?.node?.identifier)

        // Test that reviewInstructions sends assessment to the beginning, and that
        // all instruction steps are subsequently navigated to regardless of fullInstructionsOnly flag
        assessmentViewModel.reviewInstructions()
        assertEquals("overview", nodeState.currentChild?.node?.identifier)
        goForward(3)
        assertEquals("step2", nodeState.currentChild?.node?.identifier)
    }

    /**
     * Test the nested assessment: sample_wrapper_assessment
     */

    @Test
    fun testFullInstructions_sampleWrapperAssessment_showFullInstructions() {
        initializeViewModel("sampleWrapperId")
        startAndAssert("addedStep1")

        // Test that default showFullInstructions flag in the nodeStates blocks skipping instructions
        goForward(3)
        branchNodeState = nodeState.currentChild as BranchNodeState
        assertEquals("feeling", branchNodeState.currentChild?.node?.identifier)
    }

    @Test
    fun testFullInstructions_sampleWrapperAssessment_doNotShowInstructions() {
        initializeViewModel("sampleWrapperId")
        startAndAssert("addedStep1")

        // Test that assessments skip instructions when an OptionalStep's fullInstructionsOnly = true,
        // and showFullInstructions = false
        goForward(1)
        branchNodeState = nodeState.currentChild as BranchNodeState
        branchNodeState.showFullInstructions = false
        goForward(1)
        assertEquals("feeling", branchNodeState.currentChild?.node?.identifier)
    }

    @Test
    fun testFullInstructions_sampleWrapperAssessment_reviewInstructions() {
        initializeViewModel("sampleWrapperId")
        startAndAssert("addedStep1")

        goForward(3)
        branchNodeState = nodeState.currentChild as BranchNodeState
        skip(7)
        assertEquals("completion", branchNodeState.currentChild?.node?.identifier)

        // Test that reviewInstructions sends assessment to the beginning, and that
        // all instruction steps are subsequently navigated to regardless of fullInstructionsOnly flag
        assessmentViewModel.reviewInstructions()
        val newNodeState = nodeState.currentChild as LeafNodeState
        assertEquals("addedStep1", newNodeState.node.identifier)

        goForward(1)
        val newBranchNodeState = nodeState.currentChild as BranchNodeState
        assertEquals("step1", newBranchNodeState.currentChild?.node?.identifier)
        goForward(1)
        assertEquals("step2", newBranchNodeState.currentChild?.node?.identifier)
        goForward(1)
        assertEquals("feeling", newBranchNodeState.currentChild?.node?.identifier)
    }

    private fun initializeViewModel(assessmentId: String) {
        val assessmentInfo = AssessmentInfoObject(assessmentId)
        viewModel = RootAssessmentViewModel(
            AssessmentPlaceholderObject(assessmentInfo.identifier, assessmentInfo),
            get(),
            get()
        )
        val branchNodeStateTemp = getValue(viewModel.assessmentLoadedLiveData)
        Assert.assertNotNull(branchNodeStateTemp)
        branchNodeStateTemp?.rootNodeController = viewModel

        assessmentViewModel = AssessmentViewModel(branchNodeStateTemp!!)
        nodeState = assessmentViewModel.assessmentNodeState
    }

    private fun startAndAssert(expectedStartId: String) {
        assessmentViewModel.start()
        assertEquals(expectedStartId, nodeState.currentChild?.node?.identifier)
    }

    private fun goForward(count: Int) {
        for (ii in 0 until count) {
            assessmentViewModel.goForward()
        }
    }

    private fun skip(count: Int) {
        for (ii in 0 until count) {
            assessmentViewModel.skip()
        }
    }

}

@Throws(InterruptedException::class)
internal fun <T> getValue(liveData: LiveData<T>): T? {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(changedData: T?) {
            data = changedData
            latch.countDown()
            liveData.removeObserver(this)
        }
    }
    liveData.observeForever(observer)
    latch.await(2, TimeUnit.SECONDS)
    liveData.removeObserver(observer)
    return data
}