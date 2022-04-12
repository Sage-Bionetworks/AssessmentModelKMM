package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import org.sagebionetworks.assessmentmodel.navigation.Progress
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageBlack
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageSurveyTheme

@Composable
fun PauseTopBar(
    onPauseClicked: () -> Unit,
    onSkipClicked: () -> Unit,
    showSkip: Boolean = true
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        PauseButton(onClick = onPauseClicked)
        Spacer(modifier = Modifier.weight(1f))
        if (showSkip) {
            TextButton(
                onClick = onSkipClicked,
            ) {
                Text(
                    text = stringResource(R.string.skip_question),
                    textDecoration = TextDecoration.Underline,
                    color = SageBlack
                )
            }
        }
    }
}

@Composable
fun CloseTopBar(
    onCloseClicked: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1f))
        CloseButton(onClick = onCloseClicked)
    }
}

@Composable
fun PauseButton(
    onClick: () -> Unit) {
    IconButton(onClick = onClick,
        ) {
        Icon(
            painter = painterResource(id = org.sagebionetworks.assessmentmodel.presentation.R.drawable.ic_pause),
            contentDescription = stringResource(R.string.pause)
        )
    }
}

@Composable
fun CloseButton(
    onClick: () -> Unit) {
    IconButton(onClick = onClick,
    ) {
        Icon(
            painter = painterResource(id = org.sagebionetworks.assessmentmodel.presentation.R.drawable.ic_close),
            contentDescription = stringResource(R.string.close)
        )
    }
}

@Composable
fun ProgressBar(
    progress: Progress?
) {
    progress?.let {
        LinearProgressIndicator(
            progress = it.current.toFloat().div(it.total.toFloat()),
            modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
private fun TopBarPreview() {
    SageSurveyTheme {
        Column() {
            PauseTopBar({}, {})
            PauseTopBar({}, {}, false)
            CloseTopBar({})
        }
    }
}