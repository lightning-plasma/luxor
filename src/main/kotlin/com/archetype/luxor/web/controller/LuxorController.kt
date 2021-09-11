package com.archetype.luxor.web.controller

import com.archetype.luxor.web.entity.Reply
import org.hibernate.validator.constraints.Length
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Size

@RestController
@RequestMapping(
    path = ["/luxor"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@Validated
class LuxorController {
    @GetMapping("hello/{name}")
    fun hello(
        @PathVariable("name") @Size(min = 3, max = 10) name: String
    ): Reply {
        return Reply("Hello, $name")
    }
}