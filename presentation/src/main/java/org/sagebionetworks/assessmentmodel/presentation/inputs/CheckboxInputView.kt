package org.sagebionetworks.assessmentmodel.presentation.inputs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import org.sagebionetworks.assessmentmodel.presentation.databinding.CheckboxInputViewBinding
import org.sagebionetworks.assessmentmodel.survey.*

class CheckboxInputView : LinearLayout {

    val binding: CheckboxInputViewBinding
    lateinit var placeholder: String

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        binding = CheckboxInputViewBinding.inflate(LayoutInflater.from(context), this)
        orientation = VERTICAL
    }

    fun setup(questionStep : SimpleQuestion) {
        val input = questionStep.inputItem as CheckboxInputItem
        //placeholder = questionStep.inputItem.fieldLabel.toString()
        placeholder = input.fieldLabel.toString()
    }

    fun updateResult(inputItem: CheckboxInputItem) {
        inputItem.jsonValue(isSelected);
    }
}