package com.archetype.luxor.domain.entity

data class Book(
    val isbn: Isbn,
    val title: String,
    val author: String,
    val publisher: String,
    val price: Int,
    val genre: String?,
    val rating: String?
) {
    fun merge(attr: BookAttribute) =
        this.copy(
            genre = attr.genre,
            rating = attr.rating
        )

    // MapStructではValue ClassのMappingができないのでbookから参照
    fun getIsbnAsString(): String = isbn.asString()
}