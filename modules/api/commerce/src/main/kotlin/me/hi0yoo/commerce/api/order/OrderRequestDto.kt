package me.hi0yoo.commerce.api.order

import me.hi0yoo.commerce.common.domain.enums.PayMethod

data class PlaceOrderApRequest(
    val receiverName: String,
    val receiverAddress: String,
    val receiverEmail: String,
    val productQuantities: List<ProductQuantity>,
)

data class ProductQuantity(
    val productOptionId: Long,
    val quantity: Long,
)

data class PayOrderApiRequest(
    val orderId: String,
    val payMethod: PayMethod
)