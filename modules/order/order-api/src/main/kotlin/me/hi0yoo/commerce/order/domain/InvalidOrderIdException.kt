package me.hi0yoo.commerce.order.domain

class InvalidOrderIdException(id: String) : RuntimeException("Invalid order ID format: $id. Expected format: yyyyMMddHHmmss + 4-digit sequence number")