package org.sagebionetworks.assessmentmodel.serialization

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import org.sagebionetworks.assessmentmodel.ImageInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.AssetInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.StandardResourceAssetType
import java.io.BufferedReader
import java.io.InputStreamReader

class FileLoaderAndroid(private val resources: Resources, private val defaultPackageName: String): FileLoader {

    override fun loadFile(assetInfo: AssetInfo, resourceInfo: ResourceInfo): String {
        val packageName = resourceInfo.packageName ?: defaultPackageName
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

fun ImageInfo.loadDrawable(context: Context): Drawable? {
    val packageName = packageName ?: context.packageName
    val resourceId = context.resources.getIdentifier(imageName, StandardResourceAssetType.DRAWABLE, packageName)
    return AppCompatResources.getDrawable(context, resourceId)

}