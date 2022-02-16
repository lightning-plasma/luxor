package com.archetype.luxor.application.usecase

import com.archetype.luxor.application.repository.BookRatingRepository
import com.archetype.luxor.application.repository.BookRepository
import com.archetype.luxor.domain.entity.Book
import com.archetype.luxor.domain.entity.Isbn
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component

@Component
class FetchBook(
    private val bookRepository: BookRepository,
    private val bookRatingRepository: BookRatingRepository,
) {
    suspend fun list(): List<Book> = bookRepository.fetchAll()

    suspend fun get(isbn: Isbn): Book = coroutineScope {
        val bookDeferred = async { bookRepository.fetch(isbn) }
        val attrDeferred = async { bookRatingRepository.fetch(isbn) }

        bookDeferred.await().merge(attrDeferred.await())
    }
}