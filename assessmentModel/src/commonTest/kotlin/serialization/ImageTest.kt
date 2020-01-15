package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.ImageInfo
import org.sagebionetworks.assessmentmodel.ImagePlacement
import org.sagebionetworks.assessmentmodel.Size
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
data class TestImageWrapper(val image: ImageInfo)

val jsonCoder = Json(context = imageSerializersModule)

open class ImageTest {

    @Test
    fun testFetchableImageWithDefaults() {
        val image = FetchableImage("before")
        val inputString = """{"image":{"type":"fetchable","imageName":"before"}}"""

        val original = TestImageWrapper(image)
        val jsonString = jsonCoder.stringify(TestImageWrapper.serializer(), original)
        assertEquals(inputString, jsonString)
        val restored = jsonCoder.parse(TestImageWrapper.serializer(), inputString)
        assertEquals(original, restored)

        println("PASSED testFetchableImageWithDefaults")
    }

    @Test
    fun testFetchableImageWithValues() {
        val image = FetchableImage("before", imagePlacement = ImagePlacement.Standard.BackgroundBefore, label = "Foo", imageSize = Size(width = 20, height = 40))
        val inputString = """{"image":{"type":"fetchable","imageName":"before","label":"Foo","placementType":"BackgroundBefore","imageSize":{"width":20,"height":40}}}"""

        val original = TestImageWrapper(image)
        val jsonString = jsonCoder.stringify(TestImageWrapper.serializer(), original)
        assertEquals(inputString, jsonString)
        val restored = jsonCoder.parse(TestImageWrapper.serializer(), inputString)
        assertEquals(original, restored)

        println("PASSED testFetchableImageWithValues")
    }
}