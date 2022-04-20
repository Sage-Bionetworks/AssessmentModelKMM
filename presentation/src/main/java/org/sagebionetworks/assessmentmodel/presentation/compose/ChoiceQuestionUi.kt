package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.JsonPrimitive
import org.sagebionetworks.assessmentmodel.presentation.AssessmentViewModel
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.BackgroundGray
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageBlack
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.sageP1
import org.sagebionetworks.assessmentmodel.survey.*

@Composable
internal fun QuestionContent(
    questionState: QuestionState,
    assessmentViewModel: AssessmentViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(BackgroundGray)

        ,
    ) {
        val scrollState = rememberScrollState()
        QuestionHeader(
            questionState = questionState,
            assessmentViewModel = assessmentViewModel,
            scrollState = scrollState
            )
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(scrollState),
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            MultipleChoiceQuestion(
                questionState = questionState,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
            BottomNavigation(
                { assessmentViewModel.goBackward() },
                { assessmentViewModel.goForward() },
                nextEnabled = questionState.allAnswersValidFlow.collectAsState().value
                )
        }
    }
}

@Composable
private fun MultipleChoiceQuestion(
    questionState: QuestionState,
    modifier: Modifier = Modifier
) {
    val question = questionState.node as ChoiceQuestion
    Column(modifier = modifier) {
        for (inputState in questionState.itemStates) {
            when (inputState) {
                is InputItemStateAndroid -> {
                    ChoiceQuestionInput(
                        inputItemState = inputState,
                        choiceSelected = inputState.selectedState.value,
                        singleChoice = question.singleAnswer,
                        onChoiceSelected = { selected ->
                            questionState.didChangeSelectionState(selected, inputState)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ChoiceQuestionInput(
    inputItemState: InputItemState,
    choiceSelected: Boolean,
    singleChoice: Boolean,
    onChoiceSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val answerBackgroundColor = if (choiceSelected) {
        MaterialTheme.colors.primary//.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colors.background
    }
    Surface(
        shape = if (singleChoice) {
            MaterialTheme.shapes.small.copy(CornerSize(100))
        } else {
            MaterialTheme.shapes.small},
        elevation = Dp(5f),
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        val focusRequester = remember { FocusRequester() }
        val onClick = { selected:Boolean ->
            onChoiceSelected(selected)
            if (selected && inputItemState is KeyboardInputItemState<*>) {
                focusRequester.requestFocus()
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(answerBackgroundColor)
                .clickable(
                    onClick = {onClick(!choiceSelected)}
                )
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (singleChoice) {
                RadioButton(
                    selected = choiceSelected,
                    onClick = {onClick(!choiceSelected)},
                    colors = RadioButtonDefaults.colors(
                        selectedColor = SageBlack
                    )
                )
            } else {
                Checkbox(
                    checked = choiceSelected,
                    onCheckedChange = { selected ->
                        onClick(selected)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = SageBlack
                    ),
                )
            }
            when (inputItemState) {
                is ChoiceInputItemStateImpl -> {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = inputItemState.inputItem.label,
                        style = sageP1
                    )
                }

                is KeyboardInputItemStateImpl<*> -> {
                    val curAnswer = (inputItemState.currentAnswer as? JsonPrimitive)?.content ?: ""
                    var text by remember { mutableStateOf(curAnswer) }
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = inputItemState.inputItem.fieldLabel?: stringResource(R.string.other),
                        style = sageP1
                    )
                    val focusManager = LocalFocusManager.current
                    if (!choiceSelected) {
                        focusManager.clearFocus()
                    }
                    TextField(
                        modifier = Modifier.padding(end = 20.dp)
                            .focusRequester(focusRequester),
                        value = text,
                        onValueChange = {
                            text = it
                            inputItemState.currentAnswer = JsonPrimitive(it)
                            if (it.isNotEmpty()) {
                                onChoiceSelected(true)
                            }
                        },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }),
                        textStyle = sageP1,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            cursorColor = SageBlack,
                            focusedIndicatorColor = SageBlack
                            )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChoiceQuestionInputPreview() {
    //BottomNavigation()
    //OtherInput(choiceSelected = true, singleChoice = true, onChoiceSelected = { } )
    //ChoiceQuestionInput(title = "Cat", choiceSelected = false, singleChoice = false, onChoiceSelected = { } )
}

