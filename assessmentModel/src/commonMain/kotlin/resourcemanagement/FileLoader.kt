package org.sagebionetworks.assessmentmodel.resourcemanagement

interface FileLoader {

    /**
     * Apple and Android devices have different structures and information that are required to load a file.
     */
    fun loadFile(assetInfo: AssetInfo, resourceInfo: ResourceInfo): String
}