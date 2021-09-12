package com.archetype.luxor.web.entity

data class Book(
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    val price: Int
)