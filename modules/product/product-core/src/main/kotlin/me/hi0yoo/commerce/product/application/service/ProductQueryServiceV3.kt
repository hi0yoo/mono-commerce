package me.hi0yoo.commerce.product.application.service

import me.hi0yoo.commerce.product.application.dto.ProductPageByCursorResult
import me.hi0yoo.commerce.product.application.dto.ProductPagedListByCursorQuery
import me.hi0yoo.commerce.product.application.port.`in`.FetchProductPagedListByCursorUseCase
import me.hi0yoo.commerce.product.application.port.out.ProductPageByCursorQueryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductQueryServiceV3(
    private val productPageByCursorQueryPort: ProductPageByCursorQueryPort
): FetchProductPagedListByCursorUseCase {
    override fun fetchPagedList(query: ProductPagedListByCursorQuery): ProductPageByCursorResult {
        return productPageByCursorQueryPort.findPagedListByCursor(query)
    }
}