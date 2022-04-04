package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.JsonPrimitive
import org.sagebionetworks.assessmentmodel.presentation.AssessmentViewModel
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageBlack
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.sageH1
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.sageP1
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.sageP2
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
                    style = sageP2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp, bottom = 0.dp, start = 24.dp, end = 8.dp)
                )
            }
        }
        question.title?.let { title ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = sageH1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, bottom = 0.dp, start = 24.dp, end = 8.dp)
            )
        }
        question.detail?.let { detail ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = detail,
                style = sageP2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, bottom = 0.dp, start = 24.dp, end = 8.dp)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(answerBackgroundColor)
                .clickable(
                    onClick = {
                        onChoiceSelected(!choiceSelected)
                    }
                )
                .padding(vertical = 8.dp, horizontal = 8.dp),
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
                        selectedColor = SageBlack
                    )
                )
            } else {
                Checkbox(
                    checked = choiceSelected,
                    onCheckedChange = { selected ->
                        onChoiceSelected(selected)
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
                        text = inputItemState.inputItem.fieldLabel?: "Other:",
                        style = sageP1
                    )
                    TextField(
                        modifier = Modifier.padding(end = 20.dp),
                        value = text,
                        onValueChange = {
                            text = it
                            inputItemState.currentAnswer = JsonPrimitive(it)
                            if (it.isNotEmpty()) {
                                onChoiceSelected(true)
                            }
                        },
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

