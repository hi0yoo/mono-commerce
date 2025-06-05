package me.hi0yoo.commerce.order.domain

data class ProductInfo(
    val id: Long,
    val productName: String,
    val optionName: String,
    val optionPrice: Int,
)