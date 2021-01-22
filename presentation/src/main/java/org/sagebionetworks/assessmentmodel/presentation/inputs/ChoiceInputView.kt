package org.sagebionetworks.assessmentmodel.presentation.inputs

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.databinding.ChoiceItemBinding
import org.sagebionetworks.assessmentmodel.survey.ChoiceInputItemState
import org.sagebionetworks.assessmentmodel.survey.ChoiceQuestion
import org.sagebionetworks.assessmentmodel.survey.QuestionState

class ChoiceInputView : LinearLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        orientation = VERTICAL
    }


    val choiceBindingList: MutableList<ChoiceItemBinding> = mutableListOf()

    fun setup(questionState: QuestionState) {
        assert(questionState.node is ChoiceQuestion)
        val inflater = LayoutInflater.from(context)
        for (inputState in questionState.itemStates) {
            //TODO: Handle otherInputItem -nbrown 03/26/20
            val choiceItemState = inputState as ChoiceInputItemState
            val choiceItemBinding = ChoiceItemBinding.inflate(inflater, this, true)
            choiceBindingList.add(choiceItemBinding)
            choiceItemBinding.choiceRow.tag = choiceItemState
            choiceItemBinding.singleChoiceText.text = choiceItemState.inputItem.fieldLabel
            choiceItemBinding.choiceRow.isSelected = choiceItemState.selected
            choiceItemBinding.choiceRow.setOnClickListener {
                questionState.didChangeSelectionState(!choiceItemState.selected, choiceItemState)
                updateSelectedState()
            }

        }

    }

    private fun updateSelectedState() {
        for (binding in choiceBindingList) {
            binding.choiceRow.isSelected = (binding.choiceRow.tag as ChoiceInputItemState).selected
            if (binding.choiceRow.isSelected) {
                //TODO: Figure out better way of setting background for selected rows based on theme -nbrown 03/26/20
                val typedValue = TypedValue()
                context.theme.resolveAttribute(R.attr.colorControlHighlight, typedValue, true)
                binding.choiceRow.setBackgroundColor(resources.getColor(typedValue.resourceId))
            } else {
                binding.choiceRow.setBackground(null)
            }
        }

    }
}