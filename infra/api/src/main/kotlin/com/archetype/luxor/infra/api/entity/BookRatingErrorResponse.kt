package com.archetype.luxor.infra.api.entity

import com.archetype.luxor.infra.api.client.Failure

data class BookRatingErrorResponse(
    val status: Int,
    val message: String
) : ErrorResponse {
    override fun <R> toFailure(
        request: Failure.Request,
        response: Failure.Response
    ): com.archetype.luxor.infra.api.client.Result<R> =
        Failure(
            message = message,
            request = request,
            response = response
        )
}