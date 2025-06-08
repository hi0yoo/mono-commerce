package me.hi0yoo.commerce.product.application.service

import me.hi0yoo.commerce.product.application.dto.ProductPageResult
import me.hi0yoo.commerce.product.application.dto.ProductPagedListQuery
import me.hi0yoo.commerce.product.application.port.`in`.FetchProductPagedListUseCase
import me.hi0yoo.commerce.product.application.port.out.ProductQueryPort
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("productQueryServiceV2")
@Transactional(readOnly = true)
class ProductQueryServiceV2(
    @Qualifier("nativeQueryProductQueryAdapter")
    private val productQueryPort: ProductQueryPort
): FetchProductPagedListUseCase {
    override fun fetchPagedList(query: ProductPagedListQuery): ProductPageResult {
        return productQueryPort.findPagedListByCondition(query)
    }
}