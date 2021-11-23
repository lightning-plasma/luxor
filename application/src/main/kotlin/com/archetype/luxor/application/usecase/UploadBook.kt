package com.archetype.luxor.application.usecase

import com.archetype.luxor.application.repository.BookFileRepository
import com.archetype.luxor.application.repository.BookRepository
import com.archetype.luxor.domain.entity.S3File
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

@Component
class UploadBook(
    private val bookRepository: BookRepository,
    private val bookFileRepository: BookFileRepository
) {
    fun invoke(): S3File = runBlocking {
        val books = bookRepository.fetchAll()

        bookFileRepository.upload(books)
    }
}
