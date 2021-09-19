package com.archetype.luxor.infra.persistence.entity

import java.time.LocalDateTime

data class Book(
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    val price: Int,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
)