
package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.survey.*
import org.sagebionetworks.assessmentmodel.survey.AnswerType
import org.sagebionetworks.assessmentmodel.survey.BaseType

// TODO: syoung 01/28/2020 Deprecate the `text` property in the `RSDUIStep` protocol and decoding. Replace with "detail" or "subtitle" as appropriate.

val nodeSerializersModule = SerializersModule {
    polymorphic(Node::class) {
        InstructionStepObject::class with InstructionStepObject.serializer()
        SectionObject::class with SectionObject.serializer()
        AssessmentObject::class with AssessmentObject.serializer()
        QuestionObject::class with QuestionObject.serializer()
        ChoiceQuestionObject::class with ChoiceQuestionObject.serializer()
        ComboBoxQuestionObject::class with ComboBoxQuestionObject.serializer()
    }
    polymorphic(Assessment::class) {
        AssessmentObject::class with AssessmentObject.serializer()
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
}

@Serializable
@SerialName("section")
data class SectionObject(override val identifier: String,
                         @SerialName("steps")
                         override val children: List<Node>,
                         override val resultIdentifier: String? = null) : NodeContainerObject(), Section

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
@SerialName("question")
data class QuestionObject(override val identifier: String,
                          val inputItems: List<InputItem>,
                          override val resultIdentifier: String? = null,
                          @SerialName("image")
                          override var imageInfo: ImageInfo? = null,
                          override var optional: Boolean = true) : StepObject(), Question {

    override val singleAnswer: Boolean
        get() = (inputItems.count() <= 1) || inputItems.fold(true) { ret, it -> ret && it.exclusive }

    override val answerType: AnswerType? = when {
        singleAnswer -> inputItems.firstOrNull()?.answerType
        inputItems.fold(true) { sum, it -> sum && (it.resultIdentifier != null) } -> AnswerType.MAP
        else -> null
    }

    override fun buildInputItems(): List<InputItem> = inputItems
}

@Serializable
abstract class BaseChoiceQuestionObject : StepObject(), Question {
    abstract val baseType: BaseType
    abstract val choices: List<ChoiceOption>

    @SerialName("singleChoice")
    override var singleAnswer: Boolean = true
    @SerialName("image")
    override var imageInfo: ImageInfo? = null
    var uiHint: UIHint.Choice = UIHint.Choice.ListItem
    override var optional: Boolean = true

    override val answerType
        get() = if (singleAnswer) AnswerType.valueFor(baseType) else AnswerType.List(baseType)

    override fun buildInputItems(): List<InputItem> = choices.map {
        ChoiceItemWrapper(it, singleAnswer, AnswerType.valueFor(baseType), uiHint)
    }
}

@Serializable
@SerialName("choiceQuestion")
data class ChoiceQuestionObject(override val identifier: String,
                                override val choices: List<ChoiceOptionObject>,
                                override val baseType: BaseType = BaseType.STRING,
                                override val resultIdentifier: String? = null) : BaseChoiceQuestionObject()

@Serializable
@SerialName("comboBoxQuestion")
data class ComboBoxQuestionObject(override val identifier: String,
                                  override val choices: List<ChoiceOptionObject>,
                                  val otherInputItem: InputItem = StringTextInputItemObject(
                                          fieldLabel = Localization.localizeString("Other")),
                                  override val resultIdentifier: String? = null) : BaseChoiceQuestionObject() {
    override val baseType: BaseType
        get() = BaseType.STRING

    override fun buildInputItems(): List<InputItem> {
        val items = super.buildInputItems()
        val otherField = OtherChoiceItemWrapper(otherInputItem, singleAnswer)
        return items.plus(otherField)
    }
}