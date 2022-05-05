package org.sagebionetworks.assessmentmodel.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sagebionetworks.assessmentmodel.presentation.compose.ChoiceQuestion
import org.sagebionetworks.assessmentmodel.presentation.databinding.ComposeQuestionStepFragmentBinding
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageSurveyTheme
import org.sagebionetworks.assessmentmodel.survey.ChoiceQuestion
import org.sagebionetworks.assessmentmodel.survey.QuestionState

class ChoiceQuestionStepFragment: StepFragment() {

    private var _binding: ComposeQuestionStepFragmentBinding? = null
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
                              savedInstanceState: Bundle?): View {
        _binding = ComposeQuestionStepFragmentBinding.inflate(layoutInflater, container, false)
        binding.questionContent.setContent {
            //TODO: Need to figure out theming with compose -nbrown 2/17/22
            SageSurveyTheme {
                ChoiceQuestion(questionState = questionState, assessmentViewModel = assessmentViewModel)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}