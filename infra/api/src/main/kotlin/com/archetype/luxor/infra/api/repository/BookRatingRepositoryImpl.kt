package com.archetype.luxor.infra.api.repository

import com.archetype.luxor.application.repository.BookRatingRepository
import com.archetype.luxor.domain.entity.BookAttribute
import com.archetype.luxor.domain.entity.Isbn
import com.archetype.luxor.infra.api.client.Failure
import com.archetype.luxor.infra.api.client.Success
import com.archetype.luxor.infra.api.gateway.BookRatingGateway
import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import mu.KotlinLogging
import org.springframework.stereotype.Repository
import java.util.concurrent.*

@Repository
class BookRatingRepositoryImpl(
    private val gateway: BookRatingGateway,
    circuitBreakerRegistry: CircuitBreakerRegistry,
) : BookRatingRepository {
    private val circuitBreaker = circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_SERVICE_NAME)

    override suspend fun fetch(isbn: Isbn): BookAttribute {
        // open / forcedOpenのときに CallNotPermittedExceptionをThrowする
        if (circuitBreaker.tryAcquirePermission()) {
            logger.warn("circuit breaker is open")
            // circuitBreakerがOpenのときは空と同じ扱い。
            return BookAttribute(
                isbn = isbn,
                genre = "",
                rating = ""
            )
        }

        val startedAt = System.nanoTime()

        return when (val result = gateway.fetch(isbn)) {
            is Success -> {
                circuitBreaker.onSuccess(System.nanoTime() - startedAt, TimeUnit.NANOSECONDS)
                BookAttribute(
                    isbn = isbn,
                    genre = result.response.genre,
                    rating = result.response.rating,
                )
            }
            is Failure -> {
                logger.info(result.error.message)

                // 500の場合にcircuitBreakerでカウント
                if (result.isInternalServerError()) {
                    circuitBreaker.onError(System.nanoTime() - startedAt, TimeUnit.NANOSECONDS, result.error)
                }

                // 失敗したら空を返却する
                BookAttribute(
                    isbn = isbn,
                    genre = "",
                    rating = ""
                )
            }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }

        private const val CIRCUIT_BREAKER_SERVICE_NAME = "bookRatingService"
    }
}