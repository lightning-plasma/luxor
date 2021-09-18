package com.archetype.luxor.web.filter

import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ContentCachingFilter : OncePerRequestFilter() {
    // HttpServletRequest, HttpServletResponseをContentCachingWrapperに変換する
    // contentAsByteArrayがcacheされるので、afterCompletionでLog出力に利用できるようになる
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response)
        }

        val requestWrapper = requestWrapper(request)
        val responseWrapper = responseWrapper(response)

        try {
            filterChain.doFilter(requestWrapper, responseWrapper)
        } finally {
            responseWrapper.copyBodyToResponse()
        }
    }
}

private fun requestWrapper(request: HttpServletRequest) =
    if (request is ContentCachingRequestWrapper) {
        request
    } else {
        ContentCachingRequestWrapper(request)
    }

private fun responseWrapper(response: HttpServletResponse) =
    if (response is ContentCachingResponseWrapper) {
        response
    } else {
        ContentCachingResponseWrapper(response)
    }
