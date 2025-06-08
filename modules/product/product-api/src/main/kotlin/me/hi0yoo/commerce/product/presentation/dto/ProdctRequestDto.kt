package me.hi0yoo.commerce.product.presentation.dto

data class ProductSearchRequest(
    val categoryId: Long?,
    val keyword: String?,
    val page: Int = 0,
    val size: Int = 20,
)

data class ProductSearchByCursorRequest(
    val categoryId: Long?,
    val keyword: String?,
    val lastProductId: Long?,
    val size: Int = 20,
)