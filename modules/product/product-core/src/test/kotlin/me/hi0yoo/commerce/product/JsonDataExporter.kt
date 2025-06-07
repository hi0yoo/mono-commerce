package me.hi0yoo.commerce.product

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.EntityManager
import me.hi0yoo.commerce.product.domain.ProductOption
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import java.io.File
import java.nio.file.Paths

//@Disabled
@DataJpaTest
@ContextConfiguration(classes = [JpaTestConfiguration::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JsonDataExporter {
    @Autowired
    lateinit var em: EntityManager

    @Test
    fun exportProductJson() {
        val result = em.createQuery(
            """
            select o
            FROM ProductOption o
            """.trimIndent(), ProductOption::class.java
        ).setMaxResults(200_000).resultList

        val objectMapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
        val currentDir = Paths.get(System.getProperty("user.dir")).toString()
        val rootPath = currentDir.substringBefore("modules") // 'modules' 앞까지 자르기
        val targetFile = File(rootPath, "artillery/order/products.json")

        targetFile.parentFile.mkdirs()
        targetFile.writeText(objectMapper.writeValueAsString(result.map { it.id.toString() }))
    }
}