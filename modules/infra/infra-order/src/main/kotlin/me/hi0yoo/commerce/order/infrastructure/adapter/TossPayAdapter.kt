package me.hi0yoo.commerce.order.infrastructure.adapter

import me.hi0yoo.commerce.common.domain.enums.PayMethod
import me.hi0yoo.commerce.order.application.port.PaySystemPayResponse
import me.hi0yoo.commerce.order.application.port.PaySystemPort
import me.hi0yoo.commerce.order.application.port.PaySystemRequest
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class TossPayAdapter: PaySystemPort {
    override fun support(): PayMethod {
        return PayMethod.TOSS_PAY
    }

    override fun pay(request: PaySystemRequest): PaySystemPayResponse {
        return PaySystemPayResponse(
            paymentId = UUID.randomUUID().toString(),
            paidAmount = request.amount,
            paidAt = LocalDateTime.now(),
        )
    }
}