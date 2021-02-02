package org.sagebionetworks.assessmentmodel.sampleapp

import org.koin.dsl.module
import org.sagebionetworks.assessmentmodel.AssessmentRegistryProvider
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader
import org.sagebionetworks.assessmentmodel.serialization.EmbeddedJsonAssessmentRegistryProvider
import org.sagebionetworks.assessmentmodel.serialization.FileLoaderAndroid

val appModule = module {

    single<AssessmentRegistryProvider> { EmbeddedJsonAssessmentRegistryProvider(get(), "embedded_assessment_registry") }

    factory<FileLoader> {FileLoaderAndroid(get())}

}