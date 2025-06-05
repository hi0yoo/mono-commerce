package me.hi0yoo.commerce.order.domain

interface OrderRepository {
    fun findById(id: String): Order?
    fun save(order: Order): Order
}