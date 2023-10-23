package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.*
import org.sagebionetworks.assessmentmodel.EmbeddedJsonModuleInfo
import org.sagebionetworks.assessmentmodel.JsonModuleInfo
import org.sagebionetworks.assessmentmodel.ModuleInfo
import org.sagebionetworks.assessmentmodel.TransformableAssessment
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceInfo

val moduleInfoSerializersModule = SerializersModule {
    polymorphic(ModuleInfo::class) {
        subclass(ModuleInfoObject::class)
    }
    polymorphic(JsonModuleInfo::class) {
        subclass(ModuleInfoObject::class)
    }
}

@Serializable
@SerialName("defaultModuleInfo")
data class ModuleInfoObject(
    override val assessments: List<TransformableAssessment>,
    override var packageName: String? = null,
    override val bundleIdentifier: String? = null): ResourceInfo, EmbeddedJsonModuleInfo {

    override val resourceInfo: ResourceInfo
        get() = this
    override val jsonCoder: Json
        get() = Serialization.JsonCoder.default

    @Transient
    override var decoderBundle: Any? = null
}
