package me.hi0yoo.commerce.product.infrastructure.vendor

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Vendor(id: String, name: String) {
    @Id
    val id: String = id

    val name: String = name
}