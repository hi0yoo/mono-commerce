package me.hi0yoo.commerce.order.application.port.`in`

import me.hi0yoo.commerce.order.application.PaySystemFactory
import me.hi0yoo.commerce.order.application.dto.PayOrderCommand
import me.hi0yoo.commerce.order.application.port.out.PayCommand
import me.hi0yoo.commerce.order.domain.OrderNotFoundException
import me.hi0yoo.commerce.order.domain.OrderRepository
import me.hi0yoo.commerce.order.domain.PaymentInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PayOrderUseCase(
    private val orderRepository: OrderRepository,
    private val paySystemFactory: PaySystemFactory
) {
    @Transactional
    fun payOrder(command: PayOrderCommand): String {
        val order = orderRepository.findById(command.orderId) ?: throw OrderNotFoundException(command.orderId)

        val paymentResponse = paySystemFactory.getStrategy(command.payMethod)
            .pay(PayCommand(order.id, order.orderPrice))

        order.paid(
            PaymentInfo(
                payMethod = command.payMethod,
                paymentId = paymentResponse.paymentId,
                paidAmount = paymentResponse.paidAmount,
                paidAt = paymentResponse.paidAt,
            )
        )

        return paymentResponse.paymentId;
    }
}