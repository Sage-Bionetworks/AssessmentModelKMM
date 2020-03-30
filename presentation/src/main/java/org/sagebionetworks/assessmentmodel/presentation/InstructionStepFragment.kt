package org.sagebionetworks.assessmentmodel.presentation

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sagebionetworks.assessmentmodel.InstructionStep
import org.sagebionetworks.assessmentmodel.presentation.databinding.InstructionStepFragmentBinding
import org.sagebionetworks.assessmentmodel.serialization.loadDrawable

open class InstructionStepFragment: StepFragment() {

    private var _binding: InstructionStepFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var step: InstructionStep

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = InstructionStepFragmentBinding.inflate(layoutInflater, container, false)
        step = stepViewModel.nodeState.node as InstructionStep
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.title.text = step.title
        binding.detail.text = step.detail
        val drawable = step.imageInfo?.loadDrawable(context!!)
        binding.header.image.setImageDrawable(drawable)
        if (drawable is AnimationDrawable) {
            drawable.start()
        }
        binding.navBar.navBarNext.setOnClickListener { assessmentViewModel.goforward() }
        binding.navBar.navBarBack.setOnClickListener { assessmentViewModel.goBackward() }
    }

}