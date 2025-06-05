package me.hi0yoo.commerce.product.domain

interface ProductRepository {
    fun findById(productId: Long): Product?
}