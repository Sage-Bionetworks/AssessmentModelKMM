package org.sagebionetworks.assessmentmodel.serialization

import org.sagebionetworks.assessmentmodel.*
import kotlin.math.exp
import kotlin.test.Test
import kotlin.test.assertEquals

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
        val original = InstructionStepObject(
                identifier = "foo",
                title = "Hello World!",
                detail = "Some text. This is a test.",
                footnote = "This is a footnote.",
                fullInstructionsOnly = true,
                hideButtons = listOf(ButtonAction.Navigation.GoBackward),
                buttonMap = mapOf(
                        ButtonAction.Navigation.GoForward to ButtonObject(buttonTitle = "Go, Dogs! Go!"),
                        ButtonAction.Navigation.Cancel to ButtonObject(imageInfo = FetchableImage("closeX"))),
                imageInfo = AnimatedImage(
                        imageNames = listOf("foo1", "foo2", "foo3", "foo4"),
                        imagePlacement = ImagePlacement.Standard.TopBackground,
                        animationDuration = 2.0),
                spokenInstructions = mapOf(
                        "start" to "Start now"
                )
        )

        val serializer = InstructionStepObject.serializer()
        val jsonString = jsonCoder.stringify(serializer, original)
        val restored = jsonCoder.parse(serializer, jsonString)
        val decoded = jsonCoder.parse(serializer, inputString)

        assertEqualOptionalStep(original, decoded)
        assertEquals(original.detail, decoded.detail)
        assertEqualOptionalStep(original, restored)
        assertEquals(original.detail, restored.detail)
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

    fun assertEqualOptionalStep(expected: OptionalStep, actual: OptionalStep) {
        assertEqualStep(expected, actual)
        assertEquals(expected.fullInstructionsOnly, actual.fullInstructionsOnly)
    }

    fun assertEqualStep(expected: Step, actual: Step) {
        assertEqualNodes(expected, actual)
        assertEquals(expected.spokenInstructions, actual.spokenInstructions)
    }
}