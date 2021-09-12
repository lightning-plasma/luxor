package com.archetype.luxor.domain.entity

import com.google.common.annotations.VisibleForTesting

@JvmInline
value class Isbn(
    private val value: String,
) {
    init {
        require(isValid()) {
            throw IllegalArgumentException("Invalid Isbn value=$value")
        }
    }

    @VisibleForTesting
    fun isValid(): Boolean =
        regex.matches(value) && value.last().toString() == checkDigit()

    fun asString(): String = value

    // https://isbn-information.com/check-digit-for-the-13-digit-isbn.html
    private fun checkDigit(): String {
        var sum = 0
        value.dropLast(1).forEachIndexed { index, c ->
            val num = Character.getNumericValue(c)
            sum += if (index % 2 == 0) {
                num
            } else {
                num * 3
            }
        }

        val mod = sum % 10

        return if (mod == 0) {
            "0"
        } else {
            (10 - mod).toString()
        }
    }

    companion object {
        private val regex = Regex(pattern = "^[0-9]{13}$")
    }
}