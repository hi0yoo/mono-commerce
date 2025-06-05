package me.hi0yoo.commerce.product.application.dto

data class ProductPagedListResult(
    val productId: Long,
    val name: String,
    val thumbnailUrl: String,
    val price: Int,
    val vendorId: String,
    val vendorName: String,
    val category: CategoryResult,
    val badges: List<String>,
)