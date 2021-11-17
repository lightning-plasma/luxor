package com.archetype.luxor.application.repository

import com.archetype.luxor.domain.entity.Book
import com.archetype.luxor.domain.entity.S3File

interface BookFileRepository {
    suspend fun upload(books: List<Book>): S3File
}