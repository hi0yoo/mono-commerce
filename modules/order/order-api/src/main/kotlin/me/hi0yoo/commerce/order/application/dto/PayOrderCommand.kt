package me.hi0yoo.commerce.order.application.dto

import me.hi0yoo.commerce.order.domain.PayMethod

data class PayOrderCommand(
    val orderId: String,
    val payMethod: PayMethod,
)
