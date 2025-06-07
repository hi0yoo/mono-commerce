package me.hi0yoo.commerce.product.application.dto

data class ProductPageResult(
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val totalElements: Long,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val content: List<ProductPagedListResult>,
)

data class ProductPagedListResult(
    val productId: Long,
    val name: String,
    val thumbnailUrl: String,
    val price: Int,
    val vendorId: String,
    val vendorName: String,
    val category: CategoryResult,
    val badges: List<String> = emptyList(),
)