package me.hi0yoo.commerce.order.domain

data class OrderProductSpec(
    val productId: Long,
    val productName: String,
    val price: Int,
    val optionId: Long,
    val optionName: String,
    val additionalPrice: Int,
    val productVendorId: String,
)