package com.archetype.luxor.infra.persistence.entity

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class Book(
    @Id
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    val price: Int,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
)