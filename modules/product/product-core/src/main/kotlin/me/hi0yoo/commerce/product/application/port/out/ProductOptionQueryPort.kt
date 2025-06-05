package me.hi0yoo.commerce.product.application.port.out

import me.hi0yoo.commerce.product.application.dto.ProductOptionSnapshotResult

interface ProductOptionQueryPort {
    fun findAllSnapshotByIds(ids: List<Long>): List<ProductOptionSnapshotResult>
}