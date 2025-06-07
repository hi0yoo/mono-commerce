package me.hi0yoo.commerce.product.application.service

import me.hi0yoo.commerce.product.application.dto.ProductDetailResult
import me.hi0yoo.commerce.product.application.dto.ProductDetailQuery
import me.hi0yoo.commerce.product.application.dto.ProductPagedListQuery
import me.hi0yoo.commerce.product.application.dto.ProductPagedListResult
import me.hi0yoo.commerce.product.application.port.out.ProductQueryPort
import me.hi0yoo.commerce.product.application.port.`in`.FetchProductDetailUseCase
import me.hi0yoo.commerce.product.application.port.`in`.FetchProductPagedListUseCase
import me.hi0yoo.commerce.product.domain.ProductNotFountException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductQueryService(
    private val productQueryPort: ProductQueryPort
): FetchProductPagedListUseCase, FetchProductDetailUseCase {

    override fun fetchPagedList(query: ProductPagedListQuery): List<ProductPagedListResult> {
        return productQueryPort.findPagedListByCondition(query)
    }

    override fun fetchDetail(query: ProductDetailQuery): ProductDetailResult {
        return productQueryPort.findDetailById(query.productId)
            ?: throw ProductNotFountException(query.productId)
    }
}