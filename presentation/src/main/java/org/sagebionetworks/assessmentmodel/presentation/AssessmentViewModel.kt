package org.sagebionetworks.assessmentmodel.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.sagebionetworks.assessmentmodel.Node
import org.sagebionetworks.assessmentmodel.PermissionInfo
import org.sagebionetworks.assessmentmodel.Step
import org.sagebionetworks.assessmentmodel.navigation.*

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

    fun cancel() {
        assessmentNodeState.exitEarly(FinishedReason.Incomplete(saveResult = SaveResults.Never, markFinished = false, declined = false), null)
    }

    override fun canHandle(node: Node): Boolean {
        return (node is Step)
    }

    override fun handleGoForward(
        nodeState: NodeState,
        requestedPermissions: Set<PermissionInfo>?,
        asyncActionNavigations: Set<AsyncActionNavigation>?
    ) {
        //Update the LiveData stream with the new node
        currentNodeStateMutableLiveData.value =
            ShowNodeState(
                nodeState,
                NavigationPoint.Direction.Forward,
                requestedPermissions,
                asyncActionNavigations
            )
    }

    override fun handleGoBack(
        nodeState: NodeState,
        requestedPermissions: Set<PermissionInfo>?,
        asyncActionNavigations: Set<AsyncActionNavigation>?
    ) {
        currentNodeStateMutableLiveData.value =
            ShowNodeState(
                nodeState,
                NavigationPoint.Direction.Backward,
                requestedPermissions,
                asyncActionNavigations
            )
    }

    /**
     * Data class for LiveData stream.
     */
    data class ShowNodeState(
        val nodeState: NodeState,
        val direction: NavigationPoint.Direction,
        val requestedPermissions: Set<PermissionInfo>?,
        val asyncActionNavigations: Set<AsyncActionNavigation>?,
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
