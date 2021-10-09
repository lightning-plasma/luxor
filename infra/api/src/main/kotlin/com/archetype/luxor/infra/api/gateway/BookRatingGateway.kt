package com.archetype.luxor.infra.api.gateway

import com.archetype.luxor.domain.entity.Isbn
import com.archetype.luxor.infra.api.client.Result
import com.archetype.luxor.domain.error.NotFoundError
import com.archetype.luxor.infra.api.entity.BookRatingResponse
import com.archetype.luxor.infra.api.client.BookRatingClientFactory
import com.archetype.luxor.infra.api.client.Failure
import com.archetype.luxor.infra.api.client.Success
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.time.Duration
import java.util.concurrent.TimeoutException

@Gateway
class BookRatingGateway(
    private val factory: BookRatingClientFactory
) {
    // https://www.baeldung.com/spring-webflux-retry
    fun fetch(isbn: Isbn): Result<BookRatingResponse> =
        try {
            factory.client
                .get()
                .uri("/book-rating/${isbn.asString()}")
                .exchangeToMono { res ->
                    if (res.statusCode().is2xxSuccessful) {
                        res.bodyToMono(BookRatingResponse::class.java)
                            .flatMap { Mono.just(Success(it)) }
                    } else {
                        res.bodyToMono(Failure.Response::class.java).flatMap {
                            Mono.just(
                                Failure(
                                    method = "get",
                                    uri = "/book-rating/${isbn.asString()}",
                                    response = Failure.Response(res.statusCode().value())
                                )
                            )
                        }
                    }
                }
                .timeout(Duration.ofMillis(1000L))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)).jitter(0.75))
                .onErrorResume {
                    Mono.error(TimeoutException())
                }
                .block() ?: throw NotFoundError("Not found: Isbn=${isbn.asString()}")
        } catch (ex: TimeoutException) {
            Failure(
                method = "get",
                uri = "/book-rating/${isbn.asString()}",
                response = null
            )
        } catch (ex: NotFoundError) {
            Failure(
                method = "get",
                uri = "/book-rating/${isbn.asString()}",
                response = null
            )
        }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "gateway.book-rating")
data class BookRatingGatewayConfig(
    override val protocol: String,
    override val host: String,
    override val port: String,
) : GatewayConfig(protocol, host, port)