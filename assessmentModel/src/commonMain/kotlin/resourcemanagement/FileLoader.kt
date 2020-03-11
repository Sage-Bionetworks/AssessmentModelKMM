package org.sagebionetworks.assessmentmodel.resourcemanagement

interface FileLoader {

    /**
     * Apple and Android devices have a different structure and information that is required to load a file.
     */
    fun loadFile(resourceName: String, rawFileExtension: String? = null, resourceInfo: ResourceInfo): String
}