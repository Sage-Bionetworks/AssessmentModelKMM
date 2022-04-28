package org.sagebionetworks.assessmentmodel.presentation.compose

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.*

@Composable
internal fun InstructionStepUi(
    modifier: Modifier = Modifier,
    icon: Drawable?,
    iconTintColor: Color?,
    title: String?,
    detail: String?,
    nextButtonText: String,
    next:()->Unit,
    close:()->Unit,
    hideClose: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(BackgroundGray),
    ) {
        if (!hideClose) {
            CloseTopBar(onCloseClicked = close)
        }
        Column(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(32.dp),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            if (icon != null) {
                Image(
                    painter = rememberDrawablePainter(drawable = icon),
                    contentDescription = null,
                    colorFilter = if (iconTintColor != null) {
                        ColorFilter.tint(
                            color = iconTintColor,
                            blendMode = BlendMode.Modulate
                        )
                    } else {
                        null
                    }
                )
            }
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
                text = nextButtonText,
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun InstructionStepPreview() {
    SageSurveyTheme {
        InstructionStepUi(icon = null, iconTintColor = null, title = "Title", detail = "Details", nextButtonText = stringResource(R.string.start), next = {}, close = {}, hideClose = false)
    }
}
