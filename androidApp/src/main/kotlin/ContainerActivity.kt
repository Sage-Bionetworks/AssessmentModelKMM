package org.sagebionetworks.assessmentmodel.sampleapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import org.sagebionetworks.assessmentmodel.presentation.AssessmentActivity
import org.sagebionetworks.assessmentmodel.presentation.AssessmentFragment
import org.sagebionetworks.assessmentmodel.sampleapp.databinding.ActivityContainerBinding
import org.sagebionetworks.assessmentmodel.sampleapp.databinding.AssessmentRowBinding

class ContainerActivity: AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_ASSESSMENT = 1;
    }

    private lateinit var binding: ActivityContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        addAssessment("Sample Assessment - Royal", "sampleId", "sample_assessment", this.packageName, R.style.RoyalTheme)
        addAssessment("Sample Assessment - Blue", "sampleId", "sample_assessment", this.packageName, R.style.BlueberryTheme)

    }

    private fun addAssessment(title: String, assessmentId: String, resourceName: String, packageName: String, theme: Int = -1) {
        val inflater = if (theme < 0) layoutInflater else LayoutInflater.from(ContextThemeWrapper(this, theme))
        val row = AssessmentRowBinding.inflate(inflater, binding.crfTaskContainer, false)
        row.taskName.text = title
        row.buttonStartTask.setOnClickListener {
            val intent = Intent(this, AssessmentActivity::class.java)
            intent.putExtra(AssessmentActivity.ARG_ASSESSMENT_ID_KEY, assessmentId)
            intent.putExtra(AssessmentActivity.ARG_RESOURCE_NAME, resourceName)
            intent.putExtra(AssessmentActivity.ARG_PACKAGE_NAME, this.packageName)
            if (theme > 0) {
                intent.putExtra(AssessmentActivity.ARG_THEME, theme)
            }
            startActivityForResult(intent, REQUEST_CODE_ASSESSMENT)
        }
        binding.crfTaskContainer.addView(row.root)
    }

}