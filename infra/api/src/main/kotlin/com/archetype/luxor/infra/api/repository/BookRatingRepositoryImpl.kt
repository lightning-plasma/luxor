package com.archetype.luxor.infra.api.repository

import com.archetype.luxor.application.repository.BookRatingRepository
import com.archetype.luxor.domain.entity.BookAttribute
import com.archetype.luxor.domain.entity.Isbn
import com.archetype.luxor.infra.api.gateway.BookRatingGateway
import org.springframework.stereotype.Repository

@Repository
class BookRatingRepositoryImpl(
    private val gateway: BookRatingGateway
) : BookRatingRepository {
    override fun fetch(isbn: Isbn): BookAttribute {
        val response = gateway.fetch(isbn)

        return BookAttribute(
            isbn = isbn,
            genre = response.genre,
            rating = response.rating
        )
    }
}