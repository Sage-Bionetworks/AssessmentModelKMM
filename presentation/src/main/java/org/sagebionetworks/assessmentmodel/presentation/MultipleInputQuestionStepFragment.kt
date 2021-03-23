package org.sagebionetworks.assessmentmodel.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import org.sagebionetworks.assessmentmodel.Step
import org.sagebionetworks.assessmentmodel.presentation.databinding.FragmentMultipleInputQuestionStepBinding
import org.sagebionetworks.assessmentmodel.presentation.inputs.TextInputView
import org.sagebionetworks.assessmentmodel.survey.*


/**
 * A simple [Fragment] subclass.
 * Use the [MultipleInputQuestionStepFragment.newInstance] factory method to
 * create an instance of this fragment.
 * This fragment can contain any number of text fields.
 */
class MultipleInputQuestionStepFragment : StepFragment() {

    private var _binding: FragmentMultipleInputQuestionStepBinding? = null
    val binding get() = _binding!!

    lateinit var questionStep: MultipleInputQuestion
    lateinit var questionState: QuestionState

    lateinit var inputStatesList: List<InputItemState>
    lateinit var inputItemsList : List<InputItem>
    val map = mutableMapOf<InputItemState, TextInputView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionState = nodeState as QuestionState
        questionStep = questionState.node as MultipleInputQuestion
        inputStatesList = questionState.itemStates
        inputItemsList = questionStep.inputItems
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMultipleInputQuestionStepBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.navBar.setForwardOnClickListener {
            for (currInputState in map.keys) {
                map[currInputState]?.updateResult(currInputState as KeyboardInputItemState<*>)
            }
            for (inputState in inputStatesList) {
                questionState.saveAnswer(inputState.currentAnswer, inputState)
            }
            assessmentViewModel.goForward()
        }
        binding.navBar.setBackwardOnClickListener { assessmentViewModel.goBackward() }
        binding.navBar.setup(questionStep as Step)
        binding.questionHeader.questionTitle.text = questionStep.title
        binding.questionHeader.questionSubtitle.text = questionStep.subtitle
        binding.questionHeader.closeBtn.setOnClickListener{ assessmentViewModel.cancel() }
        // display skip option
        if (!questionStep.optional) {
            binding.navBar.binding.skipButton.visibility = View.INVISIBLE
        } else {
            binding.navBar.binding.skipButton.visibility = View.VISIBLE
            binding.navBar.binding.skipButton.setOnClickListener { assessmentViewModel.goForward() }
        }
        for (inputState in inputStatesList) {
            val textViewCurr = TextInputView(requireContext())
            textViewCurr.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            binding.linearLayoutView.addView(textViewCurr)
            textViewCurr.setup(inputState as KeyboardInputItemState<*>)
            map[inputState] = textViewCurr
        }
    }
}