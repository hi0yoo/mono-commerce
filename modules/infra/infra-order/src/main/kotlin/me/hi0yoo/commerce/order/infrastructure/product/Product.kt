package me.hi0yoo.commerce.order.infrastructure.product

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import me.hi0yoo.commerce.common.domain.id.ProductId

@Entity
class Product(
    vendorId: String,
    productId: String,
    productName: String,
) {
    @EmbeddedId
    val id: ProductId = ProductId(vendorId, productId)

    var productName: String = productName
        protected set
}