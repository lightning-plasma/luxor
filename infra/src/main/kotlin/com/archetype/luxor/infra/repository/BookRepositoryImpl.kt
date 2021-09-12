package com.archetype.luxor.infra.repository

import com.archetype.luxor.application.repository.BookRepository
import com.archetype.luxor.domain.entity.Book
import com.archetype.luxor.domain.entity.Isbn
import com.archetype.luxor.domain.error.NotFoundException
import com.archetype.luxor.infra.mapper.BookMapper
import org.springframework.stereotype.Repository

@Repository
class BookRepositoryImpl(
    private val bookMapper: BookMapper
) : BookRepository {
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
}
