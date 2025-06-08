package me.hi0yoo.commerce.product.application.dto

data class ProductPagedListByCursorQuery(
    val categoryId: Long?,
    val keyword: String?,
    val lastProductId: Long?,
    val size: Int,
)