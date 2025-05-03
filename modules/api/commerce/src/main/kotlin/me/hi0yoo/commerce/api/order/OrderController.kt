package me.hi0yoo.commerce.api.order

import me.hi0yoo.commerce.order.application.PayOrderRequest
import me.hi0yoo.commerce.order.application.PayOrderUseCase
import me.hi0yoo.commerce.order.application.PlaceOrderProductQuantity
import me.hi0yoo.commerce.order.application.PlaceOrderRequest
import me.hi0yoo.commerce.order.application.PlaceOrderUseCase
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
    @PostMapping
    fun placeOrder(@RequestBody request: PlaceOrderApRequest): PlaceOrderApiResponse {
        return PlaceOrderApiResponse(
            placeOrderUseCase.placeOrder(
                PlaceOrderRequest(
                    request.receiverName,
                    request.receiverAddress,
                    request.receiverEmail,
                    request.productQuantities.map {
                        PlaceOrderProductQuantity(
                            it.vendorId,
                            it.productId,
                            it.optionId,
                            it.quantity,
                        )
                    }
                )))
    }

    // 간소화를 위해 결제 등록 API 작성
    @PostMapping("/payment")
    fun payOrder(@RequestBody request: PayOrderApiRequest): PayOrderApiResponse {
        return PayOrderApiResponse(
            payOrderUseCase.payOrder(
                PayOrderRequest(
                    request.orderId,
                    request.payMethod
                )
            )
        )
    }
}