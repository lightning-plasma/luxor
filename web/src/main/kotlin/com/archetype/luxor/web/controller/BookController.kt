package com.archetype.luxor.web.controller

import com.archetype.luxor.application.repository.BookRepository
import com.archetype.luxor.domain.entity.Book
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
    private val bookRepository: BookRepository
) {
    @GetMapping("list")
    fun list(): List<Book> =
        bookRepository.findAll()
}