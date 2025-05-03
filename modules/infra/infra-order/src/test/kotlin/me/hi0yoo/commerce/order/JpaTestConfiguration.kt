package me.hi0yoo.commerce.order

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["me.hi0yoo.commerce.order.infrastructure.product"])
@EntityScan(basePackages = ["me.hi0yoo.commerce.common.domain", "me.hi0yoo.commerce.order.infrastructure.product"])
class JpaTestConfiguration {
}