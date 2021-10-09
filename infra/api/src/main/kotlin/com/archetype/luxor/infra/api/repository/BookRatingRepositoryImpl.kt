package com.archetype.luxor.infra.api.repository

import com.archetype.luxor.application.repository.BookRatingRepository
import com.archetype.luxor.domain.entity.BookAttribute
import com.archetype.luxor.domain.entity.Isbn
import com.archetype.luxor.infra.api.client.Failure
import com.archetype.luxor.infra.api.client.Success
import com.archetype.luxor.infra.api.gateway.BookRatingGateway
import org.springframework.stereotype.Repository

@Repository
class BookRatingRepositoryImpl(
    private val gateway: BookRatingGateway
) : BookRatingRepository {
    override fun fetch(isbn: Isbn): BookAttribute {
        return when(val result = gateway.fetch(isbn)) {
            is Success -> BookAttribute(
                isbn = isbn,
                result.response.rating,
                result.response.rating,
            )
            is Failure -> throw result.error
        }
    }
}