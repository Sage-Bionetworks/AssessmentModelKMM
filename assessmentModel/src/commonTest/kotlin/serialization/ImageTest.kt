package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.json
import org.sagebionetworks.assessmentmodel.ImageInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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
        val restored = jsonCoder.parse(TestImageWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestImageWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = Json.nonstrict.parseJson(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getObject("image")
        assertEquals("fetchable", jsonWrapper.getPrimitiveOrNull("type")?.content)
        assertEquals("before", jsonWrapper.getPrimitiveOrNull("imageName")?.content)
    }

    @Test
    fun testFetchableImageWithValues() {
        val image = FetchableImage("before", imagePlacementType = ImagePlacement.Standard.BackgroundBefore, label = "Foo", size = Size(width = 20.0, height = 40.0))
        val inputString = """{"image":{"type":"fetchable","imageName":"before","label":"Foo","placementType":"backgroundBefore","size":{"width":20.0,"height":40.0}}}"""

        val original = TestImageWrapper(image)
        val jsonString = jsonCoder.stringify(TestImageWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestImageWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestImageWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = Json.nonstrict.parseJson(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getObject("image")
        assertEquals("fetchable", jsonWrapper.getPrimitiveOrNull("type")?.content)
        assertEquals("before", jsonWrapper.getPrimitiveOrNull("imageName")?.content )
        assertEquals("Foo", jsonWrapper.getPrimitiveOrNull("label")?.content)
        assertEquals("BackgroundBefore", jsonWrapper.getPrimitiveOrNull("placementType")?.content)
        val sizeObject = jsonWrapper.getObjectOrNull("size")
        assertNotNull(sizeObject)
        assertEquals(20.0, sizeObject?.getPrimitiveOrNull("width")?.double)
        assertEquals(40.0, sizeObject?.getPrimitiveOrNull("height")?.double)
    }

    @Test
    fun testAnimatedImageWithDefaults() {
        val image = AnimatedImage(listOf("before","after"), 0.25)
        val inputString = """{"image":{"type":"animated","imageNames":["before", "after"],"animationDuration":0.25}}"""

        val original = TestImageWrapper(image)
        val jsonString = jsonCoder.stringify(TestImageWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestImageWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestImageWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = Json.nonstrict.parseJson(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getObject("image")
        assertEquals("animated", jsonWrapper.getPrimitiveOrNull("type")?.content)
        assertEquals(0.25, jsonWrapper.getPrimitiveOrNull("animationDuration")?.double)
        val imageNames = jsonWrapper.getArrayOrNull("imageNames")
        assertEquals("before", imageNames?.firstOrNull()?.primitive?.content)
        assertEquals("after", imageNames?.lastOrNull()?.primitive?.content)
    }

    @Test
    fun testAnimatedImageWithValues() {
        val image = AnimatedImage(listOf("before","after"), 0.25, 1,"Foo", ImagePlacement.Standard.IconBefore,Size(20.0, 40.0))
        val inputString = """{"image":{"type":"animated","imageNames":["before", "after"],"animationDuration":0.25,"animationRepeatCount":1,"label":"Foo","placementType":"iconBefore","size":{"width":20.0,"height":40.0}}}"""

        val original = TestImageWrapper(image)
        val jsonString = jsonCoder.stringify(TestImageWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestImageWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestImageWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = Json.nonstrict.parseJson(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getObject("image")
        assertEquals("animated", jsonWrapper.getPrimitiveOrNull("type")?.content)
        assertEquals(0.25, jsonWrapper.getPrimitiveOrNull("animationDuration")?.double)
        val imageNames = jsonWrapper.getArrayOrNull("imageNames")
        assertEquals("before", imageNames?.firstOrNull()?.primitive?.content)
        assertEquals("after", imageNames?.lastOrNull()?.primitive?.content)
        assertEquals("Foo", jsonWrapper.getPrimitiveOrNull("label")?.content)
        assertEquals("IconBefore", jsonWrapper.getPrimitiveOrNull("placementType")?.content)
        val sizeObject = jsonWrapper.getObjectOrNull("size")
        assertNotNull(sizeObject)
        assertEquals(20.0, sizeObject?.getPrimitiveOrNull("width")?.double)
        assertEquals(40.0, sizeObject?.getPrimitiveOrNull("height")?.double)
    }
}