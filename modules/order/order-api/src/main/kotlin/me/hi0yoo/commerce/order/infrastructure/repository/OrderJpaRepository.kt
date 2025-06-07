package me.hi0yoo.commerce.order.infrastructure.repository

import me.hi0yoo.commerce.order.domain.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderJpaRepository: JpaRepository<Order, String> {
}