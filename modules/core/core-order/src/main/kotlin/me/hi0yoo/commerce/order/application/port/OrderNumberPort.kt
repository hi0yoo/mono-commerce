package me.hi0yoo.commerce.order.application.port

interface OrderNumberPort {
    fun nextOrderNumber(yyyymmdd: String): Long
}