package me.hi0yoo.commerce.order.application.port.out

import me.hi0yoo.commerce.order.domain.PayMethod
import java.time.LocalDateTime

interface PaySystemPort {
    fun support(): PayMethod
    fun pay(request: PayCommand): PayResult
}

data class PayCommand(
    val orderId: String,
    val amount: Int,
)

data class PayResult(
    val paymentId: String,
    val paidAmount: Int,
    val paidAt: LocalDateTime,
)