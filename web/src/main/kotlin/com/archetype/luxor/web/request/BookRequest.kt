package com.archetype.luxor.web.request

import com.archetype.luxor.web.validator.BookConstraint
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.jetbrains.annotations.NotNull
import javax.validation.constraints.Size

@JsonIgnoreProperties(ignoreUnknown = true)
@BookConstraint
data class BookRequest(
    @get:NotNull
    @get:Size(min = 13, max = 13)
    val isbn: String,

    @get:NotNull
    @get:Size(min = 5, max = 100)
    val title: String,

    @get:NotNull
    @get:Size(min = 2, max = 100)
    val author: String,

    @get:NotNull
    @get:Size(min = 2, max = 100)
    val publisher: String,

    @get:NotNull
    val price: Int
)
