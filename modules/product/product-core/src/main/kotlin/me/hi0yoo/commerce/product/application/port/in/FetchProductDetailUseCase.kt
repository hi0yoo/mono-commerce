package me.hi0yoo.commerce.product.application.port.`in`

import me.hi0yoo.commerce.product.application.dto.ProductDetailResult
import me.hi0yoo.commerce.product.application.dto.ProductDetailQuery

interface FetchProductDetailUseCase {
    fun fetchDetail(query: ProductDetailQuery): ProductDetailResult
}