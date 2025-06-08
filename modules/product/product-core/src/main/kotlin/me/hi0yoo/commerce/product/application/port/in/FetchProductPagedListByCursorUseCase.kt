package me.hi0yoo.commerce.product.application.port.`in`

import me.hi0yoo.commerce.product.application.dto.ProductPageByCursorResult
import me.hi0yoo.commerce.product.application.dto.ProductPagedListByCursorQuery

interface FetchProductPagedListByCursorUseCase {
    fun fetchPagedList(query: ProductPagedListByCursorQuery): ProductPageByCursorResult
}