package me.hi0yoo.commerce.order.domain

import java.math.BigDecimal

data class ProductInfo(
    val id: Long,
    val productName: String,
    val optionName: String,
    val optionPrice: BigDecimal,
)