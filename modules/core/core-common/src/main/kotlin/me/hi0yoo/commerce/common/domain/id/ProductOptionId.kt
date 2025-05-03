package me.hi0yoo.commerce.common.domain.id

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class ProductOptionId(
    @Column(name = "vendor_id", length = 20)
    val vendorId: String, // 회사코드
    @Column(name = "product_id", length = 30)
    val productId: String, // 상품코드
    @Column(name = "option_id", length = 20)
    val optionId: String, // 상품옵션코드
) {
    constructor(productId: ProductId, optionId: String): this(
        productId.vendorId,
        productId.productId,
        optionId
    )
}