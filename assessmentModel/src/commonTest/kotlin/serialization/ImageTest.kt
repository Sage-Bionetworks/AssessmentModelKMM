package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.sagebionetworks.assessmentmodel.ImageInfo
import kotlin.test.*

open class ImageTest {

    @Serializable
    data class TestImageWrapper(val image: ImageInfo)

    // TODO: syoung 03/07/2022 Uncomment or delete once we know whether or not this is going to be used in future designs.
//    @Serializable
//    data class TestImagePlacementWrapper(val placement: ImagePlacement)

    val jsonCoder = Serialization.JsonCoder.default

    @Test
    fun testFetchableImageWithDefaults() {
        val image = FetchableImage("before")
        val inputString = """{"image":{"type":"fetchable","imageName":"before"}}"""

        val original = TestImageWrapper(image)
        val jsonString = jsonCoder.encodeToString(TestImageWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestImageWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestImageWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = jsonCoder.parseToJsonElement(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getValue("image").jsonObject
        assertEquals("fetchable", jsonWrapper["type"]?.jsonPrimitive?.content)
        assertEquals("before", jsonWrapper["imageName"]?.jsonPrimitive?.content)
    }

    // TODO: syoung 03/07/2022 Uncomment or delete once we know whether or not this is going to be used in future designs.
//    @Test
//    fun testFetchableImageWithValues() {
//        val image = FetchableImage("before", imagePlacement = ImagePlacement.Standard.BackgroundBefore, label = "Foo", size = Size(width = 20.0, height = 40.0))
//        val inputString = """{"image":{"type":"fetchable","imageName":"before","label":"Foo","placementType":"backgroundBefore","size":{"width":20.0,"height":40.0}}}"""
//
//        val original = TestImageWrapper(image)
//        val jsonString = jsonCoder.encodeToString(TestImageWrapper.serializer(), original)
//        val restored = jsonCoder.decodeFromString(TestImageWrapper.serializer(), jsonString)
//        val decoded = jsonCoder.decodeFromString(TestImageWrapper.serializer(), inputString)
//
//        // Look to see that the restored, decoded, and original all are equal
//        assertEquals(original, restored)
//        assertEquals(original, decoded)
//
//        // Check the keys and look to see that they match the expected type
//        val jsonOutput = jsonCoder.parseToJsonElement(jsonString)
//        val jsonWrapper = jsonOutput.jsonObject.getValue("image").jsonObject
//        assertEquals("fetchable", jsonWrapper["type"]?.jsonPrimitive?.content)
//        assertEquals("before", jsonWrapper["imageName"]?.jsonPrimitive?.content)
//        assertEquals("Foo", jsonWrapper["label"]?.jsonPrimitive?.content)
//        assertEquals("backgroundBefore", jsonWrapper["placementType"]?.jsonPrimitive?.content)
//        val sizeObject = jsonWrapper.getValue("size").jsonObject
//        assertNotNull(sizeObject)
//        assertEquals(20.0, sizeObject["width"]?.jsonPrimitive?.double)
//        assertEquals(40.0, sizeObject["height"]?.jsonPrimitive?.double)
//    }

    @Test
    fun testAnimatedImageWithDefaults() {
        val image = AnimatedImage(listOf("before","after"), 0.25)
        val inputString = """{"image":{"type":"animated","imageNames":["before", "after"],"animationDuration":0.25}}"""

        val original = TestImageWrapper(image)
        val jsonString = jsonCoder.encodeToString(TestImageWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestImageWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestImageWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = jsonCoder.parseToJsonElement(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getValue("image").jsonObject
        assertEquals("animated", jsonWrapper["type"]?.jsonPrimitive?.content)
        assertEquals(0.25, jsonWrapper["animationDuration"]?.jsonPrimitive?.double)
        val imageNames = jsonWrapper["imageNames"]?.jsonArray
        assertEquals("before", imageNames?.firstOrNull()?.jsonPrimitive?.content)
        assertEquals("after", imageNames?.lastOrNull()?.jsonPrimitive?.content)
    }

    // TODO: syoung 03/07/2022 Uncomment or delete once we know whether or not this is going to be used in future designs.
//    @Test
//    fun testAnimatedImageWithValues() {
//        val image = AnimatedImage(listOf("before","after"), 0.25, 1,"Foo", ImagePlacement.Standard.IconBefore,Size(20.0, 40.0))
//        val inputString = """{"image":{"type":"animated","imageNames":["before", "after"],"animationDuration":0.25,"animationRepeatCount":1,"label":"Foo","placementType":"iconBefore","size":{"width":20.0,"height":40.0}}}"""
//
//        val original = TestImageWrapper(image)
//        val jsonString = jsonCoder.encodeToString(TestImageWrapper.serializer(), original)
//        val restored = jsonCoder.decodeFromString(TestImageWrapper.serializer(), jsonString)
//        val decoded = jsonCoder.decodeFromString(TestImageWrapper.serializer(), inputString)
//
//        // Look to see that the restored, decoded, and original all are equal
//        assertEquals(original, restored)
//        assertEquals(original, decoded)
//
//        // Check the keys and look to see that they match the expected type
//        val jsonOutput = jsonCoder.parseToJsonElement(jsonString)
//        val jsonWrapper = jsonOutput.jsonObject.getValue("image").jsonObject
//        assertEquals("animated", jsonWrapper["type"]?.jsonPrimitive?.content)
//        assertEquals(0.25, jsonWrapper["animationDuration"]?.jsonPrimitive?.double)
//        val imageNames = jsonWrapper["imageNames"]?.jsonArray
//        assertEquals("before", imageNames?.firstOrNull()?.jsonPrimitive?.content)
//        assertEquals("after", imageNames?.lastOrNull()?.jsonPrimitive?.content)
//        assertEquals("Foo", jsonWrapper["label"]?.jsonPrimitive?.content)
//        assertEquals("iconBefore", jsonWrapper["placementType"]?.jsonPrimitive?.content)
//        val sizeObject = jsonWrapper.getValue("size").jsonObject
//        assertNotNull(sizeObject)
//        assertEquals(20.0, sizeObject["width"]?.jsonPrimitive?.double)
//        assertEquals(40.0, sizeObject["height"]?.jsonPrimitive?.double)
//    }
//
//    @Test
//    fun testImagePlacement_Serialization() {
//        val placements = ImagePlacement.Standard.values().toMutableList() + ImagePlacement.Custom("foo")
//        placements.forEach {
//            val name = it.name
//            val inputString = """{"placement":"$name"}"""
//
//            val original = TestImagePlacementWrapper(it)
//            val jsonString =
//                jsonCoder.encodeToString(TestImagePlacementWrapper.serializer(), original)
//            val restored =
//                jsonCoder.decodeFromString(TestImagePlacementWrapper.serializer(), jsonString)
//            val decoded =
//                jsonCoder.decodeFromString(TestImagePlacementWrapper.serializer(), inputString)
//
//            // Look to see that the restored, decoded, and original all are equal
//            assertEquals(original, restored)
//            assertEquals(original, decoded)
//            assertEquals(inputString, jsonString)
//        }
//    }
//
//    @Test
//    fun testImagePlacement_Equality() {
//
//        // list of standard placements
//        val standardPlacements = ImagePlacement.Standard.values()
//
//        // Note: if this fails, then someone added another value to the list of standard values and this test should
//        // be updated to include testing the new value.
//        assertEquals(7, standardPlacements.count())
//
//        // Test of equality within a Set
//        val foo1 = ImagePlacement.Custom("foo")
//        val foo2 = ImagePlacement.Custom("foo")
//        val placements = standardPlacements.toMutableList() + foo1
//        val set = placements.toSet()
//        assertEquals(placements.count(), set.count())
//        assertTrue(set.contains(foo2))
//        assertTrue(set.contains(ImagePlacement.Standard.IconBefore))
//        assertTrue(set.contains(ImagePlacement.Standard.IconAfter))
//        assertTrue(set.contains(ImagePlacement.Standard.TopMarginBackground))
//        assertTrue(set.contains(ImagePlacement.Standard.TopBackground))
//        assertTrue(set.contains(ImagePlacement.Standard.FullSizeBackground))
//        assertTrue(set.contains(ImagePlacement.Standard.BackgroundBefore))
//        assertTrue(set.contains(ImagePlacement.Standard.BackgroundAfter))
//
//        // Test of equality for two custom items
//        assertEquals(foo1, foo2)
//        assertEquals(foo1.hashCode(), foo2.hashCode())
//
//        // Test of equality using the valueOf method
//        standardPlacements.forEach {
//            val placement2 = ImagePlacement.valueOf(it.name.toUpperCase())
//            assertEquals(it, placement2)
//            assertEquals(it.hashCode(), placement2.hashCode())
//        }
//
//        // Test of equality using standard names
//        assertEquals(ImagePlacement.Standard.IconBefore, ImagePlacement.Standard.valueOf("IconBefore"))
//        assertEquals(ImagePlacement.Standard.IconAfter, ImagePlacement.Standard.valueOf("IconAfter"))
//        assertEquals(ImagePlacement.Standard.TopMarginBackground, ImagePlacement.Standard.valueOf("TopMarginBackground"))
//        assertEquals(ImagePlacement.Standard.TopBackground, ImagePlacement.Standard.valueOf("TopBackground"))
//        assertEquals(ImagePlacement.Standard.FullSizeBackground, ImagePlacement.Standard.valueOf("FullSizeBackground"))
//        assertEquals(ImagePlacement.Standard.BackgroundBefore, ImagePlacement.Standard.valueOf("BackgroundBefore"))
//        assertEquals(ImagePlacement.Standard.BackgroundAfter, ImagePlacement.Standard.valueOf("BackgroundAfter"))
//    }
}