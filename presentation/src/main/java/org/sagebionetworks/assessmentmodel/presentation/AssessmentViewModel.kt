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


    private var isStarted = false
    protected val currentNodeStateMutableLiveData: MutableLiveData<ShowNodeState> = MutableLiveData()
    val currentNodeStateLiveData: LiveData<ShowNodeState> = currentNodeStateMutableLiveData

    fun start() {
        if (!isStarted) {
            isStarted = true
            assessmentNodeState.nodeUIController = this
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
        assessmentNodeState.exitEarly(null)
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
        val asyncActionNavigations: Set<AsyncActionNavigation>?
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
