package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.*

@Serializable
@SerialName("section")
data class SectionObject(override val identifier: String,
                         override val resultIdentifier: String? = null,
                         @Polymorphic
                         @SerialName("steps")
                         override val children: List<Node>,
                         override val comment: String? = null,
                         override val title: String? = null,
                         override val label: String? = null,
                         @SerialName("icon")
                         @Serializable(with=ImageNameSerializer::class)
                         override val imageInfo: FetchableImage? = null,
                         override val detail: String? = null,
                         @SerialName("shouldHideActions")
                         @Serializable(with=ButtonActionTypeSerializer::class)
                         override val hideButtons: List<ButtonActionType> = listOf(),
                         @Polymorphic
                         @SerialName("actions")
                         override val buttonMap: Map<String, Button> = mapOf(),
                         @Polymorphic
                         @SerialName("asyncActions")
                         override val backgroundActions: List<AsyncActionConfiguration> = listOf()) : Section, AsyncActionContainer {
    override fun createResult(): Result {
        return CollectionResultObject(identifier = identifier)
    }
}