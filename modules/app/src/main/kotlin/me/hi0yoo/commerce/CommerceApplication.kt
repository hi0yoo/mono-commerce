package me.hi0yoo.commerce

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["me.hi0yoo.commerce"])
class CommerceApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<CommerceApplication>(*args)
        }
    }
}