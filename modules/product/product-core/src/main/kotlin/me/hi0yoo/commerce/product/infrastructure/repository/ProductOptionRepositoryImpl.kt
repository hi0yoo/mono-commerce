package me.hi0yoo.commerce.product.infrastructure.repository

import me.hi0yoo.commerce.product.domain.ProductOption
import me.hi0yoo.commerce.product.domain.ProductOptionRepository
import org.springframework.stereotype.Repository

@Repository
class ProductOptionRepositoryImpl(
    private val productOptionJpaRepository: ProductOptionJpaRepository
): ProductOptionRepository {
    override fun findAllByIdForUpdate(ids: List<Long>): List<ProductOption> {
        return productOptionJpaRepository.findAllByIdForUpdate(ids)
    }
}