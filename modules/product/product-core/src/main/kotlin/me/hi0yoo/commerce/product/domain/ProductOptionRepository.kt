package me.hi0yoo.commerce.product.domain

interface ProductOptionRepository {
    fun findAllByIdForUpdate(ids: List<Long>): List<ProductOption>
}