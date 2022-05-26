package org.sagebionetworks.assessmentmodel.presentation.compose

import android.app.TimePickerDialog
import android.text.format.DateFormat
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import kotlinx.datetime.toKotlinLocalTime
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.sagebionetworks.assessmentmodel.presentation.AssessmentViewModel
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageBlack
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.sageButton
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.sageP2
import org.sagebionetworks.assessmentmodel.survey.KeyboardInputItemState
import org.sagebionetworks.assessmentmodel.survey.QuestionState
import org.sagebionetworks.assessmentmodel.survey.TimeInputItem
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
internal fun TimeQuestion(
    questionState: QuestionState,
    assessmentViewModel: AssessmentViewModel,
) {
    val itemState = questionState.itemStates[0] as KeyboardInputItemState<*>
    val inputItem = itemState.inputItem as TimeInputItem
    val timeFormat = inputItem.formatOptions

    QuestionContainer(
        questionState = questionState,
        assessmentViewModel = assessmentViewModel
    ) {
        val timeValueString = itemState.currentAnswer?.jsonPrimitive?.contentOrNull
        val timeValue = timeValueString?.let {
            LocalTime.parse(it)
        } ?: LocalTime.now()

        var isError by remember { mutableStateOf(false)}
        var time by remember { mutableStateOf(timeValue)}
        val clickString = stringResource(R.string.click_to_pick_time)
        var timeDisplay by remember { mutableStateOf(clickString) }
        if (itemState.currentAnswer != null) {
            timeDisplay = time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        }

        val timePickerDialog = TimePickerDialog(LocalContext.current,
            { _, hour: Int, minute: Int ->
                time = LocalTime.of(hour, minute)
                timeDisplay = time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                if (timeFormat.isInRange(time.toKotlinLocalTime())) {
                    questionState.saveAnswer(JsonPrimitive(time.toString()), itemState)
                    isError = false
                } else {
                    // Show error
                    questionState.saveAnswer(null, itemState)
                    isError = true
                }
            }, time.hour, time.minute, DateFormat.is24HourFormat(LocalContext.current)
        )

        //TODO: Work with Lynn on how we show currently selected time to user -nbrown 05/26/2022
        TextButton(
            onClick = { timePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = timeDisplay,
                textDecoration = TextDecoration.Underline,
                color = SageBlack,
                style = sageButton
            )
        }
        if (isError) {
            //TODO: Work with Lynn to figure out how we want to inform user of error -nbrown 05/26/2022
            Text(
                text = stringResource(R.string.please_select_a_time),
                color = Color.Red,
                style = sageP2
            )
        }
    }
}