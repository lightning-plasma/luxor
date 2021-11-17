package com.archetype.luxor.application.usecase

import com.archetype.luxor.application.repository.BookRepository
import com.archetype.luxor.domain.entity.Book
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(rollbackFor = [Exception::class])
class RegisterBook(
    private val bookRepository: BookRepository
) {
    suspend fun invoke(book: Book) =
        bookRepository.register(book)
}
