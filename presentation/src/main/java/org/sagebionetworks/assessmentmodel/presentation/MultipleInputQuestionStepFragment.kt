package org.sagebionetworks.assessmentmodel.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import org.sagebionetworks.assessmentmodel.Step
import org.sagebionetworks.assessmentmodel.navigation.NodeState
import org.sagebionetworks.assessmentmodel.presentation.databinding.TextQuestionStepFragmentBinding
import org.sagebionetworks.assessmentmodel.survey.*


/**
 * A simple [Fragment] subclass.
 * Use the [MultipleInputQuestionStepFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MultipleInputQuestionStepFragment : StepFragment() {

    private var _binding: TextQuestionStepFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    val binding get() = _binding!!

    lateinit var questionStep: SimpleQuestion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiple_input_question_step, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.navBar.setForwardOnClickListener { assessmentViewModel.goForward() }
        binding.navBar.setBackwardOnClickListener { assessmentViewModel.goBackward() }
        binding.navBar.setup(questionStep as Step)
    }
}