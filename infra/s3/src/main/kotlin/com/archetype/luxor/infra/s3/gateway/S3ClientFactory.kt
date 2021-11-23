package com.archetype.luxor.infra.s3.gateway

import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import java.net.URI

open class S3ClientFactory(
    private val host: String,
    private val port: String,
) {
    internal fun create(): S3AsyncClient =
        when {
            host.isNotEmpty() -> createEndpointOverrideClient()
            else -> createDefaultClient()
        }

    private fun createDefaultClient(): S3AsyncClient =
        S3AsyncClient.builder()
            .region(Region.AP_NORTHEAST_1)
            .build()

    private fun createEndpointOverrideClient(): S3AsyncClient =
        S3AsyncClient.builder()
            .region(Region.AP_NORTHEAST_1)
            .endpointOverride(URI("$host:$port"))
            .build()
}
