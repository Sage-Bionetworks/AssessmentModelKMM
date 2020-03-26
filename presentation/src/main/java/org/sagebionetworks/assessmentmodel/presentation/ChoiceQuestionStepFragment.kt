package org.sagebionetworks.assessmentmodel.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sagebionetworks.assessmentmodel.presentation.databinding.ChoiceQuestionStepFragmentBinding
import org.sagebionetworks.assessmentmodel.presentation.databinding.TextQuestionStepFragmentBinding
import org.sagebionetworks.assessmentmodel.survey.ChoiceQuestion
import org.sagebionetworks.assessmentmodel.survey.KeyboardInputItemState
import org.sagebionetworks.assessmentmodel.survey.QuestionState
import org.sagebionetworks.assessmentmodel.survey.SimpleQuestion

class ChoiceQuestionStepFragment: StepFragment() {

    private var _binding: ChoiceQuestionStepFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    val binding get() = _binding!!

    lateinit var questionStep: ChoiceQuestion
    lateinit var questionState: QuestionState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionState = nodeState as QuestionState
        questionStep = questionState.node as ChoiceQuestion
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ChoiceQuestionStepFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.navBar.navBarNext.setOnClickListener {
            assessmentViewModel.goforward()
        }
        binding.navBar.navBarBack.setOnClickListener { assessmentViewModel.goBackward() }
        binding.questionHeader.questionTitle.text = questionStep.title
        binding.questionHeader.questionSubtitle.text = questionStep.subtitle

        binding.questionInput.setup(questionState)

    }


}