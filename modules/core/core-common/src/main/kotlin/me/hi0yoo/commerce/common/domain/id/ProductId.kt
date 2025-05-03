package me.hi0yoo.commerce.common.domain.id

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class ProductId(
    @Column(name = "vendor_id", length = 20)
    val vendorId: String, // 회사코드
    @Column(name = "product_id", length = 30)
    val productId: String, // 상품코드
)