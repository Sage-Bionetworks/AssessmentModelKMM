package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.JsonPrimitive
import org.sagebionetworks.assessmentmodel.presentation.AssessmentViewModel
import org.sagebionetworks.assessmentmodel.survey.*

@Composable
internal fun QuestionContent(
    questionState: QuestionState,
    assessmentViewModel: AssessmentViewModel,
    modifier: Modifier = Modifier
) {
    val question = questionState.node as ChoiceQuestion

    Column(
        modifier = modifier.fillMaxHeight()
            .background(Color(0xFFF6F6F6))
            .padding(start = 20.dp, end = 32.dp)
            .verticalScroll(rememberScrollState())
            ,
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        question.subtitle?.let { subtitle ->
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 0.dp, start = 30.dp, end = 8.dp)
                )
            }
        }
        question.title?.let { title ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp, start = 30.dp, end = 8.dp)
            )
        }
        question.detail?.let { detail ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = detail,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp, start = 30.dp, end = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(48.dp))
        MultipleChoiceQuestion(
            questionState = questionState,
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.weight(1f))
        BottomNavigation({assessmentViewModel.goBackward()}, {assessmentViewModel.goForward()})
    }
}

@Composable
fun BottomNavigation(
    onBackClicked: () -> Unit,
    onNextClicked: () -> Unit
) {
    Row(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        .fillMaxWidth()) {
        FloatingActionButton(
            onClick = onBackClicked,
            shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
            backgroundColor = Color.White,
            ) {
            Icon(
                imageVector =Icons.Filled.KeyboardArrowLeft,
                contentDescription = "back",
            )

        }
        Spacer(modifier = Modifier.weight(1f))
        FloatingActionButton(
            onClick = onNextClicked,
            backgroundColor = Color.Black,
            contentColor = Color.White
            ) {
            Icon(
                imageVector =Icons.Filled.KeyboardArrowRight,
                contentDescription = "back",
            )
        }

    }
}

@Composable
private fun QuestionTitle(title: String) {
    val backgroundColor = if (MaterialTheme.colors.isLight) {
        MaterialTheme.colors.onSurface.copy(alpha = 0.04f)
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.06f)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp)
        )
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
                is ChoiceInputItemStateImpl -> {
                    ChoiceQuestionInput(
                        title = inputState.inputItem.label,
                        choiceSelected = inputState.selectedState.value,
                        singleChoice = question.singleAnswer,
                        onChoiceSelected = { selected ->
                            questionState.didChangeSelectionState(selected, inputState)
                        }
                    )
                }
                is KeyboardInputItemStateImpl<*> -> {
                    OtherInput(
                        inputItemState = inputState,
                        choiceSelected = inputState.selected,
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
private fun OtherInput(
    inputItemState: KeyboardInputItemState<*>,
    choiceSelected: Boolean,
    singleChoice: Boolean,
    onChoiceSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    //TODO: Handle otherInputItem -nbrown 03/26/20
    val answerBorderColor = if (choiceSelected) {
        MaterialTheme.colors.primary.copy(alpha = 0.5f)
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
    }
    val answerBackgroundColor = if (choiceSelected) {
        MaterialTheme.colors.primary.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colors.background
    }
    Surface(
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(
            width = 1.dp,
            color = answerBorderColor
        ),
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(answerBackgroundColor)
                .clickable(
                    onClick = {
                        onChoiceSelected(!choiceSelected)
                    }
                )
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val curAnswer = (inputItemState.currentAnswer as? JsonPrimitive)?.content ?: ""
            var text by remember { mutableStateOf(curAnswer) }

            if (singleChoice) {
                RadioButton(
                    selected = choiceSelected,
                    onClick = {
                        onChoiceSelected(!choiceSelected)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colors.primary
                    )
                )
            } else {
                Checkbox(
                    checked = choiceSelected,
                    onCheckedChange = { selected ->
                        onChoiceSelected(selected)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colors.primary
                    ),
                )
            }
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = "Other:")
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    inputItemState.currentAnswer = JsonPrimitive(it)
                    if (it.isNotEmpty()) {
                        onChoiceSelected(true)
                    }
                },
                label = { inputItemState.inputItem.fieldLabel?.let { Text(it) } }
            )
        }
    }
}


@Composable
private fun ChoiceQuestionInput(
    title: String,
    choiceSelected: Boolean,
    singleChoice: Boolean,
    onChoiceSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    //TODO: Handle otherInputItem -nbrown 03/26/20
    val answerBackgroundColor = if (choiceSelected) {
        MaterialTheme.colors.primary.copy(alpha = 0.12f)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(answerBackgroundColor)
                .clickable(
                    onClick = {
                        onChoiceSelected(!choiceSelected)
                    }
                )
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (singleChoice) {
                RadioButton(
                    selected = choiceSelected,
                    onClick = {
                        onChoiceSelected(!choiceSelected)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colors.primary
                    )
                )
            } else {
                Checkbox(
                    checked = choiceSelected,
                    onCheckedChange = { selected ->
                        onChoiceSelected(selected)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colors.primary
                    ),
                )
            }
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = title)
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

