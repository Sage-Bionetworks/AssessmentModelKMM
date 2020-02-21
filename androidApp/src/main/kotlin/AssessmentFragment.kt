package org.sagebionetworks.assessmentmodel.sampleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.sagebionetworks.assessmentmodel.Step
import org.sagebionetworks.assessmentmodel.serialization.FileAssessmentProvider
import org.sagebionetworks.assessmentmodel.serialization.FileLoaderAndroid


class AssessmentFragment : Fragment() {

    companion object {
        fun newInstance() = AssessmentFragment()
    }

    lateinit var viewModel: AssessmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.assessment_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val assessmentProvider = FileAssessmentProvider(FileLoaderAndroid(resources))
        viewModel = ViewModelProvider(
                this, AssesmentViewModelFactory()
                .create("test_json", assessmentProvider))
                .get(AssessmentViewModel::class.java)

        viewModel.currentNodeStateLiveData
                .observe(this.viewLifecycleOwner, Observer<AssessmentViewModel.ShowNodeState>
                { showNodeState -> this.showStep(showNodeState) })
        viewModel.start()
    }

    private fun showStep(showNodeState: AssessmentViewModel.ShowNodeState) {
        val stepFragment = this.getFragmentForStep(showNodeState.nodeState.node as Step)
        val transaction = childFragmentManager.beginTransaction()
        transaction
                .replace(R.id.step_fragment_container, stepFragment)
                .commit()
        childFragmentManager.executePendingTransactions()
    }

    private fun getFragmentForStep(step: Step): Fragment {
        //TODO: need factory for loading step fragments -nbrown 02/13/2020
        val fragment = StepFragment()
        return fragment
    }

}
