package com.archetype.luxor.web.config

import com.archetype.luxor.web.filter.ContentCachingFilter
import com.archetype.luxor.web.handler.LuxorLogHandlerInterceptor
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(LuxorLogHandlerInterceptor())
            .addPathPatterns("/book/**")
    }

    @Bean
    fun contentsCachingFilter(): FilterRegistrationBean<ContentCachingFilter> {
        val filterRegistrationBean = FilterRegistrationBean<ContentCachingFilter>()
        filterRegistrationBean.filter = ContentCachingFilter()
        filterRegistrationBean.order = 1

        return filterRegistrationBean
    }
}