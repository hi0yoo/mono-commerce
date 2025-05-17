package me.hi0yoo.commerce.common.domain.exception

class InvalidOrderIdException(id: String) : RuntimeException("Invalid order ID format: $id. Expected format: yyyyMMddHHmmss + 4-digit sequence number")