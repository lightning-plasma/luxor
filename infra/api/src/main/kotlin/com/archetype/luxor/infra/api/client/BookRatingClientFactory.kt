package com.archetype.luxor.infra.api.client

import com.archetype.luxor.infra.api.gateway.BookRatingGatewayConfig
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.codec.xml.Jaxb2XmlDecoder
import org.springframework.http.codec.xml.Jaxb2XmlEncoder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Component
@EnableConfigurationProperties(value = [BookRatingGatewayConfig::class])
class BookRatingClientFactory(
    builder: WebClient.Builder,
    bookRatingGatewayConfig: BookRatingGatewayConfig,
) : ClientFactory(builder) {
    private val client = baseBuilder.baseUrl(bookRatingGatewayConfig.url())
//        .exchangeStrategies(
//            ExchangeStrategies.builder().codecs {
//                it.defaultCodecs().jaxb2Encoder(Jaxb2XmlEncoder())
//                it.defaultCodecs().jaxb2Decoder(Jaxb2XmlDecoder())
//            }.build()
//        )
        .build()

    override fun create(): LuxorWebClient.RequestBuilder = LuxorWebClient
        .RequestBuilder(client)
        .timeoutInMillis(3000)
        .retry(1)
        .backoffInMillis(100)

}
