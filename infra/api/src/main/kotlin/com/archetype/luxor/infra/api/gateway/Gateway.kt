package com.archetype.luxor.infra.api.gateway

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Component

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class Gateway(
    @get:AliasFor(annotation = Component::class)
    val value: String = ""
)