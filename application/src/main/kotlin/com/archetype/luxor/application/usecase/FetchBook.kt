package com.archetype.luxor.application.usecase

import com.archetype.luxor.application.repository.BookRepository
import com.archetype.luxor.domain.entity.Book
import org.springframework.stereotype.Component

@Component
class FetchBook(
    private val bookRepository: BookRepository
) {
    fun list(): List<Book> = bookRepository.findAll()
}