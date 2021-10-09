package com.archetype.luxor.infra.api.factory

import com.archetype.luxor.infra.api.gateway.BookRatingGatewayConfig
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
@EnableConfigurationProperties(value = [BookRatingGatewayConfig::class])
class BookRatingClientFactory(
    builder: WebClient.Builder,
    bookRatingGatewayConfig: BookRatingGatewayConfig,
) : ClientFactory(builder) {
    val client = baseBuilder.baseUrl(bookRatingGatewayConfig.url()).build()
}
