package me.hi0yoo.commerce.product.domain

data class ProductOptionSpec(
    val name: String,
    val additionalPrice: Int,
    val realStockQuantity: Int = 0
)