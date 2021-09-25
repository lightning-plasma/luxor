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

        // spring の errorsに値を設定するので LocalValidatorFactoryBean を設定して messages.propertiesにmessageを用意する
        if (TARGET_PUBLISHER == target.publisher && target.price < LOWEST_PRICE) {
            errors.reject(
                "request.validation.message",
                arrayOf<Any>(TARGET_PUBLISHER, LOWEST_PRICE, target.price),
                ""
            )
        }
    }

    companion object {
        private const val TARGET_PUBLISHER = "みすず書房"
        private const val LOWEST_PRICE = 1000
    }
}