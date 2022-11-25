package com.archetype.luxor.infra.persistence.mapper

import com.archetype.luxor.infra.persistence.entity.Book
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Mapper
interface BookMapper {
    @Select("SELECT * FROM main.book WHERE isbn = #{isbn}")
    fun findByIsbn(isbn: String): Mono<Book>

    @Select("SELECT * FROM main.book")
    fun findAll(): Flux<Book>

    @Insert("""
        INSERT INTO main.book (
            isbn
          , title
          , author
          , publisher
          , price
          , created_at
          , updated_at
        ) VALUES (
            #{isbn}
          , #{title}
          , #{author}
          , #{publisher}
          , #{price}
          , #{createdAt}
          , #{updatedAt}
        )
    """)
    fun register(book: Book): Mono<Int>

    @Update("""
        UPDATE main.book
        SET
            title = #{title}
          , publisher = #{publisher}
          , price = #{price}
          , updated_at = #{updatedAt}
        WHERE isbn = #{isbn}
    """)
    fun update(book: Book): Mono<Int>
}