package me.hi0yoo.commerce.order.domain

import me.hi0yoo.commerce.common.domain.id.ProductOptionId
import java.math.BigDecimal

data class ProductInfo(
    val id: ProductOptionId,
    val productName: String,
    val optionName: String,
    val optionPrice: BigDecimal,
)