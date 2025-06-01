package me.hi0yoo.commerce.order.infrastructure.product

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Product(
    id: Long,
    productName: String,
) {
    @Id
    @Column(name = "product_id")
    val id: Long = id

    var productName: String = productName
        protected set
}