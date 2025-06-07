package me.hi0yoo.commerce.product.infrastructure.repository

import me.hi0yoo.commerce.product.domain.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository: JpaRepository<Product, Long>