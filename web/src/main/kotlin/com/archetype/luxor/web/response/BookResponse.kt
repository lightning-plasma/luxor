package com.archetype.luxor.web.response

import com.fasterxml.jackson.annotation.JsonInclude

// @JsonInclude(JsonInclude.Include.NON_NULL)
data class BookResponse(
    val isbn: String,

    val title: String,

    val author: String,

    val publisher: String,

    val price: Int,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val genre: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val rating: String?
)
