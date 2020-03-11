
package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.copyResourceInfo
import org.sagebionetworks.assessmentmodel.survey.*
import org.sagebionetworks.assessmentmodel.survey.BaseType

// TODO: syoung 01/28/2020 Deprecate the `text` property in the `RSDUIStep` protocol and decoding. Replace with "detail" or "subtitle" as appropriate.

val nodeSerializersModule = SerializersModule {
    polymorphic(Node::class) {
        AssessmentObject::class with AssessmentObject.serializer()
        ChoiceQuestionObject::class with ChoiceQuestionObject.serializer()
        ComboBoxQuestionObject::class with ComboBoxQuestionObject.serializer()
        InstructionStepObject::class with InstructionStepObject.serializer()
        MultipleInputQuestionObject::class with MultipleInputQuestionObject.serializer()
        SimpleQuestionObject::class with SimpleQuestionObject.serializer()
        SectionObject::class with SectionObject.serializer()
        TransformableNodeObject::class with TransformableNodeObject.serializer()
    }
    polymorphic(Assessment::class) {
        AssessmentObject::class with AssessmentObject.serializer()
    }
    polymorphic(TransformableNode::class) {
        TransformableNodeObject::class with TransformableNodeObject.serializer()
    }
    polymorphic(Question::class) {
        ChoiceQuestionObject::class with ChoiceQuestionObject.serializer()
        ComboBoxQuestionObject::class with ComboBoxQuestionObject.serializer()
        MultipleInputQuestionObject::class with MultipleInputQuestionObject.serializer()
        SimpleQuestionObject::class with SimpleQuestionObject.serializer()
    }
}

@Serializable
abstract class NodeObject : ContentNode {

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
abstract class StepObject : NodeObject(), Step {
    override var spokenInstructions: Map<String, String>? = null
}

@Serializable
abstract class IconNodeObject : NodeObject() {
    @SerialName("icon")
    @Serializable(ImageNameSerializer::class)
    override var imageInfo: FetchableImage? = null
}

/**
 * TransformableNode
 */

@Serializable
@SerialName("transform")
data class TransformableNodeObject(override val identifier: String,
                                   override val resourceName: String,
                                   override val resultIdentifier: String? = null) : IconNodeObject(), TransformableNode

/**
 * NodeContainer
 */

@Serializable
abstract class NodeContainerObject : IconNodeObject(), NodeContainer {
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
    override fun unpack(fileLoader: FileLoader, resourceInfo: ResourceInfo, jsonCoder: Json): AssessmentObject {
        imageInfo?.copyResourceInfo(resourceInfo)
        val copyChildren = children.map { it.unpack(fileLoader, resourceInfo, jsonCoder) }
        return copy(children = copyChildren)
    }
}

@Serializable
@SerialName("section")
data class SectionObject(override val identifier: String,
                         @SerialName("steps")
                         override val children: List<Node>,
                         override val resultIdentifier: String? = null) : NodeContainerObject(), Section {
    override fun unpack(fileLoader: FileLoader, resourceInfo: ResourceInfo, jsonCoder: Json): Node {
        imageInfo?.copyResourceInfo(resourceInfo)
        val copyChildren = children.map { it.unpack(fileLoader, resourceInfo, jsonCoder) }
        return copy(children = copyChildren)
    }
}

/**
 * ContentNode
 */

@Serializable
@SerialName("instruction")
data class InstructionStepObject(override val identifier: String,
                                 override val resultIdentifier: String? = null,
                                 @SerialName("image")
                                 override var imageInfo: ImageInfo? = null,
                                 override var fullInstructionsOnly: Boolean = false) : StepObject(), InstructionStep

/**
 * Question
 */

/**
 * TODO: syoung 02/18/2020 This is a name change of the serialization type from "form" -> "question" and
 * "inputFields" -> "inputItems"... sort of. The deserialization defined in SageResearch uses `RSDDataType` to
 * determine the type of question decoded. In a sense, the Question interface is more closely mapped to the
 * `RSDInputField` interface. However, there's a lot of customization that would need to happen to support that
 * architecture here. Since that architecture was designed to be reverse-compatible to AppCore and Bridge Surveys,
 * the deserialization is muddled. We elected to take an approach here that works better with the kotlinx.serialization
 * patterns. Subsequently, the serialization strategy in SageResearch will need to be refactored and deprecated.
 */

@Serializable
abstract class QuestionObject : StepObject(), Question {
    @SerialName("image")
    override var imageInfo: ImageInfo? = null
    override var optional: Boolean = true
}

@Serializable
@SerialName("simpleQuestion")
data class SimpleQuestionObject(override val identifier: String,
                                override val inputItem: InputItem,
                                override val resultIdentifier: String? = null,
                                override var skipCheckbox: SkipCheckboxInputItem? = null)
    : QuestionObject(), SimpleQuestion

@Serializable
@SerialName("multipleInputQuestion")
data class MultipleInputQuestionObject(override val identifier: String,
                                       override val inputItems: List<InputItem>,
                                       override val resultIdentifier: String? = null,
                                       override var sequenceSeparator: String? = null,
                                       override var skipCheckbox: SkipCheckboxInputItem? = null)
    : QuestionObject(), MultipleInputQuestion

@Serializable
@SerialName("choiceQuestion")
data class ChoiceQuestionObject(override val identifier: String,
                                override val choices: List<ChoiceOptionObject>,
                                override val baseType: BaseType = BaseType.STRING,
                                override val resultIdentifier: String? = null,
                                @SerialName("singleChoice")
                                override var singleAnswer: Boolean = true,
                                override var uiHint: UIHint = UIHint.Choice.ListItem) : QuestionObject(), ChoiceQuestion

@Serializable
@SerialName("comboBoxQuestion")
data class ComboBoxQuestionObject(override val identifier: String,
                                  override val choices: List<ChoiceOptionObject>,
                                  override val otherInputItem: InputItem = defaultOtherInputItem,
                                  override val resultIdentifier: String? = null,
                                  @SerialName("singleChoice")
                                  override var singleAnswer: Boolean = false,
                                  override var uiHint: UIHint = UIHint.Choice.Checkbox) : QuestionObject(), ComboBoxQuestion {
    companion object {
        val defaultOtherInputItem: StringTextInputItemObject
            get() {
                val otherInputItem = StringTextInputItemObject()
                otherInputItem.fieldLabel = Localization.localizeString("Other")
                return otherInputItem
            }
    }
}