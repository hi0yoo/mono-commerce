package me.hi0yoo.commerce.order.application.port

interface ProductQueryPort {
    fun getProductDetails(requests: List<Long>): List<ProductDetailResponse>
}