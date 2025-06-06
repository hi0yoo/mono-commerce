package me.hi0yoo.commerce.order.application.port.out

interface NextOrderNumberGeneratorPort {
    fun fetchFrom(yyyyMMddHHmmss: String): Long
}