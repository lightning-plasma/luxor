package com.archetype.luxor.infra.api.entity

import com.archetype.luxor.infra.api.client.Failure
import com.archetype.luxor.infra.api.client.Result

interface ErrorResponse {
    fun <R> toFailure(
        request: Failure.Request,
        response: Failure.Response
    ): Result<R>
}