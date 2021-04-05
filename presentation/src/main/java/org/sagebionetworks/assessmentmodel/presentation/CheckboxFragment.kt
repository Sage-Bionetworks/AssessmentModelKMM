package org.sagebionetworks.assessmentmodel.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import org.sagebionetworks.assessmentmodel.presentation.databinding.CheckboxFragmentBinding

/**
 * A simple [Fragment] subclass.
 * Use the [CheckboxFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckboxFragment : Fragment() {
    private var _binding: CheckboxFragmentBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = CheckboxFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}