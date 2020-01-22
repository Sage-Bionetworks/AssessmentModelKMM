package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.sagebionetworks.assessmentmodel.BackButtonStyle
import org.sagebionetworks.assessmentmodel.Button
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
data class TestButtonWrapper(val button: Button)

open class ButtonTest {

    @Test
    fun testButton() {
        val button = ButtonObject("foo title", FetchableImage("fooImage"))
        val inputString = """
            {
                "button": {
                    "type": "default",
                    "buttonTitle": "foo title",
                    "iconName": "fooImage"
                }
            }    
            """.trimIndent()

        val original = TestButtonWrapper(button)
        val jsonString = jsonCoder.stringify(TestButtonWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestButtonWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestButtonWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = Json.nonstrict.parseJson(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getObject("button")
        assertEquals("default", jsonWrapper.getPrimitiveOrNull("type")?.content)
        assertEquals("foo title", jsonWrapper.getPrimitiveOrNull("buttonTitle")?.content)
        assertEquals("fooImage", jsonWrapper.getPrimitiveOrNull("iconName")?.content)
    }

    @Test
    fun testNavigationButton() {
        val button = NavigationButtonObject("foo title", FetchableImage("fooImage"), "skipToMaFoo")
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
        val jsonString = jsonCoder.stringify(TestButtonWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestButtonWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestButtonWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = Json.nonstrict.parseJson(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getObject("button")
        assertEquals("navigation", jsonWrapper.getPrimitiveOrNull("type")?.content)
        assertEquals("foo title", jsonWrapper.getPrimitiveOrNull("buttonTitle")?.content)
        assertEquals("fooImage", jsonWrapper.getPrimitiveOrNull("iconName")?.content)
        assertEquals("skipToMaFoo", jsonWrapper.getPrimitiveOrNull("skipToIdentifier")?.content)
    }

    @Test
    fun testReminderButtonWithValues() {
        val button = ReminderButtonObject(buttonTitle = "foo title",
                imageInfo = FetchableImage("fooImage"),
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
        val jsonString = jsonCoder.stringify(TestButtonWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestButtonWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestButtonWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = Json.nonstrict.parseJson(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getObject("button")
        assertEquals("reminder", jsonWrapper.getPrimitiveOrNull("type")?.content)
        assertEquals("foo title", jsonWrapper.getPrimitiveOrNull("buttonTitle")?.content)
        assertEquals("fooImage", jsonWrapper.getPrimitiveOrNull("iconName")?.content)
        assertEquals("remindLater", jsonWrapper.getPrimitiveOrNull("reminderIdentifier")?.content)
        assertEquals("Remind me later to do stuff", jsonWrapper.getPrimitiveOrNull("reminderPrompt")?.content)
        assertEquals("Time to do stuff", jsonWrapper.getPrimitiveOrNull("reminderAlert")?.content)
    }

    @Test
    fun testReminderButtonWithDefaults() {
        val button = ReminderButtonObject(reminderIdentifier = "remindLater")
        val inputString = """
            {
                "button": {
                    "type": "reminder",
                    "reminderIdentifier": "remindLater"
                }
            }
            """.trimIndent()

        val original = TestButtonWrapper(button)
        val jsonString = jsonCoder.stringify(TestButtonWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestButtonWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestButtonWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = Json.nonstrict.parseJson(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getObject("button")
        assertEquals("reminder", jsonWrapper.getPrimitiveOrNull("type")?.content)
        assertEquals("\$remindMeLaterButtonTitle\$", jsonWrapper.getPrimitiveOrNull("buttonTitle")?.content)
        assertEquals("remindLater", jsonWrapper.getPrimitiveOrNull("reminderIdentifier")?.content)
    }

    @Test
    fun testWebViewButton() {
        val button = WebViewButtonObject(buttonTitle = "foo title",
                imageInfo = FetchableImage("fooImage"),
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
        val jsonString = jsonCoder.stringify(TestButtonWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestButtonWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestButtonWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = Json.nonstrict.parseJson(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getObject("button")
        assertEquals("webView", jsonWrapper.getPrimitiveOrNull("type")?.content)
        assertEquals("foo title", jsonWrapper.getPrimitiveOrNull("buttonTitle")?.content)
        assertEquals("fooImage", jsonWrapper.getPrimitiveOrNull("iconName")?.content)
        assertEquals("learnMore", jsonWrapper.getPrimitiveOrNull("url")?.content)
        assertEquals("Learn More about this assessment", jsonWrapper.getPrimitiveOrNull("title")?.content)
        assertEquals("Exit", jsonWrapper.getPrimitiveOrNull("closeButtonTitle")?.content)
    }

    @Test
    fun testWebViewButton_BackButtonStyle_Footer() {
        val button = WebViewButtonObject(buttonTitle = "foo title",
                imageInfo = FetchableImage("fooImage"),
                url = "learnMore",
                closeButtonTitle = "Exit")
        assertEquals(BackButtonStyle.Footer("Exit"), button.backButtonStyle)
    }

    @Test
    fun testWebViewButton_BackButtonStyle_BackArrow() {
        val buttonA = WebViewButtonObject(buttonTitle = "foo title",
                imageInfo = FetchableImage("fooImage"),
                url = "learnMore",
                usesBackButton = true)
        assertEquals(BackButtonStyle.Icon.BackArrow, buttonA.backButtonStyle)
    }


    @Test
    fun testWebViewButton_BackButtonStyle_CloseX() {
        val buttonA = WebViewButtonObject(buttonTitle = "foo title",
                imageInfo = FetchableImage("fooImage"),
                url = "learnMore",
                usesBackButton = false)
        assertEquals(BackButtonStyle.Icon.CloseX, buttonA.backButtonStyle)
    }

    @Test
    fun testVideoViewButton() {
        val button = VideoViewButtonObject("foo title", FetchableImage("fooImage"),"learnMore", title = "Learn More about this assessment")
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
        val jsonString = jsonCoder.stringify(TestButtonWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestButtonWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestButtonWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // Check the keys and look to see that they match the expected type
        val jsonOutput = Json.nonstrict.parseJson(jsonString)
        val jsonWrapper = jsonOutput.jsonObject.getObject("button")
        assertEquals("videoView", jsonWrapper.getPrimitiveOrNull("type")?.content)
        assertEquals("foo title", jsonWrapper.getPrimitiveOrNull("buttonTitle")?.content)
        assertEquals("fooImage", jsonWrapper.getPrimitiveOrNull("iconName")?.content)
        assertEquals("learnMore", jsonWrapper.getPrimitiveOrNull("url")?.content)
        assertEquals("Learn More about this assessment", jsonWrapper.getPrimitiveOrNull("title")?.content)
    }
}