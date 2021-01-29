package org.sagebionetworks.assessmentmodel

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader

/**
 * The [ModuleInfo] is a way of describing what assessments are included in a given module.
 */
interface ModuleInfo {
    val resourceInfo: ResourceInfo

    /**
     * Does this [ModuleInfo] include an [Assessment] that matches the given [AssessmentPlaceholder]?
     */
    fun hasAssessment(assessmentPlaceholder: AssessmentPlaceholder): Boolean

    /**
     * A [TransformableNode] carries information about the that can transform it either from a
     * different embedded resource file, a cached service response, or whatnot. The transformable
     * carries enough information to allow the [ModuleInfo] to replace it with an unpacked node.
     */
    fun getReplacementNode(transformableNode: TransformableNode, registryProvider: AssessmentRegistryProvider): Node

    /**
     * Load an [Assessment] based on its [AssessmentInfo].
     */
    fun getAssessment(assessmentPlaceholder: AssessmentPlaceholder, registryProvider: AssessmentRegistryProvider): Assessment
}

/**
 * The [JsonModuleInfo] implements the module level support for unpacking assessments that use JSON
 * serialization of a JSON string defined either from services or embedded files.
 */
interface JsonModuleInfo : ModuleInfo {
    val jsonCoder: Json

    /**
     * This method is called by the [getReplacementNode] method to decode a JSON string.
     */
    fun getJsonString(transformableNode: TransformableNode, registryProvider: AssessmentRegistryProvider): String

    override fun getReplacementNode(
        transformableNode: TransformableNode,
        registryProvider: AssessmentRegistryProvider
    ): Node {
        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = getJsonString(transformableNode, registryProvider)
        return jsonCoder.decodeFromString(serializer, jsonString)
    }
}

/**
 * Most assessments will be included as embedded JSON files in the same module as the other
 * resources used by the assessments within a given module. This structure allows registering
 * several modules and their assessments without having *all* the assessment data in memory.
 */
interface EmbeddedJsonModuleInfo : JsonModuleInfo {
    val assessments: List<TransformableAssessment>

    /**
     * If *any* of the [TransformableAssessment] objects match the identifier for the placeholder's
     * [AssessmentInfo], then there is at least one [Assessment].
     */
    override fun hasAssessment(assessmentPlaceholder: AssessmentPlaceholder): Boolean {
        return assessments.any { it.identifier == assessmentPlaceholder.assessmentInfo.identifier }
    }

    /**
     * Uses the [FileLoader] provided for the platform to load the JSON from a file.
     */
    override fun getJsonString(
        transformableNode: TransformableNode,
        registryProvider: AssessmentRegistryProvider
    ): String {
        return registryProvider.fileLoader.loadFile(transformableNode, resourceInfo)
    }

    /**
     * Returns the [Assessment] that is unpacked by the best match of a [TransformableAssessment]
     * that is contained within this module.
     */
    override fun getAssessment(
        assessmentPlaceholder: AssessmentPlaceholder,
        registryProvider: AssessmentRegistryProvider
    ): Assessment {
        val sublist = assessments.filter { it.identifier == assessmentPlaceholder.assessmentInfo.identifier }
        // TODO: syoung 01/27/2021 Look at version and schema identifier
        return sublist.first().unpack(assessmentPlaceholder, this, registryProvider)
    }
}
