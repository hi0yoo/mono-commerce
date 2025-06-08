package me.hi0yoo.commerce.product.application.dto

data class ProductPageByCursorResult(
    val hasNext: Boolean,
    val content: List<ProductPagedListResult>,
)