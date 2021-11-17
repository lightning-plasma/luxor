package com.archetype.luxor.infra.s3.repository

import com.archetype.luxor.application.repository.BookFileRepository
import com.archetype.luxor.domain.entity.Book
import com.archetype.luxor.domain.entity.S3File
import com.archetype.luxor.infra.s3.entity.BookFileRow
import com.archetype.luxor.infra.s3.gateway.BookGateway
import com.archetype.luxor.infra.s3.utils.FileUtility
import mu.KotlinLogging
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository
class BookFileRepositoryImpl(
    private val gateway: BookGateway
) : BookFileRepository {
    override suspend fun upload(books: List<Book>): S3File {
        val key = FILENAME_FORMAT.format(
            DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())
        )

        val bookRows = books.map {
            BookFileRow(
                isbn = it.isbn.asString(),
                title = it.title,
                author = it.author,
                publisher = it.publisher,
                price = it.price
            )
        }

        return try {
            val path = FileUtility.createTemporaryCsvFile(bookRows, FILE_SUFFIX)

            gateway.upload(path, key, true)
        } catch (th: Throwable) {
            logger.info { th }

            throw IllegalStateException("s3 upload failure. message=${th.message}" )
        }
    }

    companion object {
        private const val FILE_SUFFIX = ".csv"
        private const val FILENAME_FORMAT = "boos-%s"

        private val logger = KotlinLogging.logger { }
    }
}