package com.archetype.luxor.web.controller

import com.archetype.luxor.application.usecase.FetchBook
import com.archetype.luxor.web.response.BookResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    path = ["/book"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
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
}