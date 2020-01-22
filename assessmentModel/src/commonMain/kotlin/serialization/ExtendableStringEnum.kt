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

class CaseInsensitiveEnumDescriptor @JvmOverloads constructor(
        name: String,
        values: Array<String> = emptyArray()
) : SerialClassDescImpl(name) {
    override val kind: SerialKind = UnionKind.ENUM_KIND

    init {
        values.forEach { addElement(it) }
    }

    override fun getElementDescriptor(index: Int): SerialDescriptor {
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is SerialDescriptor) return false
        if (other.kind !== UnionKind.ENUM_KIND) return false
        if (name.toLowerCase() != other.name.toLowerCase()) return false
        if (elementNames() != other.elementNames()) return false
        return true
    }

    override fun toString(): String {
        return elementNames().joinToString(", ", "$name(", ")")
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + elementNames().hashCode()
        return result
    }
}

open class CaseInsensitiveEnumSerializer<T: StringEnum>(
        serialName: String,
        val values: Array<T>) : KSerializer<T> {
    override val descriptor: SerialDescriptor = CaseInsensitiveEnumDescriptor(serialName, values.map { it.name }.toTypedArray())

    final override fun serialize(encoder: Encoder, obj: T) {
        val index = values.indexOf(obj)
        check(index != -1) {
            "$obj is not a valid enum ${descriptor.name}, must be one of ${values.contentToString()}"
        }
        encoder.encodeEnum(descriptor, index)
    }

    final override fun deserialize(decoder: Decoder): T {
        val index = decoder.decodeEnum(descriptor)
        check(index in values.indices) {
            "$index is not among valid $${descriptor.name} enum values, values size is ${values.size}"
        }
        return values[index]
    }
}
