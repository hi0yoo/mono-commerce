package me.hi0yoo.commerce.order.infrastructure.adapter

import me.hi0yoo.commerce.order.application.port.ProductDetailResponse
import me.hi0yoo.commerce.order.application.port.ProductDetailRequest
import me.hi0yoo.commerce.order.application.port.ProductQueryPort
import me.hi0yoo.commerce.order.infrastructure.product.ProductQueryRepository
import org.springframework.stereotype.Component

@Component
class ProductQueryAdapter(
    private val productQueryRepository: ProductQueryRepository
): ProductQueryPort {
    override fun getProductDetails(requests: List<ProductDetailRequest>): List<ProductDetailResponse> {
        return productQueryRepository.getProductDetails(requests)
    }
}