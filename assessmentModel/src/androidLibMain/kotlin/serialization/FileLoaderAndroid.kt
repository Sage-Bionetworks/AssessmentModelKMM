package org.sagebionetworks.assessmentmodel.serialization

import android.content.res.Resources
import org.sagebionetworks.assessmentmodel.resourcemanagement.AssetInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceInfo
import java.io.BufferedReader
import java.io.InputStreamReader

class FileLoaderAndroid(val resources: Resources): FileLoader {

    override fun loadFile(assetInfo: AssetInfo, resourceInfo: ResourceInfo): String {
        // TODO: syoung 03/10/2020 FIXME!! remove hardcoded packageName
        val packageName = resourceInfo.packageName ?: "org.sagebionetworks.assessmentmodel.sampleapp"
        val resourceId = resources.getIdentifier(assetInfo.resourceName, assetInfo.resourceAssetType, packageName)
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