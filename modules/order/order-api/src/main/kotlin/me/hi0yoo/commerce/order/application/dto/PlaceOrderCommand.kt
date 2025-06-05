package me.hi0yoo.commerce.order.application.dto

data class PlaceOrderCommand(
    val receiverName: String,
    val receiverAddress: String,
    val receiverEmail: String,
    val productQuantities: List<PlaceOrderProductCommand>,
)

data class PlaceOrderProductCommand(
    val productOptionId: Long,
    val quantity: Int,
)