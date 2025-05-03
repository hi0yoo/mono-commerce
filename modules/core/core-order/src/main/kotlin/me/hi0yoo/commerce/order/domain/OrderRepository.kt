package me.hi0yoo.commerce.order.domain

interface OrderRepository {
    fun save(order: Order): Order
}