package com.archetype.luxor.infra.s3.gateway

import org.springframework.stereotype.Component

@Component
class BookGateway(
    localS3ClientFactory: LocalS3ClientFactory
) : S3Gateway(
    s3AsyncClient = localS3ClientFactory.create(),
    bucket = BUCKET
) {
    companion object {
        private const val BUCKET = "test-bucket"
    }
}