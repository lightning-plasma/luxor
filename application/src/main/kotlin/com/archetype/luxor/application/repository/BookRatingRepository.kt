package com.archetype.luxor.application.repository

import com.archetype.luxor.domain.entity.BookAttribute
import com.archetype.luxor.domain.entity.Isbn

interface BookRatingRepository {
    suspend fun fetch(isbn: Isbn): BookAttribute
}