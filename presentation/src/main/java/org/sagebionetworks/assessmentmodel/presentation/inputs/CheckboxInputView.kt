package org.sagebionetworks.assessmentmodel.presentation.inputs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import kotlinx.serialization.json.jsonPrimitive
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.databinding.CheckboxInputViewBinding
import org.sagebionetworks.assessmentmodel.survey.KeyboardInputItemState
import org.sagebionetworks.assessmentmodel.survey.QuestionState
import org.sagebionetworks.assessmentmodel.survey.SimpleQuestion
import org.sagebionetworks.assessmentmodel.survey.inputTypeMask
import java.awt.font.TextAttribute

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
        placeholder = questionStep.inputItem.fieldLabel.toString()
    }

    fun updateResult() {

    }
}