package org.sagebionetworks.assessmentmodel.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.*

open class AssessmentViewModel(
    val assessmentNodeState: BranchNodeState,
    val branchNodeStateProvider: CustomBranchNodeStateProvider? = null
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
        assessmentNodeState.finish(
            NavigationPoint(
                null,
                assessmentNodeState.currentResult,
                NavigationPoint.Direction.Exit
            )
        )
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

//    override fun handleReadyToSave(reason: FinishedReason, nodeState: NodeState) {
//        val resultString = nodeState.currentResult.toString()
//        Log.d("Save Result", resultString)
//        // syoung 11/25/2020 In an application, this is the callback for uploading the results.
//    }
//
//    override fun handleFinished(reason: FinishedReason, nodeState: NodeState, error: Error?) {
//        val resultString = nodeState.currentResult.toString()
//        Log.d("Result", resultString)
//
//        //Trigger the UI to finish
//        currentNodeStateMutableLiveData.value =
//            ShowNodeState(
//                nodeState,
//                NavigationPoint.Direction.Exit,
//                null,
//                null
//            )
//    }

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
        assessmentNodeState: BranchNodeState,
        branchNodeStateProvider: CustomBranchNodeStateProvider? = null
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AssessmentViewModel::class.java)) {

                    @Suppress("UNCHECKED_CAST")
                    return AssessmentViewModel(assessmentNodeState, branchNodeStateProvider) as T
                }

                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
