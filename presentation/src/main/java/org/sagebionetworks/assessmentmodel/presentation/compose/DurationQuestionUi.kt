package org.sagebionetworks.assessmentmodel.presentation.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.sagebionetworks.assessmentmodel.presentation.AssessmentViewModel
import org.sagebionetworks.assessmentmodel.survey.AnyInputItemState
import org.sagebionetworks.assessmentmodel.survey.DurationInputItem
import org.sagebionetworks.assessmentmodel.survey.DurationUnit
import org.sagebionetworks.assessmentmodel.survey.QuestionState
import kotlin.math.roundToInt

@Composable
internal fun DurationQuestion(
    questionState: QuestionState,
    assessmentViewModel: AssessmentViewModel,
) {
    val itemState = questionState.itemStates[0] as AnyInputItemState
    val item = itemState.inputItem as DurationInputItem

    QuestionContainer(
        questionState = questionState,
        assessmentViewModel = assessmentViewModel
    ) {
        Row() {
            Spacer(modifier = Modifier.weight(1f))
            val inputFields = mutableListOf<DurationInputField>()

            var remaining: Double = itemState.currentAnswer?.jsonPrimitive?.doubleOrNull ?: 0.0
            var count = 0
            for (displayUnit in item.displayUnits) {
                count++
                val amount = remember {
                    mutableStateOf(if (itemState.currentAnswer != null) {
                        Math.floor(remaining / displayUnit.secondsMultiplier).roundToInt()
                    } else {
                        null
                    })
                }
                inputFields.add(DurationInputField(amount, displayUnit))
                remaining -= (amount.value ?: 0) * displayUnit.secondsMultiplier
                IntegerTextField(
                    numberTextValue = amount.value?.toString() ?: "",
                    inputFieldLabel = displayUnit.name,
                    updateAnswer = {
                        amount.value = it.toIntOrNull() ?: 0
                        val answer = inputFields.fold(0.0) { sum, amt ->
                            sum + ((amt.amount.value ?: 0) * amt.durationUnit.secondsMultiplier)
                        }
                        questionState.saveAnswer(
                            JsonPrimitive(answer), itemState
                        )
                                   },
                    placeHolder = null,
                    isError = false,
                    modifier = Modifier.width(125.dp)
                )
                if (count < item.displayUnits.size) {
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

private data class DurationInputField(val amount: MutableState<Int?>, val durationUnit: DurationUnit)

