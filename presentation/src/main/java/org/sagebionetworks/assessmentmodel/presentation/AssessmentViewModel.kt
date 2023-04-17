package org.sagebionetworks.assessmentmodel.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.*
import org.sagebionetworks.assessmentmodel.survey.ReservedNavigationIdentifier

open class AssessmentViewModel(
    val assessmentNodeState: BranchNodeState
) : ViewModel(), NodeUIController {

    protected val currentNodeStateMutableLiveData: MutableLiveData<ShowNodeState> = MutableLiveData()
    val currentNodeStateLiveData: LiveData<ShowNodeState> = currentNodeStateMutableLiveData

    fun start() {
        assessmentNodeState.nodeUIController = this
        if (assessmentNodeState.currentChild == null) {
            //If we don't yet have a current child then we need to start this assessment
            goForward()
        }
    }

    fun goForward() {
        assessmentNodeState.goForward()
    }

    fun goBackward() {
        assessmentNodeState.goBackward()
    }

    fun skip() {
        //Clear current results before going to next node
        (assessmentNodeState.currentChild?.currentResult as? AnswerResult)?.jsonValue = null
        goForward()
    }

    fun goToNode(nodeIdentifier: String) {
        var node: Node? = null
        if (ReservedNavigationIdentifier.Beginning.matching(nodeIdentifier)) {
            node = (assessmentNodeState.node as? NodeContainer)?.children?.firstOrNull()
        } else {
            node = (assessmentNodeState.node as? NodeContainer)?.children?.firstOrNull { it.identifier == nodeIdentifier }
        }
        node?.let {
            (assessmentNodeState as? BranchNodeStateImpl)?.moveTo(NavigationPoint(node, assessmentNodeState.currentResult))
        }
    }

    fun cancel() {
        assessmentNodeState.exitEarly(FinishedReason.Incomplete(saveResult = SaveResults.Never, markFinished = false, declined = false))
    }

    fun declineAssessment() {
        assessmentNodeState.exitEarly(FinishedReason.Incomplete(saveResult = SaveResults.Now, markFinished = false, declined = true))
    }

    override fun canHandle(node: Node): Boolean {
        return (node is Step)
    }

    override fun handleGoForward(
        nodeState: NodeState
    ) {
        //Update the LiveData stream with the new node
        currentNodeStateMutableLiveData.value =
            ShowNodeState(
                nodeState,
                Direction.Forward
            )
    }

    override fun handleGoBack(
        nodeState: NodeState
    ) {
        currentNodeStateMutableLiveData.value =
            ShowNodeState(
                nodeState,
                Direction.Backward
            )
    }

    /**
     * Data class for LiveData stream.
     */
    data class ShowNodeState(
        val nodeState: NodeState,
        val direction: Direction,
        var hasBeenHandled: Boolean = false
    )

}


/**
 * Factory for the AssesmentViewModel.
 *
 * Providing ViewModelProvider.Factory allows us to inject dependencies and pass parameters
 * to an instance since the Android framework controls the instantiation of ViewModels.
 */
open class AssessmentViewModelFactory() {

    open fun create(
        assessmentNodeState: BranchNodeState
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AssessmentViewModel::class.java)) {

                    @Suppress("UNCHECKED_CAST")
                    return AssessmentViewModel(assessmentNodeState) as T
                }

                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
