package me.hi0yoo.commerce.order.application

import me.hi0yoo.commerce.common.domain.enums.PayMethod

data class PlaceOrderRequest(
    val receiverName: String,
    val receiverAddress: String,
    val receiverEmail: String,
    val productQuantities: List<PlaceOrderProductQuantity>,
)

data class PlaceOrderProductQuantity(
    val vendorId: String,
    val productId: String,
    val optionId: String,
    val quantity: Long,
)

data class PayOrderRequest(
    val orderId: String,
    val payMethod: PayMethod,
)