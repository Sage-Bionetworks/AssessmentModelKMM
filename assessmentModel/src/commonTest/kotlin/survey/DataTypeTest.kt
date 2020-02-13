package org.sagebionetworks.assessmentmodel.survey

import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.forms.DataType
import org.sagebionetworks.assessmentmodel.serialization.Serialization
import kotlin.test.Test
import kotlin.test.assertEquals

class DataTypeTest {
    
    val jsonCoder = Serialization.JsonCoder.default

    @Serializable
    data class TestDataTypeWrapper(val dataTypes: List<DataType>)

    /**
     * The tests below list out assorted permutations of the [DataType]. If that data type is changed, these tests
     * will likely fail and should be updated.
     */

    @Test
    fun testDataType_Base_Serialization() {
        val dataTypes = DataType.Base.values()
        val original = TestDataTypeWrapper(dataTypes.toList())
        val inputString = """{"dataTypes":["boolean","date","decimal","duration","fraction","integer","string","year","codable"]}"""

        val jsonString = jsonCoder.stringify(TestDataTypeWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestDataTypeWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestDataTypeWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(inputString.toLowerCase(), jsonString.toLowerCase())
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testDataType_Collection_Serialization() {
        val dataTypes = DataType.Collection.values()
        val original = TestDataTypeWrapper(dataTypes.toList())
        val inputString = """{"dataTypes":["multipleChoice.boolean","multipleChoice.date","multipleChoice.decimal","multipleChoice.duration","multipleChoice.fraction","multipleChoice.integer","multipleChoice","multipleChoice.year","multipleChoice.codable","singleChoice.boolean","singleChoice.date","singleChoice.decimal","singleChoice.duration","singleChoice.fraction","singleChoice.integer","singleChoice","singleChoice.year","singleChoice.codable","multipleComponent.boolean","multipleComponent.date","multipleComponent.decimal","multipleComponent.duration","multipleComponent.fraction","multipleComponent.integer","multipleComponent","multipleComponent.year","multipleComponent.codable"]}"""

        val jsonString = jsonCoder.stringify(TestDataTypeWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestDataTypeWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestDataTypeWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(inputString.toLowerCase(), jsonString.toLowerCase())
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testDataType_Date_Serialization() {
        val dataTypes = DataType.Date.values()
        val original = TestDataTypeWrapper(dataTypes.toList())
        val inputString = """{"dataTypes":["timestamp","dateOnly","time"]}"""

        val jsonString = jsonCoder.stringify(TestDataTypeWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestDataTypeWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestDataTypeWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(inputString.toLowerCase(), jsonString.toLowerCase())
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testDataType_Measurement_Serialization() {
        val dataTypes = DataType.Measurement.values()
        val original = TestDataTypeWrapper(dataTypes.toList())
        val inputString = """{"dataTypes":["height","height.child","height.infant","weight","weight.child","weight.infant","bloodPressure","bloodPressure.child","bloodPressure.infant"]}"""

        val jsonString = jsonCoder.stringify(TestDataTypeWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestDataTypeWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestDataTypeWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(inputString.toLowerCase(), jsonString.toLowerCase())
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

    @Test
    fun testDataType_Custom_Serialization() {
        val dataTypes = listOf(
                DataType.Custom("foo"),
                DataType.Custom("foo.blu"),
                DataType.Custom("foo", DataType.BaseType.Boolean),
                DataType.Custom("foo", DataType.BaseType.Date),
                DataType.Custom("foo", DataType.BaseType.Decimal),
                DataType.Custom("foo", DataType.BaseType.Duration),
                DataType.Custom("foo", DataType.BaseType.Fraction),
                DataType.Custom("foo", DataType.BaseType.Integer),
                DataType.Custom("foo", DataType.BaseType.String),
                DataType.Custom("foo", DataType.BaseType.Year))
        val original = TestDataTypeWrapper(dataTypes)
        val inputString = """{"dataTypes":["foo","foo.blu","foo.boolean","foo.date","foo.decimal","foo.duration","foo.fraction","foo.integer","foo.string","foo.year"]}"""

        val jsonString = jsonCoder.stringify(TestDataTypeWrapper.serializer(), original)
        val restored = jsonCoder.parse(TestDataTypeWrapper.serializer(), jsonString)
        val decoded = jsonCoder.parse(TestDataTypeWrapper.serializer(), inputString)

        // Look to see that the restored, decoded, and original all are equal
        assertEquals(inputString.toLowerCase(), jsonString.toLowerCase())
        assertEquals(original, restored)
        assertEquals(original, decoded)
    }

}