package me.hi0yoo.commerce.order.presentation

data class PlaceOrderApRequest(
    val receiverName: String,
    val receiverAddress: String,
    val receiverEmail: String,
    val productQuantities: List<ProductQuantity>,
)

data class ProductQuantity(
    val productOptionId: Long,
    val quantity: Int,
)

data class PayOrderApiRequest(
    val orderId: String,
    val payMethod: String
)