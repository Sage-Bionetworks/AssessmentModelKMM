package org.sagebionetworks.assessmentmodel.navigation

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
data class NodeIdentifierPath(val identifier: String, val child: NodeIdentifierPath? = null)