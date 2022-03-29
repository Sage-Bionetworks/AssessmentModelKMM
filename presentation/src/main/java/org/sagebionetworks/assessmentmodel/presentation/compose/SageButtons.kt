package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageBlack
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageSurveyTheme
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageWhite

@Composable
fun SageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColors: ButtonColors = whiteButtonColors(),
    drawBorder: Boolean = true,
    enabled: Boolean = true,
    text: String? = null,
    icon: ImageVector? = null,
    iconContentDescription: String? = null,
) {
    Button(
        modifier = modifier
            .height(56.dp),
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
        border = if (drawBorder) {
            BorderStroke(2.dp, buttonColors.contentColor(enabled = enabled).value)
        } else { null },
        colors = buttonColors
    ) {
        if (text != null) {
            Text(text = text)
        }
        if (icon != null)
            Icon(
                imageVector = icon,
                contentDescription = iconContentDescription,
            )

    }
}

@Composable
fun WhiteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String? = null,
    icon: ImageVector? = null,
    iconContentDescription: String? = null
) {
    SageButton(
        onClick = onClick,
        modifier = modifier,
        buttonColors = whiteButtonColors(),
        drawBorder = true,
        enabled = enabled,
        text = text,
        icon = icon,
        iconContentDescription = iconContentDescription
        )
}

@Composable
fun BlackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String? = null,
    icon: ImageVector? = null,
    iconContentDescription: String? = null
) {
    SageButton(
        onClick = onClick,
        modifier = modifier,
        buttonColors = blackButtonColors(),
        drawBorder = false,
        enabled = enabled,
        text = text,
        icon = icon,
        iconContentDescription = iconContentDescription
    )
}

@Composable
fun WhiteBackButton(
    onClick: () -> Unit,
    enabled: Boolean = true) {
    WhiteButton(onClick = onClick,
        icon = Icons.Filled.KeyboardArrowLeft,
        enabled = enabled,
        modifier = Modifier.width(56.dp))
}

@Composable
fun BlackNextButton(
    onClick: () -> Unit,
    enabled: Boolean = true) {
    BlackButton(onClick = onClick,
        icon = Icons.Filled.KeyboardArrowRight,
        enabled = enabled,
        modifier = Modifier.width(56.dp))
}

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

@Composable
fun whiteButtonColors(
    backgroundColor: Color = SageWhite,
    contentColor: Color = SageBlack,
    disabledBackgroundColor: Color = SageWhite.copy(alpha = ContentAlpha.disabled),
    disabledContentColor: Color = SageBlack.copy(alpha = ContentAlpha.disabled)
): ButtonColors = DefaultButtonColors(
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    disabledBackgroundColor = disabledBackgroundColor,
    disabledContentColor = disabledContentColor
)

@Composable
fun blackButtonColors(
    backgroundColor: Color = SageBlack,
    contentColor: Color = SageWhite,
    disabledBackgroundColor: Color = SageBlack.copy(alpha = ContentAlpha.disabled),
    disabledContentColor: Color = SageWhite.copy(alpha = ContentAlpha.disabled)
): ButtonColors = DefaultButtonColors(
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    disabledBackgroundColor = disabledBackgroundColor,
    disabledContentColor = disabledContentColor
)

@Immutable
data class DefaultButtonColors(
    private val backgroundColor: Color,
    private val contentColor: Color,
    private val disabledBackgroundColor: Color,
    private val disabledContentColor: Color
) : ButtonColors {
    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) backgroundColor else disabledBackgroundColor)
    }

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) contentColor else disabledContentColor)
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