package com.archetype.luxor.infra.api.client

import com.archetype.luxor.infra.api.entity.ErrorResponse
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator
import kotlinx.coroutines.reactive.awaitFirst
import mu.KotlinLogging
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.time.Duration
import java.util.concurrent.TimeoutException

class LuxorWebClient(
    val builder: RequestBuilder
) {
    class RequestBuilder(
        val client: WebClient
    ) {
        // HTTP Method
        private var _method: HttpMethod = HttpMethod.GET
        val method get() = _method
        private fun method(method: HttpMethod) = apply { this._method = method }
        fun get() = method(HttpMethod.GET)
        fun post() = method(HttpMethod.POST)
        fun put() = method(HttpMethod.PUT)
        fun delete() = method(HttpMethod.DELETE)
        fun patch() = method(HttpMethod.PATCH)

        // URI
        private var _uri: String? = null
        val uri get() = _uri ?: throw IllegalStateException("not initialized")
        private var _uriVariables: Array<out Any> = emptyArray()
        val uriVariables get() = _uriVariables
        fun uri(uri: String, vararg uriVariables: Any) = apply {
            this._uri = uri
            this._uriVariables = uriVariables
        }

        // HTTP Header
        private var _headers: List<Pair<String, String>> = emptyList()
        val headers get() = _headers.toMap()
        fun headers(vararg headers: Pair<String, String>) =
            apply { this._headers = headers.toList() }

        // Accept
        private var _accept: MediaType = MediaType.APPLICATION_JSON
        val accept get() = _accept
        fun accept(accept: MediaType) = apply { this._accept = accept }

        // body
        private var _body: Any? = null
        val body get() = _body
        fun body(body: Any) = apply { this._body = body }

        // application/x-www-form-urlencoded の場合
        private var _form: MultiValueMap<String, String>? = null
        val form get() = _form
        fun form(vararg form: Pair<String, List<String>>) = apply {
            this._form = LinkedMultiValueMap(form.toMap())
        }

        // timeout (timeout<=0 means no timeout)
        private var _timeoutInMillis: Long = 0
        val timeoutInMillis get() = _timeoutInMillis
        fun timeoutInMillis(timeoutInMillis: Long) =
            apply { this._timeoutInMillis = timeoutInMillis }

        // retry (retry<=0 means no retry)
        private var _retry: Long = 0
        val retry get() = _retry
        fun retry(retry: Long) = apply { this._retry = retry }

        // backoff
        private var _backoffInMillis: Long = 0
        val backoffInMillis get() = _backoffInMillis
        fun backoffInMillis(backoffInMillis: Long) =
            apply { this._backoffInMillis = backoffInMillis }

        val isForm = form != null

        suspend fun <R> request(
            responseToResult: (Response<R>) -> Mono<out Result<R>>
        ): Result<R> =
            LuxorWebClient(
                builder = this
            ).request(responseToResult)
    }

    class Context(
        builder: RequestBuilder,
        val response: ClientResponse
    ) {
        val failureRequest = Failure.Request(
            uri = builder.uri,
            method = builder.method.name
        )

        val failureResponse = Failure.Response(
            statusCode = response.statusCode().value()
        )
    }

    class Response<R>(
        val context: Context
    ) {
        val statusCode: HttpStatus get() = context.response.statusCode()

        inline fun <reified R> toResult(): Mono<Result<R>> =
            context.response
                .bodyToMono(R::class.java)
                .flatMap { Mono.just(Success(it)) }

        inline fun <reified T : ErrorResponse> toFailure(): Mono<Result<R>> =
            context.response
                .bodyToMono(T::class.java)
                .flatMap {
                    Mono.just(
                        it.toFailure<R>(context.failureRequest, context.failureResponse)
                    )
                }
                .onErrorResume {
                    Mono.just(
                        Failure(
                            request = context.failureRequest,
                            response = context.failureResponse
                        )
                    )
                }
    }

    suspend fun <R> request(
        responseToResult: (Response<R>) -> Mono<out Result<R>>
    ): Result<R> {
        val request = builder.client
            .method(builder.method)
            .uri(builder.uri, *builder.uriVariables)
            .headers { it.setAll(builder.headers) }
            .accept(builder.accept)
            .let { spec ->
                when {
                    builder.isForm -> spec.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    else -> spec
                }
            }
            // リクエストBODYがあればセット
            .let { spec ->
                builder.form?.let {
                    spec.body(BodyInserters.fromFormData(it))
                } ?: builder.body?.let {
                    spec.body(BodyInserters.fromValue(it))
                } ?: spec
            }
            .exchangeToMono {
                responseToResult(Response(Context(builder, it)))
            }
            .let {
                val timeoutInMillis = builder.timeoutInMillis
                if (timeoutInMillis > 0) {
                    it.timeout(Duration.ofMillis(timeoutInMillis))
                } else {
                    it
                }
            }
            .let {
                // https://www.baeldung.com/spring-webflux-retry
                val retry = builder.retry
                val backoffInMillis = builder.backoffInMillis
                if (retry > 0) {
                    logger.debug("request${builder.uri} retry: $retry")
                    if (backoffInMillis > 0) {
                        logger.debug("request${builder.uri} backoff: $backoffInMillis")
                        it.retryWhen(
                            Retry.backoff(retry, Duration.ofMillis(backoffInMillis)).jitter(0.75)
                        )
                    } else {
                        it.retry(retry)
                    }
                } else {
                    it
                }
            }
            .onErrorResume {
                logger.warn("${builder.method} ${builder.uri} timeout/retry error: $it")
                Mono.error(TimeoutException())
            }

        return try {
            request.awaitFirst()
        } catch (e: TimeoutException) {
            Failure(request = Failure.Request(builder.method.name, builder.uri))
        }
    }

    companion object {
        val logger = KotlinLogging.logger { }
    }
}