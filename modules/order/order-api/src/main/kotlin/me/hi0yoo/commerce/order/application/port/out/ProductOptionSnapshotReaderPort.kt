package me.hi0yoo.commerce.order.application.port.out

interface ProductOptionSnapshotReaderPort {
    fun fetchSnapshots(query: ProductOptionSnapshotQuery): List<ProductOptionSnapshotResult>
}

data class ProductOptionSnapshotQuery(
    val productOptionIds: List<Long>,
)

data class ProductOptionSnapshotResult(
    val productOptionId: Long,
    val productName: String,
    val optionName: String,
    val price: Int,
)