package me.hi0yoo.commerce.product.application.dto

data class ProductPagedListQuery(
    val categoryId: Long?,
    val keyword: String?,
    val page: Int,
    val size: Int,
)