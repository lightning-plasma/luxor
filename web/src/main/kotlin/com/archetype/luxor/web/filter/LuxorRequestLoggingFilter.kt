package com.archetype.luxor.web.filter

import mu.KotlinLogging
import org.springframework.web.filter.AbstractRequestLoggingFilter
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest

class LuxorRequestLoggingFilter : AbstractRequestLoggingFilter() {

    override fun beforeRequest(request: HttpServletRequest, message: String) {
        myLogger.debug(message)
        request.setAttribute(attributeKey, System.nanoTime())
    }

    override fun afterRequest(request: HttpServletRequest, message: String) {
        val beforeNanoSec = request.getAttribute(attributeKey)
        if (beforeNanoSec is Long) {
            val elapsed = System.nanoTime() - beforeNanoSec
            myLogger.debug("Elapsed {} ms {}", TimeUnit.NANOSECONDS.toMillis(elapsed), message)
        } else {
            myLogger.debug(message)
        }
    }

    companion object {
        private val myLogger = KotlinLogging.logger { }
        private val attributeKey = LuxorRequestLoggingFilter::class::java.name
    }
}