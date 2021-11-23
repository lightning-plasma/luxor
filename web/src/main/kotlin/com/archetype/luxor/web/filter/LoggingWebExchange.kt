package com.archetype.luxor.web.filter

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebExchangeDecorator

class LoggingWebExchange(delegate: ServerWebExchange) : ServerWebExchangeDecorator(delegate) {
    private val requestDecorator: LoggingRequestDecorator = LoggingRequestDecorator(delegate.request)
    private val responseDecorator: LoggingResponseDecorator = LoggingResponseDecorator(delegate.response)
    override fun getRequest(): ServerHttpRequest {
        return requestDecorator
    }

    override fun getResponse(): ServerHttpResponse {
        return responseDecorator
    }
}