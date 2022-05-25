package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.job
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.sagebionetworks.assessmentmodel.ButtonAction
import org.sagebionetworks.assessmentmodel.presentation.AssessmentViewModel
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SliderGray
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.sageP2
import org.sagebionetworks.assessmentmodel.survey.KeyboardInputItemState
import org.sagebionetworks.assessmentmodel.survey.QuestionState
import org.sagebionetworks.assessmentmodel.survey.TextValidator

@Composable
internal fun TextQuestion(
    questionState: QuestionState,
    assessmentViewModel: AssessmentViewModel,
    maxChars: Int = 25
) {
    val itemState = questionState.itemStates[0] as KeyboardInputItemState<*>
    val textValidator: TextValidator<String> = itemState.textValidator as TextValidator<String>
    var textValue by remember { mutableStateOf(itemState.currentAnswer?.jsonPrimitive?.contentOrNull ?: "") }
    fun updateAnswer(value: String) {
        textValue = value
        val jsonVal = if (value.isNotBlank()) {
            textValidator.jsonValueFor(value)
        } else {
            null
        }
        questionState.saveAnswer(jsonVal, itemState)
    }

    QuestionContainer(
        subtitle = questionState.node.subtitle,
        title = questionState.node.title,
        detail = questionState.node.detail ?: stringResource(id = R.string.maximum_characters, maxChars),
        nextButtonText = questionState.node.buttonMap.get(ButtonAction.Navigation.GoForward)?.buttonTitle,
        nextEnabled = questionState.allAnswersValidFlow.collectAsState().value,
        assessmentViewModel = assessmentViewModel
    ) {

        TextInput(
            maxChars = maxChars,
            textValue = textValue,
            updateAnswer = { value -> updateAnswer(value) }

        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun TextInput(
    maxChars: Int,
    textValue: String,
    updateAnswer: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        this.coroutineContext.job.invokeOnCompletion {
            focusRequester.requestFocus()
        }
    }
    val focusManager = LocalFocusManager.current
    var atMax by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        val colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.Transparent)
        val interactionSource = remember { MutableInteractionSource() }
        val enabled = true
        val singleLine = false
        BasicTextField(
            value = textValue,
            onValueChange = {
                if (it.length <= maxChars) {
                    updateAnswer(it)
                }
                atMax = it.length >= maxChars
            },
            modifier = Modifier
                .defaultMinSize(minHeight = 150.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }),
            interactionSource = interactionSource,
            textStyle = sageP2
        ) {
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = textValue,
                visualTransformation = VisualTransformation.None,
                innerTextField = it,
                // same interaction source as the one passed to BasicTextField to read focus state
                // for text field styling
                interactionSource = interactionSource,
                enabled = enabled,
                singleLine = singleLine,
                placeholder = {Text(stringResource(R.string.tap_to_respond))},

                // update border thickness and shape
                border = {
                    TextFieldDefaults.BorderBox(
                        enabled = enabled,
                        isError = false,
                        colors = colors,
                        interactionSource = interactionSource,
                        shape = RectangleShape,
                        unfocusedBorderThickness = 1.dp,
                        focusedBorderThickness = 0.dp
                    )
                }
            )
        }
        if (atMax) {
            Text(
                modifier = Modifier.align(alignment = Alignment.End),
                text = "[$maxChars]",
                color = SliderGray,
                style = sageP2
            )
        }
    }
}