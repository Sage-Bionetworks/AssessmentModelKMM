package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
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
        AnswerType.List::class with AnswerType.List.serializer()
        AnswerType.Measurement::class with AnswerType.Measurement.serializer()
        AnswerType.MAP::class with AnswerType.MAP.serializer()
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

    @Serializable
    @SerialName("measurement")
    data class Measurement(val unit: String? = null) : AnswerType() {
        override val baseType: BaseType
            get() = BaseType.DECIMAL
    }

    @Serializable
    @SerialName("dateTime")
    data class DateTime(val codingFormat: String = ISO8601Format.DateOnly.formatString) : AnswerType() {
        override val baseType: BaseType
            get() = BaseType.STRING
    }

    @Serializable
    @SerialName("list")
    data class List(override val baseType: BaseType = BaseType.STRING,
                    val sequenceSeparator: String? = null) : AnswerType() {
        override val serialKind: SerialKind
            get() = StructureKind.LIST
    }

    @Serializable
    @SerialName("map")
    object MAP : AnswerType() {
        override val baseType: BaseType
            get() = BaseType.MAP
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
    @SerialName("decimal")
    object DECIMAL : AnswerType() {
        override val baseType: BaseType
            get() = BaseType.DECIMAL
    }

    @Serializable
    @SerialName("boolean")
    object BOOLEAN : AnswerType() {
        override val baseType: BaseType
            get() = BaseType.BOOLEAN
    }

    companion object {
        fun valueFor(baseType: BaseType) : AnswerType = when (baseType) {
            BaseType.BOOLEAN -> BOOLEAN
            BaseType.MAP -> MAP
            BaseType.DECIMAL -> DECIMAL
            BaseType.INTEGER -> INTEGER
            BaseType.STRING -> STRING
        }
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
    DECIMAL,

    /**
     * The integer question type asks the participant to enter an integer number.
     */
    INTEGER,

    /**
     * In a string question, the participant can enter text.
     */
    STRING,

    /**
     * A Serializable object. This is an object that can be represented using a JSON or XML dictionary.
     */
    MAP,
    ;

    val serialKind: SerialKind
        get() = when (this) {
            BOOLEAN -> PrimitiveKind.BOOLEAN
            DECIMAL -> PrimitiveKind.DOUBLE
            INTEGER -> PrimitiveKind.INT
            STRING -> PrimitiveKind.STRING
            MAP -> StructureKind.MAP
        }

    @Serializer(forClass = BaseType::class)
    companion object : KSerializer<BaseType> {
        override val descriptor: SerialDescriptor = StringDescriptor.withName("SerialType")
        override fun deserialize(decoder: Decoder): BaseType {
            val name = decoder.decodeString()
            return values().matching(name) ?: throw SerializationException("Unknown $name for ${descriptor.name}. Needs to be one of ${values()}")
        }
        override fun serialize(encoder: Encoder, obj: BaseType) {
            encoder.encodeString(obj.name)
        }
    }
}