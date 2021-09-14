package com.archetype.luxor.application.usecase

import com.archetype.luxor.application.repository.BookRepository
import com.archetype.luxor.domain.entity.Book
import org.springframework.stereotype.Component

@Component
class RegisterBook(
    private val bookRepository: BookRepository
) {
    fun invoke(book: Book) =
        bookRepository.register(book)
}