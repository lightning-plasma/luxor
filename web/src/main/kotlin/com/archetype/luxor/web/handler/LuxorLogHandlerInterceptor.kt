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
import java.nio.charset.Charset
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LuxorLogHandlerInterceptor : HandlerInterceptor {
    private val mapper = jacksonObjectMapper()

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        traceRequest(request, requestType(handler))
        traceResponse(response, responseType(handler))
    }

    private fun traceRequest(request: HttpServletRequest, requestType: Class<*>?) {
        // contentCachingFilterで変換していたContentCachingWrapperに戻す
        val requestWrapper = WebUtils.getNativeRequest(
            request,
            ContentCachingRequestWrapper::class.java
        ) ?: return

        // byteArrayをJsonに変換する
        val body = body(requestWrapper.contentAsByteArray, requestType)

        if (body.isNotEmpty()) {
            logger.debug("Request: {}", body)
        }
    }

    private fun traceResponse(response: HttpServletResponse, responseType: Class<*>?) {
        val responseWrapper = WebUtils.getNativeResponse(
            response,
            ContentCachingResponseWrapper::class.java
        ) ?: return

        val body = body(responseWrapper.contentAsByteArray, responseType)

        if (body.isNotEmpty()) {
            logger.debug("Response: {}", body)
        }

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
            String(payload, Charset.defaultCharset())
        }
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
    }
}
