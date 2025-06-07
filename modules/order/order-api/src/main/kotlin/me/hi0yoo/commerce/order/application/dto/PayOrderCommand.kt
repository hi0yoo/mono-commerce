package me.hi0yoo.commerce.order.application.dto

data class PayOrderCommand(
    val orderId: String,
    val payMethod: String,
)
