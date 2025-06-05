package me.hi0yoo.commerce.product.infrastructure.repository

import me.hi0yoo.commerce.product.domain.Product
import me.hi0yoo.commerce.product.domain.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository,
): ProductRepository {
    override fun findById(productId: Long): Product? {
        return productJpaRepository.findByIdOrNull(productId)
    }
}