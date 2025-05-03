package me.hi0yoo.commerce.order.application.port

import me.hi0yoo.commerce.common.domain.enums.PayMethod

interface PaySystemPort {
    fun support(): PayMethod
    fun pay(request: PaySystemRequest): PaySystemPayResponse
}