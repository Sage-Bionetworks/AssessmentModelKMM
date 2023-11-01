package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.json.JsonElement

actual class ChoiceInputItemStateImpl actual constructor(index: Int,
                                                         inputItem: ChoiceInputItem,
                                                         selected: Boolean) : ChoiceInputItemState {
    override val index: Int = index
    override val inputItem: ChoiceInputItem = inputItem
    override var selected: Boolean = selected
}

actual class KeyboardInputItemStateImpl<T> actual constructor(override val index: Int,
                                    override val inputItem: KeyboardTextInputItem<T>,
                                    override var storedAnswer: JsonElement?) : KeyboardInputItemState<T> {
    override var selected: Boolean = storedAnswer != null
    override val textValidator = inputItem.buildTextValidator()

}

