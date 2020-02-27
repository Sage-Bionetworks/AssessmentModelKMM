package org.sagebionetworks.assessmentmodel

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
 * An interface for sealed classes to use to define "enum" behavior.
 *
 * This is a similar to Swift enum representation where the enum contains nested enums that are themselves
 * `RawRepresentable` using a typealias of `String` and `CaseIterable` enumeration. We use this pattern to allow for
 * describing more complex hierarchies of "type" and "category" classifications.
 */
interface StringEnumCompanion<T : StringEnum> {
    fun values(): Array<T>
    fun valueOf(name: String): T? =
            values().matching(name)
}