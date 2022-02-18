package org.sagebionetworks.assessmentmodel.survey

actual class ChoiceInputItemStateImpl actual constructor(index: Int,
                                                         inputItem: ChoiceInputItem,
                                                         selected: Boolean) : ChoiceInputItemState {
    override val index: Int = index
    override val inputItem: ChoiceInputItem = inputItem
    override var selected: Boolean = selected
}