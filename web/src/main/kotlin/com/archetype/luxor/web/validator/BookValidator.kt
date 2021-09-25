package com.archetype.luxor.web.validator

import com.archetype.luxor.web.request.BookRequest
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class BookValidator : Validator {
    override fun supports(clazz: Class<*>): Boolean =
        BookRequest::class.java.isAssignableFrom(clazz)


    override fun validate(target: Any, errors: Errors) {
        if (target !is BookRequest) return

        // messages.properties を使う方法がいまいちわからん...
        if (TARGET_PUBLISHER == target.publisher && target.price < LOWEST_PRICE) {
            errors.reject(
                "BookValidator.message",
                ERROR_MESSAGE.format(TARGET_PUBLISHER, LOWEST_PRICE, target.price)
            )
        }
    }

    companion object {
        private const val TARGET_PUBLISHER = "みすず書房"
        private const val LOWEST_PRICE = 1000
        private const val ERROR_MESSAGE = "%sの書籍は%d以上の本を登録できます。Requested price=%d"
    }
}