package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.IdentifierPath
import org.sagebionetworks.assessmentmodel.resourcemanagement.*
import org.sagebionetworks.assessmentmodel.survey.*
import kotlin.test.*

open class NodeTest : NodeSerializationTestHelper() {

    private val jsonCoder =  Json{
        serializersModule = Serialization.SerializersModule.default
            .plus( SerializersModule {
                polymorphic(AsyncActionConfiguration::class) {
                    subclass(MotionRecorderConfiguration::class)
                }
            })
        encodeDefaults = true
    }

    /**
     * ActiveStepObject
     */

    @Test
    fun testActiveStep_Serialization() {
        val inputString = """
           {
               "identifier": "foo",
               "type": "active",
               "title": "Hello World!",
               "duration": 30,
               "requiresBackgroundAudio": true,
               "shouldEndOnInterrupt": true,
               "commands": ["playSoundOnStart", "vibrate"],
               "spokenInstructions" : { "start": "Start moving",
                                        "1.5": "Up",
                                        "3.0": "Down",
                                        "10": "Keep going",
                                        "halfway": "Halfway there",
                                        "countdown": "5",
                                        "end": "Stop moving"},
               "image"  : {    "type" : "animated",
                               "imageNames" : ["foo1", "foo2", "foo3", "foo4"],
                               "animationDuration" : 2}
            }
           """
        val original = ActiveStepObject("foo", 30.0)
        original.title = "Hello World!"
        original.spokenInstructions = mapOf(
            SpokenInstructionTiming.Keyword.Start to "Start moving",
            SpokenInstructionTiming.TimeInterval(1.5) to "Up",
            SpokenInstructionTiming.TimeInterval(3.0) to "Down",
            SpokenInstructionTiming.TimeInterval(10.0) to "Keep going",
            SpokenInstructionTiming.Keyword.Halfway to "Halfway there",
            SpokenInstructionTiming.Keyword.Countdown to "5",
            SpokenInstructionTiming.Keyword.End to "Stop moving"
        )
        original.requiresBackgroundAudio = true
        original.shouldEndOnInterrupt = true
        original.imageInfo = AnimatedImage(
                imageNames = listOf("foo1", "foo2", "foo3", "foo4"),
                animationDuration = 2.0)
        original.commands = setOf(
            ActiveStepCommand.PlaySoundOnStart,
            ActiveStepCommand.VibrateOnStart,
            ActiveStepCommand.VibrateOnFinish)

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertTrue(decoded is ActiveStepObject)
        assertEquals(original, decoded)
        assertEqualActiveStep(original, decoded)
        assertEqualContentNodes(original, decoded)

        assertTrue(restored is ActiveStepObject)
        assertEquals(original, restored)
        assertEqualActiveStep(original, restored)
        assertEqualContentNodes(original, restored)

        val copy = original.copy()
        copy.copyFrom(original)
        assertEquals(original, copy)
        assertEqualActiveStep(original, copy)
        assertEqualContentNodes(original, copy)
    }

    @Test
    fun testActiveCommands_Pairs_Serialization() {
        val stringSet = setOf("playSound", "vibrate", "transitionAutomatically")
        val commands = ActiveStepCommand.fromStrings(stringSet)
        val expected = setOf(
            ActiveStepCommand.PlaySoundOnStart, ActiveStepCommand.PlaySoundOnFinish,
            ActiveStepCommand.VibrateOnStart, ActiveStepCommand.VibrateOnFinish,
            ActiveStepCommand.StartTimerAutomatically, ActiveStepCommand.ContinueOnFinish
        )
        assertEquals(expected, commands)
    }

    @Test
    fun testActiveCommands_Enum_Serialization() {
        val stringSet = setOf(
            "playSoundOnStart", "playSoundOnFinish",
            "vibrateOnStart", "vibrateOnFinish",
            "startTimerAutomatically", "continueOnFinish",
            "shouldDisableIdleTimer",
            "speakWarningOnPause"
        )
        val commands = ActiveStepCommand.fromStrings(stringSet)
        val expected = setOf(
            ActiveStepCommand.PlaySoundOnStart, ActiveStepCommand.PlaySoundOnFinish,
            ActiveStepCommand.VibrateOnStart, ActiveStepCommand.VibrateOnFinish,
            ActiveStepCommand.StartTimerAutomatically, ActiveStepCommand.ContinueOnFinish,
            ActiveStepCommand.ShouldDisableIdleTimer,
            ActiveStepCommand.SpeakWarningOnPause
        )
        assertEquals(expected, commands)
    }


    /**
     * AssessmentObject
     */

