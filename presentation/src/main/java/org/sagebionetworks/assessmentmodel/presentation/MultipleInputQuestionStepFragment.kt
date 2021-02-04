package org.sagebionetworks.assessmentmodel.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.sagebionetworks.assessmentmodel.Step
import org.sagebionetworks.assessmentmodel.presentation.databinding.FragmentMultipleInputQuestionStepBinding
import org.sagebionetworks.assessmentmodel.serialization.ChoiceQuestionObject
import org.sagebionetworks.assessmentmodel.serialization.SimpleQuestionObject
import org.sagebionetworks.assessmentmodel.survey.*


/**
 * A simple [Fragment] subclass.
 * Use the [MultipleInputQuestionStepFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MultipleInputQuestionStepFragment : StepFragment() {

    private var _binding: FragmentMultipleInputQuestionStepBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    val binding get() = _binding!!

    lateinit var questionStep: MultipleInputQuestion
    lateinit var questionState: QuestionState
    lateinit var inputState: KeyboardInputItemState<*>

    lateinit var inputStatesList: List<InputItemState>
    lateinit var inputItemsList : List<InputItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionState = nodeState as QuestionState
        questionStep = questionState.node as MultipleInputQuestion
        inputStatesList = questionState.itemStates

        inputState = inputStatesList[2] as KeyboardInputItemState<*>
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
//            binding.question2Input.updateResult(inputState)
//            questionState.saveAnswer(inputState.currentAnswer, inputState)
            assessmentViewModel.goForward()
        }
        binding.navBar.setBackwardOnClickListener { assessmentViewModel.goBackward() }
        binding.navBar.setup(questionStep as Step)
        binding.questionHeader.questionTitle.text = questionStep.title
        binding.questionHeader.questionSubtitle.text = questionStep.subtitle
        binding.questionHeader.closeBtn.setOnClickListener{ assessmentViewModel.cancel() }
//        for (inputItem in inputItemsList) {
//            if (inputItem.equals(ChoiceQuestionObject)) { //multiple choice question
//                binding.question0Input.setup(questionState)
//                binding.question1Input.setup(questionState)
//            } else if (inputItem.equals(SimpleQuestionObject)) { //text input question
//                binding.question2Input.setup(inputState)
//            }
//            break
//        }
        binding.question0Input.setup(inputState)
        binding.question1Input.setup(inputState)
        binding.question2Input.setup(inputState)
    }
}