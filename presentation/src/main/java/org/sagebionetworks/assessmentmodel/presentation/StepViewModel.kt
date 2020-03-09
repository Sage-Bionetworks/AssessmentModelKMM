package org.sagebionetworks.assessmentmodel.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.sagebionetworks.assessmentmodel.navigation.NodeState

class StepViewModel(val nodeState: NodeState) : ViewModel() {


}



/**
 * Factory for the StepViewModel.
 *
 * Providing ViewModelProvider.Factory allows us to inject dependencies and pass parameters
 * to an instance since the Android framework controls the instantiation of ViewModels.
 */
class SteptViewModelFactory() {

    fun create(nodeState: NodeState): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(StepViewModel::class.java)) {

                    @Suppress("UNCHECKED_CAST")
                    return StepViewModel(
                        nodeState
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
