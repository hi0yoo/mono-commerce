package me.hi0yoo.commerce.order.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class Customer(
    @Column(name = "customer_id")
    val memberId: Long,
    @Column(name = "customer_name")
    val name: String,
    @Column(name = "customer_email")
    val email: String,
)