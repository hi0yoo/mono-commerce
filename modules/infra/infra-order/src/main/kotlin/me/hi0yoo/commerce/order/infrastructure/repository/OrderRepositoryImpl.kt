package me.hi0yoo.commerce.order.infrastructure.repository

import me.hi0yoo.commerce.order.domain.Order
import me.hi0yoo.commerce.order.domain.OrderRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(
    private val orderJpaRepository: OrderJpaRepository
): OrderRepository {
    override fun findById(id: String): Order? = orderJpaRepository.findByIdOrNull(id)
    override fun save(order: Order): Order = orderJpaRepository.save(order)
}