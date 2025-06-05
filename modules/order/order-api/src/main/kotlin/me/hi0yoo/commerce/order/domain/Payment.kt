package me.hi0yoo.commerce.order.domain

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime

@Embeddable
data class PaymentInfo(
    @Enumerated(EnumType.STRING)
    val payMethod: PayMethod,
    val paymentId: String,
    val paidAmount: Int,
    val paidAt: LocalDateTime,
)