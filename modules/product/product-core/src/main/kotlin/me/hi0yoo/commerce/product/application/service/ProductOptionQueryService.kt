package me.hi0yoo.commerce.product.application.service

import me.hi0yoo.commerce.product.application.dto.ProductOptionSnapshotQuery
import me.hi0yoo.commerce.product.application.dto.ProductOptionSnapshotResult
import me.hi0yoo.commerce.product.application.port.out.ProductOptionQueryPort
import me.hi0yoo.commerce.product.application.port.`in`.FetchProductOptionSnapshotUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductOptionQueryService(
    private val productOptionQueryPort: ProductOptionQueryPort
): FetchProductOptionSnapshotUseCase {

    override fun fetchSnapshots(query: ProductOptionSnapshotQuery): List<ProductOptionSnapshotResult> {
        return productOptionQueryPort.findAllSnapshotByIds(query.productOptionIds)
    }
}