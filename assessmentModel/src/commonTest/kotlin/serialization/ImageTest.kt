package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.sagebionetworks.assessmentmodel.ImageInfo
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
        val image = FetchableImage("before", imagePlacementType = ImagePlacement.Standard.BackgroundBefore, label = "Foo", size = Size(width = 20.0, height = 40.0))
        val inputString = """{"image":{"type":"fetchable","imageName":"before","label":"Foo","placementType":"BackgroundBefore","size":{"width":20.0,"height":40.0}}}"""

        val original = TestImageWrapper(image)
        val jsonString = jsonCoder.stringify(TestImageWrapper.serializer(), original)
        assertEquals(inputString, jsonString)
        val restored = jsonCoder.parse(TestImageWrapper.serializer(), inputString)
        assertEquals(original, restored)

        println("PASSED testFetchableImageWithValues")
    }
}