package com.archetype.luxor.infra.repository

import com.archetype.luxor.application.repository.BookRepository
import com.archetype.luxor.domain.entity.Book
import com.archetype.luxor.domain.entity.Isbn
import com.archetype.luxor.domain.error.NotFoundException
import com.archetype.luxor.domain.error.ResourceAccessError
import com.archetype.luxor.infra.advice.DataAccessExceptionAdvice
import com.archetype.luxor.infra.mapper.BookMapper
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class BookRepositoryImpl(
    private val bookMapper: BookMapper
) : BookRepository {
    @DataAccessExceptionAdvice("BookRepository#findAll")
    override fun findAll(): List<Book> =
        bookMapper.findAll().map {
            Book(
                isbn = Isbn(it.isbn),
                title = it.title,
                author = it.author,
                publisher = it.publisher,
                price = it.price
            )
        }

    @DataAccessExceptionAdvice("BookRepository#find")
    override fun find(isbn: Isbn): Book =
        bookMapper.find(isbn.asString())?.let {
            Book(
                isbn = Isbn(it.isbn),
                title = it.title,
                author = it.author,
                publisher = it.publisher,
                price = it.price
            )
        } ?: throw NotFoundException("No Book found. isbn=$isbn")

    @DataAccessExceptionAdvice("BookRepository#insert")
    override fun register(book: Book) {
        try {
            val now = LocalDateTime.now()
            bookMapper.insert(
                com.archetype.luxor.infra.entity.Book(
                    isbn = book.isbn.asString(),
                    title = book.title,
                    author = book.author,
                    publisher = book.publisher,
                    price = book.price,
                    createdAt = now,
                    updatedAt = now
                )
            )
        } catch (e: DuplicateKeyException) {
            throw ResourceAccessError(e, "This book is already registered")
        }
    }
}
