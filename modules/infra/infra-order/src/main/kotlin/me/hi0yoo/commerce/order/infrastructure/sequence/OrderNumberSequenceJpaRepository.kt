package me.hi0yoo.commerce.order.infrastructure.sequence

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface OrderNumberSequenceJpaRepository: JpaRepository<OrderNumberSequence, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM OrderNumberSequence s WHERE s.date = :date")
    fun findByIdForUpdate(date: String): OrderNumberSequence?
}