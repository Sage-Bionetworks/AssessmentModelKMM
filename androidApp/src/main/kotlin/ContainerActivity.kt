package org.sagebionetworks.assessmentmodel.sampleapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import org.koin.android.ext.android.inject
import org.sagebionetworks.assessmentmodel.AssessmentRegistryProvider
import org.sagebionetworks.assessmentmodel.presentation.AssessmentActivity
import org.sagebionetworks.assessmentmodel.sampleapp.databinding.ActivityContainerBinding
import org.sagebionetworks.assessmentmodel.sampleapp.databinding.AssessmentRowBinding
import org.sagebionetworks.assessmentmodel.serialization.ModuleInfoObject

class ContainerActivity: AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_ASSESSMENT = 1;
    }

    val assessmentRegistryProvider: AssessmentRegistryProvider by inject()
    private lateinit var binding: ActivityContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        for (moduleInfo in assessmentRegistryProvider.modules) {
            (moduleInfo as ModuleInfoObject)?.let {
                var theme = R.style.RoyalTheme
                for (assessment in it.assessments) {
                    addAssessment(assessment.title?:assessment.identifier, assessment.identifier, theme)
                    theme = R.style.BlueberryTheme
                }
            }
        }
    }

    private fun addAssessment(title: String, assessmentId: String, theme: Int = -1) {
        val inflater = if (theme < 0) layoutInflater else LayoutInflater.from(ContextThemeWrapper(this, theme))
        val row = AssessmentRowBinding.inflate(inflater, binding.assessmentContainer, false)
        row.taskName.text = title
        row.buttonStartTask.setOnClickListener {
            val intent = Intent(this, AssessmentActivity::class.java)
            intent.putExtra(AssessmentActivity.ARG_ASSESSMENT_ID_KEY, assessmentId)
            if (theme > 0) {
                intent.putExtra(AssessmentActivity.ARG_THEME, theme)
            }
            startActivityForResult(intent, REQUEST_CODE_ASSESSMENT)
        }
        binding.assessmentContainer.addView(row.root)
    }

}