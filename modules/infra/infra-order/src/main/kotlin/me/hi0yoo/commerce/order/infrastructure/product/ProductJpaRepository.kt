package me.hi0yoo.commerce.order.infrastructure.product

import me.hi0yoo.commerce.common.domain.id.ProductId
import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository: JpaRepository<Product, ProductId>