package com.archetype.luxor.web.validator

import com.archetype.luxor.web.request.BookRequest
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [BookValidator::class])
annotation class BookConstraint(
    val message: String = "{request.validation.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class BookValidator : ConstraintValidator<BookConstraint, BookRequest> {
    override fun isValid(value: BookRequest, context: ConstraintValidatorContext): Boolean {
        if (TARGET_PUBLISHER == value.publisher && value.price < LOWEST_PRICE) {
            val message = ERROR_MESSAGE.format(
                TARGET_PUBLISHER,
                LOWEST_PRICE,
                value.price
            )
            context.unwrap(HibernateConstraintValidatorContext::class.java)
                .addExpressionVariable("errorMessage", message)

            return false
        }

        return true
    }

    companion object {
        private const val TARGET_PUBLISHER = "みすず書房"
        private const val LOWEST_PRICE = 1000

        private const val ERROR_MESSAGE = "%sの書籍は%d円以上の本だけ設定できます。Requested Price: %d"
    }
}