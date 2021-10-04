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
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException
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
                    val message = if (ex is NoHandlerFoundException)
                        "no handler found"
                    else
                        ex.message ?: "40x..."

                    ErrorResponse(message)
                }
                status.is5xxServerError ->
                    ErrorResponse(ex.message ?: "something wrong ;-(")
                else ->
                    ErrorResponse(ex.message ?: "hmm...")
            }

            ResponseEntity(response, headers, status)
        } else {
            ResponseEntity(body, headers, status)
        }

    // どこでも拾われなかった例外はここで処理する
    @ExceptionHandler(value = [Throwable::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleThrowable(ex: Exception, req: WebRequest): ErrorResponse =
        ErrorResponse(ex.message ?: "something wrong ;-(")
}
