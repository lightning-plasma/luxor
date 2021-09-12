package com.archetype.luxor.infra.repository

import com.archetype.luxor.application.repository.BookRepository
import com.archetype.luxor.domain.entity.Book
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class BookRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : BookRepository {
    private val bookMapper: RowMapper<Book> =
        RowMapper { rs: ResultSet, _: Int ->
            Book(
                isbn = rs.getString("isbn"),
                title = rs.getString("title"),
                author = rs.getString("author"),
                publisher = rs.getString("publisher"),
                price = rs.getInt("price"),
            )
        }

    override fun findAll(): List<Book> =
        jdbcTemplate.query(
            "SELECT isbn, title, author, publisher, price FROM main.book",
            bookMapper
        )
}
