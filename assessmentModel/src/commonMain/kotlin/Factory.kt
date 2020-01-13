package org.sagebionetworks.assessmentmodel

expect class Product {
    val user: String
}

expect object Factory {
    fun create(config: Map<String, String>): Product
    val platform: String
}
