package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.sagebionetworks.assessmentmodel.ButtonAction
import org.sagebionetworks.assessmentmodel.navigation.hideButton
import org.sagebionetworks.assessmentmodel.presentation.AssessmentViewModel
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.BackgroundGray
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.sageH1
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.sageP2
import java.lang.Integer.max

@Composable
internal fun QuestionHeader(
    subtitle: String?,
    title: String?,
    detail: String?,
    assessmentViewModel: AssessmentViewModel,
    scrollState: ScrollState,
    headerCanCollapse: Boolean = false,
    modifier: Modifier = Modifier
) {
    val elevationValue = if (scrollState.value > 5) { 5.dp} else {0.dp}
    val headerElevation = animateDpAsState(targetValue = elevationValue)
    val coroutineScope = rememberCoroutineScope()
    val subtitleHeight = remember { mutableStateOf(0) }
    val detailHeight = remember { mutableStateOf(0) }

    /**
     * This logic is needed to prevent fullHeaderShouldDisplay from continuously toggling
     * on and off in certain edge cases since scrollState.maxValue changes when subtitle and
     * detail are not displayed
     */
    val maxScroll = if (scrollState.maxValue > 10000) {
        remember { mutableStateOf(0) }
    } else {
        remember { mutableStateOf(scrollState.maxValue) }
    }
    maxScroll.value = max(scrollState.maxValue, maxScroll.value)

    val fullHeaderShouldDisplay = !headerCanCollapse ||
            scrollState.value == 0 ||
            maxScroll.value < (subtitleHeight.value + detailHeight.value)
    Card(
        elevation = headerElevation.value,
        modifier = Modifier
            .padding(bottom = 4.dp)
            .clickable {
                coroutineScope.launch { scrollState.scrollTo(0) }
            }
    ) {

        Column(
            modifier = modifier
                .background(BackgroundGray),
        ) {
            ProgressBar(progress = assessmentViewModel.assessmentNodeState.progress())
            val openDialog = remember { mutableStateOf(false) }
            PauseScreenDialog(
                showDialog = openDialog.value,
                assessmentViewModel = assessmentViewModel
            ) {
                openDialog.value = false
            }
            PauseTopBar(
                onPauseClicked = { openDialog.value = true },
                onSkipClicked = { assessmentViewModel.skip() },
                hideSkip = assessmentViewModel.assessmentNodeState.currentChild?.hideButton(
                    ButtonAction.Navigation.Skip) ?: false
                )
            Column(
                modifier = modifier
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                AnimatedVisibility(visible = fullHeaderShouldDisplay) {
                    subtitle?.let { subtitle ->
                        Text(
                            text = subtitle,
                            style = sageP2,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 0.dp, bottom = 0.dp, start = 24.dp, end = 8.dp)
                                .onGloballyPositioned { subtitleHeight.value = it.size.height }
                        )
                    }
                }
                title?.let { title ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = title,
                        style = sageH1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 0.dp, bottom = 0.dp, start = 24.dp, end = 8.dp)
                    )
                }
                AnimatedVisibility(visible = fullHeaderShouldDisplay) {
                    detail?.let { detail ->
                        Column(modifier = Modifier
                            .onGloballyPositioned { detailHeight.value = it.size.height }
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = detail,
                                style = sageP2,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 0.dp, bottom = 0.dp, start = 24.dp, end = 8.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}