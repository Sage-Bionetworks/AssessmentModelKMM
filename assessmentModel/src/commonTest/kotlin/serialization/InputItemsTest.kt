package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.Json
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.forms.*
import kotlin.test.*
import kotlin.reflect.KClass

open class InputItemsTest {

    val jsonCoder = Serialization.JsonCoder.default

    @Serializable
    data class TestUIHintWrapper(val hints: List<UIHint>)

    /**
     * [UIHint] Tests
     */

    @Test
    fun testUIHint_Serializer() {
        // TODO: syoung 04/11/2020 Figure out if there is a way to test the descriptor. This is a lot of copy/paste.
//        assertEquals(UIHint::class.klassName(), UIHint.descriptor.name)
//        assertEquals(UIHint.TextField::class.klassName(), UIHint.TextField.descriptor.name)
//        assertEquals(UIHint.Choice::class.klassName(), UIHint.Choice.descriptor.name)
//        assertEquals(UIHint.Detail::class.klassName(), UIHint.Detail.descriptor.name)
    }

    @Test
    fun testUIHint_Choice_Serialization() {
        val hints = UIHint.Choice.values().toList()
        val original = TestUIHintWrapper(hints)
        val inputString = """{"hints":["list","checkmark","checkbox","radiobutton"]}"""

        val jsonString = jsonCoder.stringify(TestUIHintWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestUIHintWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestUIHintWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(inputString.toLowerCase(), jsonString.toLowerCase())
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testUIHint_Detail_Serialization() {
        val hints = UIHint.Detail.values().toList()
        val original = TestUIHintWrapper(hints)
        val inputString = """{"hints":["disclosureArrow","button","link"]}"""

        val jsonString = jsonCoder.stringify(TestUIHintWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestUIHintWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestUIHintWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(inputString.toLowerCase(), jsonString.toLowerCase())
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testUIHint_TextField_Serialization() {
        val hints = UIHint.TextField.values().toList()
        val original = TestUIHintWrapper(hints)
        val inputString = """{"hints":["textfield","multipleLine","popover"]}"""

        val jsonString = jsonCoder.stringify(TestUIHintWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestUIHintWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestUIHintWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(inputString.toLowerCase(), jsonString.toLowerCase())
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testUIHint_CustomAndPicker_Serialization() {
        val hints = listOf(UIHint.Custom("foo"), UIHint.Picker)
        val original = TestUIHintWrapper(hints)
        val inputString = """{"hints":["foo","picker"]}"""

        val jsonString = jsonCoder.stringify(TestUIHintWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestUIHintWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestUIHintWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(inputString.toLowerCase(), jsonString.toLowerCase())
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Serializable
    data class UIHintTextFieldWrapper(val types: List<UIHint.TextField>)

    @Test
    fun testUIHintTextField_Cast_Serialization() {
        val original = UIHintTextFieldWrapper(UIHint.TextField.values().toList())
        val inputString = """{"types":["textfield","multipleLine","popover"]}"""

        val jsonString = jsonCoder.stringify(UIHintTextFieldWrapper.serializer(), original)
        val restored = jsonCoder.parse(UIHintTextFieldWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(UIHintTextFieldWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(inputString.toLowerCase(), jsonString.toLowerCase())
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    /**
     * [TextFieldOptions] Tests
     */

    @Test
    fun testTextFieldOptions_Serialization() {

        val original = TextFieldOptionsObject(
                autocapitalizationType = AutoCapitalizationType.Words,
                autocorrectionType = AutoCorrectionType.No,
                keyboardType = KeyboardType.NumberPad,
                spellCheckingType = SpellCheckingType.No)
        val inputString = """
                {
                    "autocapitalizationType":"words",
                    "autocorrectionType":"no",
                    "keyboardType":"numberPad",
                    "spellCheckingType":"no"
                }
            """.trimIndent()

        val jsonString = jsonCoder.stringify(TextFieldOptionsObject.serializer(), original)
        val restored = jsonCoder.parse(TextFieldOptionsObject.serializer(), jsonString)
        val decoded = jsonCoder.parse(TextFieldOptionsObject.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)

        // TODO: syoung 04/11/2020 Figure out if there is a way to test the descriptor. This is a lot of copy/paste.
//        assertEquals(AutoCapitalizationType::class.klassName(), AutoCapitalizationType.descriptor.name)
//        assertEquals(AutoCorrectionType::class.klassName(), AutoCorrectionType.descriptor.name)
//        assertEquals(SpellCheckingType::class.klassName(), SpellCheckingType.descriptor.name)
//        assertEquals(KeyboardType::class.klassName(), KeyboardType.descriptor.name)
    }

    /**
     * [StringInputItemObject] Tests
     */

    @Test
    fun testStringInputItemObject_Serialization() {
        val inputString = """
           {
            "identifier": "foo",
            "type": "string",
            "uiHint": "popover",
            "prompt": "Favorite color",
            "placeholder": "Blue, no! Red!",
            "textFieldOptions" : {
                        "autocapitalizationType" : "words",
                        "keyboardType" : "asciiCapable",
                        "isSecureTextEntry" : true }
           }
           """
        val original = StringInputItemObject("foo")
        original.fieldLabel = "Favorite color"
        original.placeholder = "Blue, no! Red!"
        original.uiHint = UIHint.TextField.Popover
        original.textFieldOptions = TextFieldOptionsObject(
                autocapitalizationType = AutoCapitalizationType.Words,
                keyboardType = KeyboardType.AsciiCapable,
                isSecureTextEntry = true)

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.stringify(serializer, original)
        val restored = jsonCoder.parse(serializer, jsonString)
        val decoded = jsonCoder.parse(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }
}