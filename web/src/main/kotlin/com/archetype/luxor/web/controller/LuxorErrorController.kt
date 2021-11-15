package com.archetype.luxor.web.controller

import com.archetype.luxor.web.response.ErrorResponse
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.web.bind.annotation.GetMapping

// errorを上書きしたい場合
// @RestController
private class LuxorErrorController : ErrorController {

    @GetMapping("/error")
    fun handleError(): ErrorResponse = ErrorResponse("error")
}
