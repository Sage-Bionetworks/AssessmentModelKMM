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
import kotlinx.serialization.json.JsonConfiguration
import org.sagebionetworks.assessmentmodel.InstructionStep
import org.sagebionetworks.assessmentmodel.Step
import org.sagebionetworks.assessmentmodel.navigation.NavigationPoint
import org.sagebionetworks.assessmentmodel.northwestern.MtbSerialization
import org.sagebionetworks.assessmentmodel.serialization.*
import org.sagebionetworks.assessmentmodel.survey.ChoiceQuestion
import org.sagebionetworks.assessmentmodel.survey.SimpleQuestion


open class AssessmentFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newFragmentInstance(assessmentId: String, resourceName: String, packageName: String) =
            AssessmentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ASSESSMENT_ID_KEY, assessmentId)
                    putString(ARG_RESOURCE_NAME, resourceName)
                    putString(ARG_PACKAGE_NAME, packageName)
                }
            }

        const val ARG_ASSESSMENT_ID_KEY = "assessment_id_key"
        const val ARG_RESOURCE_NAME = "resource_name"
        const val ARG_PACKAGE_NAME = "package_name"

        const val ASSESSMENT_RESULT = "AsssessmentResult"
    }



    lateinit var viewModel: AssessmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val assessmentId = arguments!!.getString(ARG_ASSESSMENT_ID_KEY)!!
        val resourceName = arguments!!.getString(ARG_RESOURCE_NAME)!!
        val packageName = arguments!!.getString(ARG_PACKAGE_NAME)!!

        // TODO: syoung 03/10/2020 Move this to a singleton, factory, registry, etc.
        val fileLoader = FileLoaderAndroid(resources, context?.packageName ?: packageName)
        val assessmentGroup = AssessmentGroupInfoObject(
            assessments = listOf(TransformableAssessmentObject(assessmentId, resourceName)),
            packageName = packageName)
        val assessmentProvider = FileAssessmentProvider(fileLoader, assessmentGroup, getJsonLoader())
        viewModel = ViewModelProvider(
            this, AssessmentViewModelFactory()
                .create(assessmentId, assessmentProvider))
            .get(AssessmentViewModel::class.java)
        super.onCreate(savedInstanceState) //Needs to be called after viewModel is initialized
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.assessment_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.currentNodeStateLiveData
                .observe(this.viewLifecycleOwner, Observer<AssessmentViewModel.ShowNodeState>
                { showNodeState -> this.showStep(showNodeState) })
        viewModel.start()
    }

    private fun showStep(showNodeState: AssessmentViewModel.ShowNodeState) {
        if (NavigationPoint.Direction.Exit == showNodeState.direction) {
            val resultIntent = Intent()
            resultIntent.putExtra(ASSESSMENT_RESULT, showNodeState.nodeState.currentResult.toString())
            activity?.setResult(Activity.RESULT_CANCELED, resultIntent)
            activity?.finish()
            return
        }

        val stepFragment = this.getFragmentForStep(showNodeState.nodeState.node as Step)
        val transaction = childFragmentManager.beginTransaction()
        transaction
                .replace(R.id.step_fragment_container, stepFragment)
                .commit()
        childFragmentManager.executePendingTransactions()
    }

    open fun getJsonLoader() : Json {
        return Serialization.JsonCoder.default
    }

    open fun getFragmentForStep(step: Step): Fragment {
        //TODO: need factory for loading step fragments -nbrown 02/13/2020
        when(step) {
            is SimpleQuestion -> return TextQuestionStepFragment()
            is ChoiceQuestion -> return ChoiceQuestionStepFragment()
            is InstructionStep -> return InstructionStepFragment()
            else -> return DebugStepFragment()
        }
    }

}
