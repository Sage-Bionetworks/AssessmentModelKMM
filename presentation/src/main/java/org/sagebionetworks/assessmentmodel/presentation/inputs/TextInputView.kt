package org.sagebionetworks.assessmentmodel.presentation.inputs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.serialization.json.jsonPrimitive
import org.sagebionetworks.assessmentmodel.presentation.databinding.TextInputViewBinding
import org.sagebionetworks.assessmentmodel.survey.*


class TextInputView : LinearLayout {

    val binding: TextInputViewBinding
    val textInput: TextInputEditText
    val textInputLayout: TextInputLayout

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        binding = TextInputViewBinding.inflate(LayoutInflater.from(context), this)
        textInput = binding.textInput
        textInputLayout = binding.textInputLayout
        orientation = VERTICAL
    }

    fun setup(inputItemState: KeyboardInputItemState<*>) {
        textInput.inputType = inputItemState.inputItem.keyboardOptions.inputTypeMask()

        textInputLayout.hint = inputItemState.inputItem.placeholder
        if (inputItemState.selected) {
            textInput.setText(
                inputItemState.textValidator.valueFor(inputItemState.currentAnswer?.jsonPrimitive).toString()
            )
        }
    }

    fun updateResult(inputItemState: KeyboardInputItemState<*>) {
        val textValidator: TextValidator<Any?> = inputItemState.textValidator as TextValidator<Any?>
        val formattedValue: FormattedValue<out Any?>? = inputItemState.textValidator.valueFor(textInput.text.toString())
        formattedValue?.result.let {
            inputItemState.currentAnswer = textValidator.jsonValueFor(it)
        }
        formattedValue?.invalidMessage?.let {
            textInputLayout.error = it.toString()
        }
    }


}