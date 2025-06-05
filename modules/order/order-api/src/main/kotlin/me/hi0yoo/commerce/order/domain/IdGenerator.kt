package me.hi0yoo.commerce.order.domain

fun interface IdGenerator {
    fun nextId(): Long
}