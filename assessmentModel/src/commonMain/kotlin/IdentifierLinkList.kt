package org.sagebionetworks.assessmentmodel

import kotlinx.serialization.Serializable

@Serializable
data class IdentifierLinkList(val identifier: String, val childNode: IdentifierLinkList? = null)