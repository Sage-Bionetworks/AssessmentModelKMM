package org.sagebionetworks.assessmentmodel.serialization

import org.sagebionetworks.assessmentmodel.*

data class CollectionResultObject(override val identifier: String,
                                  override var pathHistoryResults: MutableList<Result> = mutableListOf(),
                                  override var asyncActionResults: MutableSet<Result> = mutableSetOf()) : CollectionResult

data class AssessmentResultObject(override val identifier: String,
                                  override val versionString: String? = null,
                                  override var pathHistoryResults: MutableList<Result> = mutableListOf(),
                                  override var asyncActionResults: MutableSet<Result> = mutableSetOf(),
                                  override var taskRunUUIDString: String = UUIDGenerator.uuidString(),
                                  override var startDateString: String = DateGenerator.nowString(),
                                  override var endDateString: String = DateGenerator.nowString()) : AssessmentResult