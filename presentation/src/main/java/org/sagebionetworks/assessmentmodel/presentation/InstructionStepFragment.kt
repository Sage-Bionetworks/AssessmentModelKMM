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
import org.sagebionetworks.assessmentmodel.presentation.compose.CompletionStepUi
import org.sagebionetworks.assessmentmodel.presentation.compose.InstructionStepUi
import org.sagebionetworks.assessmentmodel.presentation.databinding.ComposeQuestionStepFragmentBinding
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageSurveyTheme
import org.sagebionetworks.assessmentmodel.serialization.SageResourceImage
import org.sagebionetworks.assessmentmodel.serialization.loadDrawable

open class InstructionStepFragment: StepFragment() {

    private var _binding: ComposeQuestionStepFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var step: ContentNodeStep

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        step = nodeState.node as ContentNodeStep
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ComposeQuestionStepFragmentBinding.inflate(layoutInflater, container, false)
        val drawable = step.imageInfo?.loadDrawable(requireContext())
        val tint = step.imageInfo?.tint ?: false
        var hideClose = false
        val buttonTextResource = if (step is OverviewStep) {
            R.string.start
        } else if (step is CompletionStep) {
            hideClose = true
            R.string.exit
        } else {
            R.string.next
        }
        binding.questionContent.setContent {
            //TODO: Need to figure out theming with compose -nbrown 2/17/22
            SageSurveyTheme {
                if (step is CompletionStep) {
                    CompletionStepUi(
                        title = step.title ?: getString(R.string.well_done),
                        detail = step.detail ?: getString(R.string.thank_you_for),
                        nextButtonText = stringResource(buttonTextResource),
                        next = { assessmentViewModel.goForward() }
                    )
                } else {
                    InstructionStepUi(
                        icon = drawable,
                        iconTintColor = if (tint) {
                            MaterialTheme.colors.primary
                        } else {
                            null
                        },
                        title = step.title,
                        detail = step.detail,
                        nextButtonText = stringResource(buttonTextResource),
                        next = { assessmentViewModel.goForward() },
                        close = { assessmentViewModel.cancel() },
                        hideClose = hideClose
                    )
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