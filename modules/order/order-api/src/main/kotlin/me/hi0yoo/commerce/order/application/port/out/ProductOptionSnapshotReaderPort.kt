package me.hi0yoo.commerce.order.application.port.out

interface ProductOptionSnapshotReaderPort {
    fun fetchSnapshots(query: ProductOptionSnapshotQuery): List<ProductOptionSnapshotResult>
}

data class ProductOptionSnapshotQuery(
    val productOptionIds: List<Long>,
)

data class ProductOptionSnapshotResult(
    val vendorId: String,
    val productId: Long,
    val productName: String,
    val price: Int,
    val productOptionId: Long,
    val optionName: String,
    val additionalPrice: Int,
)