package me.hi0yoo.commerce.order.domain

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import me.hi0yoo.commerce.common.domain.enums.PayMethod
import java.math.BigDecimal
import java.time.LocalDateTime

@Embeddable
data class PaymentInfo(
    @Enumerated(EnumType.STRING)
    val payMethod: PayMethod,
    val paymentId: String,
    val paidAmount: BigDecimal,
    val paidAt: LocalDateTime,
)