package org.sagebionetworks.assessmentmodel.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.koin.android.ext.android.inject
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.BranchNodeState
import org.sagebionetworks.assessmentmodel.navigation.CustomNodeStateProvider
import org.sagebionetworks.assessmentmodel.navigation.FinishedReason
import org.sagebionetworks.assessmentmodel.serialization.*

open class AssessmentActivity: AppCompatActivity() {

    companion object {
        const val ARG_THEME = "arg_theme"
        const val ARG_ASSESSMENT_ID_KEY = "assessment_id_key"
    }

    lateinit var viewModel: RootAssessmentViewModel

    val assessmentFragmentProvider: AssessmentFragmentProvider? by inject()
    val customNodeStateProvider: CustomNodeStateProvider? by inject()
    val assessmentRegistryProvider: AssessmentRegistryProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent.hasExtra(ARG_THEME)) {
            setTheme(intent.getIntExtra(ARG_THEME, R.style.BlueberryTheme))
        }
        val assessmentId = intent.getStringExtra(ARG_ASSESSMENT_ID_KEY)!!

        val assessmentInfo = AssessmentInfoObject(assessmentId)
        val assessmentPlaceholder = AssessmentPlaceholderObject(assessmentId, assessmentInfo)

        viewModel = initViewModel(assessmentPlaceholder, assessmentRegistryProvider, customNodeStateProvider)
        // If we've already loaded the assessment then the activity is being recreated from a configuration
        // change, and the AssessmentFragment will be restored for us.
        if (!viewModel.hasHandledLoad) {
            viewModel.assessmentLoadedLiveData
                .observe(this, Observer<BranchNodeState>
                { nodeState -> this.handleAssessmentLoaded(nodeState) })
        }

        viewModel.assessmentFinishedLiveData
            .observe(this, Observer<RootAssessmentViewModel.FinishedState>
            { finished -> this.handleAssessmentFinished(finished) })

        super.onCreate(savedInstanceState)
    }

    protected fun handleAssessmentFinished(finishedState: RootAssessmentViewModel.FinishedState) {
        val resultCode = if (finishedState.finishedReason == FinishedReason.Complete) {
            Activity.RESULT_OK
        } else {
            Activity.RESULT_CANCELED
        }
        val resultIntent = Intent()
        resultIntent.putExtra(
            AssessmentFragment.ASSESSMENT_RESULT,
            finishedState.nodeState.currentResult.toString()
        )
        setResult(resultCode, resultIntent)
        finish()
        return
    }

    private fun handleAssessmentLoaded(rootNodeState: BranchNodeState) {
        rootNodeState.rootNodeController = viewModel
        val fragment = assessmentFragmentProvider?.fragmentFor(rootNodeState.node)?: AssessmentFragment.newFragmentInstance()
        supportFragmentManager.beginTransaction().add(android.R.id.content, fragment).setReorderingAllowed(true).commit()
        viewModel.hasHandledLoad = true
    }

    open fun initViewModel(assessmentInfo: AssessmentPlaceholder, assessmentProvider: AssessmentRegistryProvider, customNodeStateProvider: CustomNodeStateProvider?) =
        ViewModelProvider(
            this, RootAssessmentViewModelFactory()
                .create(assessmentInfo, assessmentProvider, customNodeStateProvider)
        ).get(RootAssessmentViewModel::class.java)


}