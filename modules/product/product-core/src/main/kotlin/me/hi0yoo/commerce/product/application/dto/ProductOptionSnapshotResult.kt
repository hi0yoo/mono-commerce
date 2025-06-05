package me.hi0yoo.commerce.product.application.dto

data class ProductOptionSnapshotResult(
    val productId: Long,
    val productName: String,
    val price: Int,

    val productOptionId: Long,
    val optionName: String,
    val additionalPrice: Int,

    val vendorId: String,
//    val vendorName: String,
)