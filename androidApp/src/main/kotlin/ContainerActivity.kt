package org.sagebionetworks.assessmentmodel.sampleapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.sagebionetworks.assessmentmodel.presentation.AssessmentActivity
import org.sagebionetworks.assessmentmodel.presentation.AssessmentFragment
import org.sagebionetworks.assessmentmodel.sampleapp.databinding.ActivityContainerBinding
import org.sagebionetworks.assessmentmodel.sampleapp.databinding.AssessmentRowBinding

class ContainerActivity: AppCompatActivity() {

    private lateinit var binding: ActivityContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val row = AssessmentRowBinding.inflate(layoutInflater)
        row.taskName.text = "Sample Assessment"
        row.buttonStartTask.setOnClickListener {
            val intent = Intent(this, AssessmentActivity::class.java)
            intent.putExtra(AssessmentFragment.ARG_ASSESSMENT_ID_KEY, "sampleId")
            intent.putExtra(AssessmentFragment.ARG_RESOURCE_NAME, "sample_assessment")
            intent.putExtra(AssessmentFragment.ARG_PACKAGE_NAME, this.packageName)
            startActivityForResult(intent, 1)
        }
        binding.crfTaskContainer.addView(row.root)
    }

}