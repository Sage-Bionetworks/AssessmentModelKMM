package org.sagebionetworks.assessmentmodel.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.sagebionetworks.assessmentmodel.presentation.databinding.TextQuestionStepFragmentBinding
import org.sagebionetworks.assessmentmodel.survey.*

class TextQuestionStepFragment: StepFragment() {

    private var _binding: TextQuestionStepFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    val binding get() = _binding!!

    lateinit var questionStep: SimpleQuestion
    lateinit var questionState: QuestionState
    lateinit var inputState: KeyboardInputItemState<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionState = nodeState as QuestionState
        questionStep = questionState.node as SimpleQuestion
        inputState = questionState.itemStates.get(0) as KeyboardInputItemState<*>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = TextQuestionStepFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.navBar.navBarNext.setOnClickListener {
            binding.questionInput.updateResult(inputState)
            questionState.saveAnswer(inputState.currentAnswer, inputState)
            assessmentViewModel.goforward()
        }
        binding.navBar.navBarBack.setOnClickListener { assessmentViewModel.goBackward() }
        binding.questionHeader.questionTitle.text = questionStep.title
        binding.questionHeader.questionSubtitle.text = questionStep.subtitle
        binding.questionHeader.closeBtn.setOnClickListener{ assessmentViewModel.cancel() }
        binding.questionInput.setup(inputState)

    }

}