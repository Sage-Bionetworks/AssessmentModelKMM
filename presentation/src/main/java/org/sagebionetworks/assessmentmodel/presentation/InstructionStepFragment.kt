package org.sagebionetworks.assessmentmodel.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.sagebionetworks.assessmentmodel.navigation.NodeState
import org.sagebionetworks.assessmentmodel.presentation.databinding.StepFragmentBinding

open class InstructionStepFragment: StepFragment() {

    private var _binding: StepFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = StepFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.textView.text = stepViewModel.nodeState.node.toString()
        binding.navBar.navBarNext.setOnClickListener { assessmentViewModel.goforward() }
        binding.navBar.navBarBack.setOnClickListener { assessmentViewModel.goBackward() }
    }

}