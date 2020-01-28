package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.PolymorphicSerializer
import org.sagebionetworks.assessmentmodel.*
import kotlin.math.exp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

open class NodeTest {

    val jsonCoder = Serialization.JsonCoder.default

    @Test
    fun testInstructionStep_Serialization() {
        val inputString = """
           {
               "identifier": "foo",
               "type": "instruction",
               "title": "Hello World!",
               "text": "Some text. This is a test.",
               "footnote": "This is a footnote.",
               "fullInstructionsOnly": true,
               "spokenInstructions": {"start": "Start now"},
               "actions": { "goForward": { "type": "default", "buttonTitle" : "Go, Dogs! Go!" },
                            "cancel": { "type": "default", "iconName" : "closeX" }
                           },
               "shouldHideActions": ["goBackward"],
               "image"  : {    "type" : "animated",
                               "imageNames" : ["foo1", "foo2", "foo3", "foo4"],
                               "placementType" : "topBackground",
                               "animationDuration" : 2
                                  }
           }
           """
        val original = InstructionStepObject("foo")
        original.title = "Hello World!"
        original.detail = "Some text. This is a test."
        original.footnote = "This is a footnote."
        original.fullInstructionsOnly = true
        original.hideButtons = listOf(ButtonAction.Navigation.GoBackward)
        original.buttonMap = mapOf(
                ButtonAction.Navigation.GoForward to ButtonObject(buttonTitle = "Go, Dogs! Go!"),
                ButtonAction.Navigation.Cancel to ButtonObject(imageInfo = FetchableImage("closeX")))
        original.imageInfo = AnimatedImage(
                imageNames = listOf("foo1", "foo2", "foo3", "foo4"),
                imagePlacement = ImagePlacement.Standard.TopBackground,
                animationDuration = 2.0)
        original.spokenInstructions = mapOf("start" to "Start now")

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.stringify(serializer, original)
        val restored = jsonCoder.parse(serializer, jsonString)
        val decoded = jsonCoder.parse(serializer, inputString)

        assertTrue(decoded is InstructionStepObject)
        assertEqualOptionalStep(original, decoded)
        assertEquals(original.detail, decoded.detail)
        assertTrue(restored is InstructionStepObject)
        assertEqualOptionalStep(original, restored)
        assertEquals(original.detail, restored.detail)
    }

    @Test
    fun testSection_Serialization() {
        val inputString = """
            {
                "identifier": "foobar",
                "type": "section",
                "title": "Hello World!",
                "subtitle": "Subtitle",
                "detail": "Some text. This is a test.",
                "icon": "fooIcon",
                "footnote": "This is a footnote.",
                "actions": { "goForward": { "type": "default", "buttonTitle" : "Go, Dogs! Go!" },
                            "cancel": { "type": "default", "iconName" : "closeX" }
                           },
                "shouldHideActions": ["goBackward"],
                "steps": [
                    {
                        "identifier": "step1",
                        "type": "instruction",
                        "title": "Step 1"
                    },
                    {
                        "identifier": "step2",
                        "type": "instruction",
                        "title": "Step 2"
                    }
                ]
            }
            """

        val original = SectionObject(
                identifier = "foobar",
                children = listOf(
                        buildInstructionStep("step1", "Step 1"),
                        buildInstructionStep("step2", "Step 2")))
        original.title = "Hello World!"
        original.subtitle = "Subtitle"
        original.detail = "Some text. This is a test."
        original.footnote = "This is a footnote."
        original.hideButtons = listOf(ButtonAction.Navigation.GoBackward)
        original.buttonMap = mapOf(
                ButtonAction.Navigation.GoForward to ButtonObject(buttonTitle = "Go, Dogs! Go!"),
                ButtonAction.Navigation.Cancel to ButtonObject(imageInfo = FetchableImage("closeX")))
        original.imageInfo = FetchableImage("fooIcon")

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.stringify(serializer, original)
        val restored = jsonCoder.parse(serializer, jsonString)
        val decoded = jsonCoder.parse(serializer, inputString)

        assertTrue(decoded is SectionObject)
        assertContainerNode(original, decoded)
        assertEquals(original.subtitle, decoded.subtitle)
        assertEquals(original.detail, decoded.detail)
        assertEquals(original.footnote, decoded.footnote)

        assertTrue(restored is SectionObject)
        assertContainerNode(original, restored)
        assertEquals(original.subtitle, restored.subtitle)
        assertEquals(original.detail, restored.detail)
        assertEquals(original.footnote, restored.footnote)
    }

    fun assertEqualResultMapElement(expected: ResultMapElement, actual: ResultMapElement) {
        assertEquals(expected.identifier, actual.identifier)
        assertEquals(expected.resultIdentifier, actual.resultIdentifier)
        assertEquals(expected.comment, actual.comment)
        assertEquals(expected.createResult(), actual.createResult())
    }

    fun assertEqualNodes(expected: Node, actual: Node) {
        assertEqualResultMapElement(expected, actual)
        assertEquals(expected.title, actual.title)
        assertEquals(expected.imageInfo, actual.imageInfo)
        assertEquals(expected.footnote, actual.footnote)
        assertEquals(expected.hideButtons, actual.hideButtons)
        assertEquals(expected.buttonMap, actual.buttonMap)
    }

    fun assertContainerNode(expected: NodeContainer, actual: NodeContainer) {
        assertEqualNodes(expected, actual)
        assertEquals(expected.children.count(), actual.children.count())
        actual.children.forEachIndexed { index, node ->
            assertEqualNodes(expected.children[index], node)
        }
    }

    fun assertEqualOptionalStep(expected: OptionalStep, actual: OptionalStep) {
        assertEqualStep(expected, actual)
        assertEquals(expected.fullInstructionsOnly, actual.fullInstructionsOnly)
    }

    fun assertEqualStep(expected: Step, actual: Step) {
        assertEqualNodes(expected, actual)
        assertEquals(expected.spokenInstructions, actual.spokenInstructions)
    }

    fun buildInstructionStep(identifier: String, title: String): InstructionStepObject {
        val instruction = InstructionStepObject(identifier)
        instruction.title = title
        return instruction
    }
}