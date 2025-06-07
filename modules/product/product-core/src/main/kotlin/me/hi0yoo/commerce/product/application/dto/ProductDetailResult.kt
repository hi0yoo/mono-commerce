package me.hi0yoo.commerce.product.application.dto

data class ProductDetailResult(
    val productId: Long,
    val name: String,
    val thumbnailUrl: String,
    val description: String?,
    val imageUrls: List<String>,
    val price: Int,
    val vendorId: String,
    val vendorName: String,
    val category: CategoryResult,
    val badges: List<String>,
    val options: List<ProductOptionResult>
)

data class ProductOptionResult(
    val optionId: Long,
    val name: String,
    val additionalPrice: Int,
    val availableStock: Int,
)
