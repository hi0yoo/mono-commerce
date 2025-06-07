package me.hi0yoo.commerce.order.infrastructure.adapter.out

import me.hi0yoo.commerce.order.application.port.out.PayCommand
import me.hi0yoo.commerce.order.application.port.out.PayResult
import me.hi0yoo.commerce.order.application.port.out.PaySystemPort
import me.hi0yoo.commerce.order.domain.PayMethod
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class TossPayAdapter: PaySystemPort {
    override fun support(): PayMethod {
        return PayMethod.TOSS_PAY
    }

    override fun pay(command: PayCommand): PayResult {
        return PayResult(
            paymentId = UUID.randomUUID().toString(),
            paidAmount = command.amount,
            paidAt = LocalDateTime.now(),
        )
    }
}