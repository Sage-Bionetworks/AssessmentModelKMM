package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.unit.dp
import org.sagebionetworks.assessmentmodel.ButtonAction
import org.sagebionetworks.assessmentmodel.presentation.AssessmentViewModel
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.BackgroundGray
import org.sagebionetworks.assessmentmodel.survey.QuestionState

@Composable
internal fun QuestionContainer(
    questionState: QuestionState,
    assessmentViewModel: AssessmentViewModel,
    modifier: Modifier = Modifier,
    headerCanCollapse: Boolean = false,
    content: @Composable @UiComposable () -> Unit
) {
   QuestionContainer(
       subtitle = questionState.node.subtitle,
       title = questionState.node.title,
       detail = questionState.node.detail,
       nextButtonText = questionState.node.buttonMap.get(ButtonAction.Navigation.GoForward)?.buttonTitle,
       nextEnabled = questionState.allAnswersValidFlow.collectAsState().value,
       assessmentViewModel = assessmentViewModel,
       modifier = modifier,
       headerCanCollapse = headerCanCollapse,
       content = content
   )
}

@Composable
internal fun QuestionContainer(
    subtitle: String? = null,
    title: String? = null,
    detail: String? = null,
    nextButtonText: String? = null,
    nextEnabled: Boolean,
    assessmentViewModel: AssessmentViewModel,
    headerCanCollapse: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable @UiComposable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(BackgroundGray)
    ) {
        val scrollState = rememberScrollState()
        QuestionHeader(
            subtitle = subtitle,
            title = title,
            detail = detail,
            assessmentViewModel = assessmentViewModel,
            scrollState = scrollState,
            headerCanCollapse = headerCanCollapse
        )
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(scrollState),
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            content()
            Spacer(modifier = Modifier.weight(1f))
            BottomNavigation(
                { assessmentViewModel.goBackward() },
                { assessmentViewModel.goForward() },
                nextText = nextButtonText,
                nextEnabled = nextEnabled
            )
        }
    }
}
