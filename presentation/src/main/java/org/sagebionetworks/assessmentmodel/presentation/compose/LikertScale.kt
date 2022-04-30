package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageBlack
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageSurveyTheme
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SliderGray


@Composable
fun LikertScale(
    minValue: Int,
    maxValue: Int,
    numSelected: Int?,
    onCircleTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val numCircles = (maxValue - minValue) + 1
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .padding(start = 12.dp, top = 10.dp)
                .width(((numCircles - 1) * 48).dp)
                .height(4.dp)
                .align(Alignment.TopStart)
        ) {
            drawLine(
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                color = SliderGray,
                strokeWidth = size.height
            )
        }
        if (numSelected != null) {
            Canvas(
                modifier = Modifier
                    .padding(start = 12.dp, top = 10.dp)
                    .width(((numSelected - minValue) * 48).dp)
                    .height(4.dp)
                    .align(Alignment.TopStart)
            ) {
                drawLine(
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    color = SageBlack,
                    strokeWidth = size.height
                )
            }
        }
        Row() {
            for (i in minValue..maxValue) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Canvas(modifier = Modifier
                        .size(24.dp)
                        .clickable { onCircleTap(i) }
                    ) {
                        drawCircle(
                            color = if (numSelected != null && i <= numSelected) {
                                SageBlack
                            } else {
                                SliderGray
                            }
                        )
                    }
                    Text(i.toString())
                }
                Spacer(modifier = Modifier.width(24.dp))
            }

        }
    }
}

@Preview
@Composable
private fun LikertPreview() {
    SageSurveyTheme {
        Column() {
            LikertScale(1, 5, 3, {})

        }
    }
}
