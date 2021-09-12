package com.archetype.luxor.web.controller

import com.archetype.luxor.application.usecase.FetchBook
import com.archetype.luxor.web.response.BookResponse
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@RestController
@RequestMapping(
    path = ["/book"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@Validated
class BookController(
    private val fetchBook: FetchBook
) {
    @GetMapping("list")
    fun list(): List<BookResponse> =
        fetchBook.list().map {
            BookResponse(
                isbn = it.isbn,
                title = it.title,
                author = it.author,
                publisher = it.publisher,
                price = it.price
            )
        }

    @GetMapping("{isbn}")
    fun get(
        @PathVariable("isbn") @Pattern(regexp = "^[0-9]{13}$") isbn: String
    ): BookResponse {
        val book = fetchBook.get(isbn)
        return BookResponse(
            isbn = book.isbn,
            title = book.title,
            author = book.author,
            publisher = book.publisher,
            price = book.price
        )
    }
}