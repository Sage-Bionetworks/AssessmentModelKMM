package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.sagebionetworks.assessmentmodel.ViewTheme

@Serializable
data class ViewThemeObject(
    override val viewIdentifier: String? = null,
    override val storyboardIdentifier: String? = null,
    override val fragmentIdentifier: String? = null,
    override val fragmentLayout: String? = null,
    override val bundleIdentifier: String? = null,
    override var packageName: String? = null,
    @Transient
    override var decoderBundle: Any? = null
) : ViewTheme