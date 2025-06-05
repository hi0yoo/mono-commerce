package me.hi0yoo.commerce.order.presentation

import me.hi0yoo.commerce.order.application.dto.PayOrderCommand
import me.hi0yoo.commerce.order.application.port.`in`.PayOrderUseCase
import me.hi0yoo.commerce.order.application.dto.PlaceOrderProductCommand
import me.hi0yoo.commerce.order.application.dto.PlaceOrderCommand
import me.hi0yoo.commerce.order.application.port.`in`.PlaceOrderUseCase
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val payOrderUseCase: PayOrderUseCase,
) {
    companion object {
        private val log = LoggerFactory.getLogger(OrderController::class.java)
    }

    @PostMapping
    fun placeOrder(@RequestBody request: PlaceOrderApRequest): PlaceOrderApiResponse {
        val start = System.currentTimeMillis()
        val orderId = placeOrderUseCase.placeOrder(
            PlaceOrderCommand(
                request.receiverName,
                request.receiverAddress,
                request.receiverEmail,
                request.productQuantities.map {
                    PlaceOrderProductCommand(
                        it.productOptionId,
                        it.quantity,
                    )
                }
            ))

        log.info("OrderController.placeOrder completed in {}ms", System.currentTimeMillis() - start)

        return PlaceOrderApiResponse(
            orderId
        )
    }

    // 간소화를 위해 결제 등록 API 작성
    @PostMapping("/payment")
    fun payOrder(@RequestBody request: PayOrderApiRequest): PayOrderApiResponse {
        return PayOrderApiResponse(
            payOrderUseCase.payOrder(
                PayOrderCommand(
                    request.orderId,
                    request.payMethod
                )
            )
        )
    }
}