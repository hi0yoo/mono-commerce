package me.hi0yoo.commerce.common.domain.id

fun interface IdGenerator {
    fun nextId(): Long
}