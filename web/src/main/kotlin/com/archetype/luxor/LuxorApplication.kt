package com.archetype.luxor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound

@SpringBootApplication
class LuxorApplication

fun main(args: Array<String>) {
	// https://github.com/reactor/BlockHound/issues/135
	// https://github.com/Kotlin/kotlinx.coroutines/issues/2190
	BlockHound.install()
	runApplication<LuxorApplication>(*args)
}
