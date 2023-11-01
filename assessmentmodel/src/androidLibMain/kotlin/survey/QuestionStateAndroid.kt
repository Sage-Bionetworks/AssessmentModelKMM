package org.sagebionetworks.assessmentmodel.survey

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.json.JsonElement

interface InputItemStateAndroid: InputItemState {
    var selectedState: MutableState<Boolean>
}

actual class ChoiceInputItemStateImpl actual constructor(index: Int,
                                                         inputItem: ChoiceInputItem,
                                                         selected: Boolean) : ChoiceInputItemState, InputItemStateAndroid {
    override val index: Int = index
    override val inputItem: ChoiceInputItem = inputItem
    override var selected: Boolean
        get() = selectedState.value
        set(value) {selectedState.value = value}
    override var selectedState = mutableStateOf(selected)
}

actual class KeyboardInputItemStateImpl<T> actual constructor(index: Int,
                                                              inputItem: KeyboardTextInputItem<T>,
                                                              storedAnswer: JsonElement?) : KeyboardInputItemState<T>, InputItemStateAndroid {
    override val index: Int = index
    override val inputItem: KeyboardTextInputItem<T> = inputItem
    override var storedAnswer: JsonElement? = storedAnswer
    override val textValidator = inputItem.buildTextValidator()
    override var selected: Boolean
        get() = selectedState.value
        set(value) {selectedState.value = value}
    override var selectedState = mutableStateOf(storedAnswer != null)
}
