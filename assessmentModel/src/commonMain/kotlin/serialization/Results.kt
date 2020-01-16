package org.sagebionetworks.assessmentmodel.serialization

import org.sagebionetworks.assessmentmodel.CollectionResult
import org.sagebionetworks.assessmentmodel.Result

data class CollectionResultObject(override val identifier: String,
                                  override var pathHistoryResults: MutableList<Result> = mutableListOf(),
                                  override var asyncActionResults: MutableSet<Result> = mutableSetOf()) : CollectionResult