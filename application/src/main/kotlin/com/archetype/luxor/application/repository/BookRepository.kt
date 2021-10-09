package com.archetype.luxor.application.repository

import com.archetype.luxor.domain.entity.Book
import com.archetype.luxor.domain.entity.Isbn

interface BookRepository {
    fun fetchAll(): List<Book>

    fun fetch(isbn: Isbn): Book

    fun register(book: Book)

    fun update(book: Book)
}