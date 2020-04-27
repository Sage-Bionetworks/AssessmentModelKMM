package org.sagebionetworks.assessmentmodel.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sagebionetworks.assessmentmodel.Step
import org.sagebionetworks.assessmentmodel.presentation.databinding.DebugStepFragmentBinding

open class DebugStepFragment: StepFragment() {

    private var _binding: DebugStepFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var step: Step

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = DebugStepFragmentBinding.inflate(layoutInflater, container, false)
        step = stepViewModel.nodeState.node as Step
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.raw.text = step.toString()
        binding.navBar.navBarNext.setOnClickListener { assessmentViewModel.goForward() }
        binding.navBar.navBarBack.setOnClickListener { assessmentViewModel.goBackward() }
        binding.header.closeBtn.setOnClickListener{ assessmentViewModel.cancel() }
    }

}