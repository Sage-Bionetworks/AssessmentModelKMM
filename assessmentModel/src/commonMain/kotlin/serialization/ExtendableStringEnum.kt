package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.internal.EnumDescriptor
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.internal.StringDescriptor
import kotlin.jvm.JvmOverloads

/**
 * A string enum is an enum that uses a string as its raw value.
 */
interface StringEnum {
    val name: String
}

/**
 * This framework considers string enums to be case insensitive. This is to allow for different languages to support
 * different conventions.
 */
fun <T> Array<T>.matching(name: String) where T : StringEnum =
        this.firstOrNull { it.name.toLowerCase() == name.toLowerCase() }

/**
 * An extendable string enum defines an interface for extending a string enum to include an array of [standardValues]
 * and also allow for [custom] string extensions.
 */
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
