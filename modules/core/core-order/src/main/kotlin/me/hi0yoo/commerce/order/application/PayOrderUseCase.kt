package me.hi0yoo.commerce.order.application

import me.hi0yoo.commerce.common.domain.exception.OrderNotFoundException
import me.hi0yoo.commerce.order.application.port.PaySystemRequest
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
    fun payOrder(request: PayOrderRequest): String {
        val order = orderRepository.findById(request.orderId) ?: throw OrderNotFoundException(request.orderId)

        val paymentResponse = paySystemFactory.getStrategy(request.payMethod)
            .pay(PaySystemRequest(order.id, order.orderPrice))

        order.paid(
            PaymentInfo(
                payMethod = request.payMethod,
                paymentId = paymentResponse.paymentId,
                paidAmount = paymentResponse.paidAmount,
                paidAt = paymentResponse.paidAt,
            )
        )

        return paymentResponse.paymentId;
    }
}