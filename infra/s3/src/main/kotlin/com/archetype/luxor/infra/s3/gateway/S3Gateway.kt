package com.archetype.luxor.infra.s3.gateway

import com.archetype.luxor.domain.entity.S3File
import kotlinx.coroutines.coroutineScope
import mu.KotlinLogging
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.file.Path

open class S3Gateway(
    private val s3AsyncClient: S3AsyncClient,
    private val bucket: String,
) {
    suspend fun exists(
        filePath: Path,
    ): Boolean = coroutineScope {
        val req = ListObjectsV2Request.builder()
            .bucket(bucket)
            .prefix(filePath.toString())
            .build()


        val result = s3AsyncClient.listObjectsV2(req).await()

        result.contents().size > 0
    }

    fun upload(
        filePath: Path,
        key: String,
    ): S3File {
        uploadFile(filePath = filePath, key = key)
        return S3File(bucket, key)
    }

    private fun uploadFile(
        filePath: Path,
        key: String
    ) {
        val request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()

        s3AsyncClient.putObject(request, filePath).join()
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}