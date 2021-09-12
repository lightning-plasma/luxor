package com.archetype.luxor.web.advice

import com.archetype.luxor.domain.error.NotFoundException
import com.archetype.luxor.domain.error.ResourceAccessError
import com.archetype.luxor.web.response.ErrorResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice("com.archetype.luxor.web.controller")
@Order(Ordered.HIGHEST_PRECEDENCE)
class ApiErrorAdvice {
    @ExceptionHandler(value = [NotFoundException::class])
    fun handleNotFound(ex: NotFoundException): ResponseEntity<Any> =
        ResponseEntity(
            ErrorResponse(ex.message ?: "data not found ;-("),
            null,
            HttpStatus.NOT_FOUND,
        )

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<Any> =
        ResponseEntity(
            ErrorResponse(ex.message ?: "illegal argument ;-("),
            null,
            HttpStatus.BAD_REQUEST,
        )

    @ExceptionHandler(value = [ResourceAccessError::class])
    fun handleInternalError(e: ResourceAccessError): ResponseEntity<Any> =
        ResponseEntity(
            ErrorResponse(e.message ?: "internal server error ;-("),
            null,
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
}