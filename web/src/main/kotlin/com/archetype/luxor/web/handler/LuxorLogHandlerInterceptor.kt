package com.archetype.luxor.web.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import org.springframework.web.util.WebUtils
import java.io.IOException
import java.lang.Exception
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LuxorLogHandlerInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        request.setAttribute(startTimeAttributeKey, System.nanoTime())
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        traceRequest(request, requestType(handler))
        traceResponse(response, responseType(handler))

        // 所要時間をlogging
        val startNanoSec = request.getAttribute(startTimeAttributeKey)
        if (startNanoSec is Long) {
            val elapsed = System.nanoTime() - startNanoSec
            logger.debug("Elapsed {} ms", TimeUnit.NANOSECONDS.toMillis(elapsed))
        }
    }

    private fun traceRequest(request: HttpServletRequest, requestType: Class<*>?) {
        // contentCachingFilterで変換していたContentCachingWrapperに戻す
        val requestWrapper = WebUtils.getNativeRequest(
            request,
            ContentCachingRequestWrapper::class.java
        ) ?: return

        // byteArrayをJsonに変換する
        val body = body(requestWrapper.contentAsByteArray, requestType)

        logger.debug("Request URI: {}{}", requestWrapper.requestURL, queryString(requestWrapper))
        logger.debug("Request Method: {}", requestWrapper.method)
        logger.debug("Request Headers: {}", requestHeaders(requestWrapper))
        logger.debug("Request Body: {}", body)
    }

    private fun traceResponse(response: HttpServletResponse, responseType: Class<*>?) {
        val responseWrapper = WebUtils.getNativeResponse(
            response,
            ContentCachingResponseWrapper::class.java
        ) ?: return

        // status: 200 - 399 = ok
        val body =
            body(responseWrapper.contentAsByteArray, responseType, responseWrapper.status < 400)

        logger.debug("Response Headers: {}", responseHeaders(responseWrapper))
        logger.debug("Response Status Code: {}", responseWrapper.status)
        logger.debug("Response Body: {}", body)
    }

    private fun body(payload: ByteArray, type: Class<*>?, ok: Boolean = true): String {
        if (type == null || !ok) {
            return String(payload, Charset.defaultCharset())
        }

        return try {
            mapper.writeValueAsString(
                mapper.readValue(payload, type)
            )
        } catch (e: IOException) {
            logger.info("request log output error. {}", e.stackTraceToString())
            String(payload, Charset.forName("UTF-8"))
        }
    }

    private fun queryString(request: HttpServletRequest): String =
        if (request.queryString.isNullOrEmpty()) {
            ""
        } else {
            URLDecoder.decode(request.queryString, Charset.forName("UTF-8"))
        }

    private fun requestHeaders(request: HttpServletRequest): Map<String, String> {
        // kotlinのmapOfはLinkedHashMap
        val map = mutableMapOf<String, String>()
        val headerNames = request.headerNames ?: return map

        while (headerNames.hasMoreElements()) {
            val key = headerNames.nextElement()
            val value = request.getHeader(key)
            map[key] = value
        }

        return map
    }

    private fun responseHeaders(response: HttpServletResponse): Map<String, String> {
        val headerNames = response.headerNames ?: return emptyMap()

        val map = mutableMapOf<String, String>()
        headerNames.forEach {
            map[it] = response.getHeader(it)
        }

        return map
    }

    // star-projection
    // https://stackoverflow.com/questions/40138923/difference-between-and-any-in-kotlin-generics
    private fun requestType(handler: Any): Class<*>? {
        return if (handler is HandlerMethod) {
            handler.methodParameters.firstOrNull {
                it.hasParameterAnnotation(RequestBody::class.java)
            }?.parameterType
        } else {
            null
        }
    }

    private fun responseType(handler: Any): Class<*>? {
        return if (handler is HandlerMethod) {
            handler.returnType.parameterType
        } else null
    }

    companion object {
        private val logger = KotlinLogging.logger { }
        private val mapper = jacksonObjectMapper()
        private val startTimeAttributeKey = LuxorLogHandlerInterceptor::class.java.name + "st"
    }
}
