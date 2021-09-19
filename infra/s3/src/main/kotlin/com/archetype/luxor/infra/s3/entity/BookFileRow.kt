package com.archetype.luxor.infra.s3.entity

import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder(
    "isbn",
    "title",
    "author",
    "publisher",
    "price",
)
data class BookFileRow(
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    val price: Int
) : CsvSerializable