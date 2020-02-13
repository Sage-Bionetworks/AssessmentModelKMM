package org.sagebionetworks.assessmentmodel.serialization

import android.content.res.Resources
import java.io.BufferedReader
import java.io.InputStreamReader

class FileLoaderAndroid(val resources: Resources): FileLoader {

    override fun loadFile(fileName: String, packageName: String): String {
        val resourceId = resources.getIdentifier(fileName, "raw", packageName)
        val inputStream = resources.openRawResource(resourceId)
        val r = BufferedReader(InputStreamReader(inputStream))
        val total = StringBuilder(inputStream.available())
        var line: String? = null
        while (r.readLine().also({ line = it }) != null) {
            total.append(line).append('\n')
        }
        return total.toString()

    }
}