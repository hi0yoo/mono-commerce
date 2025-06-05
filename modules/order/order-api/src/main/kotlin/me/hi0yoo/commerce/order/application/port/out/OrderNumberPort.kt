package me.hi0yoo.commerce.order.application.port.out

interface OrderNumberPort {
    fun fetchNextOrderNumber(yyyymmdd: String): Long
}