package org.sagebionetworks.assessmentmodel.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.serialization.json.Json
import org.sagebionetworks.assessmentmodel.navigation.BranchNodeState
import org.sagebionetworks.assessmentmodel.navigation.CustomBranchNodeStateProvider
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
    var customBranchNodeStateProvider: CustomBranchNodeStateProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent.hasExtra(ARG_THEME)) {
            setTheme(intent.getIntExtra(ARG_THEME, R.style.BlueberryTheme))
        }
        val assessmentId = intent.getStringExtra(ARG_ASSESSMENT_ID_KEY)!!
        val resourceName = intent.getStringExtra(ARG_RESOURCE_NAME)!!
        val customPackageName = intent.getStringExtra(ARG_PACKAGE_NAME)

        val fileLoader = FileLoaderAndroid(resources, customPackageName ?: this.packageName)
        val assessmentGroup = AssessmentGroupInfoObject(
            assessments = listOf(TransformableAssessmentObject(assessmentId, resourceName)),
            packageName = packageName
        )

        val assessmentProvider =
            FileAssessmentProvider(fileLoader, assessmentGroup, getJsonLoader(assessmentId))

        viewModel = initViewModel(assessmentId, assessmentProvider, customBranchNodeStateProvider)
        viewModel.assessmentLoadedLiveData
            .observe(this, Observer<BranchNodeState>
            { nodeState -> this.handleAssessmentLoaded(nodeState) })

        super.onCreate(savedInstanceState)
    }

    private fun handleAssessmentLoaded(rootNodeState: BranchNodeState) {
        val fragment = assessmentFragmentProvider?.fragmentFor(rootNodeState.node)?: AssessmentFragment.newFragmentInstance()
        supportFragmentManager.beginTransaction().add(android.R.id.content, fragment).commit()
    }



    open fun getJsonLoader(assessmentId: String): Json {
        //TODO: This should be a mapping of assessmentIds to Json coders -nbrown 01/21/21
        return Serialization.JsonCoder.default
    }

    open fun initViewModel(assessmentId: String, assessmentProvider: FileAssessmentProvider, customBranchNodeStateProvider: CustomBranchNodeStateProvider?) =
        ViewModelProvider(
            this, RootAssessmentViewModelFactory()
                .create(assessmentId, assessmentProvider, customBranchNodeStateProvider)
        ).get(RootAssessmentViewModel::class.java)


}