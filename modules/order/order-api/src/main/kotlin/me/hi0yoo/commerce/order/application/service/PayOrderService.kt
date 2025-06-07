package me.hi0yoo.commerce.order.application.service

import me.hi0yoo.commerce.order.application.dto.PayOrderCommand
import me.hi0yoo.commerce.order.application.port.`in`.PayOrderUseCase
import me.hi0yoo.commerce.order.application.port.out.PayCommand
import me.hi0yoo.commerce.order.domain.OrderNotFoundException
import me.hi0yoo.commerce.order.domain.OrderRepository
import me.hi0yoo.commerce.order.domain.PayMethod
import me.hi0yoo.commerce.order.domain.PaymentInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PayOrderService(
    private val orderRepository: OrderRepository,
    private val paySystemFactory: PaySystemFactory
): PayOrderUseCase {
    @Transactional
    override fun payOrder(command: PayOrderCommand): String {
        val order = orderRepository.findById(command.orderId) ?: throw OrderNotFoundException(command.orderId)

        val payMethod = PayMethod.valueOf(command.payMethod)
        val paymentResponse = paySystemFactory.getStrategy(payMethod)
            .pay(PayCommand(order.id, order.orderPrice))

        order.paid(
            PaymentInfo(
                payMethod = payMethod,
                paymentId = paymentResponse.paymentId,
                paidAmount = paymentResponse.paidAmount,
                paidAt = paymentResponse.paidAt,
            )
        )

        return paymentResponse.paymentId;
    }
}