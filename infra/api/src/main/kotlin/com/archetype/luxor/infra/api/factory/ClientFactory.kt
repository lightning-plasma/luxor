package com.archetype.luxor.infra.api.factory

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

abstract class ClientFactory(
    builder: WebClient.Builder
) {
    protected val baseBuilder: WebClient.Builder =
        builder
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
}