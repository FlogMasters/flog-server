package com.flogmasters.flog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlogApplication

fun main(args: Array<String>) {
    runApplication<FlogApplication>(*args)
}
