package com.archetype.luxor.infra.s3.gateway

import com.archetype.luxor.domain.entity.S3File
import mu.KotlinLogging
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload
import software.amazon.awssdk.services.s3.model.CompletedPart
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.UploadPartRequest
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.file.Path
import kotlin.math.ceil
import kotlin.math.min

open class S3Gateway(
    private val s3Client: S3Client,
    private val bucket: String,
) {
    fun upload(
        filePath: Path,
        key: String,
        isUsableMultipart: Boolean
    ): S3File {
        val size = filePath.toFile().length()

        return if (size > MIN_MULTIPART_SIZE) {
            if (isUsableMultipart) {
                uploadByMultipart(filePath = filePath, bucket = bucket)
            } else {
                logger.warn(
                    "file sizeが大きいのでUploadに時間がかかる可能性があります fileName=${filePath.fileName} size=$size"
                )
                uploadFile(filePath = filePath, key = key)
            }
        } else {
            uploadFile(filePath = filePath, key = key)
        }
    }

    private fun uploadFile(
        filePath: Path,
        key: String
    ): S3File {
        val request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()

        s3Client.putObject(request, RequestBody.fromFile(filePath))
        return S3File(bucket = bucket, key = key)
    }

    /**
     * Multipartでサイズの大きなファイルを高速アップロードします
     * 5MB 以下のファイルでは利用できません
     * NOTE: bucketにAbortIncompleteMultipartUpload ライフサイクルルールを有効にしたほうがよい
     */
    private fun uploadByMultipart(filePath: Path, bucket: String): S3File {
        val size = filePath.toFile().length()
        val key = filePath.fileName.toString()

        val randomAccessFile = RandomAccessFile(filePath.toFile(), "r")
        val channel = randomAccessFile.channel

        val request = CreateMultipartUploadRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()

        val response = s3Client.createMultipartUpload(request)

        val uploadId = response.uploadId()

        val parts = mutableListOf<CompletedPart>()

        // multipart requestの分割数を計算しています
        val multipart = ceil((size / MIN_MULTIPART_SIZE).toDouble()).toInt()

        for (i in 0..multipart) {
            val filePosition = MIN_MULTIPART_SIZE * i
            val contentLength = min(MIN_MULTIPART_SIZE, size - filePosition)

            // fileSizeがMIN_MULTIPART_SIZEの倍数だった場合に0になるケースがある (と思う) ので安全装置としてbreakを用意
            if (contentLength <= 0) {
                break
            }

            val partNumber = i + 1
            val byteBuffer = ByteBuffer.wrap(ByteArray(contentLength.toInt()))
            channel.read(byteBuffer, filePosition)

            val req = UploadPartRequest.builder()
                .bucket(bucket)
                .key(key)
                .uploadId(uploadId)
                .contentLength(contentLength)
                .partNumber(partNumber)
                .build()

            val etag = s3Client.uploadPart(
                req,
                RequestBody.fromByteBuffer(byteBuffer)
            ).eTag()

            val part = CompletedPart.builder()
                .partNumber(partNumber)
                .eTag(etag)
                .build()
            parts += part
        }

        val completedMultipartUpload = CompletedMultipartUpload.builder()
            .parts(parts)
            .build()

        val completedMultipartUploadRequest = CompleteMultipartUploadRequest.builder()
            .bucket(bucket)
            .key(key)
            .uploadId(uploadId)
            .multipartUpload(completedMultipartUpload)
            .build()

        s3Client.completeMultipartUpload(completedMultipartUploadRequest)
        return S3File(bucket = bucket, key = key)
    }

    companion object {
        private const val MB: Long = 1024L * 1024L
        private const val MIN_MULTIPART_SIZE = 5L * MB

        private val logger = KotlinLogging.logger { }

    }
}