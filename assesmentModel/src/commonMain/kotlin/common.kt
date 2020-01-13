package org.sagebionetworks.assesmentmodel

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import kotlinx.serialization.modules.SerializersModule

expect class Platform() {
    val platform: String
}

@Serializable
data class Data(val a: Int, val b: String = "42")

class Greeting {
    fun greeting(): String {
        jsonTest()
        return "Hello, ${Platform().platform}"
    }

    fun jsonTest() {

        val config = JsonConfiguration.Stable.copy(strictMode = false)

        val messageModule = SerializersModule { // 1
//            polymorphic(Image::class) { // 2
//                FetchableImage::class with FetchableImage.serializer() // 3
//                AnimatedImage::class with AnimatedImage.serializer() // 4
//            }
        }

        val json = Json(config, messageModule)

        // serializing objects
        val jsonData = json.stringify(Data.serializer(), Data(42))
        // serializing lists
        val jsonList = json.stringify(Data.serializer().list, listOf(Data(42)))
        println(jsonData) // {"a": 42, "b": "42"}
        println(jsonList) // [{"a": 42, "b": "42"}]

        // parsing data back
        val obj = json.parse(Data.serializer(), """{"a":42}""") // b is optional since it has default value
        println(obj) // Data(a=42, b="42")
    }

}
