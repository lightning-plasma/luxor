package com.archetype.luxor.infra.api.client

import com.archetype.luxor.domain.error.ApiError

sealed class Result<T>

class Success<T>(val response: T) : Result<T>()

class Failure<Nothing>(
    message: String? = "error",
    private val request: Request,
    // timeoutエラーの時はresponseがない
    private val response: Response? = null
) : Result<Nothing>() {
    class Request(
        val method: String,
        val uri: String
    )

    class Response(
        val statusCode: Int
    )

    private val message = message ?: ""
    val error: Throwable by lazy {
        ApiError(
            statusCode = response?.statusCode ?: 408,
            api = "${request.method} ${request.uri}",
            message = this.message
        )
    }

    fun isUnauthorized() = response?.statusCode == 401
    fun isNotFound() = response?.statusCode == 404
    fun isInternalServerError() = response?.statusCode == 500
    fun isServiceUnavailable() = response?.statusCode == 503
}