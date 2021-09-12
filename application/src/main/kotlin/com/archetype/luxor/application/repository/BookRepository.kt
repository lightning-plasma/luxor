package com.archetype.luxor.application.repository

import com.archetype.luxor.domain.entity.Book

interface BookRepository {
    fun findAll(): List<Book>

    fun find(isbn: String): Book
}