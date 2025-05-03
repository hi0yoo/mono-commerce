package me.hi0yoo.commerce.common.domain.id

import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

@Embeddable
data class OrderProductId(
    val orderId: String, // 주문번호
    @Embedded
    val productId: ProductOptionId,
)