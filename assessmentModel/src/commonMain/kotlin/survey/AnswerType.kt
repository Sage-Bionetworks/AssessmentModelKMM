package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.AnswerResult
import org.sagebionetworks.assessmentmodel.StringEnum
import org.sagebionetworks.assessmentmodel.matching

/**
 * TODO: syoung 02/18/2020 Deprecate SageResearch `RSDFormDataType` and `RSDResultAnswerType`.
 */

val answerTypeSerializersModule = SerializersModule {
    polymorphic(AnswerType::class) {
        AnswerType.DateTime::class with AnswerType.DateTime.serializer()
        AnswerType.Array::class with AnswerType.Array.serializer()
        AnswerType.Measurement::class with AnswerType.Measurement.serializer()
        AnswerType.OBJECT::class with AnswerType.OBJECT.serializer()
        AnswerType.STRING::class with AnswerType.STRING.serializer()
        AnswerType.BOOLEAN::class with AnswerType.BOOLEAN.serializer()
        AnswerType.INTEGER::class with AnswerType.INTEGER.serializer()
        AnswerType.DECIMAL::class with AnswerType.DECIMAL.serializer()
    }
}

/**
 * The [AnswerType] is used to allow carrying additional information about the properties of a JSON-encoded
 * [AnswerResult].
 *
 * Note: This class replaces the information stored in `RSDFormDataType` and `RSDResultAnswerType` using the kotlin
 * [PolymorphicSerializer] strategy for defining the answer type information. It is an optional property of the
 * [AnswerResult] interface. syoung 02/25/2020
 */
@Serializable
abstract class AnswerType {
    abstract val baseType: BaseType
    open val serialKind: SerialKind
        get() = baseType.serialKind

    open fun jsonElementFor(value: Any): JsonElement = baseType.jsonElementFor(value)

    @Serializable
    @SerialName("measurement")
    data class Measurement(val unit: String? = null) : AnswerType() {
        override val baseType: BaseType
            get() = BaseType.NUMBER
    }

    @Serializable
    @SerialName("dateTime")
    data class DateTime(val codingFormat: String = ISO8601Format.DateOnly.formatString) : AnswerType() {
        override val baseType: BaseType
            get() = BaseType.STRING
    }

    @Serializable
    @SerialName("array")
    data class Array(override val baseType: BaseType = BaseType.STRING,
                    val sequenceSeparator: String? = null) : AnswerType() {
        override val serialKind: SerialKind
            get() = StructureKind.LIST
        override fun jsonElementFor(value: Any): JsonElement {
            val elements: List<JsonElement> = if (value is Collection<*>) {
                value.mapNotNull { it?.let { baseType.jsonElementFor(it) } }.toList()
            } else {
                listOf(super.jsonElementFor(value))
            }
            return if (sequenceSeparator == null) {
                JsonArray(elements)
            } else {
                JsonPrimitive(elements.joinToString(sequenceSeparator) { it.toString() })
            }
        }
    }

    @Serializable
    @SerialName("object")
    object OBJECT : AnswerType() {
        override val baseType: BaseType
            get() = BaseType.OBJECT
    }

    @Serializable
    @SerialName("string")
    object STRING : AnswerType() {
        override val baseType: BaseType
            get() = BaseType.STRING
    }

    @Serializable
    @SerialName("integer")
    object INTEGER : AnswerType() {
        override val baseType: BaseType
            get() = BaseType.INTEGER
    }

    @Serializable
    @SerialName("number")
    object DECIMAL : AnswerType() {
        override val baseType: BaseType
            get() = BaseType.NUMBER
    }

    @Serializable
    @SerialName("boolean")
    object BOOLEAN : AnswerType() {
        override val baseType: BaseType
            get() = BaseType.BOOLEAN
    }

    /**
     * The [NULL] answer type is used as a placeholder for an answer which, when selected, should return [JsonNull]
     * as its value.
     */
    @Serializable
    @SerialName("null")
    object NULL : AnswerType() {
        override val baseType: BaseType
            get() = BaseType.STRING
        override fun jsonElementFor(value: Any): JsonElement = JsonNull
    }

    companion object {
        fun valueFor(baseType: BaseType) : AnswerType = when (baseType) {
            BaseType.BOOLEAN -> BOOLEAN
            BaseType.ARRAY -> Array()
            BaseType.OBJECT -> OBJECT
            BaseType.NUMBER -> DECIMAL
            BaseType.INTEGER -> INTEGER
            BaseType.STRING -> STRING
        }
        fun nullJsonElement() = JsonNull
    }
}



/**
 * The base type of the form input field. This is used to indicate what the type is of the value being prompted
 * and will affect the choice of allowed formats.
 */
@Serializable
enum class BaseType : StringEnum {

    /**
     * The Boolean question type asks the participant to enter Yes or No (or the appropriate equivalents).
     */
    BOOLEAN,

    /**
     * The decimal question type asks the participant to enter a decimal number.
     */
    NUMBER,

    /**
     * The integer question type asks the participant to enter an integer number.
     */
    INTEGER,

    /**
     * In a string question, the participant can enter text.
     */
    STRING,

    /**
     * A serializable array of objects or primitives.
     */
    ARRAY,

    /**
     * A Serializable object. This is an object that can be represented using a JSON or XML dictionary.
     */
    OBJECT,
    ;

    fun jsonElementFor(value: Any): JsonElement = when (this) {
        BOOLEAN -> JsonPrimitive((value as? Boolean) ?: value.toString().toBoolean())
        NUMBER -> JsonPrimitive( (value as? Number)?.toDouble() ?: value.toString().toDoubleOrNull())
        INTEGER -> JsonPrimitive( (value as? Number)?.toInt() ?: value.toString().toIntOrNull())
        STRING -> JsonPrimitive(value.toString())
        ARRAY -> TODO("Not implemented. syoung 03/23/2020")
        OBJECT -> TODO("Not implemented. syoung 03/23/2020")
    }

    val serialKind: SerialKind
        get() = when (this) {
            BOOLEAN -> PrimitiveKind.BOOLEAN
            NUMBER -> PrimitiveKind.DOUBLE
            INTEGER -> PrimitiveKind.INT
            STRING -> PrimitiveKind.STRING
            ARRAY -> StructureKind.LIST
            OBJECT -> StructureKind.MAP
        }

    @Serializer(forClass = BaseType::class)
    companion object : KSerializer<BaseType> {
        override val descriptor: SerialDescriptor = PrimitiveDescriptor("SerialType", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): BaseType {
            val name = decoder.decodeString()
            return values().matching(name) ?: throw SerializationException("Unknown $name for ${descriptor.serialName}. Needs to be one of ${values()}")
        }
        override fun serialize(encoder: Encoder, value: BaseType) {
            encoder.encodeString(value.name)
        }
    }
}
