package com.archetype.luxor.web.filter

import mu.KotlinLogging
import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels

class LoggingResponseDecorator internal constructor(
    delegate: ServerHttpResponse
) : ServerHttpResponseDecorator(delegate) {
    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
        return super.writeWith(
            Flux.from(body)
                .doOnNext { buffer: DataBuffer ->
                    if (logger.isDebugEnabled) {
                        val bodyStream = ByteArrayOutputStream()
                        Channels
                            .newChannel(bodyStream)
                            .write(buffer.asByteBuffer().asReadOnlyBuffer())

                        logger.debug("{}: {}", "response", String(bodyStream.toByteArray()))
                    }
                })
    }

    init {
        if (logger.isDebugEnabled) {
            logger.debug("{}", delegate.headers.toString())
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}