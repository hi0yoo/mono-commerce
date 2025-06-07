package me.hi0yoo.commerce.product.application.port.`in`

import me.hi0yoo.commerce.product.application.dto.ProductPageResult
import me.hi0yoo.commerce.product.application.dto.ProductPagedListQuery

interface FetchProductPagedListUseCase {
    fun fetchPagedList(query: ProductPagedListQuery): ProductPageResult
}