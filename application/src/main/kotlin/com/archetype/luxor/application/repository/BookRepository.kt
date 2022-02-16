package com.archetype.luxor.application.repository

import com.archetype.luxor.domain.entity.Book
import com.archetype.luxor.domain.entity.Isbn

interface BookRepository {
    suspend fun fetchAll(): List<Book>

    suspend fun fetch(isbn: Isbn): Book

    suspend fun register(book: Book)

    suspend fun update(book: Book)
}