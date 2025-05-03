package me.hi0yoo.commerce.order.application.port

import java.math.BigDecimal

data class ReserveStockRequest(
    val vendorId: String,
    val productId: String,
    val optionId: String,
    val quantity: Long,
)

data class ProductDetailRequest(
    val vendorId: String,
    val productId: String,
    val optionId: String,
)

data class ProductDetailResponse(
    val vendorId: String,
    val productId: String,
    val optionId: String,
    val productName: String,
    val optionName: String,
    val price: BigDecimal,
)