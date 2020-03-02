package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.JsonElement
import org.sagebionetworks.assessmentmodel.serialization.Localization

/**
 * A [TextValidator] can be used to both provide the localized string shown to the participant and convert the
 * participant's text answer into the value saved as the answer result. This can be used to perform regex validation,
 * number range validation, date range validation, etc.
 */
interface TextValidator<T> {
    fun valueFor(text: String): FormattedValue<T>?
    fun localizedStringFor(value: T?): FormattedValue<String>
    fun jsonValueFor(value: T?): JsonElement?
    fun valueFor(jsonValue: JsonElement?): T?
}

/**
 * An [InvalidMessage] is used to describe a user-facing message about why the input is not valid. Currently, this
 * implements the [toString] method to surface a localized message.
 *
 * TODO: syoung 02/12/2020 Figure out interface for Android which requires the context to access resources and locale.
 */
interface InvalidMessage

/**
 * This is a tuple that returns the result of a conversion *or* an [InvalidMessage] that can be displayed to the
 * participant. If both the [invalidMessage] and the [result] are null, then it should be assumed that the value is
 * valid and that the value to store is null.
 */
data class FormattedValue<T>(val result: T? = null, val invalidMessage: InvalidMessage? = null)

/**
 * TODO: syoung 02/12/2020 Figure out interface for Android which requires the context to access resources and locale.
 * Not certain what is the best way to describe and implement the serialization for this.
 */
@Serializable
data class InvalidMessageObject(val string: String) : InvalidMessage {
    override fun toString(): String = Localization.localizeString(string)

    @Serializer(forClass = InvalidMessageObject::class)
    companion object : KSerializer<InvalidMessageObject> {
        override val descriptor: SerialDescriptor = StringDescriptor.withName("InvalidMessageObject")
        override fun deserialize(decoder: Decoder): InvalidMessageObject {
            return InvalidMessageObject(decoder.decodeString())
        }
        override fun serialize(encoder: Encoder, obj: InvalidMessageObject) {
            encoder.encodeString(obj.string)
        }
    }
}
