package com.archetype.luxor.infra.advice

import com.archetype.luxor.domain.error.ResourceAccessError
import mu.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Component

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DataAccessExceptionAdvice(val value: String)

@Aspect
@Component
class DataAccessExceptionInterceptor {
    @AfterThrowing(
        value = "@annotation(dataAccessExceptionAdvice)",
        throwing = "ex"
    )
    fun afterThrowingDataAccessException(
        joinPoint: JoinPoint,
        dataAccessExceptionAdvice: DataAccessExceptionAdvice,
        ex: DataAccessException
    ) {
        logger.info(ex.stackTraceToString())

        throw ResourceAccessError(ex, "${dataAccessExceptionAdvice.value}でエラーが発生しました")
    }

    companion object {
        private val logger = KotlinLogging.logger {  }
    }
}