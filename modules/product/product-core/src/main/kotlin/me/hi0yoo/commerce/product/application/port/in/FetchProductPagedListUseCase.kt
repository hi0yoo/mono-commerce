package me.hi0yoo.commerce.product.application.port.`in`

import me.hi0yoo.commerce.product.application.dto.ProductPagedListQuery
import me.hi0yoo.commerce.product.application.dto.ProductPagedListResult

interface FetchProductPagedListUseCase {
    fun fetchPagedList(query: ProductPagedListQuery): List<ProductPagedListResult>
}