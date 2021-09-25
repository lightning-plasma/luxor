package com.archetype.luxor.web.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.core.io.Resource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

// https://www.baeldung.com/spring-custom-validation-message-source
@Configuration
class ApplicationConfig {
    @Bean
    fun messageSource(
        @Value("classpath*:messages/*.properties") resources: Array<Resource>,
    ): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setDefaultEncoding("UTF-8")

        val basenameList = resources.map {
            "classpath:/messages/" + it.filename?.replace(".properties", "")
        }

        // resources/messages配下のpropertiesをmessageSourceにセットする
        messageSource.setBasenames(*basenameList.toTypedArray())

        return messageSource
    }

    // messages.properties を validatorで利用する
    @Bean
    fun localValidatorFactoryBean(
        messageSource: MessageSource
    ): LocalValidatorFactoryBean {
        val factory = LocalValidatorFactoryBean()
        factory.setValidationMessageSource(messageSource)

        return factory
    }
}