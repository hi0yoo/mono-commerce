package me.hi0yoo.commerce.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URI

@Configuration
class StartupLogger(
    @Value("\${spring.datasource.url}") private val jdbcUrl: String
) {
    companion object {
        private val log = LoggerFactory.getLogger(StartupLogger::class.java)
    }

    @Bean
    fun logDbInfo() = ApplicationRunner {
        val uri = URI(jdbcUrl.replace("jdbc:", ""))
        val host = uri.host
        val port = uri.port

        log.info("DB Configured: $host:$port")
    }
}