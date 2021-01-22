package org.sagebionetworks.assessmentmodel.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.serialization.json.Json
import org.sagebionetworks.assessmentmodel.BranchNode
import org.sagebionetworks.assessmentmodel.InstructionStep
import org.sagebionetworks.assessmentmodel.Step
import org.sagebionetworks.assessmentmodel.navigation.BranchNodeState
import org.sagebionetworks.assessmentmodel.navigation.CustomBranchNodeStateProvider
import org.sagebionetworks.assessmentmodel.navigation.NavigationPoint
import org.sagebionetworks.assessmentmodel.serialization.*
import org.sagebionetworks.assessmentmodel.survey.ChoiceQuestion
import org.sagebionetworks.assessmentmodel.survey.SimpleQuestion


open class AssessmentFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newFragmentInstance() = AssessmentFragment()

        const val ASSESSMENT_RESULT = "AsssessmentResult"
    }


    lateinit var viewModel: AssessmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        var nodeState: BranchNodeState? = null
        if (parentFragment is AssessmentFragment) {
            (parentFragment as AssessmentFragment).let {
                nodeState = it.viewModel.currentNodeStateLiveData.value!!.nodeState as? BranchNodeState
            }
        } else if (activity is AssessmentActivity) {
            (activity as? AssessmentActivity)?.let {
                nodeState = it.viewModel.assessmentNodeState
            }
        }


        viewModel = initViewModel(nodeState!!)
        super.onCreate(savedInstanceState) //Needs to be called after viewModel is initialized
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.assessment_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.currentNodeStateLiveData
            .observe(this.viewLifecycleOwner, Observer<AssessmentViewModel.ShowNodeState>
            { showNodeState -> this.showStep(showNodeState) })
        viewModel.start()
    }

    open fun initViewModel(branchNodeState: BranchNodeState) =
        ViewModelProvider(
            this, AssessmentViewModelFactory()
                .create(branchNodeState)
        ).get(AssessmentViewModel::class.java)

    open fun showStep(showNodeState: AssessmentViewModel.ShowNodeState) {
        if (NavigationPoint.Direction.Exit == showNodeState.direction) {
            val resultIntent = Intent()
            resultIntent.putExtra(
                ASSESSMENT_RESULT,
                showNodeState.nodeState.currentResult.toString()
            )
            activity?.setResult(Activity.RESULT_CANCELED, resultIntent)
            activity?.finish()
            return
        }
        //If this is an assessment node, load the necessary assessmentFragment
        //The viewmodel of this fragment will need to be set as the nodeUiController of the nodeState
        //Viewmodel should probably have activity scope

        val stepFragment = this.getFragmentForStep(showNodeState.nodeState.node as Step)
        val transaction = childFragmentManager.beginTransaction()
        transaction
            .replace(R.id.step_fragment_container, stepFragment)
            .commit()
        childFragmentManager.executePendingTransactions()
    }

    open fun getFragmentForStep(step: Step): Fragment {
        //TODO: need factory for loading step fragments -nbrown 02/13/2020
        when (step) {
            is SimpleQuestion -> return TextQuestionStepFragment()
            is ChoiceQuestion -> return ChoiceQuestionStepFragment()
            is InstructionStep -> return InstructionStepFragment()
            else -> return DebugStepFragment()
        }
    }

}
