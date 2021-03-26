package org.sagebionetworks.assessmentmodel.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.button.MaterialButton
import org.sagebionetworks.assessmentmodel.ButtonAction
import org.sagebionetworks.assessmentmodel.ButtonActionInfo
import org.sagebionetworks.assessmentmodel.Step
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.databinding.StepNavigationBarBinding
import org.sagebionetworks.assessmentmodel.serialization.loadDrawable
import org.sagebionetworks.assessmentmodel.survey.Question

class StepNavigationBar: LinearLayout {

    lateinit var binding: StepNavigationBarBinding

    constructor(context: Context) : super(context){
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet):    super(context, attrs){
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?,    defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        val view = View.inflate(context, R.layout.step_navigation_bar, this)
        binding = StepNavigationBarBinding.bind(view)
    }

    fun setup(step: Step) {
        for (button in step.hideButtons) {
            when(button) {
                ButtonAction.Navigation.GoForward -> binding.navBarNext.visibility = View.GONE
                ButtonAction.Navigation.GoBackward -> binding.navBarBack.visibility = View.GONE
                ButtonAction.Navigation.Skip -> binding.skipButton.visibility = View.GONE
            }
        }
        for (buttonInfo in step.buttonMap.values) {
            when (buttonInfo) {
                binding.skipButton.text -> buttonInfo.buttonTitle
            }
        }

        step.buttonMap[ButtonAction.Navigation.GoForward]?.let { button ->
            configureButton(binding.navBarNext, button)
        }
        step.buttonMap[ButtonAction.Navigation.GoBackward]?.let { button ->
            configureButton(binding.navBarBack, button)
        }
        step.buttonMap[ButtonAction.Navigation.Skip]?.let{ button ->
            configureButton(binding.skipButton, button)
        }
        if (step is Question) {
            if (step.optional) {
                binding.skipButton.visibility = View.VISIBLE
            }
        } else {
            binding.skipButton.visibility = View.INVISIBLE
        }
    }

    private fun configureButton(button: MaterialButton, buttonAction: ButtonActionInfo) {
        buttonAction.buttonTitle?.let { title ->
            button.text = title
        }
        buttonAction.imageInfo?.let {
            button.icon = it.loadDrawable(context)
        }
    }

    fun setForwardOnClickListener(l: (View) -> Unit) {
        binding.navBarNext.setOnClickListener(l)
    }

    fun setBackwardOnClickListener(l: (View) -> Unit) {
        binding.navBarBack.setOnClickListener(l)
    }

    fun setSkipOnClickListener(l: (View) -> Unit) {
        binding.skipButton.setOnClickListener(l)
    }
}