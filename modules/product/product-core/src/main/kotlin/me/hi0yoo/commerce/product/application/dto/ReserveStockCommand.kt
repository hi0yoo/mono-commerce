package me.hi0yoo.commerce.product.application.dto

data class ReserveStockCommand(
    val productOptionId: Long,
    val quantity: Int,
)

data class BulkReserveStockCommand(
    val commands: List<ReserveStockCommand>
)