package com.archetype.luxor.web.advice

import com.archetype.luxor.web.entity.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class ErrorAdvice {
    @ExceptionHandler(value = [NoHandlerFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun noHandleFound(ex: NoHandlerFoundException, req: WebRequest): ErrorResponse {
        return ErrorResponse("no handler found")
    }
}
