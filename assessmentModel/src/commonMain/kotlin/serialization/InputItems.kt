package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.modules.SerializersModule
import org.sagebionetworks.assessmentmodel.forms.*

val inputItemSerializersModule = SerializersModule {
    polymorphic(InputItem::class) {
        StringInputItemObject::class with StringInputItemObject.serializer()
    }
}

/**
 * A [InputItemObject] is intended to implement shared code for serialization of the simple data types. This will
 * work to deserialize some of the existing input items that use the [DataType] to define their type.
 */
@Serializable
abstract class InputItemObject : InputItem {
    @SerialName("prompt")
    override var fieldLabel: String? = null
    override var placeholder: String? = null
    override var optional: Boolean = true
}

@Serializable
@SerialName("string")
data class StringInputItemObject(
        @SerialName("identifier")
        override val resultIdentifier: String? = null) : InputItemObject(), TextKeyboardInputItem {
    override var uiHint: UIHint.TextField = UIHint.TextField.Default
    override var textFieldOptions: TextFieldOptionsObject = TextFieldOptionsObject()
    override var exclusive: Boolean = false
    override val answerKind: SerialKind
        get() = PrimitiveKind.STRING
}

@Serializable
data class TextFieldOptionsObject(override val isSecureTextEntry: Boolean = false,
                                  override val autocapitalizationType: AutoCapitalizationType = AutoCapitalizationType.None,
                                  override val autocorrectionType: AutoCorrectionType = AutoCorrectionType.Default,
                                  override val spellCheckingType: SpellCheckingType = SpellCheckingType.Default,
                                  override val keyboardType: KeyboardType = KeyboardType.Default) : TextFieldOptions