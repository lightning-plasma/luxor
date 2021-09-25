package com.archetype.luxor.web.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration(proxyBeanMethods = false)
class ValidatorConfig {

    @Bean
    fun localValidatorFactoryBean(
        messageSource: MessageSource
    ):LocalValidatorFactoryBean  {
        val factory = LocalValidatorFactoryBean()
        factory.setValidationMessageSource(messageSource)

        return factory
    }
}