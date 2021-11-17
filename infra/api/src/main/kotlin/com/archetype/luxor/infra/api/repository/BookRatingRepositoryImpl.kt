package com.archetype.luxor.infra.api.repository

import com.archetype.luxor.application.repository.BookRatingRepository
import com.archetype.luxor.domain.entity.BookAttribute
import com.archetype.luxor.domain.entity.Isbn
import com.archetype.luxor.infra.api.client.Failure
import com.archetype.luxor.infra.api.client.Success
import com.archetype.luxor.infra.api.entity.BookRatingXmlResponse
import com.archetype.luxor.infra.api.gateway.BookRatingGateway
import mu.KotlinLogging
import org.springframework.stereotype.Repository
import javax.xml.bind.JAXBContext

@Repository
class BookRatingRepositoryImpl(
    private val gateway: BookRatingGateway
) : BookRatingRepository {
    override fun fetch(isbn: Isbn): BookAttribute {
        return when(val result = gateway.fetch(isbn)) {
            is Success -> {
                println(result.response)
                BookAttribute(
                    isbn = isbn,
                    genre = "",
                    rating = ""
//                    genre = result.response.response.genre,
//                    rating = result.response.response.rating,
                )
            }
            is Failure -> {
                // 失敗したら空を返却する
                logger.info(result.error.message)
                BookAttribute(
                    isbn = isbn,
                    genre = "",
                    rating = ""
                )
            }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}