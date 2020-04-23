
package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.resourcemanagement.FileLoader
import org.sagebionetworks.assessmentmodel.resourcemanagement.ResourceInfo
import org.sagebionetworks.assessmentmodel.resourcemanagement.copyResourceInfo
import org.sagebionetworks.assessmentmodel.survey.*
import org.sagebionetworks.assessmentmodel.survey.BaseType

val nodeSerializersModule = SerializersModule {
    polymorphic(Node::class) {
        AssessmentObject::class with AssessmentObject.serializer()
        ChoiceQuestionObject::class with ChoiceQuestionObject.serializer()
        ComboBoxQuestionObject::class with ComboBoxQuestionObject.serializer()
        FormStepObject::class with FormStepObject.serializer()
        InstructionStepObject::class with InstructionStepObject.serializer()
        MultipleInputQuestionObject::class with MultipleInputQuestionObject.serializer()
        SimpleQuestionObject::class with SimpleQuestionObject.serializer()
        SectionObject::class with SectionObject.serializer()
        StringChoiceQuestionObject::class with StringChoiceQuestionObject.serializer()
        TransformableAssessmentObject::class with TransformableAssessmentObject.serializer()
        TransformableNodeObject::class with TransformableNodeObject.serializer()
    }
    polymorphic(Assessment::class) {
        AssessmentObject::class with AssessmentObject.serializer()
        TransformableAssessmentObject::class with TransformableAssessmentObject.serializer()
    }
    polymorphic(Question::class) {
        ChoiceQuestionObject::class with ChoiceQuestionObject.serializer()
        ComboBoxQuestionObject::class with ComboBoxQuestionObject.serializer()
        MultipleInputQuestionObject::class with MultipleInputQuestionObject.serializer()
        SimpleQuestionObject::class with SimpleQuestionObject.serializer()
        StringChoiceQuestionObject::class with StringChoiceQuestionObject.serializer()
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

    open fun copyFrom(original: ContentNode) {
        this.comment = original.comment
        this.title = original.title
        this.subtitle = original.subtitle
        this.detail = original.detail
        this.footnote = original.footnote
        this.hideButtons = original.hideButtons
        this.buttonMap = original.buttonMap
    }
}

@Serializable
abstract class StepObject : NodeObject(), Step {
    override var spokenInstructions: Map<String, String>? = null

    override fun copyFrom(original: ContentNode) {
        super.copyFrom(original)
        if (original is Step) {
            this.spokenInstructions = original.spokenInstructions
        }
    }
}

@Serializable
abstract class IconNodeObject : NodeObject() {
    @SerialName("icon")
    @Serializable(ImageNameSerializer::class)
    override var imageInfo: FetchableImage? = null

    override fun copyFrom(original: ContentNode) {
        super.copyFrom(original)
        if (original is IconNodeObject) {
            this.imageInfo = original.imageInfo
        }
    }
}

/**
 * TransformableNode
 */

@Serializable
@SerialName("transform")
data class TransformableNodeObject(override val identifier: String,
                                   override val resourceName: String,
                                   override val versionString: String? = null,
                                   override val resultIdentifier: String? = null) : IconNodeObject(), TransformableNode

@Serializable
@SerialName("transformableAssessment")
data class TransformableAssessmentObject(override val identifier: String,
                                         override val resourceName: String,
                                         override val versionString: String? = null,
                                         override val estimatedMinutes: Int = 0,
                                         override val resultIdentifier: String? = null) : IconNodeObject(), TransformableAssessment

/**
 * NodeContainer
 */

@Serializable
abstract class NodeContainerObject : IconNodeObject(), NodeContainer {
    override var progressMarkers: List<String>? = null

    override fun copyFrom(original: ContentNode) {
        super.copyFrom(original)
        if (original is NodeContainer) {
            this.progressMarkers = original.progressMarkers
        }
    }
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
        val copy = copy(children = copyChildren)
        copy.copyFrom(this)
        return copy
    }
}

@Serializable
@SerialName("section")
data class SectionObject(override val identifier: String,
                         @SerialName("steps")
                         override val children: List<Node>,
                         override val resultIdentifier: String? = null) : NodeContainerObject(), Section {
    override fun unpack(fileLoader: FileLoader, resourceInfo: ResourceInfo, jsonCoder: Json): SectionObject {
        println("In unpack. $this")
        imageInfo?.copyResourceInfo(resourceInfo)
        val copyChildren = children.map { it.unpack(fileLoader, resourceInfo, jsonCoder) }
        println("In unpack. copyChildren=$copyChildren")
        val copy = copy(children = copyChildren)
        copy.copyFrom(this)
        println("In unpack. copy=$copy")
        return copy
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

@Serializable
@SerialName("form")
data class FormStepObject(
    override val identifier: String,
    override val resultIdentifier: String? = null,
    @SerialName("image")
    override val imageInfo: ImageInfo? = null,
    // TODO: syoung 04/22/2020 iOS defines the child nodes as "inputFields" but may want to change that?
    //  Basically, what is the keyword that makes sense for the nodes in a collection where the "step" shows multiple
    //  questions as a part of a single step?
    @SerialName("inputFields")
    override val children: List<Node>
) : StepObject(), FormStep

/**
 * Question
 */

@Serializable
abstract class QuestionObject : StepObject(), Question {
    @SerialName("image")
    override var imageInfo: ImageInfo? = null
    override var optional: Boolean = true

    override fun copyFrom(original: ContentNode) {
        super.copyFrom(original)
        if (original is Question) {
            this.imageInfo = original.imageInfo
            this.optional = original.optional
        }
    }
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
@SerialName("stringChoiceQuestion")
data class StringChoiceQuestionObject(override val identifier: String,
                                      @SerialName("choices")
                                      val items: List<String>,
                                      override val resultIdentifier: String? = null,
                                      @SerialName("singleChoice")
                                      override var singleAnswer: Boolean = true,
                                      override var uiHint: UIHint = UIHint.Choice.ListItem) : QuestionObject(), ChoiceQuestion {
    override val choices: List<ChoiceOptionObject>
        get() = items.map { ChoiceOptionObject(fieldLabel = it, value = JsonPrimitive(it)) }
    override val baseType: BaseType
        get() = BaseType.STRING
}

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