package com.archetype.luxor.application.repository

import com.archetype.luxor.domain.entity.Book
import com.archetype.luxor.domain.entity.Isbn

interface BookRepository {
    fun findAll(): List<Book>

    fun find(isbn: Isbn): Book

    fun register(book: Book)
}