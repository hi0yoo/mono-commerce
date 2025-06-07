package me.hi0yoo.commerce.product.domain

fun interface IdGenerator {
    fun nextId(): Long
}