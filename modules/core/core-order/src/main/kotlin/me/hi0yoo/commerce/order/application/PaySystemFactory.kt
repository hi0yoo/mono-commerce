package me.hi0yoo.commerce.order.application

import me.hi0yoo.commerce.common.domain.enums.PayMethod
import me.hi0yoo.commerce.order.application.port.PaySystemPort
import org.springframework.stereotype.Component

@Component
class PaySystemFactory(paySystems: List<PaySystemPort>) {
    private val strategyMap: Map<PayMethod, PaySystemPort> = paySystems.associateBy { it.support() }

    fun getStrategy(payMethod: PayMethod): PaySystemPort {
        return strategyMap[payMethod] ?: throw IllegalArgumentException("Unsupported pay method.")
    }
}