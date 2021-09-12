package com.archetype.luxor.application.usecase

import com.archetype.luxor.application.repository.BookRepository
import com.archetype.luxor.domain.entity.Book
import com.archetype.luxor.domain.entity.Isbn
import org.springframework.stereotype.Component

@Component
class FetchBook(
    private val bookRepository: BookRepository
) {
    fun list(): List<Book> = bookRepository.findAll()

    fun get(isbn: Isbn): Book = bookRepository.find(isbn)
}