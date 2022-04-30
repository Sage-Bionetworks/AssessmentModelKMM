package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.sagebionetworks.assessmentmodel.ButtonAction
import org.sagebionetworks.assessmentmodel.presentation.AssessmentViewModel
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.*
import org.sagebionetworks.assessmentmodel.serialization.IntFormatter
import org.sagebionetworks.assessmentmodel.serialization.IntegerTextInputItemObject
import org.sagebionetworks.assessmentmodel.serialization.YearTextInputItemObject
import org.sagebionetworks.assessmentmodel.survey.*
import kotlin.math.roundToInt

@Composable
internal fun NumericQuestion(
    questionState: QuestionState,
    assessmentViewModel: AssessmentViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(BackgroundGray)
    ) {
        val question = questionState.node as SimpleQuestion
        val itemState = questionState.itemStates[0] as KeyboardInputItemState<*>
        val uiFormatOptions = extractFormatOptions(questionState)

        val scrollState = rememberScrollState()
        QuestionHeader(
            subtitle = uiFormatOptions.getSubTitle(question = question),
            title = questionState.node.title,
            detail = uiFormatOptions.getDetail(question = question),
            assessmentViewModel = assessmentViewModel,
            scrollState = scrollState
        )
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(scrollState),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            if (question.uiHint == UIHint.NumberField.Likert) {
                var likertValue by remember{ mutableStateOf(uiFormatOptions.startValue)}
                LikertScale(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    minValue = uiFormatOptions.minVal!!,
                    maxValue = uiFormatOptions.maxVal!!,
                    numSelected = likertValue,
                    onCircleTap = { num ->
                        likertValue = num
                        questionState.saveAnswer(JsonPrimitive(num), itemState)
                    })
            } else {
                val textValidator = itemState.textValidator as IntFormatter
                val sliderMin = uiFormatOptions.minVal ?: 0
                val sliderStartValue = uiFormatOptions.startValue ?: sliderMin

                var numberTextValue by remember { mutableStateOf(uiFormatOptions.startValue?.toString() ?: "") }
                var sliderPosition by remember { mutableStateOf(sliderStartValue.toFloat()) }
                var isError by remember { mutableStateOf(false)}
                var errorText by remember { mutableStateOf("") }
                fun updateAnswer(value: String) {
                    numberTextValue = value
                    val formattedVal = textValidator.valueFor(numberTextValue)
                    if (formattedVal.invalidMessage != null) {
                        isError = true
                        errorText = formattedVal.invalidMessage.toString()
                        questionState.saveAnswer(
                            textValidator.jsonValueFor(formattedVal.result),
                            itemState
                        )
                    } else {
                        isError = false
                        errorText = ""
                        sliderPosition = numberTextValue.toFloatOrNull() ?: sliderMin.toFloat()
                        questionState.saveAnswer(
                            textValidator.jsonValueFor(formattedVal.result),
                            itemState
                        )
                    }
                }
                IntegerTextField(
                    modifier = Modifier
                        .width(100.dp)
                        .align(Alignment.CenterHorizontally),
                    numberTextValue = numberTextValue,
                    placeHolder = itemState.inputItem.placeholder,
                    updateAnswer = { value -> updateAnswer(value) },
                    isError = isError
                )
                if (uiFormatOptions.showScale) {
                    Spacer(modifier = Modifier.height(16.dp))
                    IntegerSlider(
                        sliderPosition = sliderPosition,
                        minVal = uiFormatOptions.minVal!!, maxVal = uiFormatOptions.maxVal!!,
                        updateAnswer = { value -> updateAnswer(value) }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            val forwardButtonAction = questionState.node.buttonMap.get(ButtonAction.Navigation.GoForward)
            BottomNavigation(
                { assessmentViewModel.goBackward() },
                { assessmentViewModel.goForward() },
                nextText = forwardButtonAction?.buttonTitle,
                nextEnabled = questionState.allAnswersValidFlow.collectAsState().value
            )
        }
    }
}

@Composable
private fun IntegerSlider(
    sliderPosition: Float,
    minVal: Int,
    maxVal: Int,
    updateAnswer: (String) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(minVal.toString())
        Slider(
            modifier = Modifier.weight(1f),
            value = sliderPosition,
            onValueChange = {
                updateAnswer(it.roundToInt().toString())
            },
            valueRange = minVal.toFloat()..maxVal.toFloat(),
            steps = (maxVal - minVal) - 1,
            onValueChangeFinished = {
                // This doesn't appear to be called when tapping slider -nbrown 04/29/22
                //updateState()
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.primary,
                activeTrackColor = MaterialTheme.colors.onBackground,
                inactiveTrackColor = SliderGray,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            )
        )
        Text(maxVal.toString())
    }
}

