package com.archetype.luxor.infra.persistence.mapper

import com.archetype.luxor.infra.persistence.entity.Book
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import org.springframework.stereotype.Repository

@Repository
@Mapper
interface BookMapper {
    @Select(
        """
        SELECT
           isbn,
           title,
           author,
           publisher,
           price,
           created_at,
           updated_at
        FROM
          main.book
        """
    )
    fun findAll(): List<Book>

    @Select(
        """
        SELECT
           isbn,
           title,
           author,
           publisher,
           price,
           created_at,
           updated_at
        FROM
          main.book
        WHERE
          isbn = #{isbn}
        """
    )
    fun find(
        @Param("isbn") isbn: String
    ): Book?

    @Insert(
        """
        INSERT INTO main.book (
            isbn,
            title,
            author,
            publisher,
            price,
            created_at,
            updated_at
        ) VALUES (
            #{book.isbn},
            #{book.title},
            #{book.author},
            #{book.publisher},
            #{book.price},
            #{book.createdAt},
            #{book.updatedAt}
        )
        """
    )
    fun insert(
        @Param("book") book: Book
    )

    @Update(
        """
        UPDATE
          main.book
        SET
          title = #{book.title},
          author = #{book.author},
          price = #{book.price},
          publisher = #{book.publisher},
          updated_at = #{book.updatedAt}
        WHERE
          isbn = #{book.isbn}
        """
    )
    fun update(
        @Param("book") book: Book
    ): Int
}