package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.sagebionetworks.assessmentmodel.ButtonActionInfo
import org.sagebionetworks.assessmentmodel.ButtonAction
import org.sagebionetworks.assessmentmodel.ButtonStyle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

open class ButtonTest {

    @Serializable
    data class TestButtonWrapper(val button: ButtonActionInfo)

    @Serializable
    data class TestButtonStyleWrapper(val buttonStyle: ButtonStyle)

    @Serializable
    data class TestButtonActionWrapper(val buttonAction: ButtonAction)

    private val jsonCoder = Serialization.JsonCoder.default

    @Test
    fun testButton() {
        val button = ButtonActionInfoObject("foo title", "fooImage")
        button.bundleIdentifier = "org.SageBase.Example"
        button.packageName = "org.sagebase.example.resources"
        val inputString = """
            {
                "button": {
                    "type": "default",
                    "buttonTitle": "foo title",
                    "iconName": "fooImage",
                    "bundleIdentifier": "org.SageBase.Example",
                    "packageName": "org.sagebase.example.resources"
                }
            }    
            """.trimIndent()

        val original = TestButtonWrapper(button)
        val jsonString = jsonCoder.encodeToString(TestButtonWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestButtonWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestButtonWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = jsonCoder.parseToJsonElement(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getValue("button").jsonObject
        assertEquals("default", jsonWrapper["type"]?.jsonPrimitive?.content)
        assertEquals("foo title", jsonWrapper["buttonTitle"]?.jsonPrimitive?.content)
        assertEquals("fooImage", jsonWrapper["iconName"]?.jsonPrimitive?.content)
    }

    @Test
    fun testNavigationButton() {
        val button = NavigationButtonActionInfoObject("foo title", "fooImage", "skipToMaFoo")
        val inputString = """
            {
                "button": {
                    "type": "navigation",
                    "skipToIdentifier": "skipToMaFoo",
                    "buttonTitle": "foo title",
                    "iconName": "fooImage"
                }
            }
            """.trimIndent()

        val original = TestButtonWrapper(button)
        val jsonString = jsonCoder.encodeToString(TestButtonWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestButtonWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestButtonWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = jsonCoder.parseToJsonElement(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getValue("button").jsonObject
        assertEquals("navigation", jsonWrapper["type"]?.jsonPrimitive?.content)
        assertEquals("foo title", jsonWrapper["buttonTitle"]?.jsonPrimitive?.content)
        assertEquals("fooImage", jsonWrapper["iconName"]?.jsonPrimitive?.content)
        assertEquals("skipToMaFoo", jsonWrapper["skipToIdentifier"]?.jsonPrimitive?.content)
    }

    @Test
    fun testReminderButtonWithValues() {
        val button = ReminderButtonActionInfoObject(buttonTitle = "foo title",
                iconName = "fooImage",
                reminderIdentifier = "remindLater",
                reminderPrompt = "Remind me later to do stuff",
                reminderAlert = "Time to do stuff")
        val inputString = """
            {
                "button": {
                    "type": "reminder",
                    "reminderIdentifier": "remindLater",
                    "buttonTitle": "foo title",
                    "iconName": "fooImage",
                    "reminderPrompt": "Remind me later to do stuff",
                    "reminderAlert": "Time to do stuff"
                }
            }
            """.trimIndent()

        val original = TestButtonWrapper(button)
        val jsonString = jsonCoder.encodeToString(TestButtonWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestButtonWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestButtonWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = jsonCoder.parseToJsonElement(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getValue("button").jsonObject
        assertEquals("reminder", jsonWrapper["type"]?.jsonPrimitive?.content)
        assertEquals("foo title", jsonWrapper["buttonTitle"]?.jsonPrimitive?.content)
        assertEquals("fooImage", jsonWrapper["iconName"]?.jsonPrimitive?.content)
        assertEquals("remindLater", jsonWrapper["reminderIdentifier"]?.jsonPrimitive?.content)
        assertEquals("Remind me later to do stuff", jsonWrapper["reminderPrompt"]?.jsonPrimitive?.content)
        assertEquals("Time to do stuff", jsonWrapper["reminderAlert"]?.jsonPrimitive?.content)
    }

    @Test
    fun testReminderButtonWithDefaults() {
        val button = ReminderButtonActionInfoObject(reminderIdentifier = "remindLater")
        val inputString = """
            {
                "button": {
                    "type": "reminder",
                    "reminderIdentifier": "remindLater"
                }
            }
            """.trimIndent()

        val original = TestButtonWrapper(button)
        val jsonString = jsonCoder.encodeToString(TestButtonWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestButtonWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestButtonWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = jsonCoder.parseToJsonElement(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getValue("button").jsonObject
        assertEquals("reminder", jsonWrapper["type"]?.jsonPrimitive?.content)
        assertEquals("\$remindMeLaterButtonTitle\$", jsonWrapper["buttonTitle"]?.jsonPrimitive?.content)
        assertEquals("remindLater", jsonWrapper["reminderIdentifier"]?.jsonPrimitive?.content)
    }

    @Test
    fun testWebViewButton() {
        val button = WebViewButtonActionInfoObject(buttonTitle = "foo title",
                iconName = "fooImage",
                url = "learnMore",
                title = "Learn More about this assessment",
                closeButtonTitle = "Exit")
        val inputString = """
            {
                "button": {
                    "type": "webView",
                    "url": "learnMore",
                    "buttonTitle": "foo title",
                    "iconName": "fooImage",
                    "title":"Learn More about this assessment",
                    "closeButtonTitle":"Exit"
                }
            }
            """.trimIndent()

        val original = TestButtonWrapper(button)
        val jsonString = jsonCoder.encodeToString(TestButtonWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestButtonWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestButtonWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = jsonCoder.parseToJsonElement(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getValue("button").jsonObject
        assertEquals("webView", jsonWrapper["type"]?.jsonPrimitive?.content)
        assertEquals("foo title", jsonWrapper["buttonTitle"]?.jsonPrimitive?.content)
        assertEquals("fooImage", jsonWrapper["iconName"]?.jsonPrimitive?.content)
        assertEquals("learnMore", jsonWrapper["url"]?.jsonPrimitive?.content)
        assertEquals("Learn More about this assessment", jsonWrapper["title"]?.jsonPrimitive?.content)
        assertEquals("Exit", jsonWrapper["closeButtonTitle"]?.jsonPrimitive?.content)
    }

    @Test
    fun testWebViewButton_BackButtonStyle_Footer() {
        val button = WebViewButtonActionInfoObject(buttonTitle = "foo title",
                iconName = "fooImage",
                url = "learnMore",
                closeButtonTitle = "Exit")
        assertEquals(ButtonStyle.Footer("Exit"), button.backButtonStyle)
    }

    @Test
    fun testWebViewButton_BackButtonStyle_BackArrow() {
        val buttonA = WebViewButtonActionInfoObject(buttonTitle = "foo title",
                iconName = "fooImage",
                url = "learnMore",
                usesBackButton = true)
        assertEquals(ButtonStyle.NavigationHeader.Back, buttonA.backButtonStyle)
    }


    @Test
    fun testWebViewButton_BackButtonStyle_CloseX() {
        val buttonA = WebViewButtonActionInfoObject(buttonTitle = "foo title",
                iconName = "fooImage",
                url = "learnMore",
                usesBackButton = false)
        assertEquals(ButtonStyle.NavigationHeader.Close, buttonA.backButtonStyle)
    }

    @Test
    fun testVideoViewButton() {
        val button = VideoViewButtonActionInfoObject("foo title", "fooImage","learnMore", title = "Learn More about this assessment")
        val inputString = """
            {
                "button": {
                    "type": "videoView",
                    "url": "learnMore",
                    "buttonTitle": "foo title",
                    "iconName": "fooImage",
                    "title":"Learn More about this assessment"
                }
            }
            """.trimIndent()

        val original = TestButtonWrapper(button)
        val jsonString = jsonCoder.encodeToString(TestButtonWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestButtonWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestButtonWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = jsonCoder.parseToJsonElement(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getValue("button").jsonObject
        assertEquals("videoView", jsonWrapper["type"]?.jsonPrimitive?.content)
        assertEquals("foo title", jsonWrapper["buttonTitle"]?.jsonPrimitive?.content)
        assertEquals("fooImage", jsonWrapper["iconName"]?.jsonPrimitive?.content)
        assertEquals("learnMore", jsonWrapper["url"]?.jsonPrimitive?.content)
        assertEquals("Learn More about this assessment", jsonWrapper["title"]?.jsonPrimitive?.content)
    }

    @Test
    fun testButtonStyle_Back_Serialization() {
        val buttonStyle = ButtonStyle.NavigationHeader.Back
        val inputString = """{"buttonStyle":{"type":"header.back"}}"""

        val original = TestButtonStyleWrapper(buttonStyle)
        val jsonString = jsonCoder.encodeToString(TestButtonStyleWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestButtonStyleWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestButtonStyleWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
        assertEquals(inputString, jsonString)
    }

    @Test
    fun testButtonStyle_Close_Serialization() {
        val buttonStyle = ButtonStyle.NavigationHeader.Close
        val inputString = """{"buttonStyle":{"type":"header.close"}}"""

        val original = TestButtonStyleWrapper(buttonStyle)
        val jsonString = jsonCoder.encodeToString(TestButtonStyleWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestButtonStyleWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestButtonStyleWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
        assertEquals(inputString, jsonString)
    }

    @Test
    fun testButtonStyle_Footer_Serialization() {
        val buttonStyle = ButtonStyle.Footer("Foo")
        val inputString = """{"buttonStyle":{"type":"footer","buttonTitle":"Foo"}}"""

        val original = TestButtonStyleWrapper(buttonStyle)
        val jsonString = jsonCoder.encodeToString(TestButtonStyleWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestButtonStyleWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestButtonStyleWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
        assertEquals(inputString, jsonString)
    }

    @Test
    fun testButtonAction_Serialization() {
        val actions = ButtonAction.Navigation.values().toMutableList() + ButtonAction.Custom("foo")
        actions.forEach {
            val name = it.name
            val inputString = """{"buttonAction":"$name"}"""

            val original = TestButtonActionWrapper(it)
            val jsonString = jsonCoder.encodeToString(
                TestButtonActionWrapper.serializer(),
                original
            )
            val restored =
                jsonCoder.decodeFromString(TestButtonActionWrapper.serializer(), jsonString)
            val decoded =
                jsonCoder.decodeFromString(TestButtonActionWrapper.serializer(), inputString)

            // Look to see that the restored, decoded, and original all are equal
            assertEquals(original, restored)
            assertEquals(original, decoded)
            assertEquals(inputString, jsonString)
        }
    }

    @Test
    fun testButtonAction_Equality() {

        // list of standard placements
        val naviationActions = ButtonAction.Navigation.values()

        // Note: if this fails, then someone added another value to the list of standard values and this test should
        // be updated to include testing the new value.
        assertEquals(6, naviationActions.count())

        // Test of equality within a Set
        val foo1 = ButtonAction.Custom("foo")
        val foo2 = ButtonAction.Custom("foo")
        val actions = naviationActions.toMutableList() + foo1
        val set = actions.toSet()
        assertEquals(actions.count(), set.count())
        assertTrue(set.contains(foo2))
        assertTrue(set.contains(ButtonAction.Navigation.GoForward))
        assertTrue(set.contains(ButtonAction.Navigation.GoBackward))
        assertTrue(set.contains(ButtonAction.Navigation.Skip))
        assertTrue(set.contains(ButtonAction.Navigation.Cancel))
        assertTrue(set.contains(ButtonAction.Navigation.LearnMore))
        assertTrue(set.contains(ButtonAction.Navigation.ReviewInstructions))

        // Test of equality for two custom items
        assertEquals(foo1, foo2)
        assertEquals(foo1.hashCode(), foo2.hashCode())

        // Test of equality using the valueOf method
        naviationActions.forEach {
            val obj2 = ButtonAction.valueOf(it.name.toUpperCase())
            assertEquals(it, obj2)
            assertEquals(it.hashCode(), obj2.hashCode())
        }

        // Test of equality using standard names
        assertEquals(ButtonAction.Navigation.GoForward, ButtonAction.Navigation.valueOf("GoForward"))
        assertEquals(ButtonAction.Navigation.GoBackward, ButtonAction.Navigation.valueOf("GoBackward"))
        assertEquals(ButtonAction.Navigation.Skip, ButtonAction.Navigation.valueOf("Skip"))
        assertEquals(ButtonAction.Navigation.Cancel, ButtonAction.Navigation.valueOf("Cancel"))
        assertEquals(ButtonAction.Navigation.LearnMore, ButtonAction.Navigation.valueOf("LearnMore"))
        assertEquals(ButtonAction.Navigation.ReviewInstructions, ButtonAction.Navigation.valueOf("ReviewInstructions"))
    }
}