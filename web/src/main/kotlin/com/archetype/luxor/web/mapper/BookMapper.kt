package com.archetype.luxor.web.mapper

import com.archetype.luxor.domain.entity.Book
import com.archetype.luxor.web.response.BookResponse
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.core.convert.converter.Converter

@Mapper(componentModel = "spring")
interface BookMapper : Converter<Book, BookResponse> {

    @Mapping(
        target = "isbn", expression = "java(book.getIsbnAsString())"
    )
    override fun convert(book: Book): BookResponse
}