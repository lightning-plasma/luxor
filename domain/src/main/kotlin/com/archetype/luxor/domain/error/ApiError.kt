package com.archetype.luxor.domain.error

class ApiError(
    private val statusCode: Int,
    private val api: String,
    message: String
) : Exception(message) {
    override fun toString() =
        "ApiError\tapi=$api\tstatusCode=$statusCode\tmessage=$message"
}