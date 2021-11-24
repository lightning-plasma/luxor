package com.archetype.luxor.web.handler

import com.archetype.luxor.web.response.ErrorResponse
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RequestPredicates.all
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

// https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web.reactive.webflux.error-handling
// https://stackoverflow.com/questions/60139167/string-insted-of-whitelabel-error-page-in-webflux
@Component
@Order(-2)
class LuxorExceptionHandler(
    errorAttributes: ErrorAttributes,
    applicationContext: ApplicationContext,
    serverCodecConfigurer: ServerCodecConfigurer,
) : AbstractErrorWebExceptionHandler(
    errorAttributes,
    WebProperties.Resources(),
    applicationContext
) {
    init {
        this.setMessageWriters(serverCodecConfigurer.writers)
        this.setMessageReaders(serverCodecConfigurer.readers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse> {
        return route(all(), this::renderErrorResponse)
    }

    private fun renderErrorResponse(serverRequest: ServerRequest): Mono<ServerResponse> {
        val errorProperties = getErrorAttributes(
            serverRequest,
            ErrorAttributeOptions.defaults()
        )

        logger.debug { errorProperties }

        val errorAttribute = serverRequest
            .attribute(
                "org.springframework.boot.web.reactive.error.DefaultErrorAttributes.ERROR"
            )

        if (errorAttribute.isPresent) {
            val throwable = errorAttribute.get() as Throwable

            if (throwable.message == NO_HANDLER_MESSAGE) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        BodyInserters.fromValue(ErrorResponse(NO_HANDLER_MESSAGE))
                    )
            }
        }

        return ServerResponse.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(ErrorResponse("something wrong ;-(")))
    }

    companion object {
        private const val NO_HANDLER_MESSAGE = "404 NOT_FOUND \"No matching handler\""
        private val logger = KotlinLogging.logger { }
    }
}