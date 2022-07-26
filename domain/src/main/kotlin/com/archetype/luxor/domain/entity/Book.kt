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

    // MapStruct用
    // MapStruct自動生成のコードは Isbn classではなく、String classに見えてしまうのでcompileできない
    // そのため、Isbn classを中継しないようにする必要がある (Project Valhalla次第？)
    fun getIsbnAsString(): String = isbn.asString()
}