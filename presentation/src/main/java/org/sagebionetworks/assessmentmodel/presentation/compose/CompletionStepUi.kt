package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.*

@Composable
internal fun CompletionStepUi(
    modifier: Modifier = Modifier,
    title: String?,
    detail: String?,
    nextButtonText: String,
    next:()->Unit,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(BackgroundGray),
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(32.dp),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box() {
                Column(
                    modifier = Modifier.align(TopCenter)
                ) {
                    Spacer(modifier = Modifier.height(110.dp))
                    Card(
                        modifier = Modifier.background(color = SageWhite),
                        shape = RoundedCornerShape(0.dp),
                        elevation = 4.dp
                    ) {
                        Column(

                        ) {
                            Spacer(modifier = Modifier.height(64.dp))
                            title?.let { title ->
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = title,
                                    style = sageH1,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(CenterHorizontally)
                                )
                            }
                            detail?.let { detail ->
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = detail,
                                    style = sageP2,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(CenterHorizontally)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
                Image(
                    modifier = modifier
                        .fillMaxWidth()
                        .align(TopCenter),
                    painter = painterResource(id = R.drawable.completion),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            BlackButton(
                onClick = next,
                text = nextButtonText,
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun CompletionStepPreview() {
    SageSurveyTheme {
        CompletionStepUi(title = "Well done!", detail = "Thank you for being part of our study.", nextButtonText = stringResource(R.string.exit), next = {})
    }
}
