package me.hi0yoo.commerce.order.infrastructure.adapter.out

import me.hi0yoo.commerce.order.application.port.out.ProductOptionSnapshotQuery
import me.hi0yoo.commerce.order.application.port.out.ProductOptionSnapshotResult
import me.hi0yoo.commerce.order.application.port.out.ProductOptionSnapshotReaderPort
import me.hi0yoo.commerce.product.application.dto.ProductOptionSnapshotQuery as ProductModuleProductOptionSnapshotQuery
import me.hi0yoo.commerce.product.application.port.`in`.FetchProductOptionSnapshotUseCase
import org.springframework.stereotype.Component

@Component
class ProductOptionSnapshotReaderAdapter(
    private val fetchProductOptionSnapshotUseCase: FetchProductOptionSnapshotUseCase
): ProductOptionSnapshotReaderPort {
    override fun fetchSnapshots(query: ProductOptionSnapshotQuery): List<ProductOptionSnapshotResult> {
        return fetchProductOptionSnapshotUseCase.fetchSnapshots(
            ProductModuleProductOptionSnapshotQuery(productOptionIds = query.productOptionIds)
        ).map {
            ProductOptionSnapshotResult(
                vendorId = it.vendorId,
                productId = it.productId,
                productName = it.productName,
                price = it.price,
                productOptionId = it.productOptionId,
                optionName = it.productName,
                additionalPrice = it.additionalPrice,
            )
        }
    }
}