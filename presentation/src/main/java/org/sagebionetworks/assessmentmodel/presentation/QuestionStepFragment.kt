package org.sagebionetworks.assessmentmodel.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.sagebionetworks.assessmentmodel.presentation.databinding.StepFragmentBinding

import org.sagebionetworks.assessmentmodel.survey.Question
import org.sagebionetworks.assessmentmodel.survey.SimpleQuestion

class QuestionStepFragment: StepFragment() {

    private var _binding: StepFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var questionStep: SimpleQuestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionStep = nodeState.node as SimpleQuestion
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = StepFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.textView.text = stepViewModel.nodeState.node.toString()
        binding.navBar.navBarNext.setOnClickListener { assessmentViewModel.goforward() }
        binding.questionHeader.questionTitle.text = questionStep.title
        binding.questionHeader.questionSubtitle.text = questionStep.subtitle

        binding.questionInput.textInput.hint = questionStep.inputItem.placeholder
    }

}