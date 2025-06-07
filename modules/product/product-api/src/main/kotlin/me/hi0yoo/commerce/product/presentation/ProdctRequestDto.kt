package me.hi0yoo.commerce.product.presentation

data class ProductSearchRequest(
    val categoryId: Long?,
    val keyword: String?,
    val page: Int = 0,
    val size: Int = 20,
)