package com.archetype.luxor.infra.persistence.repository

import com.archetype.luxor.application.repository.BookRepository
import com.archetype.luxor.domain.entity.Book
import com.archetype.luxor.domain.entity.Isbn
import com.archetype.luxor.domain.error.NotFoundError
import com.archetype.luxor.domain.error.ResourceAccessError
import com.archetype.luxor.infra.persistence.advice.DataAccessExceptionAdvice
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query.query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class BookRepositoryImpl(
    private val template: R2dbcEntityTemplate,
) : BookRepository {
    @DataAccessExceptionAdvice("BookRepository#fetchAll")
    override suspend fun fetchAll(): List<Book> =
        template.select(com.archetype.luxor.infra.persistence.entity.Book::class.java)
            .all()
            .collectList()
            .awaitFirst()
            .map {
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

    @DataAccessExceptionAdvice("BookRepository#fetch")
    override suspend fun fetch(isbn: Isbn): Book {
        val book = template
            .select(com.archetype.luxor.infra.persistence.entity.Book::class.java)
            .from("main.book")
            .matching(
                query(
                    where("isbn").`is`(isbn.asString()),
                )
            )
            .one()
            .awaitFirstOrNull() ?: throw NotFoundError("${isbn.asString()} is not found")

        return Book(
            isbn = Isbn(book.isbn),
            title = book.title,
            author = book.author,
            publisher = book.publisher,
            price = book.price,
            genre = null,
            rating = null,
        )
    }

    @DataAccessExceptionAdvice("BookRepository#register")
    override suspend fun register(book: Book) {
        val now = LocalDateTime.now()
        val entity = com.archetype.luxor.infra.persistence.entity.Book(
            isbn = book.isbn.asString(),
            title = book.title,
            author = book.author,
            publisher = book.publisher,
            price = book.price,
            createdAt = now,
            updatedAt = now
        )

        try {
            template.insert(entity).awaitFirst()
        } catch (e: DuplicateKeyException) {
            throw ResourceAccessError(e, "This book is already registered")
        }
    }

    @DataAccessExceptionAdvice("BookRepository#insert")
    override suspend fun update(book: Book) {
        val now = LocalDateTime.now()
        val entity = com.archetype.luxor.infra.persistence.entity.Book(
            isbn = book.isbn.asString(),
            title = book.title,
            author = book.author,
            publisher = book.publisher,
            price = book.price,
            createdAt = now,
            updatedAt = now
        )

        template.update(entity).awaitFirst()
    }
}
