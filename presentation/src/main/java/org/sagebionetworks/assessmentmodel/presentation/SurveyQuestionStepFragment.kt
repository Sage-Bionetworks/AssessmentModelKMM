package org.sagebionetworks.assessmentmodel.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sagebionetworks.assessmentmodel.presentation.compose.*
import org.sagebionetworks.assessmentmodel.presentation.databinding.ComposeQuestionStepFragmentBinding
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageSurveyTheme
import org.sagebionetworks.assessmentmodel.serialization.StringTextInputItemObject
import org.sagebionetworks.assessmentmodel.survey.*

class SurveyQuestionStepFragment: StepFragment() {

    private var _binding: ComposeQuestionStepFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    val binding get() = _binding!!

    lateinit var questionState: QuestionState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionState = nodeState as QuestionState
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = ComposeQuestionStepFragmentBinding.inflate(layoutInflater, container, false)
        binding.questionContent.setContent {
            //TODO: Need to figure out theming with compose -nbrown 2/17/22
            SageSurveyTheme {
                when (val step = questionState.node) {
                    is SimpleQuestion -> {
                        if (step.inputItem.answerType == AnswerType.INTEGER) {
                            IntegerQuestion(
                                questionState = questionState,
                                assessmentViewModel = assessmentViewModel
                            )
                        } else if (step.inputItem is TimeInputItem) {
                            TimeQuestion(
                                questionState = questionState,
                                assessmentViewModel = assessmentViewModel
                            )
                        } else if (step.inputItem is DurationInputItem) {
                            DurationQuestion(
                                questionState = questionState,
                                assessmentViewModel = assessmentViewModel
                            )
                        } else if (step.inputItem is StringTextInputItemObject) {
                            TextQuestion(
                                questionState = questionState,
                                assessmentViewModel = assessmentViewModel
                            )
                        } else {
                            DebugQuestion(
                                questionState = questionState,
                                assessmentViewModel = assessmentViewModel
                            )
                        }
                    }
                    is ChoiceQuestion -> ChoiceQuestion(questionState = questionState, assessmentViewModel = assessmentViewModel)
                    else -> DebugQuestion(questionState = questionState, assessmentViewModel = assessmentViewModel)
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}