package me.hi0yoo.commerce.order.application.service

import me.hi0yoo.commerce.order.application.port.out.PaySystemPort
import me.hi0yoo.commerce.order.domain.PayMethod
import org.springframework.stereotype.Component

@Component
class PaySystemFactory(paySystems: List<PaySystemPort>) {
    private val strategyMap: Map<PayMethod, PaySystemPort> = paySystems.associateBy { it.support() }

    fun getStrategy(payMethod: PayMethod): PaySystemPort {
        return strategyMap[payMethod] ?: throw IllegalArgumentException("Unsupported pay method.")
    }
}