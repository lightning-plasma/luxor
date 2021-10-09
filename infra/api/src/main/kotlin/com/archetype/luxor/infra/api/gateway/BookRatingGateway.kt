package com.archetype.luxor.infra.api.gateway

import com.archetype.luxor.domain.entity.Isbn
import com.archetype.luxor.infra.api.entity.BookRatingResponse
import com.archetype.luxor.infra.api.factory.BookRatingClientFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@Gateway
class BookRatingGateway(
    private val factory: BookRatingClientFactory
) {
    fun fetch(isbn: Isbn): BookRatingResponse =
        factory.client
            .get()
            .uri("/book-rating/${isbn.asString()}")
            .retrieve()
            .bodyToMono(BookRatingResponse::class.java)
            .block() ?: throw NullPointerException("check")
}

@ConstructorBinding
@ConfigurationProperties(prefix = "gateway.book-rating")
data class BookRatingGatewayConfig(
    override val protocol: String,
    override val host: String,
    override val port: String,
) : GatewayConfig(protocol, host, port)