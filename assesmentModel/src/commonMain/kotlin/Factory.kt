package org.sagebionetworks.assesmentmodel

expect class Product {
    val user: String
}

expect object Factory {
    fun create(config: Map<String, String>): Product
    val platform: String
}
