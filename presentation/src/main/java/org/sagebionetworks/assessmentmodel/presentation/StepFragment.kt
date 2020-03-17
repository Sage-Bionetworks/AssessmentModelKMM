package org.sagebionetworks.assessmentmodel.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.sagebionetworks.assessmentmodel.navigation.NodeState

abstract class StepFragment: Fragment() {

    protected lateinit var stepViewModel: StepViewModel
    protected lateinit var nodeState: NodeState

    protected lateinit var assessmentViewModel: AssessmentViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        assessmentViewModel = (parentFragment as AssessmentFragment).viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stepViewModel = ViewModelProvider(
                this, StepViewModelFactory()
                .create(assessmentViewModel.currentNodeStateLiveData.value!!.nodeState))
                .get(StepViewModel::class.java)
        nodeState = stepViewModel.nodeState
    }

}