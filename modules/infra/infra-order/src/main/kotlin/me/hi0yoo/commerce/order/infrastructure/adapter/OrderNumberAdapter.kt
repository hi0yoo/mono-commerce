package me.hi0yoo.commerce.order.infrastructure.adapter

import me.hi0yoo.commerce.order.application.port.OrderNumberPort
import me.hi0yoo.commerce.order.infrastructure.sequence.OrderNumberSequence
import me.hi0yoo.commerce.order.infrastructure.sequence.OrderNumberSequenceJpaRepository
import org.springframework.stereotype.Component

@Component
class OrderNumberAdapter(
    private val orderNumberSequenceJpaRepository: OrderNumberSequenceJpaRepository
): OrderNumberPort {
    override fun nextOrderNumber(yyyymmdd: String): Long {
        val sequence = orderNumberSequenceJpaRepository.findByIdForUpdate(yyyymmdd)
            ?: orderNumberSequenceJpaRepository.save(
                OrderNumberSequence(yyyymmdd)
            )
        return sequence.increment()
    }
}