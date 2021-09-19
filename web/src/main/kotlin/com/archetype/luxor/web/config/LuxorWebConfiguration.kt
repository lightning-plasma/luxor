package com.archetype.luxor.web.config

import com.archetype.luxor.web.filter.LuxorRequestLoggingFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter

@Configuration(proxyBeanMethods = false)
class LuxorWebConfiguration {

    // https://www.baeldung.com/spring-reading-httpservletrequest-multiple-times
    // org.springframework.web.filter.CommonsRequestLoggingFilter: DEBUG に設定する
    // @Bean
    fun commonsRequestLoggingFilter(): CommonsRequestLoggingFilter {
        val filter = CommonsRequestLoggingFilter()
        filter.setIncludeClientInfo(true)
        filter.setIncludePayload(true)
        filter.setMaxPayloadLength(10000)
        filter.setIncludeHeaders(true)
        filter.setAfterMessagePrefix("REQUEST : ")

        return filter
    }

    @Bean
    fun luxorRequestLoggingFilter(): LuxorRequestLoggingFilter {
        val filter = LuxorRequestLoggingFilter()
        filter.setIncludeClientInfo(true)
        filter.setIncludePayload(true)
        filter.setMaxPayloadLength(10000)
        filter.setIncludeHeaders(true)
        filter.setAfterMessagePrefix("REQUEST : ")

        return filter
    }
}