package org.sagebionetworks.assessmentmodel.sampleapp

import org.koin.dsl.module
import org.sagebionetworks.assessmentmodel.AssessmentRegistryProvider
import org.sagebionetworks.assessmentmodel.BranchNode
import org.sagebionetworks.assessmentmodel.navigation.CustomNodeStateProvider
import org.sagebionetworks.assessmentmodel.presentation.AssessmentFragment
import org.sagebionetworks.assessmentmodel.presentation.AssessmentFragmentProvider
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader
import org.sagebionetworks.assessmentmodel.serialization.EmbeddedJsonAssessmentRegistryProvider
import org.sagebionetworks.assessmentmodel.serialization.FileLoaderAndroid

val appModule = module {

    single<AssessmentRegistryProvider> { EmbeddedJsonAssessmentRegistryProvider(get(), "embedded_assessment_registry") }

    factory<FileLoader> {FileLoaderAndroid(get())}

    single<CustomNodeStateProvider?> {
        object : CustomNodeStateProvider {

        }}

    single<AssessmentFragmentProvider?> {
        object : AssessmentFragmentProvider {
            override fun fragmentFor(branchNode: BranchNode): AssessmentFragment {
                return AssessmentFragment()
            }
        }}

}