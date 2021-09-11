package com.archetype.luxor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LuxorApplication

fun main(args: Array<String>) {
	runApplication<LuxorApplication>(*args)
}
