package com.archetype.luxor.web.controller

import com.archetype.luxor.web.response.Reply
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import javax.validation.constraints.Size

@RestController
@RequestMapping(
    path = ["/hello"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@Validated
@EnableConfigurationProperties(value = [CustomLabel::class])
class HelloController(
    private val customLabel: CustomLabel
) {
    @GetMapping("{name}")
    fun hello(
        @PathVariable("name") @Size(min = 3, max = 10) name: String
    ): Mono<Reply> = mono {
        logger.debug { "name=$name" }
        Reply("Hello ${customLabel.text}, $name")
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}

@ConstructorBinding
@ConfigurationProperties("reply.label")
data class CustomLabel(
    val text: String
)