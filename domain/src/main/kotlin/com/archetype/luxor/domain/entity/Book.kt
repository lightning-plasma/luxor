package com.archetype.luxor.domain.entity

data class Book(
    val isbn: Isbn,
    val title: String,
    val author: String,
    val publisher: String,
    val price: Int,
    val genre: String?,
    val rating: String?
)