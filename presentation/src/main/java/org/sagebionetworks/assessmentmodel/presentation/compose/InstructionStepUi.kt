package org.sagebionetworks.assessmentmodel.presentation.compose

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageSurveyTheme
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.sageH1
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.sageP2

@Composable
internal fun InstructionStepUi(
    modifier: Modifier = Modifier,
    icon: Drawable?,
    title: String?,
    detail: String?,
    next:()->Unit,
    close:()->Unit,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color(0xFFF6F6F6)),
    ) {
        CloseTopBar(onCloseClicked = close)
        Column(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(32.dp),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = if (icon != null) {
                    rememberDrawablePainter(drawable = icon)
                } else {
                    painterResource(id = R.drawable.ic_survey_default)
                },
                contentDescription = null
            )
            title?.let { title ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = title,
                    style = sageH1,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            detail?.let { detail ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = detail,
                    style = sageP2,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            BlackButton(
                onClick = next,
                text = stringResource(R.string.start),
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun InstructionStepPreview() {
    SageSurveyTheme {
        InstructionStepUi(icon = null, title = "Title", detail = "Details", next = {}, close = {})
    }
}
