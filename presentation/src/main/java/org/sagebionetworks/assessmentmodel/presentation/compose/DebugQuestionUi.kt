package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.sagebionetworks.assessmentmodel.ButtonAction
import org.sagebionetworks.assessmentmodel.presentation.AssessmentViewModel
import org.sagebionetworks.assessmentmodel.survey.QuestionState

@Composable
internal fun DebugQuestion(
    questionState: QuestionState,
    assessmentViewModel: AssessmentViewModel,
) {

    QuestionContainer(
        subtitle = questionState.node.subtitle,
        title = questionState.node.title,
        detail = questionState.node.detail,
        nextButtonText = questionState.node.buttonMap.get(ButtonAction.Navigation.GoForward)?.buttonTitle,
        nextEnabled = true,
        assessmentViewModel = assessmentViewModel
    ) {
        Text(text = questionState.node.toString())
    }
}

