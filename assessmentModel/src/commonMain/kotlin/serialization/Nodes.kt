
package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.forms.InputItem
import org.sagebionetworks.assessmentmodel.navigation.*

// TODO: syoung 01/28/2020 Deprecate the `text` property in the `RSDUIStep` protocol and decoding. Replace with "detail" or "subtitle" as appropriate.

val nodeSerializersModule = SerializersModule {
    polymorphic(Node::class) {
        InstructionStepObject::class with InstructionStepObject.serializer()
        SectionObject::class with SectionObject.serializer()
        AssessmentObject::class with AssessmentObject.serializer()
        QuestionObject::class with QuestionObject.serializer()
    }
    polymorphic(Assessment::class) {
        AssessmentObject::class with AssessmentObject.serializer()
    }
}

@Serializable
abstract class NodeObject() : ContentNode {

    override var comment: String? = null
    override var title: String? = null
    override var subtitle: String? = null
    override var detail: String? = null
    override var footnote: String? = null
    @SerialName("shouldHideActions")
    override var hideButtons: List<ButtonAction> = listOf()
    @SerialName("actions")
    override var buttonMap: Map<ButtonAction, Button> = mapOf()
}

@Serializable
abstract class StepObject() : NodeObject(), Step {
    override var spokenInstructions: Map<String, String>? = null
}

@Serializable
abstract class IconNodeObject() : NodeObject() {
    @SerialName("icon")
    @Serializable(ImageNameSerializer::class)
    override var imageInfo: FetchableImage? = null
}

@Serializable
abstract class NodeContainerObject() : IconNodeObject(), NodeContainer {
    override var progressMarkers: List<String>? = null
}

@Serializable
@SerialName("assessment")
data class AssessmentObject(override val identifier: String,
                            @SerialName("steps")
                            override val children: List<Node>,
                            override val versionString: String? = null,
                            override val resultIdentifier: String? = null,
                            override var estimatedMinutes: Int = 0) : NodeContainerObject(), Assessment {
    override fun createResult(): AssessmentResult = super<Assessment>.createResult()
}

@Serializable
@SerialName("instruction")
data class InstructionStepObject(override val identifier: String,
                                 override val resultIdentifier: String? = null,
                                 @SerialName("image")
                                 override var imageInfo: ImageInfo? = null,
                                 override var fullInstructionsOnly: Boolean = false) : StepObject(), InstructionStep

// TODO: syoung 02/18/2020 This is a name change of the serialization type from "form" -> "question" and "inputFields" -> "inputItems".
@Serializable
@SerialName("question")
data class QuestionObject(override val identifier: String,
                          val inputItems: List<InputItem>,
                          override val resultIdentifier: String? = null,
                          @SerialName("image")
                          override var imageInfo: ImageInfo? = null,
                          override var optional: Boolean = true) : StepObject(), Question {
    override fun getInputItems(): List<InputItem> = inputItems
}

@Serializable
@SerialName("section")
data class SectionObject(override val identifier: String,
                         @SerialName("steps")
                         override val children: List<Node>,
                         override val resultIdentifier: String? = null) : NodeContainerObject(), Section
