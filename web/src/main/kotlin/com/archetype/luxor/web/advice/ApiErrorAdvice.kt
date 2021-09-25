package com.archetype.luxor.web.advice

import com.archetype.luxor.domain.error.NotFoundException
import com.archetype.luxor.domain.error.ResourceAccessError
import com.archetype.luxor.web.response.ErrorResponse
import org.springframework.context.MessageSource
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice("com.archetype.luxor.web.controller")
@Order(Ordered.HIGHEST_PRECEDENCE)
class ApiErrorAdvice(
    messageSource: MessageSource
) {
    private val messageSourceAccessor: MessageSourceAccessor = MessageSourceAccessor(messageSource)

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

    @ExceptionHandler(value = [IllegalStateException::class])
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<Any> =
        ResponseEntity(
            ErrorResponse(ex.message ?: "invalid server status ;-("),
            null,
            HttpStatus.INTERNAL_SERVER_ERROR,
        )

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handleValidationException(ex: ConstraintViolationException): ResponseEntity<Any> =
        ResponseEntity(
            ErrorResponse("invalid parameter: detail: [${ex.localizedMessage}]"),
            null,
            HttpStatus.BAD_REQUEST,
        )


    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<Any> =
        ResponseEntity(
            ErrorResponse(validationErrorMessage(e)),
            null,
            HttpStatus.BAD_REQUEST,
        )

    @ExceptionHandler(value = [ResourceAccessError::class])
    fun handleResourceAccessError(e: ResourceAccessError): ResponseEntity<Any> =
        ResponseEntity(
            ErrorResponse(e.message ?: "internal server error ;-("),
            null,
            HttpStatus.INTERNAL_SERVER_ERROR,
        )

    // こんなんでいいんだっけ ;-(
    private fun validationErrorMessage(ex: MethodArgumentNotValidException): String {
        return if (ex.hasFieldErrors()) {
            ex.bindingResult.fieldErrors.joinToString(", ") {
                it.field + ": " + messageSourceAccessor.getMessage(it)
            }
        } else {
            val message = ex.bindingResult.allErrors.joinToString(", ") {
                messageSourceAccessor.getMessage(it)
            }

            return message
        }
    }
}