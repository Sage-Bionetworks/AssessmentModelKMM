package org.sagebionetworks.assessmentmodel.navigation

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * The [IdentifierPath] is intended as a light-weight serializable linked list for describing a path to a node or
 * result.
 */
@Serializable(IdentifierPath.Companion::class)
data class IdentifierPath(val path: String) {
    @Transient val pathParts by lazy {
        path.split("/", limit = 2)
    }
    @Transient val identifier: String by lazy {
        pathParts.first()
    }
    @Transient val child: IdentifierPath? by lazy {
        if (pathParts.count() == 2) IdentifierPath(pathParts[1]) else null
    }

    // TODO: syoung 05/08/2020 Keep an eye out for inline class serialization to reduce this type of boilerplate.
    companion object : KSerializer<IdentifierPath> {
        override val descriptor: SerialDescriptor
                = PrimitiveSerialDescriptor("NodeIdentifierPath", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): IdentifierPath = IdentifierPath(decoder.decodeString())
        override fun serialize(encoder: Encoder, value: IdentifierPath) = encoder.encodeString(value.path)
    }
}