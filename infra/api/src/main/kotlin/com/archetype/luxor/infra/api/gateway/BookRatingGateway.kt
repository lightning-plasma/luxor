package com.archetype.luxor.infra.api.gateway

import com.archetype.luxor.domain.entity.Isbn
import com.archetype.luxor.infra.api.client.Result
import com.archetype.luxor.infra.api.entity.BookRatingResponse
import com.archetype.luxor.infra.api.client.BookRatingClientFactory
import com.archetype.luxor.infra.api.entity.BookRatingErrorResponse
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@Gateway
class BookRatingGateway(
    private val factory: BookRatingClientFactory
) {
    // https://www.baeldung.com/spring-webflux-retry
    fun fetch(isbn: Isbn): Result<BookRatingResponse> =
        factory.create()
            .get()
            .uri("/book-rating/${isbn.asString()}")
            .request {
                when {
                    it.statusCode.is2xxSuccessful -> it.toResult()
                    else -> it.toFailure<BookRatingErrorResponse>()
                }
            }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "gateway.book-rating")
data class BookRatingGatewayConfig(
    override val protocol: String,
    override val host: String,
    override val port: String,
) : GatewayConfig(protocol, host, port)