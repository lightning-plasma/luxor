package com.archetype.luxor.web.controller

import com.archetype.luxor.web.entity.Reply
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    path = ["/luxor"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class LuxorController {
    @GetMapping("hello/{name}")
    fun hello(@PathVariable("name") name: String): Reply {
        return Reply("Hello, $name")
    }
}