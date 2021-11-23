package com.archetype.luxor.web.filter

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

// https://www.baeldung.com/kotlin/spring-webflux-log-request-response-body
// https://stackoverflow.com/questions/45240005/how-to-log-request-and-response-bodies-in-spring-webflux
@Component
@Profile(value = ["default"])
class LoggingWebFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
        chain.filter(LoggingWebExchange(exchange))
}