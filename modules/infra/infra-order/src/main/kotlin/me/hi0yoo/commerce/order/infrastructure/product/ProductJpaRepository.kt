package me.hi0yoo.commerce.order.infrastructure.product

import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository: JpaRepository<Product, Long>