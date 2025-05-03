package me.hi0yoo.commerce.order.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class DeliveryAddress(
    val receiverName: String,
    @Column(name = "receiver_address")
    val address: String,
    @Column(name = "receiver_email")
    val email: String,
)