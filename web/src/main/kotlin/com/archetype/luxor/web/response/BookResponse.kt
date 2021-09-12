package com.archetype.luxor.web.response

data class BookResponse(
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    val price: Int
)
