package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.StringFormat

// TODO: syoung 01/21/2020 Implement a localization strategy

object Localization {
    fun localizeString(stringKey: String): String = stringKey
}

//interface LocalizedMessage
//
//@Serializable
//@SerialName("text")
//data class LocalizedMessageKey(val messageText: String,
//                            @SerialName("description")
//                            val comment: String? = null) : LocalizedMessage
//
//@Serializable
//data class PluralRule(val textFormat: String?, val type: PluralRule.Type, val category: PluralRule.Category) {
//
//    enum class Type : StringEnum {
//        Cardinal, Ordinal, Range;
//        @Serializer(forClass = Type::class)
//        companion object : CaseInsensitiveEnumSerializer<Type>("Type", Type.values())
//    }
//
//    enum class Category : StringEnum {
//        Zero, One, Two, Few, Many, Other;
//        @Serializer(forClass = Category::class)
//        companion object : CaseInsensitiveEnumSerializer<Category>("Category", Category.values())
//    }
//}






//String message (
//String messageText,
//{String desc: '',
//    Map<String, Object> examples,
//    String locale,
//    String name,
//    List<Object> args,
//    String meaning,
//    bool skip}
//)

