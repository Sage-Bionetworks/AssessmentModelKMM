package org.sagebionetworks.assessmentmodel.sampleapp

import org.koin.dsl.module
import org.sagebionetworks.assessmentmodel.AssessmentRegistryProvider

val appModule = module {

    single<AssessmentRegistryProvider> { AppAssessmentRegistryProvider(get()) }

}