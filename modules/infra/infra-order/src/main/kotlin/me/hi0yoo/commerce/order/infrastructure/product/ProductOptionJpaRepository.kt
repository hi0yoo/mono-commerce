package me.hi0yoo.commerce.order.infrastructure.product

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface ProductOptionJpaRepository: JpaRepository<ProductOption, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ProductOption p WHERE p.id IN :ids")
    fun findAllByIdForUpdate(ids: List<Long>): List<ProductOption>
}