package org.sagebionetworks.assessmentmodel.sampleapp

import android.content.Context
import org.sagebionetworks.assessmentmodel.AssessmentRegistryProvider
import org.sagebionetworks.assessmentmodel.ModuleInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader
import org.sagebionetworks.assessmentmodel.serialization.*

class AppAssessmentRegistryProvider(context: Context): AssessmentRegistryProvider {


    private val defaultPackageName = context.packageName

    override val fileLoader: FileLoader = FileLoaderAndroid(context.resources, context.packageName)
    private var _modules: List<ModuleInfo>? = null
    override val modules: List<ModuleInfo>
        get() {
            if (_modules == null) {
                val transformableAssessment = TransformableAssessmentObject("sampleId", "sample_assessment")
                val moduleInfo = ModuleInfoObject(
                    assessments = listOf(transformableAssessment),
                    packageName = defaultPackageName,
                    jsonCoder = Serialization.JsonCoder.default)
                _modules = listOf(moduleInfo)
            }
            return _modules?: throw AssertionError("Set to null by another thread")
        }

}