package me.hi0yoo.commerce.product.application.port.out

import me.hi0yoo.commerce.product.application.dto.ProductDetailResult
import me.hi0yoo.commerce.product.application.dto.ProductPagedListQuery
import me.hi0yoo.commerce.product.application.dto.ProductPagedListResult

interface ProductQueryPort {
    fun findPagedListByCondition(query: ProductPagedListQuery): List<ProductPagedListResult>
    fun findDetailById(productId: Long): ProductDetailResult?
}