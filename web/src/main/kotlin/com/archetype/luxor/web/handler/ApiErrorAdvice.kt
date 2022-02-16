package com.archetype.luxor.web.handler

import com.archetype.luxor.domain.error.NotFoundError
import com.archetype.luxor.domain.error.ResourceAccessError
import com.archetype.luxor.web.response.ErrorResponse
import org.springframework.context.MessageSource
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice("com.archetype.luxor.web.controller")
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
@Order(Ordered.HIGHEST_PRECEDENCE)
class ApiErrorAdvice(
    messageSource: MessageSource
) {
    private val messageSourceAccessor: MessageSourceAccessor = MessageSourceAccessor(messageSource)

    @ExceptionHandler(value = [NotFoundError::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(ex: NotFoundError): ErrorResponse =
        ErrorResponse(ex.message ?: "data not found ;-(")

    @ExceptionHandler(value = [IllegalArgumentException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(ex: IllegalArgumentException): ErrorResponse =
        ErrorResponse(ex.message ?: "illegal argument ;-(")

    @ExceptionHandler(value = [IllegalStateException::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleIllegalStateException(ex: IllegalStateException): ErrorResponse =
        ErrorResponse(ex.message ?: "invalid server status ;-(")

    @ExceptionHandler(value = [ConstraintViolationException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: ConstraintViolationException): ErrorResponse =
        ErrorResponse("invalid parameter: detail: [${ex.localizedMessage}]")

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ErrorResponse =
        ErrorResponse(validationErrorMessage(e))

    @ExceptionHandler(value = [ResourceAccessError::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleResourceAccessError(e: ResourceAccessError): ErrorResponse =
        ErrorResponse(e.message ?: "internal server error ;-(")

    @ExceptionHandler(value = [Throwable::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleThrowable(th: Throwable): ErrorResponse =
        ErrorResponse(th.message ?: "internal server error ;-(")

    private fun validationErrorMessage(ex: MethodArgumentNotValidException): String =
        ex.bindingResult.allErrors.joinToString(", ") {
            messageSourceAccessor.getMessage(it)
        }
}