    @Test
    fun testAssessment_Serialization() {
        val inputString = """
            {
                "type": "assessment",
                "identifier": "foo",
                "versionString": "1.2.3",
                "title": "Hello World!",
                "subtitle": "Subtitle",
                "detail": "Some text. This is a test.",
                "estimatedMinutes": 4,
                "icon": "fooIcon",
                "footnote": "This is a footnote.",
                "actions": { "goForward": { "type": "default", "buttonTitle" : "Go, Dogs! Go!" },
                            "cancel": { "type": "default", "iconName" : "closeX" }
                           },
                "shouldHideActions": ["goBackward"],
                "progressMarkers": ["step1","step2"],
                "asyncActions"      : [
                                      {
                                      "identifier"              : "shakeItUp",
                                      "type"                    : "motion"
                                      }
                                      ],
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

        val original = AssessmentObject(
                identifier = "foo",
                versionString = "1.2.3",
                children = listOf(
                        buildInstructionStep("step1", "Step 1"),
                        buildInstructionStep("step2", "Step 2")),
                backgroundActions = listOf(MotionRecorderConfiguration("shakeItUp")))
        original.title = "Hello World!"
        original.subtitle = "Subtitle"
        original.detail = "Some text. This is a test."
        original.estimatedMinutes = 4
        original.footnote = "This is a footnote."
        original.hideButtons = listOf(ButtonAction.Navigation.GoBackward)
        original.buttonMap = mapOf(
                ButtonAction.Navigation.GoForward to ButtonActionInfoObject(buttonTitle = "Go, Dogs! Go!"),
                ButtonAction.Navigation.Cancel to ButtonActionInfoObject(iconName = "closeX"))
        original.imageInfo = FetchableImage("fooIcon")
        original.progressMarkers = listOf("step1", "step2")

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertTrue(decoded is AssessmentObject)
        assertContainerNode(original, decoded)
        assertEqualContentNodes(original, decoded)
        assertEquals(original.estimatedMinutes, decoded.estimatedMinutes)
        assertEquals(original.versionString, decoded.versionString)

        assertTrue(restored is AssessmentObject)
        assertContainerNode(original, restored)
        assertEqualContentNodes(original, restored)
        assertEquals(original.estimatedMinutes, restored.estimatedMinutes)
        assertEquals(original.versionString, restored.versionString)
    }

    @Test
    fun testAssessment_Result() {
        val original = AssessmentObject(
                identifier = "foo",
                children = listOf(
                        buildInstructionStep("step1", "Step 1"),
                        buildInstructionStep("step2", "Step 2")))
        val result = original.createResult()
        assertEquals("foo", result.identifier)
    }

    /**
     * ChoiceQuestionObject
     */

    @Test
    fun testChoiceQuestion_Serialization() {
        val inputString = """
           {
               "identifier": "foo",
               "type": "choiceQuestion",
               "title": "Hello World!",
               "subtitle": "Question subtitle",
               "detail": "Some text. This is a test.",
               "footnote": "This is a footnote.",
               "image"  : {    "type" : "animated",
                               "imageNames" : ["foo1", "foo2", "foo3", "foo4"],
                               "animationDuration" : 2
                                  },
                "optional": false,
                "singleChoice": false,
                "baseType": "integer",
                "uiHint": "checkbox",
                "choices":[
                {"text":"choice 1","icon":"choice1","value":1},
                {"text":"choice 2","value":2},
                {"text":"choice 3","value":3},
                {"text":"none of the above","selectorType":"exclusive"}
                ]
           }
           """
        val original = ChoiceQuestionObject(
                identifier = "foo",
                choices = listOf(
                        JsonChoiceObject(JsonPrimitive(1), "choice 1", "choice1"),
                        JsonChoiceObject(JsonPrimitive(2), "choice 2"),
                        JsonChoiceObject(JsonPrimitive(3), "choice 3"),
                        JsonChoiceObject(JsonNull, "none of the above", null, ChoiceSelectorType.Exclusive)
                ),
                baseType = BaseType.INTEGER)
        original.uiHint = UIHint.Choice.Checkbox
        original.title = "Hello World!"
        original.subtitle = "Question subtitle"
        original.detail = "Some text. This is a test."
        original.footnote = "This is a footnote."
        original.imageInfo = AnimatedImage(
                imageNames = listOf("foo1", "foo2", "foo3", "foo4"),
                animationDuration = 2.0)

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertTrue(decoded is ChoiceQuestionObject)
        assertEqualStep(original, decoded)
        assertEqualContentNodes(original, decoded)
        assertTrue(restored is ChoiceQuestionObject)
        assertEqualStep(original, restored)
        assertEqualContentNodes(original, restored)
    }

    /**
     * ComboBoxQuestion
     */

    @Test
    fun testComboBoxQuestion_Serialization() {
        val inputString = """
           {
               "identifier": "foo",
               "type": "choiceQuestion",
               "title": "Hello World!",
               "subtitle": "Question subtitle",
               "detail": "Some text. This is a test.",
               "footnote": "This is a footnote.",
               "image"  : {    "type" : "animated",
                               "imageNames" : ["foo1", "foo2", "foo3", "foo4"],
                               "animationDuration" : 2
                                  },
                "optional": false,
                "singleChoice": false,
                "uiHint": "checkbox",
                "choices":[
                {"text":"choice 1","value":"one"},
                {"text":"choice 2","value":"two"},
                {"text":"choice 3","value":"three"}
                ],
                "other":{
                    "type": "string",
                    "fieldLabel": "Something else"
                   }
           }
           """
        val otherInputItem = StringTextInputItemObject()
        otherInputItem.fieldLabel = "Something else"

        val original = ChoiceQuestionObject(
                identifier = "foo",
                choices = listOf(
                        JsonChoiceObject(JsonPrimitive("one"), "choice 1"),
                        JsonChoiceObject(JsonPrimitive("two"), "choice 2"),
                        JsonChoiceObject(JsonPrimitive("three"), "choice 3"),
                        JsonChoiceObject(JsonNull, "none of the above", null, ChoiceSelectorType.Exclusive)
                ),
                other = otherInputItem)
        original.uiHint = UIHint.Choice.Checkbox
        original.title = "Hello World!"
        original.subtitle = "Question subtitle"
        original.detail = "Some text. This is a test."
        original.footnote = "This is a footnote."
        original.imageInfo = AnimatedImage(
                imageNames = listOf("foo1", "foo2", "foo3", "foo4"),
                animationDuration = 2.0)

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertTrue(decoded is ChoiceQuestionObject)
        assertEqualStep(original, decoded)
        assertEqualContentNodes(original, decoded)
        assertTrue(restored is ChoiceQuestionObject)
        assertEqualStep(original, restored)
        assertEqualContentNodes(original, restored)
    }

    /**
     * CountdownStepObject
     */

    @Test
    fun testCountdownStep_DefaultParams() {
        // Check default situation that duration = 5.0, and
        // commands contains auto transition.
        val original = CountdownStepObject("foo", fullInstructionsOnly = true)
        original.title = "Hello World!"

        assertEquals(5.0, original.duration)
        assertEquals(setOf(ActiveStepCommand.ContinueOnFinish, ActiveStepCommand.StartTimerAutomatically), original.commands)

        // Test that the auto transition always exists in the commands.
        original.commands.plus(ActiveStepCommand.PlaySoundOnFinish)
        assertTrue(original.commands.contains(ActiveStepCommand.ContinueOnFinish))
        assertTrue(original.commands.contains(ActiveStepCommand.StartTimerAutomatically))

        original.commands = setOf()
        assertTrue(original.commands.contains(ActiveStepCommand.ContinueOnFinish))
        assertTrue(original.commands.contains(ActiveStepCommand.StartTimerAutomatically))
    }

    @Test
    fun testCountdownStep_Serialization() {
        val inputString = """
           {
               "identifier": "foo",
               "type": "countdown",
               "fullInstructionsOnly": true,
               "title": "Hello World!",
               "duration": 30,
               "requiresBackgroundAudio": true,
               "shouldEndOnInterrupt": true,
               "commands": ["playSoundOnStart", "vibrate"],
               "spokenInstructions" : { "start": "Start moving",
                                        "1.5": "Up",
                                        "3.0": "Down",
                                        "10": "Keep going",
                                        "halfway": "Halfway there",
                                        "countdown": "5",
                                        "end": "Stop moving"},
               "image"  : {    "type" : "animated",
                               "imageNames" : ["foo1", "foo2", "foo3", "foo4"],
                               "animationDuration" : 2}
            }
           """
        val original = CountdownStepObject("foo", 30.0, true)
        original.title = "Hello World!"
        original.spokenInstructions = mapOf(
            SpokenInstructionTiming.Keyword.Start to "Start moving",
            SpokenInstructionTiming.TimeInterval(1.5) to "Up",
            SpokenInstructionTiming.TimeInterval(3.0) to "Down",
            SpokenInstructionTiming.TimeInterval(10.0) to "Keep going",
            SpokenInstructionTiming.Keyword.Halfway to "Halfway there",
            SpokenInstructionTiming.Keyword.Countdown to "5",
            SpokenInstructionTiming.Keyword.End to "Stop moving"
        )
        original.requiresBackgroundAudio = true
        original.shouldEndOnInterrupt = true
        original.imageInfo = AnimatedImage(
            imageNames = listOf("foo1", "foo2", "foo3", "foo4"),
            animationDuration = 2.0)
        original.commands = setOf(
            ActiveStepCommand.PlaySoundOnStart,
            ActiveStepCommand.VibrateOnStart,
            ActiveStepCommand.VibrateOnFinish)

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertTrue(decoded is CountdownStepObject)
        assertEquals(original, decoded)
        assertEqualActiveStep(original, decoded)
        assertEqualContentNodes(original, decoded)

        assertTrue(restored is CountdownStepObject)
        assertEquals(original, restored)
        assertEqualActiveStep(original, restored)
        assertEqualContentNodes(original, restored)

        val copy = original.copy()
        copy.copyFrom(original)
        assertEquals(original, copy)
        assertEqualActiveStep(original, copy)
        assertEqualContentNodes(original, copy)
    }

    /**
     * InstructionStepObject
     */

    @Test
    fun testInstructionStep_Serialization() {
        val inputString = """
           {
               "identifier": "foo",
               "type": "instruction",
               "title": "Hello World!",
               "detail": "Some text. This is a test.",
               "footnote": "This is a footnote.",
               "fullInstructionsOnly": true,
               "spokenInstructions": {"start": "Start now"},
               "nextStepIdentifier": "ragu",
               "actions": { "goForward": { "type": "default", "buttonTitle" : "Go, Dogs! Go!" },
                            "cancel": { "type": "default", "iconName" : "closeX" }
                           },
               "shouldHideActions": ["goBackward"],
               "image"  : {    "type" : "animated",
                               "imageNames" : ["foo1", "foo2", "foo3", "foo4"],
                               "animationDuration" : 2
                                  },
                "viewTheme": { 
                            "viewIdentifier":"Moo",
                            "storyboardIdentifier":"Ba",
                            "fragmentIdentifier":"La",
                            "fragmentLayout":"LaLa" }
           }
           """
        val original = InstructionStepObject("foo")
        original.title = "Hello World!"
        original.detail = "Some text. This is a test."
        original.footnote = "This is a footnote."
        original.fullInstructionsOnly = true
        original.nextNodeIdentifier = "ragu"
        original.hideButtons = listOf(ButtonAction.Navigation.GoBackward)
        original.buttonMap = mapOf(
                ButtonAction.Navigation.GoForward to ButtonActionInfoObject(buttonTitle = "Go, Dogs! Go!"),
                ButtonAction.Navigation.Cancel to ButtonActionInfoObject(iconName ="closeX"))
        original.imageInfo = AnimatedImage(
                imageNames = listOf("foo1", "foo2", "foo3", "foo4"),
                animationDuration = 2.0)
        original.spokenInstructions = mapOf(SpokenInstructionTiming.Keyword.Start to "Start now")

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertTrue(decoded is InstructionStepObject)
        assertEqualOptionalStep(original, decoded)
        assertEqualContentNodes(original, decoded)
        assertEquals(original.nextNodeIdentifier, decoded.nextNodeIdentifier)

        assertTrue(restored is InstructionStepObject)
        assertEqualOptionalStep(original, restored)
        assertEqualContentNodes(original, restored)
        assertEquals(original.nextNodeIdentifier, restored.nextNodeIdentifier)

        val copy = original.copy()
        copy.copyFrom(original)
        assertEqualOptionalStep(original, copy)
        assertEqualContentNodes(original, copy)
        assertEquals(original.nextNodeIdentifier, copy.nextNodeIdentifier)
    }

    @Test
    fun testInstructionStep_Result() {
        val original = InstructionStepObject("foo")
        val result = original.createResult()
        assertEquals("foo", result.identifier)
    }

    /**
     * OverviewStepObject
     */

    @Test
    fun testOverviewStep_Serialization() {
        val inputString = """
           {
               "identifier": "foo",
               "type": "overview",
               "title": "Hello World!",
               "detail": "Some text. This is a test.",
               "footnote": "This is a footnote.",
               "actions": { "goForward": { "type": "default", "buttonTitle" : "Go, Dogs! Go!" },
                            "cancel": { "type": "default", "iconName" : "closeX" }
                           },
               "icons" : [ {"icon": "cuteDogs", "title": "Cute Dogs"} ],                
               "permissions":[
                    {
                        "permissionType":"motion",
                        "reason":"Access to Motion and Fitness sensors is used to measure the phone's orientation.",
                        "optional":true
                   }
                ],
               "image"  : {    "type" : "animated",
                               "imageNames" : ["foo1", "foo2", "foo3", "foo4"],
                               "animationDuration" : 2}
           }
           """
        val original = OverviewStepObject("foo")
        original.title = "Hello World!"
        original.detail = "Some text. This is a test."
        original.footnote = "This is a footnote."
        original.buttonMap = mapOf(
            ButtonAction.Navigation.GoForward to ButtonActionInfoObject(buttonTitle = "Go, Dogs! Go!"),
            ButtonAction.Navigation.Cancel to ButtonActionInfoObject(iconName ="closeX"))
        original.icons = listOf(IconInfoObject("cuteDogs", "Cute Dogs"))
        original.permissions = listOf(PermissionInfoObject(
            permissionType = PermissionType.Standard.Motion,
            optional = true,
            reason = "Access to Motion and Fitness sensors is used to measure the phone's orientation."
        ))
        original.imageInfo = AnimatedImage(
            imageNames = listOf("foo1", "foo2", "foo3", "foo4"),
            animationDuration = 2.0)

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertTrue(decoded is OverviewStepObject)
        assertEquals(original, decoded)
        assertEqualContentNodes(original, decoded)
        assertEquals(original.icons, decoded.icons)

        assertTrue(restored is OverviewStepObject)
        assertEquals(original, restored)
        assertEqualContentNodes(original, restored)
        assertEquals(original.icons, restored.icons)

        val copy = original.copy()
        copy.copyFrom(original)
        assertEquals(original, copy)
        assertEqualContentNodes(original, copy)
        assertEquals(original.icons, copy.icons)
    }


    /**
     * ResultSummaryStepObject
     */

    @Test
    fun testNodeIdentifierPath() {
        val root = IdentifierPath("TestTask/sectionB/roo")
        assertEquals("TestTask", root.identifier)
        val section = root.child
        assertNotNull(section)
        assertEquals("sectionB", section.identifier)
        val child = section.child
        assertNotNull(child)
        assertEquals("roo", child.identifier)
        assertNull(child.child)
    }

    @Test
    fun testResultSummaryStep_Serialization() {
        val inputString = """
           {
               "identifier": "foo",
               "type": "feedback",
               "scoringResultPath": "TestTask/sectionB/roo",
               "resultTitle": "Your score is",
               "title": "Hello World!",
               "detail": "Some text. This is a test.",
               "footnote": "This is a footnote.",
               "actions": { "goForward": { "type": "default", "buttonTitle" : "Go, Dogs! Go!" },
                            "cancel": { "type": "default", "iconName" : "closeX" }
                           },
               "image"  : {    "type" : "animated",
                               "imageNames" : ["foo1", "foo2", "foo3", "foo4"],
                               "animationDuration" : 2}
           }
           """
        val original = ResultSummaryStepObject(
            identifier = "foo",
            scoringResultPath = IdentifierPath("TestTask/sectionB/roo")
        )
        original.resultTitle = "Your score is"
        original.title = "Hello World!"
        original.detail = "Some text. This is a test."
        original.footnote = "This is a footnote."
        original.buttonMap = mapOf(
            ButtonAction.Navigation.GoForward to ButtonActionInfoObject(buttonTitle = "Go, Dogs! Go!"),
            ButtonAction.Navigation.Cancel to ButtonActionInfoObject(iconName ="closeX"))
        original.imageInfo = AnimatedImage(
            imageNames = listOf("foo1", "foo2", "foo3", "foo4"),
            animationDuration = 2.0)

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertTrue(decoded is ResultSummaryStepObject)
        assertEquals(original, decoded)
        assertEqualContentNodes(original, decoded)
        assertEqualStep(original, decoded)

        assertTrue(restored is ResultSummaryStepObject)
        assertEquals(original, restored)
        assertEqualContentNodes(original, restored)
        assertEqualStep(original, restored)

        val copy = original.copy()
        copy.copyFrom(original)
        assertEquals(original, copy)
        assertEqualContentNodes(original, copy)
        assertEqualStep(original, copy)
    }


    /**
     * SimpleQuestion
     */

    @Test
    fun testSimpleQuestion_Serialization() {
        val inputString = """
           {
               "identifier": "foo",
               "type": "simpleQuestion",
               "title": "Hello World!",
               "subtitle": "Question subtitle",
               "detail": "Some text. This is a test.",
               "footnote": "This is a footnote.",
               "image"  : {    "type" : "animated",
                               "imageNames" : ["foo1", "foo2", "foo3", "foo4"],
                               "animationDuration" : 2
                                  },
                "optional": false,
                "inputItem":{"type" : "year"},
                "surveyRules": [{ "matchingAnswer": 1900}]
           }
           """
        val original = SimpleQuestionObject(
                identifier = "foo",
                inputItem = YearTextInputItemObject())
        original.optional = false
        original.title = "Hello World!"
        original.subtitle = "Question subtitle"
        original.detail = "Some text. This is a test."
        original.footnote = "This is a footnote."
        original.imageInfo = AnimatedImage(
                imageNames = listOf("foo1", "foo2", "foo3", "foo4"),
                animationDuration = 2.0)
        original.surveyRules = listOf(ComparableSurveyRuleObject(JsonPrimitive(1900)))

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertTrue(decoded is SimpleQuestionObject)
        assertEqualStep(original, decoded)
        assertEqualContentNodes(original, decoded)
        assertEquals(original.optional, decoded.optional)
        assertEquals(original.surveyRules, decoded.surveyRules)

        assertTrue(restored is SimpleQuestionObject)
        assertEqualStep(original, restored)
        assertEqualContentNodes(original, restored)
        assertEquals(original.optional, restored.optional)
        assertEquals(original.surveyRules, restored.surveyRules)

        val copy = original.copy()
        copy.copyFrom(original)
        assertEqualStep(original, copy)
        assertEqualContentNodes(original, copy)
        assertEquals(original.optional, copy.optional)
        assertEquals(original.surveyRules, copy.surveyRules)
    }

    /**
     * SectionObject
     */

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
                "progressMarkers": ["step1","step2"],
                "asyncActions"      : [
                      {
                      "identifier"              : "shakeItUp",
                      "type"                    : "motion"
                      }
                      ],
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
                        buildInstructionStep("step2", "Step 2")),
                backgroundActions = listOf(MotionRecorderConfiguration("shakeItUp"))
        )
        original.title = "Hello World!"
        original.subtitle = "Subtitle"
        original.detail = "Some text. This is a test."
        original.footnote = "This is a footnote."
        original.hideButtons = listOf(ButtonAction.Navigation.GoBackward)
        original.buttonMap = mapOf(
                ButtonAction.Navigation.GoForward to ButtonActionInfoObject(buttonTitle = "Go, Dogs! Go!"),
                ButtonAction.Navigation.Cancel to ButtonActionInfoObject(iconName ="closeX"))
        original.imageInfo = FetchableImage("fooIcon")
        original.progressMarkers = listOf("step1", "step2")

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertTrue(decoded is SectionObject)
        assertContainerNode(original, decoded)
        assertEqualContentNodes(original, decoded)

        assertTrue(restored is SectionObject)
        assertContainerNode(original, restored)
        assertEqualContentNodes(original, restored)
    }

    @Test
    fun testSection_Result() {
        val original = SectionObject(
                identifier = "foo",
                children = listOf(
                        buildInstructionStep("step1", "Step 1"),
                        buildInstructionStep("step2", "Step 2")))
        val result = original.createResult()
        assertEquals("foo", result.identifier)
    }

    /**
     * TransformableNodeObject
     */

    class TestFileLoader(private val jsonMap: Map<String, String>) : FileLoader {
        override fun loadFile(assetInfo: AssetInfo, resourceInfo: ResourceInfo): String
                = jsonMap[assetInfo.resourceName] ?: error("JSON mapping not found for $assetInfo")
    }

    data class TestEmbeddedJsonModuleInfo(
        override var packageName: String? = null,
        override var decoderBundle: Any? = null,
        override val bundleIdentifier: String? = null,
        override val assessments: List<TransformableAssessment> = listOf(),
        override val jsonCoder: Json = Serialization.JsonCoder.default
    ) : ResourceInfo, EmbeddedJsonModuleInfo {
        override val resourceInfo: ResourceInfo
            get() = this
    }

    data class TestResourceBundle(val bundleIdentifier: String? = null)

    @Test
    fun testTransformableNodeObject_Serialization() {
        val inputString = """
            {
                "identifier": "foo",
                "resourceName": "foo_test",
                "type": "transform"
            }
            """

        val original = TransformableNodeObject(
                identifier = "foo",
                resourceName = "foo_test"
        )

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertTrue(decoded is TransformableNodeObject)
        assertEqualNodes(original, decoded)

        assertTrue(restored is TransformableNodeObject)
        assertEqualNodes(original, restored)
    }

    @Test
    fun testTransformInstruction() {
        val inputString = """
           {
               "identifier": "foo",
               "type": "instruction",
               "title": "Hello World!",
               "image"  : {    "type" : "animated",
                               "imageNames" : ["foo1", "foo2", "foo3", "foo4"],
                               "animationDuration" : 2
                                  }
           }
           """
        // syoung 01/28/2021 Currently, an InstructionStepObject does not inherit *any* properties
        // from the transform
        val original = InstructionStepObject(identifier = "foo")
        original.title = "Hello World!"
        val originalImageInfo = AnimatedImage(
                imageNames = listOf("foo1", "foo2", "foo3", "foo4"),
                animationDuration = 2.0)
        original.imageInfo = originalImageInfo

        val bundle = TestResourceBundle()
        val packageName = "org.foo.exampleToo"
        originalImageInfo.decoderBundle = bundle
        originalImageInfo.packageName = packageName

        val transform = TransformableNodeObject(identifier = "bar", resourceName = "foo_test")
        val fileLoader = TestFileLoader(mapOf("foo_test" to inputString))
        val moduleInfo = TestEmbeddedJsonModuleInfo(packageName = packageName, decoderBundle = bundle)
        val registryProvider = getRegistryProvider(fileLoader, listOf(moduleInfo))
        val decoded = transform.unpack(null, moduleInfo, registryProvider)

        assertTrue(decoded is InstructionStepObject)
        assertEqualStep(original, decoded)
        assertEqualContentNodes(original, decoded)

        val imageInfo = decoded.imageInfo
        assertNotNull(imageInfo)
        assertNull(imageInfo.bundleIdentifier)
        assertSame(bundle, imageInfo.decoderBundle)
        assertEquals(packageName, imageInfo.packageName)
    }

    @Test
    fun testTransformSection() {
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
                "progressMarkers": ["step1","step2"],
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

        val bundle = TestResourceBundle()
        val packageName = "org.foo.exampleToo"

        fun button(buttonTitle: String? = null, iconName: String? = null): ButtonActionInfoObject {
            val ret = ButtonActionInfoObject(buttonTitle, iconName, packageName)
            ret.decoderBundle = bundle
            return ret
        }

        val original = SectionObject(
                identifier = "foo",
                children = listOf(
                        buildInstructionStep("step1", "Step 1"),
                        buildInstructionStep("step2", "Step 2")))
        original.title = "Hello World!"
        original.subtitle = "Subtitle"
        original.detail = "Some text. This is a test."
        original.footnote = "This is a footnote."
        original.hideButtons = listOf(ButtonAction.Navigation.GoBackward)
        original.buttonMap = mapOf(
                ButtonAction.Navigation.GoForward to button(buttonTitle = "Go, Dogs! Go!"),
                ButtonAction.Navigation.Cancel to button(iconName = "closeX"))
        original.progressMarkers = listOf("step1", "step2")
        val originalImageInfo = FetchableImage("fooIcon")
        original.imageInfo = originalImageInfo

        originalImageInfo.decoderBundle = bundle
        originalImageInfo.packageName = packageName

        val transform = TransformableNodeObject(identifier = "foo", resourceName = "foo_test")
        val fileLoader = TestFileLoader(mapOf("foo_test" to inputString))
        val moduleInfo = TestEmbeddedJsonModuleInfo(packageName = packageName, decoderBundle = bundle)
        val registryProvider = getRegistryProvider(fileLoader, listOf(moduleInfo))
        val decoded = transform.unpack(null, moduleInfo, registryProvider)

        assertTrue(decoded is SectionObject)
        assertContainerNode(original, decoded)
        assertEqualContentNodes(original, decoded)

        val imageInfo = decoded.imageInfo
        assertNotNull(imageInfo)
        assertNull(imageInfo.bundleIdentifier)
        assertSame(bundle, imageInfo.decoderBundle)
        assertEquals(packageName, imageInfo.packageName)
    }

    @Test
    fun testTransformableAssessmentObject_Serialization_Node() {
        val inputString = """
            {
                "identifier": "foo",
                "resourceName": "foo_test",
                "type": "transformableAssessment",
                "title": "Hello World!",
                "subtitle": "Subtitle"
            }
            """

        val original = TransformableAssessmentObject(
                identifier = "foo",
                resourceName = "foo_test"
        )

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertTrue(decoded is TransformableAssessmentObject)
        assertEqualNodes(original, decoded)

        assertTrue(restored is TransformableAssessmentObject)
        assertEqualNodes(original, restored)
    }

    @Test
    fun testTransformableAssessmentObject_Serialization_Assessment() {
        val inputString = """
            {
                "identifier": "foo",
                "resourceName": "foo_test",
                "type": "transformableAssessment",
                "title": "Hello World!",
                "subtitle": "Subtitle"
            }
            """

        val original = TransformableAssessmentObject(
            identifier = "foo",
            resourceName = "foo_test",
            title = "Hello World!",
            subtitle = "Subtitle"
        )

        val serializer = PolymorphicSerializer(TransformableAssessment::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertTrue(decoded is TransformableAssessmentObject)
        assertEqualContentInfo(original, decoded)
        assertEqualNodes(original, decoded)

        assertTrue(restored is TransformableAssessmentObject)
        assertEqualContentInfo(original, restored)
        assertEqualNodes(original, restored)
    }

    @Test
    fun testTransformAssessment_Get() {
        val inputString = """
            {
                "type": "assessment",
                "identifier": "foobaroo",
                "versionString": "1.2.3",
                "title": "Hello World!",
                "subtitle": "Subtitle",
                "icon": "fooIcon",
                "steps": [
                    {
                        "identifier": "step1",
                        "type": "instruction",
                        "title": "Step 1",
                        "image": {"type":"fetchable","imageName":"step1Image"}
                    },
                    {
                        "identifier": "step2",
                        "type": "instruction",
                        "title": "Step 2"
                    }
                ]
            }
            """

        val bundle = TestResourceBundle()
        val packageName = "org.foo.exampleToo"

        val originalStep1 = buildInstructionStep("step1", "Step 1")
        val originalStep1Image = FetchableImage("step1Image")
        originalStep1.imageInfo = originalStep1Image
        originalStep1Image.decoderBundle = bundle
        originalStep1Image.packageName = packageName

        val original = AssessmentObject(
                identifier = "foo",
                versionString = "1.2.3",
                children = listOf(
                        originalStep1,
                        buildInstructionStep("step2", "Step 2")))
        original.title = "Hello World!"
        original.subtitle = "Subtitle"
        val originalImageInfo = FetchableImage("fooIcon")
        original.imageInfo = originalImageInfo
        originalImageInfo.decoderBundle = bundle
        originalImageInfo.packageName = packageName

        val transform = TransformableAssessmentObject(identifier = "foo", resourceName = "foo_test")
        val fileLoader = TestFileLoader(mapOf("foo_test" to inputString))
        val moduleInfo = TestEmbeddedJsonModuleInfo(packageName = packageName, decoderBundle = bundle)
        val registryProvider = getRegistryProvider(fileLoader, listOf(moduleInfo))
        val decoded = transform.unpack(null, moduleInfo, registryProvider)

        assertTrue(decoded is AssessmentObject)
        assertContainerNode(original, decoded)
        assertEqualContentNodes(original, decoded)

        val imageInfo = decoded.imageInfo
        assertNotNull(imageInfo)
        assertNull(imageInfo.bundleIdentifier)
        assertSame(bundle, imageInfo.decoderBundle)
        assertEquals(packageName, imageInfo.packageName)

        val step1 = decoded.children.first()
        assertTrue(step1 is InstructionStepObject)
        val step1Image = step1.imageInfo
        assertNotNull(step1Image)
        assertSame(bundle, step1Image.decoderBundle)
        assertEquals(packageName, step1Image.packageName)
    }

    @Test
    fun testAssessmentPlaceholderObject_Serialization() {
        val inputString = """
            {
                "identifier": "foo",
                "assessmentInfo": {
                    "identifier": "bar",
                    "versionString": "1.2.3"
                },
                "type": "assessmentPlaceholder",
                "title": "Hello World!",
                "subtitle": "Subtitle"
            }
            """

        val original = AssessmentPlaceholderObject(
            identifier = "foo",
            assessmentInfo = AssessmentInfoObject("bar", "1.2.3"),
            title = "Hello World!",
            subtitle = "Subtitle"
        )

        val serializer = PolymorphicSerializer(Node::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertTrue(decoded is AssessmentPlaceholderObject)
        assertEqualNodes(original, decoded)

        assertTrue(restored is AssessmentPlaceholderObject)
        assertEqualNodes(original, restored)
    }

    @Test
    fun testAssessmentPlaceholder_Get() {
        val inputString = """
            {
                "type": "assessment",
                "identifier": "foobaroo",
                "versionString": "1.2.3",
                "title": "Hello World!",
                "subtitle": "Subtitle",
                "icon": "fooIcon",
                "steps": [
                    {
                        "identifier": "step1",
                        "type": "instruction",
                        "title": "Step 1",
                        "image": {"type":"fetchable","imageName":"step1Image"}
                    },
                    {
                        "identifier": "step2",
                        "type": "instruction",
                        "title": "Step 2"
                    }
                ]
            }
            """

        val bundle = TestResourceBundle()
        val packageName = "org.foo.exampleToo"

        val originalStep1 = buildInstructionStep("step1", "Step 1")
        val originalStep1Image = FetchableImage("step1Image")
        originalStep1.imageInfo = originalStep1Image
        originalStep1Image.decoderBundle = bundle
        originalStep1Image.packageName = packageName

        val original = AssessmentObject(
            identifier = "foo",
            versionString = "1.2.3",
            children = listOf(
                originalStep1,
                buildInstructionStep("step2", "Step 2")))
        original.title = "Hello World!"
        original.subtitle = "Subtitle"
        val originalImageInfo = FetchableImage("fooIcon")
        original.imageInfo = originalImageInfo
        originalImageInfo.decoderBundle = bundle
        originalImageInfo.packageName = packageName

        val transform = TransformableAssessmentObject(identifier = "bar", resourceName = "foo_test")
        val placeholder = AssessmentPlaceholderObject(identifier = "foo", AssessmentInfoObject("bar"))
        val fileLoader = TestFileLoader(mapOf("foo_test" to inputString))
        val moduleInfo = TestEmbeddedJsonModuleInfo(
            packageName = packageName,
            decoderBundle = bundle,
            assessments = listOf(transform)
        )
        val registryProvider = getRegistryProvider(fileLoader, listOf(moduleInfo))
        val decoded = registryProvider.loadAssessment(placeholder)

        assertTrue(decoded is AssessmentObject)
        assertContainerNode(original, decoded)
        assertEqualContentNodes(original, decoded)

        val imageInfo = decoded.imageInfo
        assertNotNull(imageInfo)
        assertNull(imageInfo.bundleIdentifier)
        assertSame(bundle, imageInfo.decoderBundle)
        assertEquals(packageName, imageInfo.packageName)

        val step1 = decoded.children.first()
        assertTrue(step1 is InstructionStepObject)
        val step1Image = step1.imageInfo
        assertNotNull(step1Image)
        assertSame(bundle, step1Image.decoderBundle)
        assertEquals(packageName, step1Image.packageName)
    }
}

open class NodeSerializationTestHelper {
    /* MARK: Helper methods */

    fun assertEqualResultMapElement(expected: ResultMapElement, actual: ResultMapElement) {
        assertEquals(expected.identifier, actual.identifier)
        assertEquals(expected.comment, actual.comment)
    }

    fun assertEqualNodes(expected: Node, actual: Node) {
        assertEqualResultMapElement(expected, actual)
        assertEquals(expected.hideButtons, actual.hideButtons)
        assertEquals(expected.buttonMap, actual.buttonMap)
    }

    fun assertEqualContentInfo(expected: ContentInfo, actual: ContentInfo) {
        assertEquals(expected.title, actual.title)
        assertEquals(expected.subtitle, actual.subtitle)
        assertEquals(expected.detail, actual.detail)
    }

    fun assertEqualContentNodes(expected: ContentNode, actual: ContentNode) {
        assertEqualContentInfo(expected, actual)
        assertEquals(expected.imageInfo, actual.imageInfo)
        assertEquals(expected.footnote, actual.footnote)
        assertEquals(expected.hideButtons, actual.hideButtons)
        assertEquals(expected.buttonMap, actual.buttonMap)
    }

    fun assertContainerNode(expected: NodeContainer, actual: NodeContainer) {
        assertEqualNodes(expected, actual)
        assertEquals(expected.progressMarkers, actual.progressMarkers)
        assertEquals(expected.children.count(), actual.children.count())
        actual.children.forEachIndexed { index, node ->
            assertEqualNodes(expected.children[index], node)
        }
    }

    fun assertFormStepNode(expected: FormStep, actual: FormStep) {
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

    fun assertEqualActiveStep(expected: ActiveStep, actual: ActiveStep) {
        assertEqualStep(expected, actual)
        assertEquals(expected.duration, actual.duration)
        assertEquals(expected.commands, actual.commands)
        assertEquals(expected.shouldEndOnInterrupt, actual.shouldEndOnInterrupt)
        assertEquals(expected.requiresBackgroundAudio, actual.requiresBackgroundAudio)
    }

    fun buildInstructionStep(identifier: String, title: String): InstructionStepObject {
        val instruction = InstructionStepObject(identifier)
        instruction.title = title
        return instruction
    }

    fun getRegistryProvider(fileLoader: FileLoader, modules: List<ModuleInfo>) : AssessmentRegistryProvider {
        return object : AssessmentRegistryProvider {
            override val fileLoader = fileLoader
            override val modules = modules
        }
    }
}

@Serializable
@SerialName("motion")
data class MotionRecorderConfiguration(
    override val identifier: String,
    override val comment: String? = null,
    override val startStepIdentifier: String? = null
) : AsyncActionConfiguration
