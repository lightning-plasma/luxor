package com.archetype.luxor.web.filter

import mu.KotlinLogging
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpMethod
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import reactor.core.publisher.Flux
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels

class LoggingRequestDecorator internal constructor(
    delegate: ServerHttpRequest
) : ServerHttpRequestDecorator(delegate) {

    private val body: Flux<DataBuffer>?

    override fun getBody(): Flux<DataBuffer> = body!!

    init {
        if (logger.isDebugEnabled) {
            val path = delegate.uri.path
            val query = if (delegate.uri.query.isNullOrEmpty()) "" else "?${delegate.uri.query}"
            val method = (delegate.method ?: HttpMethod.GET).name
            val headers = delegate.headers.toString()

            logger.debug(
                "{} {}\n {}",
                method,
                path + query,
                headers
            )
            body = super.getBody().doOnNext { buffer: DataBuffer ->
                val bodyStream = ByteArrayOutputStream()
                Channels.newChannel(bodyStream).write(buffer.asByteBuffer().asReadOnlyBuffer())
                logger.debug("{}: {}", "request", String(bodyStream.toByteArray()))
            }
        } else {
            body = super.getBody()
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
