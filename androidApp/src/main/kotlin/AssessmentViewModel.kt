package org.sagebionetworks.assessmentmodel.sampleapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.*
import java.util.*

class AssessmentViewModel(val assesmentIdentifier: String, val assessmentProvider: AssessmentProvider) : ViewModel(), RootNodeController {

    //TODO: This should probably be done asynchronously -nbrown 02/06/20
    private var _assessment: Assessment? = null
    fun loadAssesment(): Assessment? {
        if (_assessment == null) {
            _assessment = assessmentProvider.loadAssessment(assesmentIdentifier)
        }
        return _assessment
    }

    private var isStarted = false
    private val assessmentNodeState = BranchNodeStateImpl(loadAssesment()!!, null)
    private val currentNodeStateMutableLiveData: MutableLiveData<ShowNodeState> = MutableLiveData()
    val currentNodeStateLiveData: LiveData<ShowNodeState> = currentNodeStateMutableLiveData

    fun start() {
        if (!isStarted) {
            isStarted = true
            assessmentNodeState.rootNodeController = this
            goforward()
        }
    }

    fun goforward() {
        assessmentNodeState.goForward()
    }

    override fun canHandle(node: Node): Boolean {
        return (node is Step)
    }

    override fun handleGoForward(nodeState: NodeState, requestedPermissions: Set<Permission>?, asyncActionNavigations: Set<AsyncActionNavigation>?) {
        //Update the LiveData stream with the new node
        currentNodeStateMutableLiveData.value = ShowNodeState(nodeState, NavigationPoint.Direction.Forward, requestedPermissions, asyncActionNavigations)
    }

    override fun handleGoBack(nodeState: NodeState, requestedPermissions: Set<Permission>?, asyncActionNavigations: Set<AsyncActionNavigation>?) {
        currentNodeStateMutableLiveData.value = ShowNodeState(nodeState, NavigationPoint.Direction.Backward, requestedPermissions, asyncActionNavigations)
    }

    override fun handleFinished(reason: FinishedReason, nodeState: NodeState, error: Error?) {
        //TODO: -nbrown 02/13/2020
    }

    /**
     * Data class for LiveData stream.
     */
    data class ShowNodeState(val nodeState: NodeState,
                             val direction: NavigationPoint.Direction,
                             val requestedPermissions: Set<Permission>?,
                             val asyncActionNavigations: Set<AsyncActionNavigation>?)

}



/**
 * Factory for the AssesmentViewModel.
 *
 * Providing ViewModelProvider.Factory allows us to inject dependencies and pass parameters
 * to an instance since the Android framework controls the instantiation of ViewModels.
 */
class AssesmentViewModelFactory() {

    fun create(assesmentIdentifier: String, assessmentProvider: AssessmentProvider): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AssessmentViewModel::class.java)) {

                    @Suppress("UNCHECKED_CAST")
                    return AssessmentViewModel(assesmentIdentifier, assessmentProvider) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
