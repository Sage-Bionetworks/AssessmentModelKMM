package org.sagebionetworks.assessmentmodel.serialization

interface FileLoader {

    fun loadFile(fileName: String, packageName: String): String

}