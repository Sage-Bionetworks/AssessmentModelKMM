package org.sagebionetworks.assessmentmodel.sampleapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class StepFragment: Fragment() {

    protected lateinit var stepViewModel: StepViewModel

    protected lateinit var assessmentViewModel: AssessmentViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        assessmentViewModel = (parentFragment as AssessmentFragment).viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stepViewModel = ViewModelProvider(
                this, StepViewModelFactory()
                .create(assessmentViewModel.currentNodeStateLiveData.value!!.nodeState))
                .get(StepViewModel::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.step_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //TODO: We should use view binding when Android Studio 3.6 and/or IntelliJ 2020.1 is released -nbrown 02/13/2020
        this.view?.findViewById<TextView>(R.id.textView)?.text = stepViewModel.nodeState.node.toString()
        this.view?.findViewById<Button>(R.id.next_button)?.setOnClickListener { assessmentViewModel.goforward() }
    }

}