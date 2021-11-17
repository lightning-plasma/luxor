package com.archetype.luxor.infra.api.gateway

import com.archetype.luxor.domain.entity.Isbn
import com.archetype.luxor.infra.api.client.Result
import com.archetype.luxor.infra.api.client.BookRatingClientFactory
import com.archetype.luxor.infra.api.entity.BookRatingErrorResponse
import com.archetype.luxor.infra.api.entity.BookRatingXmlResponse
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@Gateway
class BookRatingGateway(
    private val factory: BookRatingClientFactory
) {
    fun fetch(isbn: Isbn): Result<BookRatingXmlResponse> {
        return factory.create()
            .get()
            .headers(HttpHeaders.ACCEPT to MediaType.APPLICATION_XML_VALUE)
            .uri("/book-rating/${isbn.asString()}")
            .request {
                when {
                    it.statusCode.is2xxSuccessful -> it.toResult()
                    else -> it.toFailure<BookRatingErrorResponse>()
                }
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