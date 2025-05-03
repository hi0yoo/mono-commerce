package me.hi0yoo.commerce.order.application.port

import java.math.BigDecimal
import java.time.LocalDateTime

data class PaySystemRequest(
    val orderId: String,
    val amount: BigDecimal,
)

data class PaySystemPayResponse(
    val paymentId: String,
    val paidAmount: BigDecimal,
    val paidAt: LocalDateTime,
)