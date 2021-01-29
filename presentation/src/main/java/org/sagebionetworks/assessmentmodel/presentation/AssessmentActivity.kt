package org.sagebionetworks.assessmentmodel.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.BranchNodeState
import org.sagebionetworks.assessmentmodel.navigation.CustomNodeStateProvider
import org.sagebionetworks.assessmentmodel.navigation.FinishedReason
import org.sagebionetworks.assessmentmodel.serialization.*

open class AssessmentActivity: AppCompatActivity() {

    companion object {

        const val ARG_THEME = "arg_theme"

        const val ARG_ASSESSMENT_ID_KEY = "assessment_id_key"
        const val ARG_RESOURCE_NAME = "resource_name"
        const val ARG_PACKAGE_NAME = "package_name"

        const val ASSESSMENT_RESULT = "AsssessmentResult"

    }

    lateinit var viewModel: RootAssessmentViewModel
    var assessmentFragmentProvider: AssessmentFragmentProvider? = null
    var customNodeStateProvider: CustomNodeStateProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent.hasExtra(ARG_THEME)) {
            setTheme(intent.getIntExtra(ARG_THEME, R.style.BlueberryTheme))
        }
        val assessmentId = intent.getStringExtra(ARG_ASSESSMENT_ID_KEY)!!
        val resourceName = intent.getStringExtra(ARG_RESOURCE_NAME)!!
        val packageName = intent.getStringExtra(ARG_PACKAGE_NAME) ?: this.packageName

        val fileLoader = FileLoaderAndroid(resources, packageName)
        val transformableAssessment = TransformableAssessmentObject(assessmentId, resourceName)
        val assessmentInfo = AssessmentInfoObject(assessmentId)
        val assessmentPlaceholder = AssessmentPlaceholderObject(assessmentId, assessmentInfo)

        val moduleInfo = ModuleInfoObject(
            assessments = listOf(transformableAssessment),
            packageName = packageName
        )
        val registryProvider = object : AssessmentRegistryProvider {
            override val fileLoader = fileLoader
            override val modules: List<ModuleInfo> = listOf(moduleInfo)
        }

        // TODO: syoung 01/25/2021 Refactor this to have the activity take the [ModuleInfoProvider] as a setup input.

        viewModel = initViewModel(assessmentPlaceholder, registryProvider, customNodeStateProvider)
        viewModel.assessmentLoadedLiveData
            .observe(this, Observer<BranchNodeState>
            { nodeState -> this.handleAssessmentLoaded(nodeState) })

        viewModel.assessmentFinishedLiveData
            .observe(this, Observer<RootAssessmentViewModel.FinishedState>
            { finished -> this.handleAssessmentFinished(finished) })



        super.onCreate(savedInstanceState)
    }

    private fun handleAssessmentFinished(finishedState: RootAssessmentViewModel.FinishedState) {
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
        supportFragmentManager.beginTransaction().add(android.R.id.content, fragment).commit()
    }

    open fun initViewModel(assessmentInfo: AssessmentPlaceholder, assessmentProvider: AssessmentRegistryProvider, customNodeStateProvider: CustomNodeStateProvider?) =
        ViewModelProvider(
            this, RootAssessmentViewModelFactory()
                .create(assessmentInfo, assessmentProvider, customNodeStateProvider)
        ).get(RootAssessmentViewModel::class.java)


}