package com.archetype.luxor.web.advice

import com.archetype.luxor.web.response.ErrorResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
@Order(Ordered.LOWEST_PRECEDENCE)
class CommonErrorAdvice : ResponseEntityExceptionHandler() {
    // ApiErrorAdviceで個別定義していない && Spring Bootが拾ってくれる例外がここにくる
    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> =
        if (body !is ErrorResponse) {
            val response = when {
                status.is4xxClientError -> {
                    ErrorResponse("no handler found")
                }
                status.is5xxServerError ->
                    ErrorResponse("something wrong ;-(")
                else ->
                    ErrorResponse("hmm...")
            }

            ResponseEntity(response, headers, status)
        } else {
            ResponseEntity(body, headers, status)
        }

    // どこでも拾われなかった例外はここで処理する
    @ExceptionHandler(value = [Throwable::class])
    fun handleThrowable(ex: Exception, req: WebRequest): ResponseEntity<Any> =
        ResponseEntity(
            ErrorResponse("something wrong ;-("),
            null,
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
}
