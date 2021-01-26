package org.sagebionetworks.assessmentmodel.resourcemanagement

import kotlinx.serialization.json.Json
import org.sagebionetworks.assessmentmodel.Assessment

/**
 * A [SerializableModuleInfo] extends [ModuleInfo] to include the [Json] coding information with the
 * serializers  to use when decoding an [Assessment].
 */
interface SerializableModuleInfo : ModuleInfo {
    val jsonCoder: Json
}