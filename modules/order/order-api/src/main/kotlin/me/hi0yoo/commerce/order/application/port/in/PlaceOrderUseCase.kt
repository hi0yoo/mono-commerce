package me.hi0yoo.commerce.order.application.port.`in`

import me.hi0yoo.commerce.order.application.dto.PlaceOrderCommand

interface PlaceOrderUseCase {
    fun placeOrder(request: PlaceOrderCommand): String
}