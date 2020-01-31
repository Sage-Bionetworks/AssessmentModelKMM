
package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.navigation.NodeNavigator
import org.sagebionetworks.assessmentmodel.navigation.Navigator

// TODO: syoung 01/28/2020 Deprecate the `text` property in the `RSDUIStep` protocol and decoding. Replace with "detail" or "subtitle" as appropriate.

val nodeSerializersModule = SerializersModule {
    polymorphic(Node::class) {
        InstructionStepObject::class with InstructionStepObject.serializer()
        SectionObject::class with SectionObject.serializer()
        AssessmentObject::class with AssessmentObject.serializer()
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
@SerialName("instruction")
data class InstructionStepObject(override val identifier: String,
                                 override val resultIdentifier: String? = null) : StepObject(), InstructionStep {
    @SerialName("image")
    override var imageInfo: ImageInfo? = null
    override var fullInstructionsOnly: Boolean = false
}

@Serializable
abstract class NodeContainerObject() : NodeObject(), NodeContainer {
    @SerialName("icon")
    @Serializable(ImageNameSerializer::class)
    override var imageInfo: FetchableImage? = null
    override var progressMarkers: List<String>? = null
}

@Serializable
@SerialName("section")
data class SectionObject(override val identifier: String,
                         @SerialName("steps")
                         override val children: List<Node>,
                         override val resultIdentifier: String? = null) : NodeContainerObject(), Section

@Serializable
@SerialName("assessment")
data class AssessmentObject(override val identifier: String,
                            @SerialName("steps")
                            override val children: List<Node>,
                            override val versionString: String? = null,
                            override val resultIdentifier: String? = null) : NodeContainerObject(), Assessment {

    override var estimatedMinutes: Int = 0

    @Transient
    private var _navigator: NodeNavigator? = null
    override val navigator: Navigator?
        get() {
            if (_navigator == null) {
                _navigator = NodeNavigator(this)
            }
            return _navigator
        }
}
