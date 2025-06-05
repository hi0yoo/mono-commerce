package me.hi0yoo.commerce.product.infrastructure.repository

import jakarta.persistence.LockModeType
import me.hi0yoo.commerce.product.domain.ProductOption
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface ProductOptionJpaRepository: JpaRepository<ProductOption, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ProductOption p WHERE p.id IN :ids")
    fun findAllByIdForUpdate(ids: List<Long>): List<ProductOption>
}