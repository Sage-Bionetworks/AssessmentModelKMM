package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.sagebionetworks.assessmentmodel.Assessment
import org.sagebionetworks.assessmentmodel.InterruptionHandling
import org.sagebionetworks.assessmentmodel.InterruptionHandlingObject
import org.sagebionetworks.assessmentmodel.presentation.AssessmentViewModel
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageSurveyTheme
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageWhite
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.sageH1
import org.sagebionetworks.assessmentmodel.survey.ReservedNavigationIdentifier


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PauseScreenDialog(showDialog:Boolean,
                      assessmentViewModel: AssessmentViewModel,
                      onClose:()->Unit) {
    if (showDialog) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest =  onClose ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                PauseScreen(
                    assessmentViewModel = assessmentViewModel,
                    close = onClose
                    )
            }
        }
    }
}

@Composable
internal fun PauseScreen(
    modifier: Modifier = Modifier,
    assessmentViewModel: AssessmentViewModel,
    close:()->Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color(0xFF575E71)),
    ) {
        val interruptionHandling = (assessmentViewModel.assessmentNodeState.node as Assessment).interruptionHandling
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 30.dp, bottom = 30.dp),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.paused),
            color = SageWhite,
            style = sageH1
        )
        Divider(color = SageWhite, thickness = 1.dp)
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp),
        ) {
            SageButton(
                modifier = modifier.fillMaxWidth(),
                onClick = close,
                buttonColors = whiteButtonColors(),
                drawBorder = false,
                text = stringResource(R.string.resume)
            )
            if (interruptionHandling.reviewIdentifier != null) {
                Spacer(modifier = Modifier.height(16.dp))
                ClearButton(onClick = {
                                      if (ReservedNavigationIdentifier.Beginning.matching(interruptionHandling.reviewIdentifier)) {
                                          assessmentViewModel.goToStart()
                                      }
                                      /*TODO: Jump to instruction step -nbrown 04/05/22*/
                                      },
                    stringResource(R.string.review_instructions))
            }
            if (interruptionHandling.canSkip) {
                Spacer(modifier = Modifier.height(16.dp))
                ClearButton(onClick = { assessmentViewModel.declineAssessment() }, stringResource(R.string.skip_this_survey))
            }
            if (interruptionHandling.canSaveForLater) {
                Spacer(modifier = Modifier.height(16.dp))
                ClearButton(onClick = { /*TODO: Save partial progress for surveys -nbrown 04/05/22*/ },
                    stringResource(R.string.continue_later))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }

}

@Composable
private fun ClearButton(
    onClick: () -> Unit,
    text: String? = null,
) {
    val clearButtonColors = DefaultButtonColors(
        backgroundColor = Color.Transparent,
        contentColor = SageWhite,
        disabledBackgroundColor = Color.Transparent.copy(alpha = ContentAlpha.disabled),
        disabledContentColor = SageWhite.copy(alpha = ContentAlpha.disabled)
    )
    SageButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        buttonColors = clearButtonColors,
        drawBorder = true,
        text = text
    )
}

@Preview
@Composable
private fun PauseScreenPreview() {
    SageSurveyTheme {
//        PauseScreenDialog(showDialog = true, interruptionHandling =  InterruptionHandlingObject(
//            reviewIdentifier = "test",
//            canResume = true,
//            canSaveForLater = true,
//            canSkip = true
//        ), {})
    }
}
