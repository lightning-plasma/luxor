package com.archetype.luxor.web.controller

import com.archetype.luxor.application.usecase.RegisterBook
import com.archetype.luxor.application.usecase.FetchBook
import com.archetype.luxor.application.usecase.UpdateBook
import com.archetype.luxor.application.usecase.UploadBook
import com.archetype.luxor.domain.entity.Book
import com.archetype.luxor.domain.entity.Isbn
import com.archetype.luxor.web.request.BookRequest
import com.archetype.luxor.web.response.BookResponse
import com.archetype.luxor.web.response.ResultResponse
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Pattern

@RestController
@RequestMapping(
    path = ["/book"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@Validated
class BookController(
    private val fetchBook: FetchBook,
    private val registerBook: RegisterBook,
    private val updateBook: UpdateBook,
    private val uploadBook: UploadBook
) {
    @GetMapping("list")
    fun list(): List<BookResponse> =
        fetchBook.list().map {
            BookResponse(
                isbn = it.isbn.asString(),
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
        val book = fetchBook.get(Isbn(isbn))
        return BookResponse(
            isbn = book.isbn.asString(),
            title = book.title,
            author = book.author,
            publisher = book.publisher,
            price = book.price
        )
    }

    @PostMapping("new")
    fun new(
        @RequestBody @Validated book: BookRequest
    ): ResultResponse {
        registerBook.invoke(
            Book(
                isbn = Isbn(book.isbn),
                title = book.title,
                author = book.author,
                publisher = book.publisher,
                price = book.price
            )
        )

        return ResultResponse("ok")
    }

    @PutMapping("update/{isbn}")
    fun update(
        @PathVariable("isbn") @Pattern(regexp = "^[0-9]{13}$") isbn: String,
        @RequestBody @Validated book: BookRequest
    ): ResultResponse {
        updateBook.invoke(
            Book(
                isbn = Isbn(isbn),
                title = book.title,
                author = book.author,
                publisher = book.publisher,
                price = book.price
            )
        )

        return ResultResponse("ok")
    }

    @GetMapping("upload")
    fun upload(): ResultResponse {
        val s3File = uploadBook.invoke()

        return ResultResponse(s3File.uri())
    }
}