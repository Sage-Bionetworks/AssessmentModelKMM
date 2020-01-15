package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor

interface StringEnum {
    val name: String
}

fun <T> Array<T>.matching(name: String) where T : StringEnum =
        this.firstOrNull { it.name.toLowerCase() == name.toLowerCase() }

interface ExtendableStringEnum<T : StringEnum> {
    fun standardValues(): Array<T>
    fun custom(name: String): T

    fun valueOf(name: String): T =
            standardValues().matching(name) ?: custom(name)
}

open class ExtendableStringEnumSerializer<T : StringEnum>(
        serialName: String,
        val stringEnum: ExtendableStringEnum<T>)
    : KSerializer<T> {

    override val descriptor: SerialDescriptor
            = StringDescriptor.withName(serialName)

    override fun serialize(encoder: Encoder, obj: T) {
        encoder.encodeString(obj.name)
    }

    override fun deserialize(decoder: Decoder): T {
        return stringEnum.valueOf(decoder.decodeString())
    }
}
