package me.hi0yoo.commerce.product.application.port.out

import me.hi0yoo.commerce.product.application.dto.ProductPageByCursorResult
import me.hi0yoo.commerce.product.application.dto.ProductPagedListByCursorQuery

interface ProductPageByCursorQueryPort {
    fun findPagedListByCursor(query: ProductPagedListByCursorQuery): ProductPageByCursorResult
}