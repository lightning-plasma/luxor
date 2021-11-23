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
import kotlinx.coroutines.reactor.mono
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import javax.validation.constraints.Pattern

@RestController
@RequestMapping(
    path = ["/books"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@Validated
class BookController(
    private val fetchBook: FetchBook,
    private val registerBook: RegisterBook,
    private val updateBook: UpdateBook,
    private val uploadBook: UploadBook
) {
    // request mappingはURI変数とワイルドカードの数が少ないパターンに優先的にマッチする
    // https://blog.tagbangers.co.jp/2015/05/21/request-mapping-priority
    @GetMapping
    fun list(): Mono<List<BookResponse>> = mono {
        fetchBook.list().map {
            BookResponse(
                isbn = it.isbn.asString(),
                title = it.title,
                author = it.author,
                publisher = it.publisher,
                price = it.price,
                genre = it.genre,
                rating = it.rating
            )
        }
    }

    @GetMapping("{isbn}")
    fun get(
        @PathVariable("isbn") @Pattern(regexp = "^[0-9]{13}$") isbn: String
    ): Mono<BookResponse> = mono {
        val book = fetchBook.get(Isbn(isbn))

        BookResponse(
            isbn = book.isbn.asString(),
            title = book.title,
            author = book.author,
            publisher = book.publisher,
            price = book.price,
            genre = book.genre,
            rating = book.rating
        )
    }

    @PostMapping("new")
    fun new(
        @RequestBody @Validated book: BookRequest
    ): Mono<ResultResponse> = mono {
        registerBook.invoke(
            Book(
                isbn = Isbn(book.isbn),
                title = book.title,
                author = book.author,
                publisher = book.publisher,
                price = book.price,
                genre = null,
                rating = null,
            )
        )

        ResultResponse("ok")
    }

    @PutMapping("update/{isbn}")
    fun update(
        @PathVariable("isbn") @Pattern(regexp = "^[0-9]{13}$") isbn: String,
        @RequestBody @Validated book: BookRequest
    ): Mono<ResultResponse> = mono {
        updateBook.invoke(
            Book(
                isbn = Isbn(isbn),
                title = book.title,
                author = book.author,
                publisher = book.publisher,
                price = book.price,
                genre = null,
                rating = null,
            )
        )

        ResultResponse("ok")
    }

    // このfunctionは Blocking Code なので実行時はBlockHoundをoffにすること
    @GetMapping("upload")
    fun upload(): Mono<ResultResponse> = mono {
        val s3File = uploadBook.invoke()

        ResultResponse(s3File.uri())
    }
}