package me.hi0yoo.commerce.product.application.port.`in`

import me.hi0yoo.commerce.product.application.dto.ProductOptionSnapshotQuery
import me.hi0yoo.commerce.product.application.dto.ProductOptionSnapshotResult

interface FetchProductOptionSnapshotUseCase {
    fun fetchSnapshots(query: ProductOptionSnapshotQuery): List<ProductOptionSnapshotResult>
}