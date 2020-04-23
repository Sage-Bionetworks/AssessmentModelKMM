package org.sagebionetworks.assessmentmodel

import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceInfo

/// [ViewTheme] tells the UI where to find the view controller or fragment to use when instantiating a [Step].
interface ViewTheme : ResourceInfo {

    // MARK: Apple

    /// The storyboard view controller identifier or the nib name for this view controller. If null, then a custom
    /// view is not used on Apple platforms.
    val viewIdentifier: String?

    /// If the storyboard identifier is non-null then the view is assumed to be accessible within the storyboard
    /// via the `viewIdentifier`.
    val storyboardIdentifier: String?

    // MARK: Android

    /// The identifier for a custom Fragment.
    val fragmentIdentifier: String?

    /// The name of the layout file for the Fragment.
    val fragmentLayout: String?
}