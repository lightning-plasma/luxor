package com.archetype.luxor.application.usecase

import com.archetype.luxor.application.repository.BookRepository
import com.archetype.luxor.domain.entity.Book
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(rollbackFor = [Exception::class])
class UpdateBook(
    private val bookRepository: BookRepository
) {
    fun invoke(book: Book) {
        bookRepository.update(book)
    }
}