package org.sagebionetworks.assessmentmodel.survey

import androidx.compose.runtime.mutableStateOf

actual class ChoiceInputItemStateImpl actual constructor(index: Int,
                                                         inputItem: ChoiceInputItem,
                                                         selected: Boolean) : ChoiceInputItemState {
    override val index: Int = index
    override val inputItem: ChoiceInputItem = inputItem
    override var selected: Boolean
        get() = selectedState.value
        set(value) {selectedState.value = value}
    var selectedState = mutableStateOf(selected)
}
