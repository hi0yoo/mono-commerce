package me.hi0yoo.commerce.order.application

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