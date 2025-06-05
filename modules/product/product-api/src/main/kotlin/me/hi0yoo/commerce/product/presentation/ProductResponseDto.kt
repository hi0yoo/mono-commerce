package me.hi0yoo.commerce.product.presentation

data class ProductPagedListApiResponse(
    val page: Int,
    val size: Int,
    val total: Int,
    val products: List<ProductSummaryResponse>
)

data class ProductSummaryResponse(
    val productId: Long,
    val name: String,
    val thumbnailUrl: String,
    val price: Int,
    val vendorId: String,
    val vendorName: String,
    val category: CategoryApiResponse,
    val badges: List<String>
)

data class CategoryApiResponse(
    val mainId: Long,
    val mainName: String,
    val subId: Long?,
    val subName: String?
)

data class FetchProductDetailApiResponse(
    val productId: Long,
    val name: String,
    val thumbnailUrl: String,
    val description: String?,
    val imageUrls: List<String>,
    val price: Int,
    val vendorId: String,
    val vendorName: String,
    val category: CategoryApiResponse,
    val badges: List<String>,
    val options: List<ProductOptionResponse>
)

data class ProductOptionResponse(
    val optionId: Long,
    val name: String,
    val additionalPrice: Int,
    val availableStock: Int?  // null 이면 5개 초과
)