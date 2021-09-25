package com.archetype.luxor.web.validator

import com.archetype.luxor.web.request.BookRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import javax.validation.Validation

class BookValidatorTests {
    private val validator = Validation.buildDefaultValidatorFactory().validator

    @DisplayName("みすず書房で1000円未満はNG")
    @Test
    fun misuzuAndLowPrice() {
        val request = BookRequest(
            isbn = "9784839961749",
            title = "Test Book",
            author = "Test Author",
            publisher = "みすず書房",
            price = 999,
        )

        val violations = validator.validate(request)

        // messageの中身まではtestしていません
        Assertions.assertEquals(1, violations.size)
    }

    @Test
    @DisplayName("みすず書房で1000円以上はOK")
    fun misuzuAndHighPrice() {
        val request = BookRequest(
            isbn = "9784839961749",
            title = "Test Book",
            author = "Test Author",
            publisher = "みすず書房",
            price = 1000,
        )

        val violations = validator.validate(request)

        Assertions.assertEquals(0, violations.size)
    }

    @Test
    @DisplayName("その他出版社は何円でもOK")
    fun anyPublisherAndLowPrice() {
        val request = BookRequest(
            isbn = "9784839961749",
            title = "Test Book",
            author = "Test Author",
            publisher = "マイナビ出版",
            price = 100,
        )

        val violations = validator.validate(request)

        Assertions.assertEquals(0, violations.size)
    }
}