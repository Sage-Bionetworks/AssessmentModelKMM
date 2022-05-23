package org.sagebionetworks.assessmentmodel

import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json

/**
 * An [AssessmentResultCache] is used for storing the results of partially completed assessments.
 * This is to support users starting a survey and being able to resume again in the future where they left off.
 */
interface AssessmentResultCache {

    /**
     * Load a previously cached [AssessmentResult] associated with the given [instanceGuid].
     * The jsonCoder is used to deserialize the stored result into an [AssessmentResult]
     */
    fun loadAssessmentResult(instanceGuid: String, jsonCoder: Json) : AssessmentResult?

    /**
     * Store an [AssessmentResult] keyed by its [instanceGuid].
     * @param [instanceGuid] : Key used to store this result.
     * @param [result] : The result to store.
     * @param [jsonCoder] : Json encoder used to serialize the result into Json.
     * @param [expire] : When this [AssessmentResult] is no longer need and can be removed from the cache.
     */
    fun storeAssessmentResult(instanceGuid: String, result: AssessmentResult, jsonCoder: Json, expire: Instant?)

    /**
     * Remove any expired results from the cache.
     */
    fun clearExpiredAssessmentResults()

}