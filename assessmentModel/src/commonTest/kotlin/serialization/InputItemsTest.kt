package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import org.sagebionetworks.assessmentmodel.survey.*
import kotlin.test.*

open class InputItemsTest {

    private val jsonCoder = Serialization.JsonCoder.default

    /**
     * [KeyboardOptions] Tests
     */

    @Test
    fun testTextFieldOptions_Serialization() {

        val original = KeyboardOptionsObject(
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

        val jsonString = jsonCoder.encodeToString(KeyboardOptionsObject.serializer(), original)
        val restored = jsonCoder.decodeFromString(KeyboardOptionsObject.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(KeyboardOptionsObject.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    /**
     * [UIHint] Tests
     */

    @Serializable
    data class TestUIHintWrapper(val hints: List<UIHint>)

    @Test
    fun testUIHint_Choice_Serialization() {
        val hints = UIHint.Choice.values().toList()
        val original = TestUIHintWrapper(hints)
        val inputString = """{"hints":["checkbox","radiobutton"]}"""

        val jsonString = jsonCoder.encodeToString(TestUIHintWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestUIHintWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestUIHintWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(inputString.lowercase(), jsonString.lowercase())
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testUIHint_TextField_Serialization() {
        val hints = UIHint.StringField.values().toList()
        val original = TestUIHintWrapper(hints)
        val inputString = """{"hints":["multipleLine"]}"""

        val jsonString = jsonCoder.encodeToString(TestUIHintWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestUIHintWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestUIHintWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(inputString.lowercase(), jsonString.lowercase())
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testUIHint_CustomAndPicker_Serialization() {
        val hints = listOf(UIHint.Custom("foo"))
        val original = TestUIHintWrapper(hints)
        val inputString = """{"hints":["foo"]}"""

        val jsonString = jsonCoder.encodeToString(TestUIHintWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(TestUIHintWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(TestUIHintWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(inputString.lowercase(), jsonString.lowercase())
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Serializable
    data class UIHintTextFieldWrapper(val types: List<UIHint>)

    @Test
    fun testUIHintTextField_Cast_Serialization() {
        val original = UIHintTextFieldWrapper(listOf(UIHint.TextField))
        val inputString = """{"types":["textfield"]}"""

        val jsonString = jsonCoder.encodeToString(UIHintTextFieldWrapper.serializer(), original)
        val restored = jsonCoder.decodeFromString(UIHintTextFieldWrapper.serializer(), jsonString)
        val decoded = jsonCoder.decodeFromString(UIHintTextFieldWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(inputString.lowercase(), jsonString.lowercase())
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    /**
     * CheckboxInputItem
     */

    @Test
    fun testCheckboxInputItem() {
        val inputString = """{"type":"checkbox","label":"Pick me!","identifier":"pickMe"}"""
        val original = CheckboxInputItemObject(resultIdentifier = "pickMe", label = "Pick me!")

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    /**
     * ChoiceItemObject
     */

    @Test
    fun testChoiceOptionObject_Boolean() {
        val inputString = """{"value":true,"text":"Pick me!","icon":"pickMe","selectorType":"exclusive","detail":"more info"}"""
        val original = JsonChoiceObject(
                value = JsonPrimitive(true),
                text = "Pick me!",
                iconName = "pickMe",
                selectorType = ChoiceSelectorType.Exclusive,
                detail = "more info")

        val serializer = JsonChoiceObject.serializer()
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
        assertEquals(inputString, jsonString)
    }

    @Test
    fun testChoiceOptionObject_String() {
        val inputString = """{"value":"foo","text":"Pick me!","icon":"pickMe","selectorType":"exclusive","detail":"more info"}"""
        val original = JsonChoiceObject(
                value = JsonPrimitive("foo"),
                text = "Pick me!",
                iconName = "pickMe",
                selectorType = ChoiceSelectorType.Exclusive,
                detail = "more info")

        val serializer = JsonChoiceObject.serializer()
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
        assertEquals(inputString, jsonString)
    }

    @Test
    fun testChoiceOptionObject_Int() {
        val inputString = """{"value":1,"text":"Pick me!","icon":"pickMe","selectorType":"exclusive","detail":"more info"}"""
        val original = JsonChoiceObject(
                value = JsonPrimitive(1),
                text = "Pick me!",
                iconName = "pickMe",
                selectorType = ChoiceSelectorType.Exclusive,
                detail = "more info")

        val serializer = JsonChoiceObject.serializer()
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
        assertEquals(inputString, jsonString)
    }

    /**
     * [DoubleTextInputItemObject] Tests
     */

    @Test
    fun testDoubleFormatOptions_Serialization() {
        val inputString = """
           {
                "numberStyle" : "percent",
                "usesGroupingSeparator" : false,
                "maximumFractionDigits" : 1,
                "minimumValue" : 0,
                "maximumValue" : 1000,
                "stepInterval" : 10,
                "minInvalidMessage" : "Min is zero",
                "maxInvalidMessage" : "Max is one thousand",
                "invalidMessage" : "You must enter an integer between 0 and 1000"
           }
           """

        val original = DoubleFormatOptions(
                numberStyle = NumberFormatOptions.Style.Percent,
                usesGroupingSeparator = false,
                maximumFractionDigits = 1)
        original.minimumValue = 0.0
        original.maximumValue = 1000.0
        original.stepInterval = 10.0
        original.minInvalidMessage = InvalidMessageObject("Min is zero")
        original.maxInvalidMessage = InvalidMessageObject("Min is one thousand")
        original.invalidMessage = InvalidMessageObject("You must enter an integer between 0 and 1000")

        val serializer = DoubleFormatOptions.serializer()
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testDoubleFormatOptions_Format() {
        val original = DoubleFormatOptions(numberStyle = NumberFormatOptions.Style.Percent)
        original.minimumValue = 0.0
        original.maximumValue = 1.0
        val invalidMessage = InvalidMessageObject("You must enter a percentage between 0% and 100%")
        original.invalidMessage = invalidMessage

        val formatter = DoubleFormatter(original)

        val retString: FormattedValue<String> = formatter.localizedStringFor(0.05)
        assertEquals(FormattedValue("5%"), retString)

        val retVal0 = formatter.valueFor("3%")
        assertEquals(FormattedValue(0.03), retVal0)

        val expected = FormattedValue<Double>(invalidMessage = invalidMessage)
        val retVal1 = formatter.valueFor("-1")
        assertEquals(expected, retVal1)

        val retVal2 = formatter.valueFor("150%")
        assertEquals(expected, retVal2)

        val retVal3 = formatter.valueFor("foo")
        assertEquals(expected, retVal3)
    }

    @Test
    fun testDoubleInputItemObject_Serialization() {
        val inputString = """
           {
            "identifier": "foo",
            "type": "number",
            "fieldLabel": "Favorite color",
            "placeholder": "Blue, no! Red!",
            "formatOptions" : {
                        "usesGroupingSeparator" : false,
                        "minimumValue" : 0,
                        "maximumValue" : 1000,
                        "stepInterval" : 10,
                        "minInvalidMessage" : "Min is zero",
                        "maxInvalidMessage" : "Max is one thousand",
                        "invalidMessage" : "You must enter an integer between 0 and 1000"
            }
           }
           """
        val original = DoubleTextInputItemObject("foo")
        original.fieldLabel = "Favorite color"
        original.placeholder = "Blue, no! Red!"
        original.formatOptions = DoubleFormatOptions(usesGroupingSeparator = false)
        original.formatOptions.minimumValue = 0.0
        original.formatOptions.maximumValue = 1000.0
        original.formatOptions.stepInterval = 10.0
        original.formatOptions.minInvalidMessage = InvalidMessageObject("Min is zero")
        original.formatOptions.maxInvalidMessage = InvalidMessageObject("Min is one thousand")
        original.formatOptions.invalidMessage = InvalidMessageObject("You must enter an integer between 0 and 1000")

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testDecimalTextInputItemObject_DefaultValue_Serialization() {
        val inputString = """
           {
            "type": "number"
           }
           """
        val original = DoubleTextInputItemObject()

        // Check the defaults for an decimal
        assertTrue(original.formatOptions.usesGroupingSeparator)
        assertEquals(KeyboardOptionsObject.DecimalEntryOptions, original.keyboardOptions)

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    /**
     * [IntegerTextInputItemObject] Tests
     */

    @Test
    fun testIntNumberOptions_Serialization() {
        val inputString = """
           {
                "numberStyle" : "percent",
                "usesGroupingSeparator" : false,
                "minimumValue" : 0,
                "maximumValue" : 1000,
                "stepInterval" : 10,
                "minInvalidMessage" : "Min is zero",
                "maxInvalidMessage" : "Max is one thousand",
                "invalidMessage" : "You must enter an integer between 0 and 1000"
           }
           """

        val original = IntFormatOptions(
                numberStyle = NumberFormatOptions.Style.Percent,
                usesGroupingSeparator = false)
        original.minimumValue = 0
        original.maximumValue = 1000
        original.stepInterval = 10
        original.minInvalidMessage = InvalidMessageObject("Min is zero")
        original.maxInvalidMessage = InvalidMessageObject("Max is one thousand")
        original.invalidMessage = InvalidMessageObject("You must enter an integer between 0 and 1000")

        val serializer = IntFormatOptions.serializer()
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)

        assertEquals(original.minInvalidMessage, restored.minInvalidMessage)
        assertEquals(original.maxInvalidMessage, restored.maxInvalidMessage)
        assertEquals(original.invalidMessage, restored.invalidMessage)
    }

    @Test
    fun testIntNumberOptions_Serialization_Decimal() {
        val inputString = """{"numberStyle":"decimal"}"""

        val original = IntFormatOptions(NumberFormatOptions.Style.Decimal)

        val serializer = IntFormatOptions.serializer()
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testIntNumberOptions_Serialization_Currency() {
        val inputString = """{"numberStyle":"currency"}"""

        val original = IntFormatOptions(NumberFormatOptions.Style.Currency)

        val serializer = IntFormatOptions.serializer()
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testIntNumberOptions_Serialization_Percent() {
        val inputString = """{"numberStyle":"percent"}"""

        val original = IntFormatOptions(NumberFormatOptions.Style.Percent)

        val serializer = IntFormatOptions.serializer()
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testIntNumberOptions_Serialization_Scientific() {
        val inputString = """{"numberStyle":"scientific"}"""

        val original = IntFormatOptions(NumberFormatOptions.Style.Scientific)

        val serializer = IntFormatOptions.serializer()
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testIntNumberOptions_Serialization_SpellOut() {
        val inputString = """{"numberStyle":"spellOut"}"""

        val original = IntFormatOptions(NumberFormatOptions.Style.SpellOut)

        val serializer = IntFormatOptions.serializer()
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testIntNumberOptions_Serialization_OrdinalNumber() {
        val inputString = """{"numberStyle":"ordinal"}"""

        val original = IntFormatOptions(NumberFormatOptions.Style.OrdinalNumber)

        val serializer = IntFormatOptions.serializer()
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testIntNumberOptions_Format() {
        val original = IntFormatOptions(usesGroupingSeparator = false)
        original.minimumValue = 0
        original.maximumValue = 1000
        original.minInvalidMessage = InvalidMessageObject("Min is zero")
        original.maxInvalidMessage = InvalidMessageObject("Max is one thousand")
        original.invalidMessage = InvalidMessageObject("You must enter an integer between 0 and 1000")

        val formatter = IntFormatter(original)

        val retString0: FormattedValue<String> = formatter.localizedStringFor(5)
        assertEquals(FormattedValue("5"), retString0)

        val retString1: FormattedValue<String> = formatter.localizedStringFor(1000)
        assertEquals(FormattedValue("1000"), retString1)

        val validate1 = original.validate(10)
        assertEquals(FormattedValue(10), validate1)

        val retVal0 = formatter.valueFor("10")
        assertEquals(FormattedValue(10), retVal0)
    }

    @Test
    fun testIntInputItemObject_Serialization() {
        val inputString = """
           {
            "identifier": "foo",
            "type": "integer",
            "fieldLabel": "Favorite color",
            "placeholder": "Blue, no! Red!",
            "keyboardOptions" : {
                        "keyboardType" : "numbersAndPunctuation",
                        "isSecureTextEntry" : true },
            "formatOptions" : {
                        "usesGroupingSeparator" : false,
                        "minimumValue" : 0,
                        "maximumValue" : 1000,
                        "stepInterval" : 10,
                        "minInvalidMessage" : "Min is zero",
                        "maxInvalidMessage" : "Max is one thousand",
                        "invalidMessage" : "You must enter an integer between 0 and 1000",
                        "minimumLabel" : "None",
                        "maximumLabel" : "A thousand"
            }
           }
           """
        val original = IntegerTextInputItemObject("foo")
        original.fieldLabel = "Favorite color"
        original.placeholder = "Blue, no! Red!"
        original.textOptions = KeyboardOptionsObject(
                keyboardType = KeyboardType.NumbersAndPunctuation,
                isSecureTextEntry = true)
        original.formatOptions = IntFormatOptions(usesGroupingSeparator = false)
        original.formatOptions.minimumValue = 0
        original.formatOptions.maximumValue = 1000
        original.formatOptions.stepInterval = 10
        original.formatOptions.minInvalidMessage = InvalidMessageObject("Min is zero")
        original.formatOptions.maxInvalidMessage = InvalidMessageObject("Min is one thousand")
        original.formatOptions.invalidMessage = InvalidMessageObject("You must enter an integer between 0 and 1000")
        original.formatOptions.maximumLabel = "A thousand"
        original.formatOptions.minimumLabel = "None"

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testIntInputItemObject_DefaultValue_Serialization() {
        val inputString = """
           {
            "type": "integer"
           }
           """
        val original = IntegerTextInputItemObject()

        // Check the defaults for an integer
        assertTrue(original.formatOptions.usesGroupingSeparator)
        assertEquals(KeyboardOptionsObject.NumberEntryOptions, original.keyboardOptions)

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    /**
     * [StringTextInputItemObject] Tests
     */

    @Test
    fun testStringInputItemObject_Serialization() {
        val inputString = """
           {
            "identifier": "foo",
            "type": "string",
            "fieldLabel": "Favorite color",
            "placeholder": "Blue, no! Red!",
            "keyboardOptions" : {
                        "autocapitalizationType" : "words",
                        "keyboardType" : "asciiCapable",
                        "isSecureTextEntry" : true },
            "regExValidator" : {
                        "pattern" : "[A:D]",
                        "invalidMessage" : "Only ABCD are valid letters."
            }
           }
           """
        val original = StringTextInputItemObject("foo")
        original.fieldLabel = "Favorite color"
        original.placeholder = "Blue, no! Red!"
        original.textOptions = KeyboardOptionsObject(
                autocapitalizationType = AutoCapitalizationType.Words,
                keyboardType = KeyboardType.AsciiCapable,
                isSecureTextEntry = true)
        original.regExValidator = RegExValidator("[A:D]", InvalidMessageObject("Only ABCD are valid letters."))

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testStringInputItemObject_DefaultValue_Serialization() {
        val inputString = """
           {
            "type": "string"
           }
           """
        val original = StringTextInputItemObject()

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    /**
     * TimeInputItemObject
     */

    @Test
    fun testTimeInputItemObject_Min_Serialization() {
        val inputString = """
           {
            "identifier": "foo",
            "type": "time",
            "fieldLabel": "Favorite color",
            "placeholder": "Blue, no! Red!",
            "formatOptions" : {
                        "minimumValue" : "06:00",
                        "allowFuture" : false
            }
           }
           """
        val original = TimeInputItemObject(
                resultIdentifier = "foo",
                formatOptions = TimeFormatOptions(
                        minimumValue = Json.decodeFromString("\"06:00\""),
                        allowFuture = false))

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testTimeInputItemObject_Max_Serialization() {
        val inputString = """
           {
            "identifier": "foo",
            "type": "time",
            "fieldLabel": "Favorite color",
            "placeholder": "Blue, no! Red!",
            "formatOptions" : {
                        "allowPast" : false,
                        "maximumValue" : "22:00"
            }
           }
           """
        val original = TimeInputItemObject(
                resultIdentifier = "foo",
                formatOptions = TimeFormatOptions(
                        allowPast = false,
                        maximumValue = Json.decodeFromString("\"22:00\"")))

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testTimeInputItemObject_Default_Serialization() {
        val inputString = """
           {
            "type": "time"
           }
           """
        val original = TimeInputItemObject()

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    /**
     * DurationInputItemObject
     */

    @Test
    fun testDurationInputItemObject_Default_Serialization() {
        val inputString = """
           {
            "type": "duration"
           }
           """
        val original = DurationInputItemObject()

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }


    @Test
    fun testDurationInputItemObject_DisplayUnits_Serialization() {
        val inputString = """
           {
            "type": "duration",
            "displayUnits": ["minute","second"]
           }
           """
        val original = DurationInputItemObject(displayUnits = listOf(DurationUnit.Minute, DurationUnit.Second))

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    /**
     * [YearTextInputItemObject] Tests
     */

    @Test
    fun testYearInputItemObject_Serialization() {
        val inputString = """
           {
            "identifier": "foo",
            "type": "year",
            "fieldLabel": "Favorite color",
            "placeholder": "Blue, no! Red!",
            "exclusive": true,
            "formatOptions": {
                "allowFuture": false,
                "minimumYear": 1900
            }
           }
           """
        val original = YearTextInputItemObject("foo")
        original.exclusive = true
        original.fieldLabel = "Favorite color"
        original.placeholder = "Blue, no! Red!"
        original.formatOptions = YearFormatOptions(allowFuture = false, minimumYear = 1900)

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testYearInputItemObject_DefaultValue_Serialization() {
        val inputString = """
           {
            "type": "year"
           }
           """
        val original = YearTextInputItemObject()

        val serializer = PolymorphicSerializer(InputItem::class)
        val jsonString = jsonCoder.encodeToString(serializer, original)
        val restored = jsonCoder.decodeFromString(serializer, jsonString)
        val decoded = jsonCoder.decodeFromString(serializer, inputString)

        assertEquals(original, restored)
        assertEquals(original, decoded)
    }
}