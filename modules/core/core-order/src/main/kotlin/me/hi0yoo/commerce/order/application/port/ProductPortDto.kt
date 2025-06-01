package me.hi0yoo.commerce.order.application.port

import java.math.BigDecimal

data class ReserveStockRequest(
    val productOptionId: Long,
    val quantity: Long,
)

data class ProductDetailResponse(
    val productOptionId: Long,
    val productName: String,
    val optionName: String,
    val price: BigDecimal,
)