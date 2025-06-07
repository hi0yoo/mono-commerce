package me.hi0yoo.commerce.product.application.port.out

import me.hi0yoo.commerce.product.application.dto.ProductDetailResult
import me.hi0yoo.commerce.product.application.dto.ProductPageResult
import me.hi0yoo.commerce.product.application.dto.ProductPagedListQuery

interface ProductQueryPort {
    fun findPagedListByCondition(query: ProductPagedListQuery): ProductPageResult
    fun findDetailById(productId: Long): ProductDetailResult?
}