package org.sagebionetworks.assessmentmodel.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.sagebionetworks.assessmentmodel.navigation.NodeState

abstract class StepFragment: Fragment() {

    protected lateinit var stepViewModel: StepViewModel
    protected lateinit var nodeState: NodeState

    protected lateinit var assessmentViewModel: AssessmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assessmentViewModel = (parentFragment as AssessmentFragment).viewModel
        stepViewModel = ViewModelProvider(
                this, StepViewModelFactory()
                .create(assessmentViewModel.currentNodeStateLiveData.value!!.nodeState))
                .get(StepViewModel::class.java)
        nodeState = stepViewModel.nodeState
    }

}