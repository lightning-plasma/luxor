package com.archetype.luxor.repository

import com.archetype.luxor.web.entity.Book
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet


@Repository
class BookRepository(
    private val jdbcTemplate: JdbcTemplate
) {
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

    fun findAll(): List<Book> =
        jdbcTemplate.query(
            "SELECT isbn, title, author, publisher, price FROM main.book",
            bookMapper
        )
}