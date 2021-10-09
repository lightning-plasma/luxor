package com.archetype.luxor.infra.persistence.repository

import com.archetype.luxor.application.repository.BookRepository
import com.archetype.luxor.domain.entity.Book
import com.archetype.luxor.domain.entity.Isbn
import com.archetype.luxor.domain.error.NotFoundError
import com.archetype.luxor.domain.error.ResourceAccessError
import com.archetype.luxor.infra.persistence.advice.DataAccessExceptionAdvice
import com.archetype.luxor.infra.persistence.mapper.BookMapper
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class BookRepositoryImpl(
    private val bookMapper: BookMapper
) : BookRepository {
    @DataAccessExceptionAdvice("BookRepository#findAll")
    override fun fetchAll(): List<Book> =
        bookMapper.findAll().map {
            Book(
                isbn = Isbn(it.isbn),
                title = it.title,
                author = it.author,
                publisher = it.publisher,
                price = it.price,
                genre = null,
                rating = null,
            )
        }

    @DataAccessExceptionAdvice("BookRepository#find")
    override fun fetch(isbn: Isbn): Book =
        bookMapper.find(isbn.asString())?.let {
            Book(
                isbn = Isbn(it.isbn),
                title = it.title,
                author = it.author,
                publisher = it.publisher,
                price = it.price,
                genre = null,
                rating = null,
            )
        } ?: throw NotFoundError("No Book found. isbn=$isbn")

    @DataAccessExceptionAdvice("BookRepository#insert")
    override fun register(book: Book) {
        try {
            val now = LocalDateTime.now()
            bookMapper.insert(
                com.archetype.luxor.infra.persistence.entity.Book(
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

    @DataAccessExceptionAdvice("BookRepository#update")
    override fun update(book: Book) {
        val now = LocalDateTime.now()
        val count = bookMapper.update(
            com.archetype.luxor.infra.persistence.entity.Book(
                isbn = book.isbn.asString(),
                title = book.title,
                author = book.author,
                publisher = book.publisher,
                price = book.price,
                updatedAt = now
            )
        )

        if (count != 1) {
            throw IllegalStateException("Update failure. updated count=$count")
        }
    }
}
