package me.hi0yoo.commerce.order.application.port.`in`

import me.hi0yoo.commerce.order.application.dto.PayOrderCommand

interface PayOrderUseCase {
    fun payOrder(command: PayOrderCommand): String
}