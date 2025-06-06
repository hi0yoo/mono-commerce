package me.hi0yoo.commerce.order.application.dto

data class PlaceOrderCommand(
    val receiverName: String,
    val receiverAddress: String,
    val receiverEmail: String,
    val productQuantities: List<ProductQuantity>,
) {
    data class ProductQuantity(
        val productOptionId: Long,
        val quantity: Int,
    )
}