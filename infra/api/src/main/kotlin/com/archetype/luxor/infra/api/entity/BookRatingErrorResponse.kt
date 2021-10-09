package com.archetype.luxor.infra.api.entity

import com.archetype.luxor.infra.api.client.Failure

data class BookRatingErrorResponse(
    val resultSet: ResultSet? = null
) : ErrorResponse {

    data class ResultSet(
        val listResult: List<Result>
    )

    data class Result(
        val status: Int,
        val message: String
    )

    override fun <R> toFailure(request: Failure.Request, response: Failure.Response):
        com.archetype.luxor.infra.api.client.Result<R> =
        this.resultSet?.listResult?.first()?.let {
            Failure(
                message = it.message,
                request = request,
                response = response
            )
        } ?: Failure(request = request, response = response)
}