package org.sagebionetworks.assessmentmodel

import kotlinx.serialization.Serializable

@Serializable
data class NodeIdentifierPath(val identifier: String, val child: NodeIdentifierPath? = null)