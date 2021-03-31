package org.sagebionetworks.assessmentmodel.presentation.inputs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.databinding.CheckboxInputViewBinding

class CheckboxInputView : LinearLayout {

    val binding: CheckboxInputViewBinding

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        binding = CheckboxInputViewBinding.inflate(LayoutInflater.from(context))
        orientation = VERTICAL
    }

    fun setup() {

    }

    fun updateResult() {

    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.checkbox_zero -> {
                    if (checked) {
                        // do something
                    } else {
                        // not checked

                    }
                }
                R.id.checkbox_one -> {
                    if (checked) {
                        // do something
                    } else {
                        // not checked
                    }
                }
            }
        }
    }
}