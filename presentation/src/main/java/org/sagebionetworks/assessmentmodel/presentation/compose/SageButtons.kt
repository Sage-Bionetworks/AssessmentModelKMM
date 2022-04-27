package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageBlack
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageWhite
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.sageButton

@Composable
fun SageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColors: ButtonColors = whiteButtonColors(),
    drawBorder: Boolean = true,
    enabled: Boolean = true,
    text: String? = null,
    @DrawableRes iconDrawable: Int? = null,
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
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = text,
                style = sageButton
            )
        }
        if (iconDrawable != null)
            Icon(
                painter = painterResource(id = iconDrawable),
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
    @DrawableRes iconDrawable: Int? = null,
    iconContentDescription: String? = null
) {
    SageButton(
        onClick = onClick,
        modifier = modifier,
        buttonColors = whiteButtonColors(),
        drawBorder = true,
        enabled = enabled,
        text = text,
        iconDrawable = iconDrawable,
        iconContentDescription = iconContentDescription
        )
}

@Composable
fun BlackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String? = null,
    @DrawableRes iconDrawable: Int? = null,
    iconContentDescription: String? = null
) {
    SageButton(
        onClick = onClick,
        modifier = modifier,
        buttonColors = blackButtonColors(),
        drawBorder = false,
        enabled = enabled,
        text = text,
        iconDrawable = iconDrawable,
        iconContentDescription = iconContentDescription
    )
}

@Composable
fun WhiteBackButton(
    onClick: () -> Unit,
    enabled: Boolean = true) {
    WhiteButton(onClick = onClick,
        iconDrawable = R.drawable.ic_left_arrow,
        enabled = enabled,
        modifier = Modifier.width(56.dp))
}

@Composable
fun BlackNextButton(
    onClick: () -> Unit,
    enabled: Boolean = true) {
    BlackButton(onClick = onClick,
        iconDrawable = R.drawable.ic_right_arrow,
        enabled = enabled,
        modifier = Modifier.width(56.dp))
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