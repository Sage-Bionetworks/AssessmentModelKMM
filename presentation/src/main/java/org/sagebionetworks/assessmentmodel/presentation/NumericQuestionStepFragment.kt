package org.sagebionetworks.assessmentmodel.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.res.stringResource
import org.sagebionetworks.assessmentmodel.CompletionStep
import org.sagebionetworks.assessmentmodel.ContentNodeStep
import org.sagebionetworks.assessmentmodel.OverviewStep
import org.sagebionetworks.assessmentmodel.presentation.compose.InstructionStepUi
import org.sagebionetworks.assessmentmodel.presentation.compose.NumericQuestion
import org.sagebionetworks.assessmentmodel.presentation.databinding.ComposeQuestionStepFragmentBinding
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageSurveyTheme
import org.sagebionetworks.assessmentmodel.serialization.SageResourceImage
import org.sagebionetworks.assessmentmodel.serialization.loadDrawable
import org.sagebionetworks.assessmentmodel.survey.QuestionState
import org.sagebionetworks.assessmentmodel.survey.SimpleQuestion

open class NumericQuestionStepFragment: StepFragment() {

    private var _binding: ComposeQuestionStepFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var step: SimpleQuestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        step = nodeState.node as SimpleQuestion
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ComposeQuestionStepFragmentBinding.inflate(layoutInflater, container, false)
        binding.questionContent.setContent {
            SageSurveyTheme {
                NumericQuestion(questionState = nodeState as QuestionState, assessmentViewModel = assessmentViewModel)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}