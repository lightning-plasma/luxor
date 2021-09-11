package com.archetype.luxor.web.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/luxor")
class LuxorController {
    @GetMapping
    fun hello(): String {
        return "hello"
    }
}