@Composable
private fun IntegerTextField(
    numberTextValue: String,
    updateAnswer: (String) -> Unit,
    placeHolder: String?,
    isError: Boolean,
    modifier: Modifier = Modifier
    ) {
     val focusManager = LocalFocusManager.current
     OutlinedTextField(
         value = numberTextValue,
         modifier = modifier,
         singleLine = true,
         onValueChange = {
             updateAnswer(it)
         },
         label = {
             if (placeHolder != null) {
                 Text(placeHolder)
             }
         },
         isError = isError,
         keyboardOptions = KeyboardOptions(
             keyboardType = KeyboardType.NumberPassword,
             imeAction = ImeAction.Done
         ),
         keyboardActions = KeyboardActions(
             onDone = {
                 focusManager.clearFocus()
             }),
         textStyle = sageP2,
         colors = TextFieldDefaults.textFieldColors(
             backgroundColor = SageWhite,
             cursorColor = SageBlack,
             focusedIndicatorColor = SageBlack,
             focusedLabelColor = SageBlack
         )
     )
}

data class UiFormatOptions (
    val minVal: Int?,
    val maxVal: Int?,
    val minLabel: String?,
    val maxLabel: String?,
    val showScale: Boolean,
    val startValue: Int?
) {

    @Composable
    fun getSubTitle(question: SimpleQuestion) : String? {
        var subTitle = question.subtitle
        if (subTitle == null && showScale && minVal != null && maxVal != null) {
            subTitle = stringResource(id = R.string.on_a_scale, minVal, maxVal)
        }
        return subTitle
    }

    @Composable
    fun getDetail(question: SimpleQuestion) : String? {
        var detail = question.detail
        if (detail == null && showScale) {
            detail = "$minVal = $minLabel\n$maxVal = $maxLabel"
        }
        return detail
    }

}

fun extractFormatOptions(questionState: QuestionState) : UiFormatOptions {
    val question = questionState.node as SimpleQuestion
    var minVal: Int? = null
    var maxVal: Int? = null
    var minLabel: String? = null
    var maxLabel: String? = null
    val inputItem = question.inputItem as KeyboardTextInputItem<*>
    when (inputItem) {
        is IntegerTextInputItemObject -> {
            minVal = inputItem.formatOptions.minimumValue
            maxVal = inputItem.formatOptions.maximumValue
            minLabel = inputItem.formatOptions.minimumLabel
            maxLabel = inputItem.formatOptions.maximumLabel
        }
        is YearTextInputItemObject -> {
            minVal = inputItem.formatOptions.minimumValue
            maxVal = inputItem.formatOptions.maximumValue
            minLabel = inputItem.formatOptions.minimumLabel
            maxLabel = inputItem.formatOptions.maximumLabel
        }
    }
    var showScale = false
    if (question.uiHint == UIHint.NumberField.Slider) {
        showScale = minVal != null && maxVal != null && minVal < maxVal
    } else if (question.uiHint == UIHint.NumberField.Likert) {
        showScale = true
        if (minVal == null) {
            minVal = 1
        }
        if (maxVal == null) {
            maxVal = 5
        }
    }
    val itemState = questionState.itemStates[0] as KeyboardInputItemState<*>
    var startValue: Int? = itemState.currentAnswer?.jsonPrimitive?.intOrNull
    if (question.uiHint == UIHint.NumberField.Slider) {
        startValue = startValue ?: minVal
    }
    return UiFormatOptions(
        minVal = minVal,
        maxVal = maxVal,
        minLabel = minLabel,
        maxLabel = maxLabel,
        showScale = showScale,
        startValue = startValue
    )
}

@Preview
@Composable
private fun NumberQuestionPreview() {

    SageSurveyTheme {
    }
}