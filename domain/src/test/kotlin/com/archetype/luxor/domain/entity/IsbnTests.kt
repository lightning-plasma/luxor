package com.archetype.luxor.domain.entity

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class IsbnTests {
    @Test
    @DisplayName("正しいISBNの場合初期化に成功する")
    fun isValid() {
        Assertions.assertDoesNotThrow { Isbn("9784422114361") }
        Assertions.assertDoesNotThrow { Isbn("9784423114360") }
        Assertions.assertDoesNotThrow { Isbn("9784622012184") }
    }

    @Test
    @DisplayName("不正ISBNの場合IllegalArgumentExceptionを投げる")
    fun isInvalid() {
        Assertions.assertThrows(IllegalArgumentException::class.java, {
            Isbn("978442211436")
        }, "チェックディジット計算エラー")

        Assertions.assertThrows(IllegalArgumentException::class.java, {
            Isbn("978442211436X")
        }, "フォーマットエラー (数字以外)")

        Assertions.assertThrows(IllegalArgumentException::class.java, {
            Isbn("978442211436")
        }, "フォーマットエラー (13桁以外)")
    }
}