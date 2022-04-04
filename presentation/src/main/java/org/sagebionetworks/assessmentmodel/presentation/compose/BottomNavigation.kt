package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageSurveyTheme

@Composable
fun BottomNavigation(
    onBackClicked: () -> Unit,
    onNextClicked: () -> Unit,
    backEnabled: Boolean = true,
    nextEnabled: Boolean = true
) {
    Row(modifier = Modifier
        .padding(top = 10.dp, bottom = 10.dp)
        .fillMaxWidth()) {
        WhiteBackButton(onClick = onBackClicked, enabled = backEnabled)
        Spacer(modifier = Modifier.weight(1f))
        BlackNextButton(onClick = onNextClicked, enabled = nextEnabled)
    }
}

@Preview
@Composable
private fun BottomNavPreview() {
    SageSurveyTheme {
        Column() {
            BottomNavigation({}, {})
            BottomNavigation({}, {}, false, false)
        }
    }
}