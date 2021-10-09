package com.archetype.luxor.application.usecase

import com.archetype.luxor.application.repository.BookRatingRepository
import com.archetype.luxor.application.repository.BookRepository
import com.archetype.luxor.domain.entity.Book
import com.archetype.luxor.domain.entity.Isbn
import org.springframework.stereotype.Component

@Component
class FetchBook(
    private val bookRepository: BookRepository,
    private val bookRatingRepository: BookRatingRepository,
) {
    fun list(): List<Book> = bookRepository.fetchAll()

    fun get(isbn: Isbn): Book {
        val book = bookRepository.fetch(isbn)
        val attr = bookRatingRepository.fetch(isbn)

        return book.copy(
            genre = attr.genre,
            rating = attr.rating
        )
    }
}