package org.sagebionetworks.assessmentmodel.presentation.compose

import android.widget.EditText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.JsonPrimitive
import org.sagebionetworks.assessmentmodel.survey.*

@Composable
internal fun QuestionContent(
    questionState: QuestionState,
    modifier: Modifier = Modifier
) {
    val question = questionState.node as ChoiceQuestion

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))
            if (question.title != null) {
                QuestionTitle(question.title!!)
                Spacer(modifier = Modifier.height(24.dp))
            }
            if (question.subtitle != null) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = question.subtitle!!,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(bottom = 18.dp, start = 8.dp, end = 8.dp)
                    )
                }
            }
            MultipleChoiceQuestion(
                questionState = questionState,
                modifier = Modifier.fillParentMaxWidth())

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
                        title = inputState.inputItem.fieldLabel!!,
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
//            val choiceItemState = inputState as ChoiceInputItemStateImpl
//            ChoiceQuestionInput(
//                title = choiceItemState.inputItem.fieldLabel!!,
//                choiceSelected = choiceItemState.selectedState.value,
//                singleChoice = question.singleAnswer,
//                onChoiceSelected = { selected ->
//                    questionState.didChangeSelectionState(selected, choiceItemState)
//                }
//            )

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

